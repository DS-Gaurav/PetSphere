package com.examplejjwt.jwtauth.service;

import com.examplejjwt.jwtauth.dto.PetDto;
import com.examplejjwt.jwtauth.dto.ProfileRequest;
import com.examplejjwt.jwtauth.entity.Pet;
import com.examplejjwt.jwtauth.entity.Profile;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.repository.ProfileRepository;
import com.examplejjwt.jwtauth.repository.UserRepository;
import com.examplejjwt.jwtauth.responsedata.InvalidRecord;
import com.examplejjwt.jwtauth.responsedata.UploadResponse;
import com.examplejjwt.jwtauth.validation.ValidateData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ValidateData validateData;
    private final int Thread_count = 4;
    private final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    public UploadResponse uploadprofiles(MultipartFile file) throws IOException {
        return parseCsv(file);
    }

    private UploadResponse parseCsv(MultipartFile file) throws IOException {
        Set<Profile> validProfiles = Collections.synchronizedSet(new HashSet<>());
        List<InvalidRecord> invalidRecords = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executorService = Executors.newFixedThreadPool(Thread_count);
        List<Future<Void>> futures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            if(lines.isEmpty()){
                return new UploadResponse(0,List.of(new InvalidRecord(1,"","Csv file empty")));
            }
            String expectHead = "mobile,email,age,user_id";
            String head = lines.get(0).trim();
            if(!head.equalsIgnoreCase(expectHead)){
                return new UploadResponse(0,List.of(new InvalidRecord(1,head,"Invalid CSV formate. expected"+ expectHead)));
            }
            List<String> datalines = lines.subList(1,lines.size());
            int batch_size = Math.max(1,datalines.size()/Thread_count);
            for(int i=0;i<datalines.size();i+=batch_size){
                int start =i;
                int end = Math.min(i+batch_size, datalines.size());
                List<String> batch = datalines.subList(start,end);
                Future<Void> future = executorService.submit(() -> {
                    logger.info("..............START BATCH on the................. "+ start+2);
                    processbatch(batch,validProfiles,invalidRecords,start+2);
                    return null;
                });
                futures.add(future);
            }
        }
        for(Future<Void> future : futures){
            try{
                future.get();
            }
            catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        if (!validProfiles.isEmpty()) {
            profileRepository.saveAll(validProfiles);
        }
        return new UploadResponse(validProfiles.size(), invalidRecords);
    }
    private void processbatch(List<String> batch, Set<Profile> validProfiles, List<InvalidRecord> invalidRecords, int rowset) {
        for(int i=0;i<batch.size();i++){
            String line = batch.get(i);
            int rowNumber = rowset+i;
            logger.info("...........................running the row number of....................... "+ rowNumber);
            String[] data = line.split(",");
            if (data.length != 4) {
                invalidRecords.add(new InvalidRecord(rowNumber, line, "Incomplete data"));
                continue;
            }
            String umobile = data[0].trim();
            String uemail = data[1].trim();
            String uagestr = data[2].trim();
            Long uidd = Long.parseLong(data[3].trim());
            User user = userRepository.findById(uidd).orElse(null);
            List<String> errors = new ArrayList<>();
            if (!(validateData.isValidMobile(umobile))) errors.add("Invalid Mobile Number");
            if (!(validateData.isValidEmail(uemail))) errors.add("Invalid email");
            if (!(validateData.isValidAge(uagestr))) errors.add("Invalid age");
            if (user == null) errors.add("This user not exist in users list");
            if(profileRepository.existsByUserId(uidd)) errors.add("This user id  already have profile");
            if (profileRepository.existsByMobile(umobile)) errors.add("Mobile number Already exist");
            if (profileRepository.existsByEmail(uemail)) errors.add("Email Already exist");
            synchronized (validProfiles){
                if ((validateData.isMobileExist(umobile,validProfiles)) || (validateData.isEmailExist(uemail,validProfiles)) || (validateData.isUserExit(user,validProfiles))) errors.add("Double Entry not Allow");
            }
            if (!errors.isEmpty()) {
                invalidRecords.add(new InvalidRecord(rowNumber, line, String.join(", ", errors)));
                continue;
            }
            int uage = Integer.parseInt(uagestr);
            Profile profile = new Profile(umobile, uemail, uage, user);
            validProfiles.add(profile);
        }
    }
    private Map<String, String> createFailedRecord(String line, String error) {
        Map<String, String> record = new HashMap<>();
        record.put("data", line);
        record.put("error", error);
        return record;
    }

    public Map<String, Object> deleteprofiles(MultipartFile file) throws IOException {
        List<Map<String, String>> dinvalidrecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String dline;
            boolean isdfline = true;
            int drowNumber = 0;
            int deleted = 0;
            String expectHead = "mobile";
            while ((dline = reader.readLine()) != null) {
                drowNumber++;
                if (isdfline) {
                    isdfline = false;
                    if (!dline.trim().equalsIgnoreCase(expectHead)) {
                        Map<String,Object> response = new HashMap<>();
                        response.put("delete Account",0);
                        dinvalidrecords.add(createFailedRecord(dline, "Invalid CSV header format. Expected: " + expectHead));
                        response.put("failed records",dinvalidrecords);
                        return response;
                    }
                    continue;
                }
                String[] d_data = dline.split(",");
                String umobile = d_data[0].trim();
                Optional<Profile> profile = profileRepository.findByMobile(umobile);
                if (profile.isPresent()) {
                    profileRepository.deleteByMobile(umobile);
                    deleted++;
                } else {
                    dinvalidrecords.add(createFailedRecord(dline, "User not found"));
                }
            }
            Map<String,Object> response = new HashMap<>();
            response.put("Delete Account",deleted);
            response.put("failed records",dinvalidrecords);
            return response;
        }
    }

    public Map<String, Object> updateprofiles(MultipartFile file) throws IOException {
        List<InvalidRecord> uinvalidrecords = new ArrayList<>();
        Set<Profile> validProfiles = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String uline;
            boolean isufline = true;
            int urowNumber = 0;
            String expectHead = "mobile,email,age,user_id";
            while((uline = reader.readLine()) != null){
                urowNumber++;
                if(isufline){
                    isufline=false;
                    if (!uline.trim().equalsIgnoreCase(expectHead)) {
                        Map<String,Object> response = new HashMap<>();
                        response.put("Update Account",0);
                        uinvalidrecords.add(new InvalidRecord(urowNumber, uline, "Invalid CSV header format. Expected: " + expectHead));
                        response.put("failed records",uinvalidrecords);
                        return response;
                    }
                    continue;
                }
                String[] u_data = uline.split(",");
                if(u_data.length != 4 ){
                    uinvalidrecords.add(new InvalidRecord(urowNumber, uline, "Incomplete data"));
                    continue;
                }
                String umobile = u_data[0].trim();
                String uemail = u_data[1].trim();
                String uagestr = u_data[2].trim();
                Long uidd = Long.parseLong(u_data[3].trim());
                User user = userRepository.findById(uidd).orElse(null);
                Optional<Profile> profile = profileRepository.findByMobile(umobile);
                List<String> uerrors = new ArrayList<>();
                if (profile.isEmpty()) uerrors.add("This profile not exist");
                if (!(validateData.isValidAge(uagestr))) uerrors.add("Invalid age");
                if (!(validateData.isValidEmail(uemail))) uerrors.add("Invalid email");
                if (!(validateData.isValidMobile(umobile))) uerrors.add("Invalid Mobile Number");
                if (!validProfiles.isEmpty()){
                    if ((validateData.isMobileExist(umobile,validProfiles))) uerrors.add("Double Entry not Allow");
                    if ((validateData.isEmailExist(uemail,validProfiles))) uerrors.add("Double Entry not Allow");
                }
                if (!(profileRepository.existsByMobile(umobile))) if(profileRepository.existsByEmail(uemail)) uerrors.add("Email Already exist");
                if (!uerrors.isEmpty()) {
                    uinvalidrecords.add(new InvalidRecord(urowNumber, uline, String.join(", ", uerrors)));
                    continue;
                }
                int uage = Integer.parseInt(uagestr);
                Profile newp = profile.get();
                newp.setEmail(uemail);
                newp.setAge(uage);
                validProfiles.add(newp);
            }
            if (!validProfiles.isEmpty()) {
                profileRepository.saveAll(validProfiles);
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("Update Account",validProfiles.size());
        response.put("failed records",uinvalidrecords);
        return response;
    }

    public ProfileRequest createprofile(ProfileRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            if (!(profileRepository.existsByEmail(request.getEmail()))){
                String username = ((UserDetails) principal).getUsername();
                User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
                Profile newp = new Profile();
                newp.setMobile(request.getMobile());
                newp.setEmail(request.getEmail());
                newp.setAge(request.getAge());
                newp.setUser(user);
                Profile savedProfile =  profileRepository.save(newp);
                return new ProfileRequest(
                        savedProfile.getId(),
                        savedProfile.getMobile(),
                        savedProfile.getEmail(),
                        savedProfile.getAge(),
                        username
                );
            }
        }
        return null;
    }

    public ProfileRequest getprofile(Long id) {
        Profile profile = profileRepository.findByUserId(id).orElseThrow(()->new RuntimeException("Profile not found"));
        ProfileRequest profileRequest = new ProfileRequest(
                profile.getId(),
                profile.getMobile(),
                profile.getEmail(),
                profile.getAge(),
                profile.getUser().getUsername()
        );
        return profileRequest;
    }

    public List<ProfileRequest> getallprofile() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream().map(profile -> new ProfileRequest(
                profile.getId(),
                profile.getMobile(),
                profile.getEmail(),
                profile.getAge(),
                profile.getUser().getUsername()
        )).collect(Collectors.toList());
    }
}
//    public Integer uploadprofiles(MultipartFile file) throws IOException {
//        Set<Profile> profiles = parseCsv(file);
//        profileRepository.saveAll(profiles);
//        return profiles.size();
//    }
//    private Set<Profile> parseCsv(MultipartFile file) throws IOException {
//        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
//            HeaderColumnNameMappingStrategy<ProfileCsv> strategy =
//                    new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(ProfileCsv.class);
//            CsvToBean<ProfileCsv> csvToBean =
//                    new CsvToBeanBuilder<ProfileCsv>(reader)
//                            .withMappingStrategy(strategy)
//                            .withIgnoreEmptyLine(true)
//                            .withIgnoreLeadingWhiteSpace(true)
//                            .build();
//            return csvToBean.parse()
//                    .stream()
//                    .map(csvLine -> new Profile(
//                            csvLine.getUmobile(),csvLine.getUmobile(),csvLine.getUage(),csvLine.getUid()
//                    ))
//                    .collect(Collectors.toSet());
//
//        }
//    }

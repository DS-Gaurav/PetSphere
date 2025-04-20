package com.examplejjwt.jwtauth.service;

import com.examplejjwt.jwtauth.dto.BlogDetailResponse;
import com.examplejjwt.jwtauth.dto.PetDto;
import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.Comment;
import com.examplejjwt.jwtauth.entity.Pet;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.exception.UnauthorizedAccessException;
import com.examplejjwt.jwtauth.repository.PetRepository;
import com.examplejjwt.jwtauth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    public PetService(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }
    public PetDto registerpet(PetDto petDto, List<MultipartFile> files) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            Pet newpet = new Pet();
            newpet.setCategory(petDto.getCategory());
            newpet.setBreed(petDto.getBreed());
            newpet.setAge(petDto.getAge());
            newpet.setPrice(petDto.getPrice());
            newpet.setUser(user);
            String imageurl = null;
            String reporturl = null;
            imageurl= saveFile(files.get(0), "images/");
            reporturl= saveFile(files.get(1), "reports/");
            newpet.setImage(imageurl);
            newpet.setMceretificate(reporturl);
            Pet savedPet =  petRepository.save(newpet);
            return new PetDto(
                    savedPet.getId(),
                    savedPet.getCategory(),
                    savedPet.getImage(),
                    savedPet.getBreed(),
                    savedPet.getAge(),
                    savedPet.getPrice(),
                    savedPet.getMceretificate(),
                    username
            );
        }
        return null;
    }
    private String saveFile(MultipartFile multipartFile, String directory) throws IOException {
        String uploadDir = "/home/dsgaurav/uploads/" + directory;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + uploadDir);
            }
        }
        String filename = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
        File destFile = new File(uploadDir, filename);
        multipartFile.transferTo(destFile);

        return "http://localhost:9090/"+ directory + filename;
    }
    public Page<PetDto> allpets(int page) {
        if (page < 0 ) {
            throw new IllegalArgumentException("Page number must be non-negative ");
        }
        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.ASC, "id"));
        Page<Pet> pets = petRepository.findAll(pageable);
        return pets.map(pet -> new PetDto(
                pet.getId(),
                pet.getCategory(),
                pet.getImage(),
                pet.getBreed(),
                pet.getAge(),
                pet.getPrice(),
                pet.getMceretificate(),
                pet.getUser().getUsername()
        ));
    }

    public Page<PetDto> sellpets(int page) {
        if (page < 0 ) {
            throw new IllegalArgumentException("Page number must be non-negative ");
        }
        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.ASC, "id"));
        Page<Pet> pets = petRepository.findBySellTrue(pageable);
        return pets.map(pet -> new PetDto(
                pet.getId(),
                pet.getCategory(),
                pet.getImage(),
                pet.getBreed(),
                pet.getAge(),
                pet.getPrice(),
                pet.getMceretificate(),
                pet.getUser().getUsername()
        ));
    }

    public PetDto getpetbyid(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(()->new RuntimeException("Pet not found"));
        PetDto petDto = new PetDto(
                pet.getId(),
                pet.getCategory(),
                pet.getImage(),
                pet.getBreed(),
                pet.getAge(),
                pet.getPrice(),
                pet.getMceretificate(),
                pet.getUser().getUsername()
        );
        return petDto;
    }
    public Page<PetDto> allmypets(int page) {
        if (page < 0 ) {
            throw new IllegalArgumentException("Page number must be non-negative ");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.ASC, "id"));
            Page<Pet> pets = petRepository.findByUser(loginuser,pageable);
            return pets.map(pet -> new PetDto(
                    pet.getId(),
                    pet.getCategory(),
                    pet.getImage(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getPrice(),
                    pet.getMceretificate()
            ));
        }
        return null;
    }

    public String deletepet(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(()->new RuntimeException("Pet not found"));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            if(!pet.getUser().getUsername().equals(loginuser.getUsername())){
                throw new UnauthorizedAccessException("you have not access for delete this pet");
            }
            petRepository.delete(pet);
            return "Pet delete Succesfully";
        }
        return null;
    }

    public String selltrue(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(()->new RuntimeException("Pet not found"));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            if(!pet.getUser().getUsername().equals(loginuser.getUsername())){
                throw new UnauthorizedAccessException("you have not access for delete this pet");
            }
            pet.setSell(!pet.isSell());
            petRepository.save(pet);
            if(pet.isSell())
                return "Pet sell true Succesfully";
            else
                return "Pet sell false Succesfully";
        }
        return null;
    }
}

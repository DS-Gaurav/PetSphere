package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.dto.ProfileRequest;
import com.examplejjwt.jwtauth.exception.UnauthorizedAccessException;
import com.examplejjwt.jwtauth.responsedata.UploadResponse;
import com.examplejjwt.jwtauth.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
    public ResponseEntity<UploadResponse> uploadProfiles(@RequestPart("file")MultipartFile file) throws IOException {
        UploadResponse uploadResponse = service.uploadprofiles(file);
        return ResponseEntity.ok(uploadResponse);
    }
    @DeleteMapping(value = "/delete",consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String,Object>> deleteprofiles(@RequestPart("file")MultipartFile file) throws IOException{
        Map<String,Object> response = service.deleteprofiles(file);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/update",consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String,Object>> updateprofiles(@RequestPart("file")MultipartFile file) throws IOException{
        Map<String,Object> response = service.updateprofiles(file);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> createProfile(@RequestBody ProfileRequest request){
        try {
            return ResponseEntity.ok(service.createprofile(request));
        }
        catch (Exception e){
            throw new UnauthorizedAccessException("you are unauthorize");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getprofile(@PathVariable Long id){
        return ResponseEntity.ok(service.getprofile(id));
    }
    @GetMapping()
    public ResponseEntity<?> getprofile(){
        return ResponseEntity.ok(service.getallprofile());
    }


}

package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.dto.PetDto;
import com.examplejjwt.jwtauth.repository.PetRepository;
import com.examplejjwt.jwtauth.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "http://localhost:5174")
public class PetController {
    private final PetRepository petRepository;
    private final PetService service;
    private final Logger logger = LoggerFactory.getLogger(PetController.class);
    public PetController(PetRepository petRepository, PetService service) {
        this.petRepository = petRepository;
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<?> registerpet(@RequestPart("pet") String petjson, @RequestPart("files") List<MultipartFile> files) throws IOException {
        logger.info(".......................in the pet register api.........................");
        ObjectMapper objectMapper = new ObjectMapper();
        PetDto petDto = objectMapper.readValue(petjson, PetDto.class);
        if(petDto.getId() != null && petRepository.existsById(petDto.getId())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pet with this id already exist");
        }
        if (files.size() != 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You must upload exactly 2 files: 1 image and 1 report.");
        }
        PetDto petResponse = service.registerpet(petDto,files);
        if(petResponse == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(petResponse);
    }
    @GetMapping("/allpets")
    public ResponseEntity<?> allpets(@RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(service.allpets(page));
    }

    @GetMapping("/available")
    public ResponseEntity<?> sellpets(@RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(service.sellpets(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> petbyid(@PathVariable Long id){
        return ResponseEntity.ok(service.getpetbyid(id));
    }

    @GetMapping("/mypets")
    public ResponseEntity<?> allmypets(@RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(service.allmypets(page));
    }
    @PostMapping("/settrue/{id}")
    public ResponseEntity<?> settruepet(@PathVariable Long id){
        String response = service.selltrue(id);
        if(response == null){
        logger.error("unauthorize user not pet sell ");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }
        logger.info("pet sell true");
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletepet(@PathVariable Long id){
        String response = service.deletepet(id);
        if(response == null){
            logger.error("unauthorize user not pet delete ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        logger.info("pet delete");
        return ResponseEntity.ok(response);
    }

}

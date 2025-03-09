package com.awstest.examples_labs.s3.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.awstest.examples_labs.s3.services.IS3Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("bucket")
@RequiredArgsConstructor
public class S3Controller {

    private final IS3Service is3Service;

    @Value("${spring.destination.folder}")
    private String destinationFolder;

    @PostMapping("/{bucketName}")
    public ResponseEntity<String> createBucket(@RequestParam String bucketName){
        return ResponseEntity.ok(is3Service.createBucket(bucketName));
    } 

    @GetMapping("/{bucketName}")
    public ResponseEntity<String> checkIfBucketExist(@PathVariable String bucketName){
        return ResponseEntity.ok(is3Service.checkIfBucketExist(bucketName));
    }

    @GetMapping
    public ResponseEntity<List<String>> listBuckets() {
        return ResponseEntity.ok(is3Service.getAllBuckets());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String nameBucket, 
                                       @RequestParam String key, 
                                       @RequestPart MultipartFile fileLocation) throws IOException{

        try {
            Path staticDir = Paths.get(destinationFolder);
            if (!Files.exists(staticDir)) {
                Files.createDirectories(staticDir);
            }

            Path filePath = staticDir.resolve(fileLocation.getOriginalFilename());
            Path finalPath =Files.write(filePath, fileLocation.getBytes());

            Boolean response = is3Service.uploadFile(nameBucket, key, finalPath);

            if (response) {
                Files.delete(finalPath);
                return ResponseEntity.ok("Archivo se cargo correctamente");
            } else {
                return ResponseEntity.internalServerError().body("Error al cargar el archivo");
            }

        } catch (IOException e) {
            // TODO: handle exception
            throw new IOException("Error al procesar el archivo");
        }
    }

    @PostMapping("/download")
    public ResponseEntity<String> downloadFile(@RequestParam String nameBucket, 
                                       @RequestParam String key) throws IOException {

        is3Service.downloadFile(nameBucket, key);
        return ResponseEntity.ok("Archivo se descargo correctamente");
            
    }

    @PostMapping("/upload/presigned")
    public String uploadPresignedFile(@RequestParam String nameBucket, 
                                    @RequestParam String key, 
                                    @RequestParam Long minutes) {
        //TODO: process POST request
        Duration activeLink = Duration.ofMinutes(minutes);
        return is3Service.generatePresignedUploadURL(nameBucket, key, activeLink);
    }

    @PostMapping("/download/presigned")
    public String downloadPresignedFile(@RequestParam String nameBucket, 
                                    @RequestParam String key, 
                                    @RequestParam Long minutes) {
        //TODO: process POST request
        Duration activeLink = Duration.ofMinutes(minutes);
        return is3Service.generatePresignedDownloadURL(nameBucket, key, activeLink);
    }
    
}
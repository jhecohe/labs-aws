package com.awstest.examples_labs.s3.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements IS3Service {

    private final S3Client s3Client;
    // private final S3AsyncClient s3AsyncClient;

    @Value("${spring.destination.folder}")
    private String destinationFolder;

    @Override
    public String createBucket(String bucketName) {
        // TODO Auto-generated method stub
        CreateBucketResponse cbr = s3Client.createBucket(newBucket -> newBucket.bucket(bucketName));
        return "Bucket creted in the location " + cbr.location();
    }

    @Override
    public String checkIfBucketExist(String bucketName) {
        // TODO Auto-generated method stub
        try {
            HeadBucketResponse hbr = s3Client.headBucket(headBucket -> headBucket.bucket(bucketName));
        } catch (Exception e) {
            // TODO: handle exception
            return "No se encontro informacion para el bucket" + bucketName;
        }

        return "El bucket existe";
    }

    @Override
    public List<String> getAllBuckets() {
        // TODO Auto-generated method stub
        ListBucketsResponse lbr = s3Client.listBuckets();
        if(lbr.hasBuckets()) {
            return lbr.buckets()
            .stream()
            // .map(b -> b.name())
            .map(Bucket::name)
            .toList();
        }
        return List.of();
    }

    @Override
    public Boolean uploadFile(String nameBucket, String key, Path fileLocation) {
        // TODO Auto-generated method stub

        PutObjectRequest request = PutObjectRequest
        .builder()
        .bucket(nameBucket)
        .key(key)
        .build();
        PutObjectResponse response = s3Client.putObject(request, fileLocation);

        return response.sdkHttpResponse().isSuccessful();
    }

    @Override
    public void downloadFile(String nameBucket, String key) throws IOException {
        // TODO Auto-generated method stub
        GetObjectRequest request = GetObjectRequest
        .builder()
        .bucket(nameBucket)
        .key(key)
        .build();

        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);

        String fileName;
        fileName = key.contains("/") ?
            key.substring(key.lastIndexOf("/")) : key;

        String filePath = destinationFolder.concat(fileName);

        File file = new File(filePath);
        file.getParentFile().mkdir();
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(response.asByteArray());
        } catch (IOException e) {
            // TODO: handle exception
            throw new IOException("Error al descargar el archivo " + e.getCause());
        }
    }

    @Override
    public String generatePresignedUploadURL(String nameBucket, String key, Duration duration) {
        // TODO Auto-generated method stub
        PutObjectRequest request = PutObjectRequest
            .builder()
            .bucket(nameBucket)
            .key(key)
            .build();

            return null;
    }

    @Override
    public String generatePresignedDownloadURL(String nameBucket, String key, Duration duration) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generatePresignedDownloadURL'");
    }
    
}

package com.awstest.examples_labs.s3.services;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;


public interface IS3Service {
    
    String createBucket(String bucketName);
    String checkIfBucketExist(String bucketName);
    List<String> getAllBuckets();
    Boolean uploadFile(String nameBucket, String key, Path fileLocation);
    void downloadFile(String nameBucket, String key) throws IOException;
    // Genrar URL prefirmada para subir archivos duarante un periodo de tiempo
    String generatePresignedUploadURL(String nameBucket, String key, Duration duration);
    // Genrar URL prefirmada para baajr archivos duarante un periodo de tiempo 
    // para un archivo espcifico.
    String generatePresignedDownloadURL(String nameBucket, String key, Duration duration);

}

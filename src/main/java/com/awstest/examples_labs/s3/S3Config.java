package com.awstest.examples_labs.s3;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    
    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.access.password}")
    private String awsPasswordKey;
    @Value("${aws.region}")
    private String awsRegion;

    /**
     *  Cliente s3 syncrono
     */
    @Bean
    public S3Client connectS3Client() {
        AwsCredentials ac = AwsBasicCredentials.create(awsAccessKey, awsPasswordKey);
        return S3Client.builder()
        .region(Region.of(awsRegion))
        .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
        .credentialsProvider(StaticCredentialsProvider.create(ac))
        .build();
    }

    /**
     *  Cliente s3 asyncrono
     */
    @Bean
    public S3AsyncClient connectS3ClientAsync() {
        AwsCredentials ac = AwsBasicCredentials.create(awsAccessKey, awsPasswordKey);
        return S3AsyncClient.builder()
        .region(Region.of(awsRegion))
        .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
        .credentialsProvider(StaticCredentialsProvider.create(ac))
        .build();
    }


    // Configuracion para firmar URL's con S3
    @Bean
    public S3Presigner s3Presigner() {
        AwsCredentials ac = AwsBasicCredentials.create(awsAccessKey, awsPasswordKey);
        return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(ac))
            .region(Region.of(awsRegion))
            .build();
    }
}

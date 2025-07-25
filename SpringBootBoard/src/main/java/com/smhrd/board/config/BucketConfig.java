package com.smhrd.board.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class BucketConfig {


	@Value("${ncp.access-key}")
	private String accessKey;

	@Value("${ncp.secret-key}")
	private String secretKey;
	
	@Value("${ncp.region}")
	private String region;
	
	@Value("${ncp.end-point}")
	private String endPoint;

	@Value("${ncp.bucket-name}")
	private String bucketName;
	
	public String getbucketName() {
		return this.bucketName;
	}

	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withPathStyleAccessEnabled(true)
                .build();
	}

}

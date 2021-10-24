package com.quickreads.user.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AWSS3Util {

	@Autowired
	private AmazonS3 amazonS3Client;

	@Value("${quickreads.application.bucket.name}")
	String bucket;

	Region region = Region.AP_Mumbai;

	public void uploadData(String itemName, String data) {
		try {
			log.info("Uploading item to S3 : ", itemName);
			amazonS3Client.putObject(bucket, itemName, data);
			
		} catch (AmazonServiceException e) {
			log.error("Failed pushing to S3 bucket ", e.getLocalizedMessage());
		}
	}

	public String getData(String id) {
		try {
			S3Object s3Obj = amazonS3Client.getObject(bucket, id);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Obj.getObjectContent()));
	        String readLine = null;
	        StringBuilder sBuilder = new StringBuilder();
			while((readLine = reader.readLine()) != null) {
				sBuilder.append(readLine);
			}
			return sBuilder.toString();
		}
		catch(AmazonServiceException e) {
			log.error("Failed Getting data from S3 bucket ", e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			log.error("Failed parsing data from S3 bucket ", e.getLocalizedMessage());
			return null;
		}
		
	}

}

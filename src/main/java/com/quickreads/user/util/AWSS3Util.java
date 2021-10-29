package com.quickreads.user.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AWSS3Util {

	@Autowired
	private AmazonS3 amazonS3Client;

	@Value("${quickreads.application.bucket.name}")
	String bucket;
	
	private final long expTimeMillis = 1000 * 60 * 60;

	/**
	 * Uploads String data
	 * @param itemName
	 * @param data
	 */
	//TODO:: Remove method later
	public void uploadData(String itemName, String data) {
		try {
			log.info("Uploading item to S3 : ", itemName);
			amazonS3Client.putObject(bucket, itemName, data);
			
		} catch (AmazonServiceException e) {
			log.error("Failed pushing to S3 bucket ", e.getLocalizedMessage());
		}
	}
	
	/**
	 * Uploads file
	 * @param itemName
	 * @param file
	 */
	public void uploadDataWithFile(String itemName, MultipartFile file) {
		try {
			log.info("Uploading item to S3 : {}", itemName);
			File fileToUpload = convert(file);
			amazonS3Client.putObject(bucket, itemName, fileToUpload);
			fileToUpload.delete();
		} catch (AmazonServiceException e) {
			log.error("Failed pushing to S3 bucket {}", e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Failed in I/O {}", e.getLocalizedMessage());
		}
	}
	
	/**
	 * Retrieves presigned URL
	 * @param objectKey
	 * @return
	 * @throws Exception
	 */
	public URL generatePreSignedUrl(String objectKey) throws Exception{
		java.util.Date expiration = new java.util.Date();
		final long validity = expTimeMillis + expiration.getTime();
		expiration.setTime(validity);
		
		log.info("Generating pre-signed url");
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.GET).withExpiration(expiration);
		
		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url;
	}

	/**
	 * Retrieves String data
	 * @param id
	 * @return
	 */
	//TODO:: Remove method later
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
	
	/**
	 * Utility method to convert multipart file to file
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private File convert(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile();
	      try(FileOutputStream fos = new FileOutputStream(convFile)) {
	    	  fos.write(file.getBytes());
	      }
	    return convFile;
	  }

}

package com.smhrd.board.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

// 환경설정 클래스 파일
@Configuration
public class FileUploadConfig {
	
	// application.properites에 있는 file.upload-dir를 참고
	@Value("${file.upload-dir}") 
	private String uploadDir; // C:upload라는 폴더
	
	public String getUploadDir() {
		return this.uploadDir;
	}
	
}

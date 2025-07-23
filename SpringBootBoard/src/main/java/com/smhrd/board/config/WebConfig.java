package com.smhrd.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 파일 업로드를 위해 web의 환경설정을 수정
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	// WebMvcConfigurer ==> Spring에서 웹에 관한 설정을 커스텀마이징 할 때 사용하는 인터페이스
	// 기본 설정을 유지하고 필요한 부분만 수정할 수 있음
	
	// 저장 될 경로를 수정
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**") // 브라우저에서 접근할 경로를 설정
												   // localhost:포트/uploads/파일명
				.addResourceLocations("file:/home/git/upload"); // 실제 서버에 저장할 경로
	} 
	

	
	
	
}

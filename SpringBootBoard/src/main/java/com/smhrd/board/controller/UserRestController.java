package com.smhrd.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.board.service.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	UserService userService;
	
	// RestController 란?
	// 일반 Controller는 view(html)반환
	// RestController는 데이터를 반환하는 Controller
	// ㄴ데이터란? String, int, Json 등
	// legacy -> Controller + ResponseBody
	
	// 아이디 중복체크
	// 1. 매핑할 메소드
	@GetMapping("/member/check-id")
	public boolean checkId(@RequestParam String id) {
		// 2. 필요한 데이터 -> RequestParam으로 설정
		// 3. DB 접근
		boolean isChecked = userService.checkId(id);
		
		return isChecked;
		
	}
}

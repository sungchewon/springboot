package com.smhrd.board.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.repository.UserRepository;

@Service
public class UserService {

	/*
	 * Spring 에서는 3계층 구조를 띔
	 * Controller - Service - Repository(mapper)
	 * 
	 * Controller : mapping 처리 HTTP 요청 처리 사용자와 직접적인 상호 작용
	 * Service : 비지니스 로직 담당, Repository(mapper)연결
	 * Repository(mapper) : 데이터베이스와 직접 통신
	 * 
	 * Controller와 Repository가 직접적인 연결을 한다면
	 * Controller의 역할이 많아지며 가독성이 낮아지고, Repository 결합도가 올라갑니다
	 * --> 유지보수가 힘들어진다
	 * --> 여러개의 쿼리문 사용이 힘들어짐
	 * 
	 * Service 객체를 사용하는 이유는 설계에 따라 유지보수성을 올린다
	 * 결론 : Service 객체가 Repository와 연결
	 * Controller는 Service와 연결
	 * */
	
	@Autowired
	private UserRepository userRepository;
	
	// 기능구현 --> 회원가입 기능 구현
	public void register(UserEntity entity) {
		userRepository.save(entity);
	}
	
	// 아이디 중복체크 기능
	public boolean checkId(String user_Id) {
		return userRepository.existsByUserId(user_Id);
	}
	
	// 로그인 기능
	public Optional<UserEntity> login(String id, String pw) {
		// 아이디와 패스워드 받기
		// 3.3 repository에 적절한 메소드가 있는지 여부 판단
		// ㄴ select * from 테이블명 where userId = id and pw = pw
		// 메소드 없으면 만든 후 실행
		
		Optional<UserEntity> entity = userRepository.findByUserIdAndPw(id, pw);
		return entity;
	}
	
	
	
}

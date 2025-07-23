package com.smhrd.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // db 테이블처럼 쓰겠다
@Getter
@Setter
@AllArgsConstructor //모든 필드를 매개변수로 받는 생성자 자동 생성
@NoArgsConstructor //기본 생성자 (파라미터 없는 생성자) 자동 생성
public class UserEntity {
	
	// 필드들은 --> DB의 컬럼이 될 예정
	// ★★★필수조건★★★ 반드시 pk가 존재해야한다
	// --> 숫자(long)으로 지정
	
	@Id // pk 지정 --> 자동으로 숫자 증가
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increase
	private Long id;
	
	// 컬럼 설정하는 법 -- unique, not null, length
	// @Column
	@Column(unique = true, nullable=false) // unique 설정 + not null설정
	private String userId;
	
	// 길이 조정
	@Column(length = 100)
	private String pw;
	
	private String name;
	private int age;
}

package com.smhrd.board.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardEntity {
	// pk가 존재해야함 
	
	@Id // pk
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increase
	private Long id; // 번호
	
	// 각 컬럼들에게 not null(ingPath 뺴고)
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String writer;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content; // 내용의 경우 길이가 길어야 하므로
	
	private String imgPath; 
	// ing(file)을 넣는 것이 아니라 경로를 DB에 저장
	// DB서버에 직접적으로 이미지와 같은 파일을 저장 하지 않음
	// 이미지는 서버에 저장, 해당 서버의 주소를 DB에 저장
	
	@Column(nullable = false, updatable = false) // db에 저장시 insert는 가능하나 update는 불가능
	private LocalDate writeDay;
	
	// 글 작성 시 자동으로 writeDay가 입력 되도록 코드 작성
	// emtity가 생성 될 때
	@PrePersist
	protected void onCreate() {
		this.writeDay = LocalDate.now();
	}
	
}

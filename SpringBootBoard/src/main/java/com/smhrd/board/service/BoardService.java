package com.smhrd.board.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.repository.BoardRepository;

@Service
public class BoardService {
	
	@Autowired
	BoardRepository boardRepository;
	
	// 게시글 작성
	
	public BoardEntity write(BoardEntity entity) {
		// insert문 실행
		return boardRepository.save(entity);
	}
	
	// 게시글 모두 출력
	public List<BoardEntity> show() {
		
		return boardRepository.findAllByOrderByIdDesc();
		
	}
	
	// 게시글 상세 보기 기능
	public Optional<BoardEntity> detail(Long id) {
		return boardRepository.findById(id);
	}
	
	// 게시글 검색 기능
	public List<BoardEntity> search(String type, String keyword) {
		// type에 따라 다르게 검색
		// -> 제목, 내용, 작성자 
		List<BoardEntity> list = null;
		switch (type) {
		case "title":
			// select * from board_entity where title like %keyword%
			list = boardRepository.findByTitleContaining(keyword);
			break;
			
		case "writer":
			//select * from board_entity where writer like %keyword%
			//select * from board_entity where writer = keyword -> 검색한게 정확히 일치할 떄
			list = boardRepository.findByWriter(keyword);
			break;
			
		case "content":
			//select * from board_entity where content like %keyword%
			list = boardRepository.searchContent(keyword);
			break;
		default:
			break;
		}
		
		return list;
	}
	

}

package com.smhrd.board.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.smhrd.board.config.BucketConfig;
import com.smhrd.board.config.FileUploadConfig;
import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.service.BoardService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board") // controller에 requestmapping 진행 시 default url 변경
public class BoardController {


	@Autowired
	BoardService boardService;
	
    private final BucketConfig bucketConfig;
    private final AmazonS3 amazonS3;
    private final FileUploadConfig fileUploadConfig;

    BoardController(FileUploadConfig fileUploadConfig, BucketConfig bucketConfig,
    				AmazonS3 amazonS3) {
        this.fileUploadConfig = fileUploadConfig;
        this.amazonS3 = amazonS3;
        this.bucketConfig = bucketConfig;
    }
	
	// 글쓰기 기능
	@PostMapping("/write")
	public String write(@RequestParam String title, @RequestParam String content,
						HttpSession session, @RequestParam MultipartFile image) {
		String imgPath = "";
		
		if(!image.isEmpty()) {
			String img_name = image.getOriginalFilename();
			
			String file_name = UUID.randomUUID() + "_" +img_name;	
			
			try {
				 ObjectMetadata metadata = new ObjectMetadata();
			     metadata.setContentLength(image.getSize());
			     metadata.setContentType(image.getContentType());

			     PutObjectRequest request = new PutObjectRequest(bucketConfig.getbucketName(), file_name, image.getInputStream(), metadata)
			             .withCannedAcl(CannedAccessControlList.PublicRead); // public 접근 허용

			     amazonS3.putObject(request);
			     imgPath = amazonS3.getUrl(bucketConfig.getbucketName(), file_name).toString();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		BoardEntity entity = new BoardEntity();
		entity.setTitle(title);
		entity.setContent(content);
		entity.setImgPath(imgPath);
		
		// writer --session에서 가지고 오기 -- down casting
		UserEntity user = (UserEntity) session.getAttribute("user");
		// (UserEntity)-> 다운캐스팅 (업캐스팅 된 것을 다시 다운 시키는 것?)
		
		String writer = user.getUserId();
		
		entity.setWriter(writer);
		
		BoardEntity result = boardService.write(entity);
		if(result != null) {
			// 성공
			// 글이 작성이 될 시 index페이지로 이동
			return "redirect:/";
		}else {
			return "redirect:/board/write";
		}
		
	}
	
	// 게시글 상세 페이지 이동
	@GetMapping("/detail/{id}") //{} url의 변수
	public String detail(@PathVariable Long id, Model model) { //url의 변수 가지고 오는 법
		System.out.println(id);
		// id를 바탕으로 select 진행
		// 게시글의 상세 정보 출력
		
		// db접근 --> service 객체 기능 구현
		boardService.detail(id);
		Optional<BoardEntity> entity = boardService.detail(id);
		
		model.addAttribute("detail", entity.get());
		
		return "detail";
	}
	
	//게시글 수정 페이지 이동
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		
		// id를 바탕으로 데이터 select 
		Optional<BoardEntity> entity = boardService.detail(id);
		
		model.addAttribute("edit", entity.get());
		
		return "edit";
	}
	
	@PostMapping("/update")
	public String update(@RequestParam Long id, @RequestParam String title,
						@RequestParam String content, @RequestParam String oldImgPath,
						@RequestParam MultipartFile image) {
		// 필요한거 정의 --> title, id, content, imgPath
		
		// 데이터 불러오기
		Optional<BoardEntity> board = boardService.detail(id);
		BoardEntity entity = board.get();
		
		String uploadDir = fileUploadConfig.getUploadDir();
		
		
		// 새로운 이미지 저장 시 기존 이미지 삭제
		if(!image.isEmpty()) {
			// 기존 이미지가 있는지 여부 판단
			if(oldImgPath != null && !oldImgPath.isEmpty()) {
				// 삭제 -- 서버에서 이미지 삭제
				
				// 파일명만 남기는 코드
				String oldFile = oldImgPath.replace("/uploads/","");
				
				// 서버에 저장되어있는 경로 + 파일명
				Path oldFilePath = Paths.get(uploadDir, oldFile);
				
				// 삭제 코드
				try {
					Files.deleteIfExists(oldFilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// 새로운 이미지 저장
				try {
		               String newFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
		               Path newFilePath = Paths.get(uploadDir, newFileName);
		               image.transferTo(newFilePath.toFile());
		               entity.setImgPath("/uploads/" + newFileName);
		        } catch (IllegalStateException e) {
		               // TODO Auto-generated catch block
		               e.printStackTrace();
		        } catch (IOException e) {
		               // TODO Auto-generated catch block
		               e.printStackTrace();
		        }
					
			}
		}
		
		entity.setTitle(title);
		entity.setContent(content);
		
		// update문 실행
		// JPA에서 update문이 없는게 아니라 save()함수가 update문 실행
		// save() --> 2가지 
		// save가 update문을 실행하는 조건
		// findById() 데이터를 불러 오는 것 (select)이후 데이터는 영속상태(수정상태)
		// save함수 사용 시 update문을 실행
		
		// 복잡한 update문은 실행x --> @Query() 활용하여 update 실행
		
		boardService.write(entity);
		
		return"redirect:/board/detail/" +id;
	}
	

}

package org.iclass.mvc.controller;

import lombok.extern.log4j.Log4j2;
import org.iclass.mvc.dto.Community;
import org.iclass.mvc.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//  url 이 community 로 시작하면 DispatcherServlet 으로부터 CommunityController가 위임받아 처리합니다.
@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {

	@GetMapping("/hello")
	public void hello(Model model) {
		model.addAttribute("message","하이 스프링~~!");
		model.addAttribute("list", List.of("모모", "나연", "나나", "쯔위"));
	}

	@GetMapping("/spring")
	public void spring(Community community,
			@RequestParam(required = false) String name,
					   @RequestParam(required = false) Integer age,
						Model model) {
		log.info("파라미터 name : {}", name);
		log.info("파라미터 age : {}", age);
		// required = false 로 하면 파라미터 값이 null 이 되어야하므로
		// int,long 둘은 Integer, Long 과 같이 래퍼 타입으로 선언합니다.
		model.addAttribute("name",name);

		// DTO 클래스는 파라미터를 받고 다시 view의 Model(뷰로 전달하는 데이터 저장 객체)로 전달도 할 수 있습니다.
		// int,long 등 기본형과 String은 파라미터 받은 것을 Model에 직접 저장.

	}
}
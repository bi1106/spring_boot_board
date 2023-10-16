package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.iclass.mvc.dto.Community;
import org.iclass.mvc.dto.CommunityComments;
import org.iclass.mvc.dto.PageRequestDTO;
import org.iclass.mvc.dto.PageResponseDTO;
import org.iclass.mvc.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/community")
@Log4j2
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService service;



    @GetMapping("/list")
    public void pagelist(PageRequestDTO pageRequestDTO,Model model){
        log.info(">>> pageRequestDTO : {}" , pageRequestDTO);
        PageResponseDTO pageResponseDTO= service.listWithSearch(pageRequestDTO);
        pageResponseDTO.getList().forEach(i->{
            log.info(">>> 글 : {}" ,i);
        });
        model.addAttribute("paging",pageResponseDTO);
        model.addAttribute("today",LocalDate.now());

    }
/*    @GetMapping("/list")
    public void list(@RequestParam(defaultValue = "1")   //파라미터 값이 없으면 오류. 기본값을 설정합니다.
                     int page, Model model) {   //메소드 인자 int page는 url 의 파라미터
        //Model : view 로 전달될 데이터를 주로 저장합니다.
        // addAttribute 메소드 : request.setAttribute 와 동일.(session 도 Model 사용가능. 로그인에서 합니다.)
        model.addAttribute("list", service.pagelist(page).get("list"));
        model.addAttribute("paging", service.pagelist(page).get("paging"));
        model.addAttribute("today", LocalDate.now());
    }*/

    @GetMapping("/read")   //url community/read? idx=287 &page=1 필요한것은 idx와 page
    public void read(long idx,
                     PageRequestDTO pageRequestDTO ,Model model) {
        Community community=service.read(idx);
        model.addAttribute("dto", community);
        // 요청이 /read이면 뷰리졸버가 read.html로 전달.
        // 요청이 /update이면 뷰리졸버가 update.html로 전달.
    }

    @GetMapping("/write")
    public void write() {
        // 글쓰기 GET 요청은 view name 만 지정하고 끝.
    }

    @PostMapping("/write")
    public String save(Community dto
            , RedirectAttributes reAttr) {      // 파라미터가 많을 때, 그것들을 필드로 갖는 dto 또는 map 타입으로 전달받기
        log.info("dto : {} " , dto);
        service.insert(dto);

        reAttr.addFlashAttribute("message", "글 등록이 완료 되었습니다.");
        return "redirect:/community/list";
    }
    // location.href = 'list.jsp' 는 자바스크립트 - 클라이언트 브라우저에서 주소 변경
    // response.sendRedirect("list.jsp") 는 서버에서 클라이언트가 새로 보낼 요청을 강제로 실행.
    //            ㄴ POST 요청을 처리한 후에는 redirect 를 합니다.


    @PostMapping("/delete") //pageRequestDTO를 ㅍ라미터로 받아서 수정 후에도 검색이 유지되도록 합니다.
    public String delete(PageRequestDTO pageRequestDTO,long idx, RedirectAttributes redirectAttributes) {
        service.delete(idx);
        redirectAttributes.addFlashAttribute("result","글을 삭제했습니다.("+idx+"번)");

        return "redirect:/community/list?"+pageRequestDTO.getLink();
    }

    @GetMapping("/update")
    public void update(long idx,Model model,
                       PageRequestDTO pageRequestDTO) {

        Community community = service.read(idx);
        model.addAttribute("dto",community);
        // int page는 @ModelAttribute 로 model.addAttribute("page",page); 를 대신햇
        //      update.jsp로 전달합니다.
    }


    @PostMapping("/update") //pageRequestDTO를 ㅍ라미터로 받아서 수정 후에도 검색이 유지되도록 합니다.
    public String updateAction(PageRequestDTO pageRequestDTO, Community community, RedirectAttributes redirectAttributes) {
        String link= pageRequestDTO.getLink();

        service.update(community);
        redirectAttributes.addFlashAttribute("result","글을 수정했습니다.");
        redirectAttributes.addAttribute("idx",community.getIdx());
        return "redirect:/community/read?"+link;
    }

    @PostMapping("/comments")   // 댓글
    public String commments(int page, int f, CommunityComments dto, RedirectAttributes redirectAttributes) {
        log.info(">>>>>>>>>>>> dto : {} ", dto);
        service.comments(dto,f);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("idx", dto.getMref());
        String message = null;
        if(f==1) message = "댓글 등록이 완료되었습니다.";
        else if(f==2) message = "댓글 삭제가 완료되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);



//      return "redirect:/community/read?page="+page +"&idx="+dto.Mref();
        return "redirect:/community/read";

    }

}
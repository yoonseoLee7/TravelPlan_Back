package travel.plan.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import travel.plan.data.dto.RplyHstrDTO;
import travel.plan.data.service.RplyHstrService;



@Slf4j
@RestController
@RequestMapping(value = "/api-docs")
public class RplyHstrController {

    @Autowired
    private RplyHstrService rplyHstrService;

    //댓글테이블 정보저장
    @PostMapping("/saveComment")
    public void saveComment(@RequestBody RplyHstrDTO rplyHstrDTO){
        rplyHstrService.saveComment(rplyHstrDTO);
    }
    
    //댓글창에 최신 5개 정렬
    @GetMapping("/getCommet")
    public List<RplyHstrDTO> getComment(@PathVariable String cont_type_id) {
        return rplyHstrService.getComments(cont_type_id);
    }
    
}

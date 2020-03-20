package controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : linghan.ma
 * @Package joinpoint.controller
 * @Description:
 * @date Date : 2020年03月12日 12:42 AM
 **/
@Controller
@RequestMapping("/test")
public class AspectController {


    @ApiOperation("添加测试")
    @GetMapping("get-advice")
    @ResponseBody
    public Object advice(){
        return "aaa";
    }
}

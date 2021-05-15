package cn.xyz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {
    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @RequestMapping("/test2")
    public ModelAndView test2() {
        return new ModelAndView("test3.html");
    }
    @RequestMapping("/test4")
    public String test4() {
        int a = 1 / 0;
        return "test3.html";
    }
}

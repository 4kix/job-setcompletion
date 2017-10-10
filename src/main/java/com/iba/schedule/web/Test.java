package com.iba.schedule.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("api")
public class Test {

    @RequestMapping(method = RequestMethod.GET, value = "/test", produces = "application/json")
    public ResponseEntity<String> test(){

        return new ResponseEntity<>("TESTING SPRING BOOT PROJECT", HttpStatus.OK);
    }
}

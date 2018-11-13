package com.github.zawataki.text2svgapi.controller;

import com.github.zawataki.text2svgapi.service.Text2SvgService;
import net.rossillo.spring.web.mvc.CacheControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Text2SvgApiController {

    @Autowired
    Text2SvgService text2SvgService;

    @CacheControl(maxAge = 60)
    @GetMapping(value = "/", produces = "image/svg+xml")
    @ResponseBody
    String textToSvg(@RequestParam String url) {
        return text2SvgService.convertToSvg(url);
    }
}

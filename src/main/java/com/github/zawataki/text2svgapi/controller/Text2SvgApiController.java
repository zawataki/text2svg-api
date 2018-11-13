package com.github.zawataki.text2svgapi.controller;

import com.github.zawataki.text2svgapi.service.Text2SvgService;
import lombok.extern.slf4j.Slf4j;
import net.rossillo.spring.web.mvc.CacheControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class Text2SvgApiController {

    @Autowired
    Text2SvgService text2SvgService;

    @CacheControl(maxAge = 60)
    @GetMapping(value = "/", produces = "image/svg+xml")
    @ResponseBody
    String textToSvg(@RequestParam(required = false) String text,
            @RequestParam(required = false) String url) {

        if (StringUtils.isAllBlank(text, url)) {
            throw new IllegalArgumentException(
                    "'text' or 'url' is required parameter");
        }

        if (StringUtils.isNoneBlank(text, url)) {
            throw new IllegalArgumentException(
                    "'text' and 'url' must not be specified at same time");
        }

        if (StringUtils.isNotBlank(text)) {
            return text2SvgService.convertTextToSvg(text);
        }

        try {
            return text2SvgService.convertUrlToSvg(new URL(url));
        } catch (MalformedURLException e) {
            log.error("Invalid URL", e);
            throw new IllegalArgumentException("Invalid 'url' parameter");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleIllegalArgumentException(
            IllegalArgumentException e) {

        // TODO See to: https://www.eisbahn.jp/yoichiro/2017/01/rfc_7807.html
        // See to: https://tools.ietf.org/html/rfc7807#section-2
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", e.getMessage());
        return errorMap;
    }
}

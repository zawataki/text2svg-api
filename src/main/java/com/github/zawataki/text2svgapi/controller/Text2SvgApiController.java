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

import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class Text2SvgApiController {

    @Autowired
    Text2SvgService text2SvgService;

    /**
     * Cache max expiration period (seconds)
     */
    private final int CACHE_MAX_AGE = 180;

    @CacheControl(maxAge = CACHE_MAX_AGE)
    @GetMapping(value = "/text", produces = "image/svg+xml")
    @ResponseBody
    String textToSvg(@RequestParam String text) {

        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("'text' is required parameter");
        }

        return text2SvgService.convertTextToSvg(text);
    }

    @CacheControl(maxAge = CACHE_MAX_AGE)
    @GetMapping(value = "/url", produces = "image/svg+xml")
    @ResponseBody
    String urlToSvg(@RequestParam String url, @RequestParam(required = false)
    @Pattern(regexp = "\\d+(-\\d+)?") String line) {

        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("'url' is required parameter");
        }

        final URL convertedUrl;
        try {
            convertedUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.error("Invalid URL", e);
            throw new IllegalArgumentException("Invalid 'url' parameter", e);
        }

        final String[] strings = line.split("-");
        if (strings.length != 2) {
            throw new IllegalArgumentException(
                    "'line' parameter must contains only two integers");
        }
        final BigInteger startLineNumber = new BigInteger(strings[0]);
        final BigInteger endLineNumber = new BigInteger(strings[1]);

        return text2SvgService.convertUrlToSvg(convertedUrl, startLineNumber,
                endLineNumber);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    private Map<String, Object> handleIllegalArgumentException(
            IllegalArgumentException e) {

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", e.getMessage());
        return errorMap;
    }
}

package com.github.zawataki.text2svgapi.controller;

import com.github.zawataki.text2svgapi.service.Text2SvgService;
import lombok.extern.slf4j.Slf4j;
import net.rossillo.spring.web.mvc.CacheControl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @GetMapping(value = "/svg", produces = "image/svg+xml")
    @ResponseBody
    String textToSvg(@RequestParam(required = false) String text,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String line) {

        if (!ObjectUtils.anyNotNull(text, url)) {
            throw new IllegalArgumentException(
                    "'text' or 'url' is required parameter");
        }

        if (text != null) {
            if (ObjectUtils.anyNotNull(url, line)) {
                throw new IllegalArgumentException(
                        "'text' parameter doesn't allow other parameter");
            }

            return text2SvgService.convertTextToSvg(text);
        }

        final URL convertedUrl;
        try {
            convertedUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.error("Invalid URL", e);
            throw new IllegalArgumentException(
                    "'url' parameter must be valid URL", e);
        }

        if (line == null) {
            return text2SvgService.convertUrlToSvg(convertedUrl);
        }

        final Pattern pattern = Pattern.compile("^([1-9]\\d*)(-([1-9]\\d*))?$");
        final Matcher matcher = pattern.matcher(line.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "'line' parameter must match pattern '" + pattern + "'");
        }

        final BigInteger startLineNumber = new BigInteger(matcher.group(1));
        final int groupNumber = 3;
        final BigInteger endLineNumber = matcher.groupCount() >= groupNumber ?
                new BigInteger(matcher.group(groupNumber)) :
                null;
        final BigInteger LIMIT_NUMBER_OF_LINE = BigInteger.valueOf(1000);
        if (endLineNumber != null) {
            if (endLineNumber.compareTo(startLineNumber) < 0) {
                throw new IllegalArgumentException(
                        "The end line number must be greater than or equal to the start line number. start line number = " +
                                startLineNumber + ", end line number = " +
                                endLineNumber);
            }
            if (endLineNumber.subtract(startLineNumber).add(BigInteger.ONE)
                    .compareTo(LIMIT_NUMBER_OF_LINE) > 0) {
                throw new IllegalArgumentException(
                        "Number of target line must be less than or equal to " +
                                LIMIT_NUMBER_OF_LINE +
                                ". start line number = " + startLineNumber +
                                ", end line number = " +
                                endLineNumber);
            }
        }
        return text2SvgService.convertUrlToSvg(convertedUrl, startLineNumber,
                endLineNumber);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    private Map<String, Object> handleIllegalArgumentException(
            IllegalArgumentException e) {

        log.error("Handle IllegalArgumentException", e);
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", e.getMessage());
        return errorMap;
    }
}

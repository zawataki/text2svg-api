package com.github.zawataki.text2svgapi.controller;

import com.github.zawataki.text2svgapi.service.Text2SvgService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.net.URL;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class Text2SvgApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Text2SvgService text2SvgService;

    private static final String SVG_ELEMENT_STR_FROM_TEXT =
            "<svg><text>Hello</text></svg>";
    private static final String SVG_ELEMENT_STR_FROM_URL =
            "<svg><text>URL</text></svg>";
    private static final String SVG_ELEMENT_STR_FROM_URL_AND_LINE =
            "<svg><text>URL with line</text></svg>";
    private static final String API_ENDPOINT = "/svg";
    private static final String NORMAL_CONTENT_TYPE =
            "image/svg+xml;charset=UTF-8";
    private static final String ERROR_CONTENT_TYPE =
            MediaType.APPLICATION_JSON_UTF8_VALUE;
    private static final String NORMAL_URL =
            "https://raw.githubusercontent.com/zawataki/text2svg-api/master/build.gradle";

    @Before
    public void setUp() {
        when(text2SvgService.convertTextToSvg(anyString())).thenReturn(
                SVG_ELEMENT_STR_FROM_TEXT);
        when(text2SvgService.convertUrlToSvg(any(URL.class))).thenReturn(
                SVG_ELEMENT_STR_FROM_URL);
        when(text2SvgService.convertUrlToSvg(any(URL.class),
                any(BigInteger.class), any(BigInteger.class))).thenReturn(
                SVG_ELEMENT_STR_FROM_URL_AND_LINE);
    }

    @Test
    public void convertNormalText() throws Exception {
        mockMvc.perform(get(API_ENDPOINT).param("text", "foo"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        NORMAL_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(SVG_ELEMENT_STR_FROM_TEXT));
    }

    @Test
    public void convertNormalUrl() throws Exception {

        mockMvc.perform(get(API_ENDPOINT).param("url", NORMAL_URL))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        NORMAL_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(SVG_ELEMENT_STR_FROM_URL));
    }

    @Test
    public void convertNonUrl() throws Exception {
        mockMvc.perform(get(API_ENDPOINT).param("url", "foo"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passAllParameters() throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("text", "foo")
                        .param("url", "bar")
                        .param("line", "2-4"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passNoParameter() throws Exception {
        mockMvc.perform(get(API_ENDPOINT))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterContainsNonNumber() throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL).param("line", "a"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterContainsNonNumberWithHyphen()
            throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL).param("line", "a-b"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterStartsWithZero() throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL).param("line", "0-3"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterStartsWithHyphen() throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL).param("line", "-3"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterNumOfLineIsGreaterThan1000() throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL)
                        .param("line", "1-1001"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterNumOfLineEqualsTo1000() throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL)
                        .param("line", "1001-2000"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        NORMAL_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(SVG_ELEMENT_STR_FROM_URL_AND_LINE));
    }

    @Test
    public void passLineParameterEndLineNumIsGreaterThanStartLineNum()
            throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL)
                        .param("line", "15-14"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passLineParameterEndLineNumEqualsToStartLineNum()
            throws Exception {
        mockMvc.perform(
                get(API_ENDPOINT).param("url", NORMAL_URL)
                        .param("line", "15-15"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        NORMAL_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(SVG_ELEMENT_STR_FROM_URL_AND_LINE));
    }
}

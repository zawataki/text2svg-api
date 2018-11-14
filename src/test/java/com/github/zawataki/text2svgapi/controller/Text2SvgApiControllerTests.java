package com.github.zawataki.text2svgapi.controller;

import com.github.zawataki.text2svgapi.service.Text2SvgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

    private final String API_ENDPOINT_TEXT = "/text";
    private final String API_ENDPOINT_URL = "/url";
    private final String NORMAL_CONTENT_TYPE = "image/svg+xml;charset=UTF-8";
    private final String ERROR_CONTENT_TYPE =
            MediaType.APPLICATION_JSON_UTF8_VALUE;

    @Test
    public void convertNormalText() throws Exception {
        when(text2SvgService.convertTextToSvg(anyString())).thenReturn(
                "<svg><text>Hello</text></svg>");

        mockMvc.perform(get(API_ENDPOINT_TEXT).param("text", "foo"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        NORMAL_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string("<svg><text>Hello</text></svg>"));
    }

    @Test
    public void convertNormalUrl() throws Exception {
        final String response = "<svg><text>Hello</text></svg>";
        when(text2SvgService.convertUrlToSvg(any(URL.class))).thenReturn(
                response);

        final String normalUrl =
                "https://raw.githubusercontent.com/zawataki/text2svg-api/master/build.gradle";
        mockMvc.perform(get(API_ENDPOINT_URL).param("url", normalUrl))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        NORMAL_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void convertNonUrl() throws Exception {
        final String response = "<svg><text>Hello</text></svg>";
        when(text2SvgService.convertUrlToSvg(any(URL.class))).thenReturn(
                response);

        mockMvc.perform(get(API_ENDPOINT_URL).param("url", "foo"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passAllParameters() throws Exception {
        when(text2SvgService.convertTextToSvg(anyString())).thenReturn(
                "<svg><text>Hello</text></svg>");
        when(text2SvgService.convertUrlToSvg(any(URL.class))).thenReturn("");

        mockMvc.perform(
                get(API_ENDPOINT_TEXT).param("text", "foo").param("url", "bar"))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void passNoParameter() throws Exception {
        when(text2SvgService.convertTextToSvg(anyString())).thenReturn(
                "<svg><text>Hello</text></svg>");
        when(text2SvgService.convertUrlToSvg(any(URL.class))).thenReturn("");

        mockMvc.perform(get(API_ENDPOINT_TEXT))
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        ERROR_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }
}

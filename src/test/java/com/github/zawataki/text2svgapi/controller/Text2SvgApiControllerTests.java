package com.github.zawataki.text2svgapi.controller;

import com.github.zawataki.text2svgapi.service.Text2SvgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

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

    private final String API_ENDPOINT = "/";

    @Test
    public void convertNormalText() throws Exception {
        when(text2SvgService.convertTextToSvg(anyString())).thenReturn(
                "<svg><text>Hello</text></svg>");

        mockMvc.perform(get(API_ENDPOINT).param("text", "hoge"))
                .andDo(print())
                .andExpect(header().exists("Cache-Control"))
                .andExpect(header().string("Content-Type",
                        "image/svg+xml;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("<svg><text>Hello</text></svg>"));
    }

    @Test
    public void passAllParameters() throws Exception {
        when(text2SvgService.convertTextToSvg(anyString())).thenReturn(
                "<svg><text>Hello</text></svg>");
        when(text2SvgService.convertUrlToSvg(any(URL.class))).thenReturn("");

        mockMvc.perform(
                get(API_ENDPOINT).param("text", "foo").param("url", "bar"))
                .andDo(print())
                .andExpect(header().exists("Cache-Control"))
                .andExpect(header().string("Content-Type",
                        "application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }
}

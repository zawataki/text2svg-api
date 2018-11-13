package com.github.zawataki.text2svgapi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Text2SvgServiceTests {

    @Autowired
    Text2SvgService text2SvgService;

    @Test
    public void convertToSvg() {
        final String inputText = "Hello";
        final String svg = text2SvgService.convertTextToSvg(inputText);

        assertThat("Returned string does NOT start with svg tag", svg,
                startsWith("<svg "));
        assertThat("Returned string does NOT end with svg tag", svg.trim(),
                endsWith("</svg>"));
        assertThat("Returned string does NOT contain text start tag", svg,
                containsString("<text"));
        assertThat("Returned string does NOT contain text end tag", svg,
                containsString("</text>"));
        assertThat("Returned string does NOT contain a given text", svg,
                containsString(inputText));
    }
}
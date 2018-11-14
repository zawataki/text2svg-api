package com.github.zawataki.text2svgapi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

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
    public void convertNormalTextToSvg() {
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

    @Test
    public void convertNormalUrlToSvg() throws MalformedURLException {
        final URL url =
                new URL("https://raw.githubusercontent.com/zawataki/text2svg-api/master/build.gradle");
        final String svg = text2SvgService.convertUrlToSvg(url);

        assertThat("Returned string does NOT start with svg tag", svg,
                startsWith("<svg "));
        assertThat("Returned string does NOT end with svg tag", svg.trim(),
                endsWith("</svg>"));
        assertThat("Returned string does NOT contain text start tag", svg,
                containsString("<text"));
        assertThat("Returned string does NOT contain text end tag", svg,
                containsString("</text>"));
        assertThat("Returned string does NOT contain a given text", svg,
                containsString("buildscript"));
    }

    @Test
    public void convertNormalUrlWith5LinesToSvg() throws MalformedURLException {
        final URL url =
                new URL("https://raw.githubusercontent.com/zawataki/text2svg-api/master/build.gradle");
        final String svg = text2SvgService.convertUrlToSvg(url);

        assertThat("Returned string does NOT start with svg tag", svg,
                startsWith("<svg "));
        assertThat("Returned string does NOT end with svg tag", svg.trim(),
                endsWith("</svg>"));
        assertThat("Returned string does NOT contain text start tag", svg,
                containsString("<text"));
        assertThat("Returned string does NOT contain text end tag", svg,
                containsString("</text>"));
        assertThat("Returned string does NOT contain a given text", svg,
                containsString("buildscript"));
    }
}

package com.github.zawataki.text2svgapi.service;

import org.apache.commons.lang3.Range;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.HtmlUtils;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
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

        final String expectedString = "buildscript {";
        assertThat("Returned string must contain '" + expectedString + "'", svg,
                containsString(HtmlUtils.htmlEscape(expectedString)));
    }

    @Test
    public void convertNormalUrlWithNumOfLineToSvg()
            throws MalformedURLException {
        final URL url =
                new URL("https://raw.githubusercontent.com/zawataki/text2svg-api/master/build.gradle");
        final BigInteger startLineNumber = BigInteger.valueOf(2);
        final BigInteger endLineNumber = BigInteger.valueOf(4);
        final String svg = text2SvgService.convertUrlToSvg(url, startLineNumber,
                endLineNumber);

        assertThat("Returned string does NOT start with svg tag", svg,
                startsWith("<svg "));
        assertThat("Returned string does NOT end with svg tag", svg.trim(),
                endsWith("</svg>"));
        assertThat("Returned string does NOT contain text start tag", svg,
                containsString("<text"));
        assertThat("Returned string does NOT contain text end tag", svg,
                containsString("</text>"));

        List<String> expectedLines = Arrays.asList("buildscript {", "    ext {",
                "        springBootVersion = '2.1.0.RELEASE'", "    }",
                "    repositories {");

        BigInteger numberOfLine = BigInteger.ONE;
        for (String line : expectedLines) {

            final String message;
            final Matcher matcher;
            if (Range.between(startLineNumber, endLineNumber)
                    .contains(numberOfLine)) {
                message = "Returned string must contain '" + line + "'";
                matcher = containsString(HtmlUtils.htmlEscape(line));
            } else {
                message = "Returned string must NOT contain '" + line + "'";
                matcher = not(containsString(HtmlUtils.htmlEscape(line)));
            }

            assertThat(message + ". numberOfLine = " + numberOfLine, svg,
                    matcher);

            numberOfLine = numberOfLine.add(BigInteger.ONE);
        }
    }
}

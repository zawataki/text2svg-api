package com.github.zawataki.text2svgapi.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Text2SvgApiControllerTests {

    @Autowired
    protected Text2SvgApiController text2SvgApiController;

    @Test
    public void textToSvg() {
        final String result = text2SvgApiController.textToSvg("hoge");
        Assert.assertThat("Returned string is empty", result, not(""));
        Assert.assertThat("Returned string is NOT empty", result,
                startsWith("<svg"));
    }
}

package com.example.helloworld;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    protected DemoApplication demoApplication;

    @Test
    public void textToSvg() throws IOException {
        final String result = demoApplication.textToSvg("hoge");
        Assert.assertThat("Returned string is empty", result, not(""));
        Assert.assertThat("Returned string is NOT empty", result, startsWith("<svg"));
    }
}

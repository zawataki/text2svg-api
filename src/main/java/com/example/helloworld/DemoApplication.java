package com.example.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Controller
@SpringBootApplication
public class DemoApplication {

    @Autowired
    protected ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    @ResponseBody
    String textToSvg(@RequestParam String url) throws IOException {

        final Resource resource = resourceLoader.getResource("classpath:" + Paths.get("static", "sample.svg"));
        final String fileContents = Files.lines(resource.getFile().toPath()).collect(Collectors.joining());

        return fileContents;
    }
}

package com.example.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    String textToSvg(@RequestParam String url) {

        final String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "     width=\"500\" height=\"40\" viewBox=\"0 0 500 40\">\n" +
                "\n" +
                "    <rect fill=\"#fff\" id=\"canvas_background\" width=\"500\" height=\"40\" y=\"0\"\n" +
                "          x=\"0\"/>\n" +
                "\n" +
                "    <text x=\"0\" y=\"35\" font-family=\"Verdana\" font-size=\"35\">\n" +
                "        Hello, World\n" +
                "    </text>\n" +
                "</svg>\n";

        return svg;
    }
}

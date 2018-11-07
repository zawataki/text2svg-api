package com.example.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping(value = "/", produces = "image/svg+xml")
    @ResponseBody
    String textToSvg(@RequestParam String url) {
        return convertToSvg(url);
    }

    private String convertToSvg(String text) {

        final int fontSize = 30;
        final int width = fontSize * text.length();
        final int height = fontSize + 10;

        // Same with GitHub code font
        final String fontFamily =
                "SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace";

        return "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "     width=\"" + width + "\" height=\"" + height + "\">\n" +
                "\n" +
                "    <text x=\"0\" y=\"0\" dominant-baseline=\"text-before-edge\"\n" +
                "          font-family=\"" + fontFamily + "\"\n" +
                "          font-size=\"" + fontSize + "\">\n" +
                "        " + text + "\n" +
                "    </text>\n" +
                "</svg>\n";
    }
}

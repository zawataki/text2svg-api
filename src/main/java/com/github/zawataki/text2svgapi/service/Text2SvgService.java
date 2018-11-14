package com.github.zawataki.text2svgapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
@Service
public class Text2SvgService {

    /**
     * @param text a text to convert to SVG
     * @return a string representing SVG element
     */
    public String convertTextToSvg(String text) {

        final int fontSize = 14;
        final int width = fontSize * text.length();
        final int height = fontSize + 10;

        // Same with GitHub code font
        final String fontFamily =
                "SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace";

        return "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" + "     width=\"" +
                width + "\" height=\"" + height + "\">\n" + "\n" +
                "    <text x=\"0\" y=\"0\" dominant-baseline=\"text-before-edge\"\n" +
                "          font-family=\"" + fontFamily + "\"\n" +
                "          font-size=\"" + fontSize + "\">\n" + "        " +
                text + "\n" + "    </text>\n" + "</svg>\n";
    }

    /**
     * @param url a URL representing a text file to convert to SVG
     * @return a string representing SVG element
     */
    public String convertUrlToSvg(URL url) {
        log.info("A given URL = " + url);

        // TODO Specify line number
        final RestTemplate restTemplate = new RestTemplate();
        final String content;
        try {
            content = restTemplate.getForObject(url.toURI(), String.class);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }

        log.info("content: " + content);

        return convertTextToSvg(content);
    }
}

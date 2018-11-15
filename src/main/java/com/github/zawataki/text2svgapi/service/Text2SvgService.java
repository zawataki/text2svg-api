package com.github.zawataki.text2svgapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class Text2SvgService {

    public final static BigInteger LIMIT_NUMBER_OF_LINE =
            BigInteger.valueOf(1000);

    /**
     * @param text a text to convert to SVG
     *
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
     *
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

        final String targetContent = multipleLineStringToSvg(content);
        log.info("content: " + targetContent);

        return convertTextToSvg(targetContent);
    }

    /**
     * @param url the URL representing a text file to convert to SVG
     * @param startLineNumber the start line number
     * @param endLineNumber the end line number. if null, regards as end of
     * file
     *
     * @return a string representing SVG element
     */
    public String convertUrlToSvg(URL url, BigInteger startLineNumber,
            BigInteger endLineNumber) {

        final RestTemplate restTemplate = new RestTemplate();
        final String content;
        try {
            content = restTemplate.getForObject(url.toURI(), String.class);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }

        final String targetContent = multipleLineStringToSvg(content);
        log.info("targetContent: " + targetContent);

        return convertTextToSvg(targetContent);
    }

    private String multipleLineStringToSvg(String multipleLineString) {

        if (multipleLineString == null) {
            return "";
        }

        final String[] lines =
                multipleLineString.replaceAll("\\r\\n", "\n").split("\\n");

        return Stream.of(lines)
                .map(line -> "<tspan dy=\"1.2em\" x=\"0\">" + line + "</tspan>")
                .collect(Collectors.joining());
    }

}

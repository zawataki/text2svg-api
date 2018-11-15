package com.github.zawataki.text2svgapi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<String> multiLineSvgStr =
                multipleLineStringToSvg(text, fontSize);

        final Integer maxLineLength = multiLineSvgStr.parallelStream()
                .max(Comparator.comparingInt(String::length))
                .map(s -> s.length())
                .orElse(0);

        final int width = fontSize * maxLineLength;
        final int heightMargin = 10;
        final int height = fontSize * multiLineSvgStr.size() + heightMargin;

        // Same with GitHub code font
        final String fontFamily =
                "SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace";

        return "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" + "     width=\"" +
                width + "\" height=\"" + height + "\">\n" + "\n" +
                "    <text x=\"0\" y=\"0\" dominant-baseline=\"text-before-edge\"\n" +
                "          font-family=\"" + fontFamily + "\"\n" +
                "          font-size=\"" + fontSize + "\">\n" + "        " +
                String.join(StringUtils.LF, multiLineSvgStr) + "\n" +
                "    </text>\n" + "</svg>\n";
    }

    /**
     * @param url a URL representing a text file to convert to SVG
     *
     * @return a string representing SVG element
     */
    public String convertUrlToSvg(URL url) {
        return convertUrlToSvg(url, BigInteger.ONE, LIMIT_NUMBER_OF_LINE);
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

        log.info("Convert from URL = " + url);

        final RestTemplate restTemplate = new RestTemplate();
        final String content;
        try {
            content = restTemplate.getForObject(url.toURI(), String.class);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }

        final String targetLines = splitByNewLine(content).stream()
                .skip(startLineNumber.subtract(BigInteger.ONE).longValue())
                .limit(endLineNumber.subtract(startLineNumber)
                        .add(BigInteger.ONE)
                        .longValue())
                .collect(Collectors.joining(StringUtils.LF));

        return convertTextToSvg(targetLines);
    }

    private List<String> splitByNewLine(String multipleLineString) {

        if (multipleLineString == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(
                multipleLineString.replaceAll("\\r\\n", StringUtils.LF)
                        .split("\\n"));
    }

    private List<String> multipleLineStringToSvg(String multipleLineString,
            int fontSize) {

        if (multipleLineString == null) {
            return new ArrayList<>();
        }

        int i = 0, jackpot = 0;
        final List<String> svgStrings = new ArrayList<>();
        for (String line : splitByNewLine(multipleLineString)) {

            // a shift along the y-axis on the position from prev element
            final int shiftPositionY = i == 0 || StringUtils.isBlank(line) ?
                    0 :
                    fontSize + jackpot;

            svgStrings.add("<tspan dy=\"" + shiftPositionY + "\" x=\"0\">" +
                    HtmlUtils.htmlEscape(line) + "</tspan>");

            if (StringUtils.isBlank(line)) {
                jackpot += fontSize;
            } else {
                jackpot = 0;
            }

            i++;
        }

        return svgStrings;
    }

}

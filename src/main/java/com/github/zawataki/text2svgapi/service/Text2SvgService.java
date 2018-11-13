package com.github.zawataki.text2svgapi.service;

import org.springframework.stereotype.Service;

@Service
public class Text2SvgService {

    /**
     * @param text a text to convert to SVG
     *
     * @return a string representing SVG element
     */
    public String convertToSvg(String text) {

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
}

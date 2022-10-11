package com.ironhack.otxt;

public class TxTUtils {
    /**
     * Method to center string by adding blank spaces both sides
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     * @return resulting String
     */
    public static String centerLine(String line, int width) {
        int leftSpace, rightSpace, remainSpace;
        remainSpace = Math.max(width - countValidCharacters(line), 0);
        leftSpace = remainSpace > 0 ? remainSpace / 2 : 0;
        rightSpace = (remainSpace == 0 || remainSpace % 2 == 0) ? leftSpace : leftSpace + 1;
        return (ColorFactory.BLANK_SPACE.repeat(leftSpace)) + line + (ColorFactory.BLANK_SPACE.repeat(rightSpace));
    }

    /**
     * Method to count characters that will be printed, it doesn't count scape characters nor colors or styles tags.
     *
     * @param line line to count
     * @return integer value of char count
     */
    public static int countValidCharacters(String line) {
        int colourCount = 0;
        int charCount = 0;
        var chArray = line.toCharArray();
        for (char ch : chArray) {
            charCount++;
            if (ch == ColorFactory.COLOR_CHAR) colourCount++;
            if (ch == ColorFactory.NEW_LINE_CH || ch == ColorFactory.DELETE_CURRENT_LINE) charCount--;
        }
        return charCount - (colourCount * ColorFactory.COLOR_LABEL_CHAR_SIZE);
    }

    /**
     * Method to align text on right by adding blank spaces at start of the string
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     * @return resulting String
     */
    public static String lineToRight(String line, int width) {
        int count = width - countValidCharacters(line);
        return (count > 0 ? ColorFactory.BLANK_SPACE.repeat(count) : "") + line;
    }

    /**
     * Recursive method that checks line size. If it's bigger than limit it splits the String recursively
     * until all resulting lines fits on specified limit char size
     *
     * @param line  text to analyze
     * @param limit maximum chars width
     * @return String[] array with all resulting strings
     */
    public static String[] wrapLine(String line, int limit) {
        String[] result;

        var wordList = line.replace(ColorFactory.BLANK_SPACE + ColorFactory.BLANK_SPACE, "**").split(ColorFactory.BLANK_SPACE);
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder("");
        int charCounter = 0;
        for (String word : wordList) {
            var wordChar = countValidCharacters(word);
            charCounter += wordChar + (java.util.Objects.equals(word, "**") ? 0 : 1);
            if (wordChar > limit) {
                line1.append(word.substring(0, limit).replace("*", " "));
                line2.append("-" + word.substring(limit).replace("*", " "));

            } else {
                if (charCounter <= limit) line1.append(" ").append(word.replace("**", "  "));
                else line2.append(" ").append(word.replace("**", "  "));
            }
        }
        if (charCounter > limit * 2) {
            var auxList = wrapLine(line2.toString(), limit);
            var resVal = new String[auxList.length + 1];
            resVal[0] = line1.toString();
            System.arraycopy(auxList, 0, resVal, 1, auxList.length);
            result = resVal;
        } else {
            result = new String[]{line1.toString(), line2.toString()};
        }
        return result;
    }

    /**
     * Method to fill available right space of a string with blank spaces
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     * @return resulting String
     */
    public static String fillLine(String line, int width) {
        return line + (ColorFactory.BLANK_SPACE.repeat(Math.max(width - countValidCharacters(line), 0)));
    }

    /**
     * Quit all Color, Style and BG modifiers
     *
     * @param line text to modify
     * @return text without those characters
     */
    public static String removeStyleAndColorLine(String line) {
        var textParts = line.split(String.valueOf(ColorFactory.COLOR_CHAR));
        for (int i = 1; i < textParts.length; i++) {
            textParts[i] = textParts[i].substring(ColorFactory.COLOR_LABEL_CHAR_SIZE - 1);
        }
        var sb = new StringBuilder();
        for (String part : textParts) sb.append(part);
        return sb.toString();
    }

    /**
     * Splits a multi line string into array of String lines
     *
     * @param text multi line text
     * @return String Lines Array
     */
    public static String[] splitTextInLines(String text) {
        return text.split(ColorFactory.NEW_LINE);
    }

    public static TxTObject alignTextTop(TxTObject txt, int size) {
        if (txt.getTotalHeight() < size) {
            txt.addText(ColorFactory.BLANK_SPACE);
            alignTextTop(txt, size);
        }
        return txt;
    }

    public static String deleteDuplicateResets(String res, String target) {
        while (res.contains(target + target)) {
            res = res.replace(target + target, target);
        }
        return res;
    }
}

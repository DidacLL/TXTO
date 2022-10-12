package com.ironhack.otxt;

import java.util.ArrayList;
import java.util.List;

import static com.ironhack.otxt.TxTFormatters.*;
import static com.ironhack.otxt.TxTFormatters.TxTStyle.*;

/**
 * Utils Class with convenient methods for TxTObject management
 * @author DidacLl
 * @since v0.1
 */
public class TxTUtils {


    /**
     * Method to count characters that will be printed, it doesn't count scape characters nor colors or styles tags.
     *
     * @param line line to count
     * @return integer value of char count
     */
    static int countValidCharacters(String line) {
        int colourCount = 0;
        int charCount = 0;
        var chArray = line.toCharArray();
        for (char ch : chArray) {
            charCount++;
            if (ch == TxTFormatters.COLOR_CHAR) colourCount++;
            if (ch == TxTFormatters.NEW_LINE_CH || ch == TxTFormatters.DELETE_CURRENT_LINE) charCount--;
        }
        return charCount - (colourCount * TxTFormatters.COLOR_LABEL_CHAR_SIZE);
    }

    /**
     * Method that returns a Substring of given String plus TxTFormatters
     *
     * @param word  String to be cut off
     * @param start "visible" char where to start (inclusive)
     * @param end   "vidible" char where to end (exclusive)
     * @return Resulting String with format modifiers included
     */
    static String validSubstring(String word, int start, int end) {
        var arr = word.toCharArray();
        var sb = new StringBuilder();
        int validCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (arr[i] == COLOR_CHAR) {
                for (int j = 0; j < COLOR_LABEL_CHAR_SIZE; j++) sb.append(arr[i + j]);
                i += COLOR_LABEL_CHAR_SIZE-1;
            } else {
                if (validCount >= start && validCount < end) sb.append(arr[i]);
                validCount++;
            }
            if (validCount >= end) break;
        }
        if(word.contains(RESET.toString()))sb.append(RESET);
        return sb.toString();
    }

    /**
     * Method to align text on right by adding blank spaces at start of the string
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     * @return resulting String
     */
    static String lineToRight(String line, int width) {
        int count = width - countValidCharacters(line);
        return (count > 0 ? BLANK_SPACE.repeat(count) : "") + line;
    }

    /**
     * Method to center string by adding blank spaces both sides
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     * @return resulting String
     */
    static String centerLine(String line, int width) {
        int leftSpace, rightSpace, remainSpace;
        remainSpace = Math.max(width - countValidCharacters(line), 0);
        leftSpace = remainSpace > 0 ? remainSpace / 2 : 0;
        rightSpace = (remainSpace % 2 == 0) ? leftSpace : leftSpace + 1;
        return (BLANK_SPACE.repeat(leftSpace)) + line + (BLANK_SPACE.repeat(rightSpace));
    }

    /**
     * Method to fill available right space of a string with blank spaces
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     * @return resulting String
     */
    static String fillLine(String line, int width) {
        return line + (BLANK_SPACE.repeat(Math.max(width - countValidCharacters(line), 0)));
    }

    /**
     * Recursive method that checks line size. If it's bigger than limit it splits the String recursively
     * until all resulting lines fits on specified limit char size
     *
     * @param line  text to analyze
     * @param limit maximum chars width
     * @return String[] array with all resulting strings
     */
    static List<String> wrapLine(String line, int limit) {
        var result = new ArrayList<String>();
        var sb = new StringBuilder();
        int currentLineLength = 0;
        String word;
        int wordLength;
        var wordList = new ArrayList<>(List.of(line.replace(BLANK_SPACE + BLANK_SPACE, "**").split(BLANK_SPACE)));

        for (String s : wordList) {
            word = s;
            wordLength = countValidCharacters(word);
            while (wordLength + currentLineLength > limit) {
                int restLength = limit - currentLineLength;
                if (restLength > Math.min(2, limit) || sb.isEmpty()) {
                    result.add(sb.append(validSubstring(word, 0, restLength - (restLength > 2 ? 1 : 0))).append(restLength > 2 ? "-" : "").toString());
                    word = validSubstring(word, restLength - (restLength > 2 ? 1 : 0), wordLength);
                    wordLength = countValidCharacters(word);
                    sb = new StringBuilder(restLength > 2 ? "-" : "");
                    currentLineLength = sb.length();
                } else {
                    result.add(sb.toString());
                    sb = new StringBuilder();
                    currentLineLength = 0;
                }
            }
            sb.append(word).append(BLANK_SPACE);
            currentLineLength += ++wordLength;


        }

        return result;
    }


    /**
     * Quit all Color, Style and BG modifiers
     *
     * @param line text to modify
     * @return text without those characters
     */
    static String removeStyleAndColorLine(String line) {
        var textParts = line.split(String.valueOf(TxTFormatters.COLOR_CHAR));
        for (int i = 1; i < textParts.length; i++) {
            textParts[i] = textParts[i].substring(TxTFormatters.COLOR_LABEL_CHAR_SIZE - 1);
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
    static String[] splitTextInLines(String text) {
        return text.split(TxTFormatters.NEW_LINE);
    }


    /**Method that Deletes duplicated formatters
     * @param str String to be modified
     * @param target Formatter to search for duplicates
     * @return Modified String
     */
    static String deleteDuplicateResets(String str, String target) {
        while (str.contains(target + target)) {
            str = str.replace(target + target, target);
        }
        return str;
    }
}

package com.ironhack.otxt;

import com.ironhack.otxt.TxTFormatters.TxTBackground;
import com.ironhack.otxt.TxTFormatters.TxTColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ironhack.otxt.TxTFormatters.NEW_LINE_CH;
import static com.ironhack.otxt.TxTFormatters.TxTStyle.RESET;
import static com.ironhack.otxt.TxTUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class TxTUtilsTest {
    String text=("Praeter propria digressurus alias digressurus existimo praeter \n mirari quosdam nihil vilitates si perstringam summatim sponte harum similis quae cum contigerit narratur quosdam a forsitan existimo \nRomae oratio Romae narratur monstranda propria cum mirari cum haec harum nusquam tabernas vilitates lecturos haec harum summatim perstringam haec vilitates harum praeter quae et quosdam propria narratur deflexerit mirari deflexerit quoniam contigerit forsitan veritate harum haec gererentur lecturos quamobrem mirari peregrinos existimo \n quae mirari posse monstranda propria posse veritate ad quamobrem haec veritate gererentur deflexerit Romae harum peregrinos deflexerit veritate Romae et praeter seditiones cum a Romae propria nihil perstringam quosdam nusquam similis cum.");

    @Test
    @DisplayName("Count Characters with formatters")
    void countValidCharacters_test1() {
        assertEquals(TxTUtils.countValidCharacters(TxTColor.RED.toString() + TxTBackground.BG_WHITE + "patata" + RESET), "patata".length());
    }
    @Test
    @DisplayName("Count Characters with formatters and empty text")
    void countValidCharacters_test2() {
        assertEquals(0, TxTUtils.countValidCharacters(TxTColor.RED.toString() + TxTBackground.BG_WHITE + RESET));
    }
    @Test
    @DisplayName("Count Characters with formatters and spaces as text")
    void countValidCharacters_test3() {
        assertEquals(3, TxTUtils.countValidCharacters(TxTColor.RED.toString() + TxTBackground.BG_WHITE + "   "+RESET));
    }
    @Test
    @DisplayName("Count Characters with invalid chars in text")
    void countValidCharacters_test4() {
        System.out.println(TxTColor.RED.toString() + TxTBackground.BG_WHITE + "€á"+RESET);
        assertEquals(2, TxTUtils.countValidCharacters(TxTColor.RED.toString() + TxTBackground.BG_WHITE + "€á"+RESET));
    }

    @Test
    @DisplayName("Count Characters with long multiline text")
    void countValidCharacters_test5() {
        int nlCount=0;
        for(char ch: text.toCharArray())if(ch==NEW_LINE_CH)nlCount++;
        assertEquals(text.length()-nlCount, TxTUtils.countValidCharacters(TxTColor.RED.toString() + TxTBackground.BG_WHITE + text+RESET));
    }

    @Test
    @DisplayName("Get a substring with valid characters")
    void validSubstring_test1() {
        String str= TxTColor.RED.toString() + TxTBackground.BG_WHITE + "patata"+RESET;
        String line = validSubstring(str, 0, 3);
        System.out.println(line);
        assertEquals(3,countValidCharacters(line));
    }
    @Test
    @DisplayName("Get a substring without valid characters")
    void validSubstring_test2() {
        String str= TxTColor.RED.toString() + TxTBackground.BG_WHITE+RESET ;
        String line = validSubstring(str, 0, 3);
        System.out.println(line);
        assertEquals(0,countValidCharacters(line));
    }
    @Test
    @DisplayName("Get all as substring")
    void validSubstring_test3() {
        String str= TxTColor.RED.toString() + TxTBackground.BG_WHITE+"patata"+RESET ;
        String line = validSubstring(str, 0, countValidCharacters(str));
        System.out.println(line);
        assertEquals(countValidCharacters(str),countValidCharacters(line));
    }

    @Test
    @DisplayName("Get end chars as substring")
    void validSubstring_test4() {
        String str= TxTColor.RED.toString() + TxTBackground.BG_WHITE+"patata"+RESET ;
        String line = validSubstring(str, 4, countValidCharacters(str));
        System.out.println(line);
        assertEquals(2,countValidCharacters(line));
    }

    @Test
    @DisplayName("Line to right spaces OK")
    void lineToRight_test1() {
        assertEquals(100,lineToRight("patata",100).length());
    }
    @Test
    @DisplayName("Line to right oversize NOK")
    void lineToRight_test2() {
        assertEquals("patata".length(),lineToRight("patata",4).length());
    }


    @Test
    void centerLine_test() {
    }

    @Test
    void fillLine_test() {
    }

    @Test
    void wrapLine_test() {
    }

    @Test
    void removeStyleAndColorLine_test() {
    }

    @Test
    void splitTextInLines_test() {
    }

    @Test
    void deleteDuplicateResets_test() {
    }
}
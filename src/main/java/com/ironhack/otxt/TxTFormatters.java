package com.ironhack.otxt;

import com.ironhack.otxt.interfaces.TxTFormat;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.ironhack.otxt.TxTFormatters.TxTStyle.RESET;

public class TxTFormatters {

    public static final char DELETE_CURRENT_LINE='\r';
    public static final char NEW_LINE_CH = '\n';
    public static final String NEW_LINE = "\n";
    public static final char BLANK_SPACE_CH = ' ';
    public static final String BLANK_SPACE = " ";


    public static final int COLOR_LABEL_CHAR_SIZE=6;
    public static final char COLOR_CHAR='\u001B';
    public enum TxTVAlignment implements TxTFormat {TOP,MIDDLE,BOTTOM}
    public enum TxTHAlignment implements TxTFormat {LEFT,CENTER,RIGHT}
    public enum TxTPrintType implements TxTFormat {BLOCK,LINE,TYPER_WRITER,ANIMATE}

    public enum TxTStyle implements TxTFormat {
        NULL_STYLE(""),
        BOLD("[001m"),
        UNDERLINE("[004m"),
        BLINK("[005m"),
        REVERSED("[007m"),
        RESET( "[000m");
        final String label;
        TxTStyle(String label){
            this.label=label;
        }

        @Override
        public String toString() {
            return this.equals(NULL_STYLE)?"":COLOR_CHAR+label;
        }

    }
    public enum TxTBackground implements TxTFormat {
        BG_BLACK( "[040m"),
        BG_RED( "[041m"),
        BG_GREEN( "[042m"),
        BG_YELLOW( "[043m"),
        BG_BLUE( "[044m"),
        BG_PURPLE( "[045m"),
        BG_CYAN( "[046m"),
        BG_WHITE( "[047m"),
        BG_BRIGHT_BLACK( "[100m"),
        BG_BRIGHT_RED( "[101m"),
        BG_BRIGHT_GREEN( "[102m"),
        BG_BRIGHT_YELLOW( "[103m"),
        BG_BRIGHT_BLUE( "[104m"),
        BG_BRIGHT_PURPLE( "[105m"),
        BG_BRIGHT_CYAN( "[106m"),
        BG_BRIGHT_WHITE( "[107m"),
        BG_NULL("[049m");
        final String label;
        TxTBackground(String label){
            this.label=label;
        }

        @Override
        public String toString() {
            return this.equals(BG_NULL)?"":COLOR_CHAR+label;
        }

    }
    public enum TxTColor implements TxTFormat {
        BLACK( "[030m"),
        BRIGHT_BLACK( "[090m"),
        WHITE( "[037m"),
        BRIGHT_WHITE( "[097m"),
        RED( "[031m"),
        BRIGHT_RED( "[091m"),
        BRIGHT_YELLOW( "[093m"),
        YELLOW( "[033m"),
        GREEN( "[032m"),
        BRIGHT_GREEN( "[092m"),
        BRIGHT_CYAN( "[096m"),
        CYAN( "[036m"),
        BRIGHT_BLUE( "[094m"),
        BLUE( "[034m"),
        PURPLE( "[035m"),
        BRIGHT_PURPLE( "[095m"),
        NULL_COLOR("[039m");


        final String label;
            TxTColor(String label){
                this.label=label;
            }

        @Override
        public String toString() {
            return this.equals(NULL_COLOR)?"": COLOR_CHAR+label;
        }
    }

    private static TxTColor getRandomColor(){
        int num;
            num = new java.util.Random().nextInt(4, TxTColor.values().length);
        return TxTColor.values()[num];
    }
    private static TxTColor getNextRainbowColor(TxTColor currentColor){
        int num= currentColor.ordinal()+1;
        if(num>= TxTColor.values().length||num<4)num=4;
        return TxTColor.values()[num];
    }

    private static String changeColors(String text, TxTColor...newColors){
        var charList= text.toCharArray();
        boolean isRandom = true;
        for (int i = 0; i < charList.length; i++) {
            if (i< newColors.length) isRandom=false;
            var currentChar=charList[i];
            if(charList[i]==COLOR_CHAR){
                var color = (isRandom?getRandomColor().toString():newColors[i].toString()).toCharArray();
                System.arraycopy(color, 0, charList, i, COLOR_LABEL_CHAR_SIZE - 1);
                i+=COLOR_LABEL_CHAR_SIZE-1;
            }
        }
        return new String(charList);

    }
    public static String formatText(String text, TxTFormat... txTFormats){
        return Arrays.stream(txTFormats).map(String::valueOf).collect(Collectors.joining())+text+RESET;
    }
    public static String rainbowCharacters(String line,int startVal){
        var color= TxTColor.values()[startVal];
        var charList=line.toCharArray();
        var sb= new StringBuilder();
        for (int i = 0; i < charList.length; i++) {
            char ch = charList[i];
            if (ch != BLANK_SPACE_CH) {
                if (ch==COLOR_CHAR)i+=COLOR_LABEL_CHAR_SIZE-1;
                else {
                    sb.append(color);
                    color = getNextRainbowColor(color);
                }
            }
            sb.append(ch);
        }
        return sb.append(RESET).toString();
    }

    public static boolean isASpecialCharacter(char ch){
        return ch==NEW_LINE_CH || ch==DELETE_CURRENT_LINE ||ch==COLOR_CHAR;
    }

    public static boolean containsSpecialCharacters(String str){
        return str.contains(NEW_LINE)||str.contains(DELETE_CURRENT_LINE+"")||str.contains(COLOR_CHAR+"[");
    }

}

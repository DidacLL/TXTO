package com.ironhack.otxt;


import com.ironhack.otxt.interfaces.TxTFormat;

import javax.lang.model.type.ErrorType;
import java.util.ArrayList;

import static com.ironhack.otxt.TxTFormatters.*;
import static com.ironhack.otxt.TxTFormatters.TxTVAlignment.TOP;


public class TxTPrinter {
    public final int TXT_MAX_WITH, TXT_MAX_HEIGHT;
    public final TxTBackground TXT_DEF_BG;
    public final TxTColor TXT_DEF_COLOR;
    public final TxTStyle TXT_DEF_STYLE;
    public final double TXT_PRINT_RATE;
    private final ArrayList<TxTObject> printQueue;

    //------------------------------------------------------------------------------------------------------CONSTRUCTORS
    public TxTPrinter() {
        TXT_MAX_WITH = 120;
        TXT_MAX_HEIGHT = 25;
        TXT_DEF_BG = TxTBackground.BG_NULL;
        TXT_DEF_COLOR = TxTColor.NULL_COLOR;
        TXT_DEF_STYLE = TxTStyle.NULL_STYLE;
        printQueue = new ArrayList<>();
        TXT_PRINT_RATE = 0.5;
    }

    public TxTPrinter(int maxWith, int maxHeight, TxTFormat... txTFormats) {
        TXT_MAX_WITH = maxWith;
        TXT_MAX_HEIGHT = maxHeight;
        var style = TxTStyle.NULL_STYLE;
        var color = TxTColor.NULL_COLOR;
        var background = TxTBackground.BG_NULL;
        for (var format : txTFormats) {
            if (format instanceof TxTColor txtColor) color = txtColor;
            else if (format instanceof TxTBackground txTBackground) background = txTBackground;
            else if (format instanceof TxTStyle txtStyle) style = txtStyle;
        }
        TXT_DEF_COLOR = color;
        TXT_DEF_BG = background;
        TXT_DEF_STYLE = style;
        printQueue = new ArrayList<>();
        TXT_PRINT_RATE = 0.5;
    }


    //------------------------------------------------------------------------------------------------------IMPORTED METHODS
    public void printAll() {
        var sb = new StringBuilder();
        while (!printQueue.isEmpty()) {
            printNext();
        }
    }

    public void printNext() {
        var txtObj = pollNext();
        switch (txtObj.getPrintType()) {
            case BLOCK -> System.out.print(txtObj.printObject());
            case LINE -> {
                var txt = txtObj.printObject().split(NEW_LINE);
                for (String line : txt) {
                    System.out.print(line + NEW_LINE);
                    waitFor(500 * TXT_PRINT_RATE);
                }
            }
            case TYPER_WRITER -> {
                var txt = txtObj.printObject().split(NEW_LINE);
                for (String line : txt) {
                    for(char ch:line.toCharArray()) {
                        System.out.print(ch);
                        waitFor(100 * TXT_PRINT_RATE);
                    }
                    System.out.print(NEW_LINE);
                }
            }
            case ANIMATE -> {
                var txt = txtObj.printObject().split(NEW_LINE);
                for (String line : txt) {
                    System.out.print(DELETE_CURRENT_LINE+line);
                    waitFor(100 * TXT_PRINT_RATE);
                }
            }
        }
        System.out.print(txtObj.printObject());
    }

    public void sendToQueue(TxTObject txtObj) {
        this.printQueue.add(txtObj);
    }

    /**
     * Shorthand for Thread.sleep(milliseconds)
     *
     * @param millis time to sleep in milliseconds
     */
    private void waitFor(Number millis) {
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends new lines to fill screen and clear last output
     */
    public void clearScreen() {
        sendToQueue(new TxTObject(TXT_MAX_WITH, (int) (TXT_MAX_HEIGHT * 1.5), TOP).addText(BLANK_SPACE));
        printAll();
    }

    private TxTObject pollNext() {
        return printQueue.remove(0);
    }


    /**
     * Method that prints an error message on console's last line,
     * the message gets auto erased after 1s/TXT_PRINT_RATE + message length(millis)
     *
     * @param txt Message to be shown
     */
    public void showErrorLine(String txt) {
        System.out.print(DELETE_CURRENT_LINE + TxTColor.RED.toString() + TxTStyle.BOLD + BLANK_SPACE
                .repeat((TXT_MAX_WITH - txt.length()) / 2) + txt + TxTStyle.RESET);
        waitFor(1000 * TXT_PRINT_RATE + txt.length());
        System.out.print(DELETE_CURRENT_LINE + BLANK_SPACE.repeat(TXT_MAX_WITH / 2));
    }

    public void showErrorLine(ErrorType error) {
        showErrorLine(error.toString());
    }

}

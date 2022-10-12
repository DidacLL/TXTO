package com.ironhack.otxt;


import com.ironhack.otxt.exceptions.TxTEmptyObjectException;
import com.ironhack.otxt.interfaces.TxTFormat;

import java.util.ArrayList;

import static com.ironhack.otxt.TxTFormatters.*;
import static com.ironhack.otxt.TxTFormatters.TxTPrintType.BLOCK;
import static com.ironhack.otxt.TxTFormatters.TxTStyle.RESET;
import static com.ironhack.otxt.TxTUtils.*;

/**
 * TxTObject class:
 * A textObject is mostly a sorted ArrayList of String, each as lines of the total text.
 * Thought as a kind of StringBuilder, lets you construct complicated text structure for a console printer
 * without constructing and deconstructing each text on each change and adding all necessary methods for this use case.
 * It also has a bunch of utilities to format its lines, change color,background,style,create boxes and titles,
 * merge into new textObjects as columns, fit in determinate sizes, etc...
 *
 * @author DidacLL
 * @since SoutBattle v0.1 (IronHack bootcamp project)
 */
public class TxTObject {

    private final TxTBackground bgColor;
    private final TxTColor txtColor;
    private final TxTStyle txtStyle;
    private final TxTVAlignment verticalAlignment;
    private final TxTHAlignment horizontalAlignment;

    private final ArrayList<String> text;
    private final int MAX_WIDTH;
    private final int MAX_HEIGHT;
    private int totalWidth, totalHeight;
    private boolean overload;
    private final TxTPrintType printType;

    /**Main TxtObject Constructor
     * @param max_width Max Width in Characters
     * @param max_height Max Height in Characters
     *                   (this value could be modified to text height by changing overload to true)
     * @param formats Optional TxTFormat values to set TxTObject configuration. Formats can only be set here.
     */
    public TxTObject(int max_width, int max_height, TxTFormat... formats) {
        MAX_WIDTH = max_width;
        MAX_HEIGHT = max_height;
        text = new ArrayList<>();
        var style = TxTStyle.NULL_STYLE;
        var color = TxTColor.NULL_COLOR;
        var background = TxTBackground.BG_NULL;
        var vAlign = TxTVAlignment.MIDDLE;
        var hAlign = TxTHAlignment.CENTER;
        var print = BLOCK;
        for (var format : formats) {
            if (format instanceof TxTColor fColor) color = fColor;
            else if (format instanceof TxTBackground fBackground) background = fBackground;
            else if (format instanceof TxTStyle fStyle) style = fStyle;
            else if (format instanceof TxTVAlignment alignment) vAlign = alignment;
            else if (format instanceof TxTHAlignment alignment) hAlign = alignment;
            else if (format instanceof TxTPrintType fPrint) print = fPrint;
        }
        this.bgColor = background;
        this.txtColor = color;
        this.txtStyle = style;
        this.verticalAlignment = vAlign;
        this.horizontalAlignment = hAlign;
        this.printType = print;
        this.overload=false;
    }

    public boolean isOverload() {
        return overload;
    }

    public TxTObject setOverload(boolean overload) {
        this.overload = overload;
        return this;
    }
    public int getTotalHeight() {
        updateTotalHeight();
        return this.totalHeight;
    }

    private void updateTotalHeight() {
        this.totalHeight = text.size();
    }

    private int updateTotalWidth() {
        int max = 0;
        for (String s : text) max = Math.max(countValidCharacters(s), max);
        return max;
    }

    public TxTPrintType getPrintType() {
        return printType;
    }

    //----------------------------------------------------------------------------------------------------ADDERS

    /**
     * Adds new lines to text by calling addText(String[])-->addSimpleLine()
     *
     * @param text String to be added, always splits it by "\n" to add line by line
     * @return this TxTObject itself to allow chained calls
     */
    public TxTObject addText(String text) {
        return addText(splitTextInLines(text));
    }

    /**
     * Adds new lines to text by calling addSimpleLine()
     *
     * @param lines String[] to be added
     * @return this TxTObject itself to allow chained calls
     */
    public TxTObject addText(String[] lines) {
        for (String line : lines) {
            addSimpleLine(line);
        }
        return this;
    }

    /**
     * Appends new lines to text by calling addSimpleLine()
     *
     * @param txtObject txtObject to be added at the end of this one
     * @return this TxTObject itself to allow chained calls
     */
    public TxTObject addText(TxTObject txtObject) {
        while (txtObject.hasText()) {
            try {
                addSimpleLine(txtObject.poll());
            } catch (TxTEmptyObjectException e) {
                return this;
            }
        }
        return this;
    }

    /**
     * Append to this text new lines by merging various textObjects in diferent justified columns
     *
     * @param numberOfColumns number of desired columns in our pattern
     * @param totalSize       total available space to be divided in columns (count in chars)
     * @param columnsContent  array of TxTObjects[] to be merged (it does a resize and wrap logic)
     * @return this textObject to allow chain calls.
     */
    public TxTObject addGroupInColumns(int numberOfColumns, int totalSize, TxTObject[] columnsContent) {
        int charLimit = (numberOfColumns > 1 ? (totalSize / numberOfColumns) : totalSize);
        int rest = totalSize % numberOfColumns;
        int totalLines = 0;
        //Get DATA and readjust TxTObject columns
        for (int i = 0; i < columnsContent.length; i++) {
            TxTObject textColumn = columnsContent[i];
            if (textColumn.updateTotalWidth() > charLimit) {
                columnsContent[i] = textColumn.getResizedTxTObject(charLimit, MAX_HEIGHT);
                return addGroupInColumns(numberOfColumns, totalSize, columnsContent);
            }
            trimAllText();
            totalLines = Math.max(totalLines, textColumn.getTotalHeight());
        }
        //Construct columns
        for (int j = 0; j < totalLines; j++) {
            var strBuilder = new StringBuilder();
            for (int i = 0; i < numberOfColumns; i++) {
                var currentContent = columnsContent[i];
                String currentVal;
                try {
                    currentVal = currentContent.poll(charLimit);
                } catch (TxTEmptyObjectException expected) {
                    currentVal = currentContent.getTextColorsModifiers() + BLANK_SPACE.repeat(charLimit) + RESET;
                }
                strBuilder.append(currentVal);
            }
            if (rest > 0) strBuilder.append(getTextColorsModifiers() + BLANK_SPACE.repeat(rest));
            var val = strBuilder.toString();
            addText(val);
        }
        return this;
    }

    //-----------------------------------------------------------------------------------------------------INNER_METHODS
    boolean hasText() {
        return getTotalHeight() > 0;
    }

    private String poll() throws TxTEmptyObjectException {
        if (!hasText()) throw new TxTEmptyObjectException();
        return printLine(text.remove(0));
    }

    private String poll(int size) throws TxTEmptyObjectException {
        if (!hasText()) throw new TxTEmptyObjectException();
        return printLine(fillLine(text.remove(0), size));
    }

    private String printLine(String line) {
        try {
            var splitLine = line.split(RESET.toString());
            if (splitLine.length > 1) {
                var sb = new StringBuilder();
                for (String current : splitLine) {
                    if (current.trim().charAt(0) == COLOR_CHAR) {
                        sb.append(RESET).append(current);
                    } else {
                        sb.append(getTextColorsModifiers()).append(current);
                    }
                }
                line = sb.toString();
            }
        }catch (Exception ignored){}
        return getTextColorsModifiers() + line + RESET;
    }

    /**
     * Add Line Method, It Checks That Size Fits On Specified Width
     * if not it splits line by wrap() method. After check adds line/s by addSafe()
     *
     * @param line text to be added, not necessary an oneliner string
     */
    private void addSimpleLine(String line) {
        int sizeCounter;
        sizeCounter = countValidCharacters(line);
        if (sizeCounter > MAX_WIDTH) {
            var group = wrapLine(line, MAX_WIDTH);
            for (String wrapLine : group) addSafe(wrapLine);
        } else {
            totalWidth = sizeCounter;
            addSafe(line);
        }
    }

    /**
     * Only method that can add a new element to text attribute
     *
     * @param line oneliner String to be added
     */
    private void addSafe(String line) {
        this.text.add(line.replace(NEW_LINE, ""));
        updateTotalHeight();
    }

    /**
     * Only method that can add a new element to text attribute
     *
     * @param index position where to add it (doesn't overwrite)
     * @param line  oneliner String to be added
     */
    private void addSafe(int index, String line) {
        try {
            this.text.add(index, line.replace(NEW_LINE, ""));
        }catch (Exception err){
            this.text.add(line.replace(NEW_LINE,""));
        }
        updateTotalHeight();
    }


    private TxTObject fillAllLines() {
        text.replaceAll(line -> fillLine(line, MAX_WIDTH));
        return this;
    }

    /**
     * Method that creates a new TxTObject with the current content of this one but with a different sizes
     *
     * @param newWidth  new desired width in characters
     * @param newHeight new desired height in lines
     * @return new TxTObject
     */
    public TxTObject getResizedTxTObject(int newWidth, int newHeight) {
        var txtObj = new TxTObject(newWidth, newHeight, bgColor, txtColor, txtStyle);
        for (int i = 0; i < this.getTotalHeight(); i++) {
            txtObj.addText(this.text.get(i));
        }
        return txtObj;
    }
    /**
     * Method that creates a new TxTObject with the current content of this one but with a different format values
     *
     * @param formats  new desired formats
     * @return new TxTObject
     */
    public TxTObject getReformattedTxTObject(TxTFormat... formats) {
        var style = this.txtStyle;
        var color = this.txtColor;
        var background = this.bgColor;
        var vAlign = this.verticalAlignment;
        var hAlign = this.horizontalAlignment;
        var print = this.printType;
        for (var format : formats) {
            if (format instanceof TxTColor fColor) color = fColor;
            else if (format instanceof TxTBackground fBackground) background = fBackground;
            else if (format instanceof TxTStyle fStyle) style = fStyle;
            else if (format instanceof TxTVAlignment alignment) vAlign = alignment;
            else if (format instanceof TxTHAlignment alignment) hAlign = alignment;
            else if (format instanceof TxTPrintType fPrint) print = fPrint;
        }
        return new TxTObject(this.MAX_WIDTH,this.MAX_HEIGHT,color,background,style,vAlign,hAlign,print).setOverload(this.isOverload());
    }
    public String[] getRawText(){
        return text.toArray(new String[0]);
    }

    /**
     * Method to align current text at center vertically by adding necessary blank space lines at top and bottom.
     * It fills all MAX_HEIGHT
     *
     * @return this TxTObject to allow chain calls.
     */
    private TxTObject alignTextMiddle() {
        int remainingLines = MAX_HEIGHT - getTotalHeight();
        int num;
        if (remainingLines > 1) {
            num = Math.floorDiv(remainingLines, 2);
            for (int i = 0; i < num; i++) {
                addSafe(0, BLANK_SPACE);
                addSafe(BLANK_SPACE);
            }
            if (remainingLines % 2 != 0) addSafe(BLANK_SPACE);
        }
        return this;
    }

    /**
     * Method to align current text at top vertically by adding necessary blank space lines at bottom.
     * It fills all MAX_HEIGHT
     *
     * @return this TxTObject to allow chain calls.
     */
    private TxTObject alignTextTop() {
        if (getTotalHeight() < MAX_HEIGHT) {
            addSafe(BLANK_SPACE);
            alignTextTop();
        }
        return this;
    }

    /**
     * Method to align current text at bottom vertically by adding necessary blank space lines at top.
     * It fills all MAX_HEIGHT
     *
     * @return this TxTObject to allow chain calls.
     */
    private TxTObject alignTextBottom() {
        if (getTotalHeight() < MAX_HEIGHT) {
            addSafe(0, BLANK_SPACE);
            alignTextBottom();
        }
        return this;
    }


    /**
     * Method to align current text at right horizontally by adding necessary blank space chars at left.
     * It fills all MAX_WIDTH
     *
     * @return this TxTObject to allow chain calls.
     */
    private TxTObject alignTextRight() {
        for (int i = 0; i < totalHeight; i++) {
            text.set(i, lineToRight(text.get(i), MAX_WIDTH));
        }
        return this;
    }

    /**
     * Method to align current text at center horizontally by adding necessary blank space chars at left and right.
     * It fills all MAX_WIDTH
     *
     * @return this TxTObject to allow chain calls.
     */
    private TxTObject alignTextCenter() {
        for (int i = 0; i < totalHeight; i++) {
            text.set(i, centerLine(text.get(i), MAX_WIDTH));
        }
        return fillAllLines();
    }

    private TxTObject trimAllText() {
        for (int i = 0; i < getTotalHeight(); i++) {
            text.set(i, removeStyleAndColorLine(text.get(i)).trim());
        }
        return this;
    }

    private String getTextColorsModifiers() {
        return (txtStyle)
                + (bgColor.toString())
                + (txtColor);
    }


    private String get(int i) {
        return text.get(i);
    }

    private void applyAlignment() {
        switch (verticalAlignment) {
            case TOP -> alignTextTop();
            case MIDDLE -> alignTextMiddle();
            case BOTTOM -> alignTextBottom();
        }
        switch (horizontalAlignment) {
            case LEFT -> fillAllLines();
            case CENTER -> alignTextCenter();
            case RIGHT -> alignTextRight();
        }
    }
    //----------------------------------------------------------------------------------------------------------PRINTERS
    @Override
    public String toString() {
//        applyAlignment();
        final var sb = new StringBuilder();

        for (int i = 0; i < getTotalHeight(); i++) {

            String line = "";
            try {
                line = printLine(get(i));
            } catch (Exception e) {
                return deleteDuplicateResets(sb.toString(), RESET.toString());
            }
            sb.append(line).append(i < getTotalHeight() - 1 ? NEW_LINE : "");
        }
        return deleteDuplicateResets(sb.toString(), RESET.toString());
    }


    private String[] getOnlyTextArray() {
        return text.toArray(new String[0]);
    }

    public String getRawTextString() {
        var sb = new StringBuilder();
        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);
            sb.append(line);
            if (i < text.size() - 1) sb.append(NEW_LINE);
        }
        return sb.toString();
    }
    public String printObject() {
        applyAlignment();
        var sb = new StringBuilder();
        var limit= MAX_HEIGHT;
        while (hasText()&&(limit>0||isOverload())) {
            try {
                if(limit!=1||isOverload()) {
                    sb.append(poll()).append(hasText() ? NEW_LINE : "");
                }else{
                    String str= poll();

                    int val= str.length() - (COLOR_LABEL_CHAR_SIZE + Math.min(3,MAX_WIDTH));
                    String substring = str.substring(0, val);
                    sb.append(substring).append(".".repeat(Math.min(3, MAX_WIDTH))).append(RESET);

                }
                limit--;
            } catch (TxTEmptyObjectException e) {
                return deleteDuplicateResets(sb.toString(), RESET.toString());
            }
        }
        return deleteDuplicateResets(sb.toString(), RESET.toString());
    }

}

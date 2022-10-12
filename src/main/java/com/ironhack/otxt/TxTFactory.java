package com.ironhack.otxt;

import java.util.ArrayList;

public class TxTFactory {
    public static TxTObject newSimpleTxTObject(Integer ... size){
        int a=120;
        int b=30;
        if(size.length>0)a=size[0];
        if (size.length>1)b=size[1];
        return new TxTObject(a,b).setOverload(true);
    }

    static class TxTScreenBuilder {
        private TxTFormatters.TxTBackground bgColor;
        private TxTFormatters.TxTColor txtColor;
        private TxTFormatters.TxTStyle txtStyle;
        private TxTFormatters.TxTVAlignment verticalAlignment;
        private TxTFormatters.TxTHAlignment horizontalAlignment;

        private ArrayList<String> text;
        private int MAX_WIDTH;
        private int MAX_HEIGHT;
        private int totalWidth, totalHeight;
        private boolean overload;
        private TxTFormatters.TxTPrintType printType;
        public TxTScreenBuilder() {
            this.text=new ArrayList<>();
        }
    }
}

package com.ironhack;

import com.ironhack.otxt.TxTObject;

import static com.ironhack.otxt.TxTFormatters.TxTBackground.BG_BLUE;
import static com.ironhack.otxt.TxTFormatters.TxTHAlignment.CENTER;
import static com.ironhack.otxt.TxTFormatters.TxTVAlignment.MIDDLE;
import static com.ironhack.otxt.TxTFormatters.TxTColor.RED;

public class Main {
    public static void main(String[] args) {
        TxTObject txTObject = new TxTObject(10, 2, CENTER, MIDDLE, RED, BG_BLUE);
        TxTObject patatas = txTObject.addText("PATATASsssssssssifsdsdjkfnsdkjfsdUUUUUsdlkfhjlsdkfIIIIIdsffvsfsdffsdOOOOOOOOOOO");
        patatas.setOverload(true);
        System.out.println(patatas.printObject());
    }
}
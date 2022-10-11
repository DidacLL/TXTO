package com.ironhack;

import com.ironhack.otxt.TxTObject;

import static com.ironhack.otxt.ColorFactory.TxTBackground.BLUE;
import static com.ironhack.otxt.ColorFactory.TxTHAlignment.CENTER;
import static com.ironhack.otxt.ColorFactory.TxTVAlignment.MIDDLE;
import static com.ironhack.otxt.ColorFactory.TxtColor.RED;

public class Main {
    public static void main(String[] args) {
        TxTObject txTObject = new TxTObject(10, 2, CENTER, MIDDLE, RED, BLUE);
        TxTObject patatas = txTObject.addText("PATATASsssssssssifsdsdjkfnsdkjfsdUUUUUsdlkfhjlsdkfIIIIIdsffvsfsdffsdOOOOOOOOOOO");
        patatas.setOverload(true);
        System.out.println(patatas.printObject());
    }
}
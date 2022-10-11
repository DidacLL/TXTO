package com.ironhack.otxt.interfaces;


import com.ironhack.otxt.TxTObject;

public interface TxTCommand {
    TxTObject toTextObject();
    String shortPrint();
    TxTObject fullPrint();
     String[] getPrintableAttributes();

}

package com.ironhack.otxt;

import org.junit.jupiter.api.Test;

import static com.ironhack.otxt.TxTFactory.newSimpleTxTObject;

class TxTObjectTest {
    String text=("Praeter propria digressurus alias digressurus existimo praeter mirari quosdam nihil vilitates si perstringam summatim sponte harum similis quae cum contigerit narratur quosdam a forsitan existimo Romae oratio Romae narratur monstranda propria cum mirari cum haec harum nusquam tabernas vilitates lecturos haec harum summatim perstringam haec vilitates harum praeter quae et quosdam propria narratur deflexerit mirari deflexerit quoniam contigerit forsitan veritate harum haec gererentur lecturos quamobrem mirari peregrinos existimo quae mirari posse monstranda propria posse veritate ad quamobrem haec veritate gererentur deflexerit Romae harum peregrinos deflexerit veritate Romae et praeter seditiones cum a Romae propria nihil perstringam quosdam nusquam similis cum.");
    @Test
    void isOverload_test() {
        System.out.println("Full overloaded length text:");
        System.out.println(newSimpleTxTObject(40,10).addText(text).printObject());
        System.out.println("\n\nWithout overload:");
        System.out.println(newSimpleTxTObject(1,10).addText(text).setOverload(false).printObject());
    }

    @Test
    void setOverload_test() {
    }

    @Test
    void getTotalHeight_test() {
    }

    @Test
    void getPrintType_test() {
    }

    @Test
    void addText_test() {
    }

    @Test
    void testAddText_test() {
    }

    @Test
    void testAddText1_test() {
    }

    @Test
    void addGroupInColumns_test() {
    }

    @Test
    void hasText_test() {
    }

    @Test
    void getResizedTxTObject_test() {
    }

    @Test
    void getRawText_test() {
    }

    @Test
    void testToString_test() {
    }

    @Test
    void getRawTextString_test() {
    }

    @Test
    void printObject_test() {
    }
}
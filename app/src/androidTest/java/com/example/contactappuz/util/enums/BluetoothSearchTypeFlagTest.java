package com.example.contactappuz.util.enums;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class BluetoothSearchTypeFlagTest {

    @Test
    public void Serialize() {
        assertArrayEquals(new int[]{0, 0, 1}, new BluetoothSearchTypeFlag(1).serialize());
        assertArrayEquals(new int[]{0, 0, 0}, new BluetoothSearchTypeFlag(0).serialize());
    }

    @Test
    public void Constructor(){
        BluetoothSearchTypeFlag test = new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES);
        assertArrayEquals(new int[] {0,0,1}, new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES).serialize());
        assertArrayEquals(new int[] {0,1,1}, test.serialize());
        assertArrayEquals(new int[] {1,0,0}, new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.UNKNOWN_DEVICES).serialize());
    }
    @Test
    public void or() {
        BluetoothSearchTypeFlag test = new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES);
        assertArrayEquals(new int[] {1,0,1}, test.or(BluetoothSearchTypeFlag.UNKNOWN_DEVICES).serialize());

        BluetoothSearchTypeFlag test2 = new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES);
        test2 = test2.or(BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES);
        assertArrayEquals(new int[] {0,1,1}, test2.serialize());
    }

    @Test
    public void hasFlag() {
        BluetoothSearchTypeFlag test = new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES | BluetoothSearchTypeFlag.UNKNOWN_DEVICES);
        assertTrue(test.containFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES));
        assertFalse(test.containFlag(BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES));
        assertTrue(test.containFlag(BluetoothSearchTypeFlag.UNKNOWN_DEVICES));

        BluetoothSearchTypeFlag test2 = new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES);
        assertTrue(test2.containFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES));
        assertTrue(test2.containFlag(BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES));
        assertFalse(test2.containFlag(BluetoothSearchTypeFlag.UNKNOWN_DEVICES));
    }
}
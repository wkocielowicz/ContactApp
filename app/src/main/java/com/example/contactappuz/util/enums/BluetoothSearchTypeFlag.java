package com.example.contactappuz.util.enums;

/**
 * "Enum" representing available modes of devices searching.
 */
public class BluetoothSearchTypeFlag {
    public static final int AVAILABLE_PAIRED_DEVICES = 1;
    public static final int ALL_PAIRED_DEVICES = 3;
    public static final int UNKNOWN_DEVICES = 4;

    private int modes;

    /**
     * Constructs a BluetoothSearchTypeFlag with the specified available modes.
     * @param modes The selected mode(s) of the enum instance.
     */
    public BluetoothSearchTypeFlag(int modes){
        this.modes = modes;
    }

    /**
     * Allows to merge flags
     * @param b the flag to merge with
     * @return merged flags
     */
    public BluetoothSearchTypeFlag or(BluetoothSearchTypeFlag b){
        return new BluetoothSearchTypeFlag(this.modes | b.modes);
    }
    /**
     * Allows to merge flags
     * @param b the flag to merge with
     * @return merged flags
     */
    public BluetoothSearchTypeFlag or(int b){
        return new BluetoothSearchTypeFlag(this.modes | b);}

    /**
     * Tells, if mode can be resolved the mode you selected
     * @param modeTest The mode being a test, on which your mode is tested.
     * @return True if the modeTest is among available (your) modes, False otherwise
     */
    public boolean containFlag(int modeTest) {
        return (modes & modeTest) == modeTest;
    }

    /**
     * converts the class instance to array
     * @return an array being a representation of the class instance as bits (with possible values being 0, or 1)
     */
    public int[] serialize() {
        int[] returnInt = new int[]{0, 0, 0};
        int len = returnInt.length;
        for (int i = 0; i < len; i++) {    //maximum possible value for now is 2^3-1
            if ((modes & (1 << (-i+len-1))) != 0) {
                returnInt[i] = 1;
            }
        }
        return returnInt;
    }
}

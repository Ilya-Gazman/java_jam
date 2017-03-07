package com.gazman.processor;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by ilyagazman on 4/12/15.
 */
public class Parser {

    private FileManager fileManager;

    public void init(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public String readString() {
        StringBuilder stringBuilder = new StringBuilder();
        int value = startReading(' ', '\n', '\r');
        do {
            switch (value) {
                case -1:
                    terminate();
                    return stringBuilder.toString();
                case ' ':
                case '\n':
                case '\r':
                    return stringBuilder.toString();
                default:
                    stringBuilder.append((char) value);
                    value = fileManager.read();
                    break;
            }
        } while (true);
    }

    private int startReading(char... ignoreValues) {
        MAIN:
        do {
            int value = fileManager.read();
            for (char ignoreValue : ignoreValues) {
                if (value == -1) {
                    return -1;
                }
                if (value == ignoreValue) {
                    continue MAIN;
                }
            }
            return value;
        } while (true);
    }

    public int readInt() {
        return Integer.parseInt(readString());
    }

    public long readLong() {
        return Long.parseLong(readString());
    }

    public double readDouble() {
        return Double.parseDouble(readString());
    }

    public float readFloat() {
        return Float.parseFloat(readString());
    }

    public BigInteger readBigInteger() {
        return new BigInteger(readString());
    }

    public BigInteger readBigInteger(int radix) {
        return new BigInteger(readString(), radix);
    }

    private void terminate() {
    }

    public int[][] readMultiIntArray(int rows, int columns) {
        int[][] data = new int[rows][columns];

        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                data[column][row] = readInt();
            }
        }
        return data;
    }

    public int[] readArray(int count) {
        int[] array = new int[count];

        for (int i = 0; i < count; i++) {
            array[i] = readInt();
        }
        return array;
    }

    public ArrayList<Integer> readListArray(int count) {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(readInt());
        }
        return list;
    }

    public String[] readStingArray(int count) {
        String[] strings = new String[count];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = readString();
        }
        return strings;
    }

    public class SequenceArray {
        private StringBuilder stringBuilder = new StringBuilder();
        private int position = -1;
        private int count;
        private ArrayList<Integer> data = new ArrayList<>();

        void addChar(char c) {
            stringBuilder.append(c);
            position++;
            data.add(1);
            count = 1;
        }

        void addCount() {
            count++;
            data.set(position, count);
        }

        public ArrayList<Integer> getData() {
            return data;
        }

        public String getSequence() {
            return stringBuilder.toString();
        }
    }

    public SequenceArray readSequence() {
        SequenceArray sequenceArray = new SequenceArray();
        String data = readString();

        int lastChar = -1;
        for (int i = 0, count = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            count++;
            if (lastChar == -1 || c != lastChar) {
                sequenceArray.addChar(c);
            } else {
                sequenceArray.addCount();
            }
            lastChar = c;
        }

        return sequenceArray;
    }
}

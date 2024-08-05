package com.automation;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HIDDecoder {
    private static final Map<Integer, String> keyMap = new HashMap<>();
    private static final Map<Integer, String> shiftedKeyMap = new HashMap<>();

    static {
        // Regular key mappings
        keyMap.put(0x04, "a");
        keyMap.put(0x05, "b");
        keyMap.put(0x06, "c");
        keyMap.put(0x07, "d");
        keyMap.put(0x08, "e");
        keyMap.put(0x09, "f");
        keyMap.put(0x0A, "g");
        keyMap.put(0x0B, "h");
        keyMap.put(0x0C, "i");
        keyMap.put(0x0D, "j");
        keyMap.put(0x0E, "k");
        keyMap.put(0x0F, "l");
        keyMap.put(0x10, "m");
        keyMap.put(0x11, "n");
        keyMap.put(0x12, "o");
        keyMap.put(0x13, "p");
        keyMap.put(0x14, "q");
        keyMap.put(0x15, "r");
        keyMap.put(0x16, "s");
        keyMap.put(0x17, "t");
        keyMap.put(0x18, "u");
        keyMap.put(0x19, "v");
        keyMap.put(0x1A, "w");
        keyMap.put(0x1B, "x");
        keyMap.put(0x1C, "y");
        keyMap.put(0x1D, "z");
        keyMap.put(0x1E, "1");
        keyMap.put(0x1F, "2");
        keyMap.put(0x20, "3");
        keyMap.put(0x21, "4");
        keyMap.put(0x22, "5");
        keyMap.put(0x23, "6");
        keyMap.put(0x24, "7");
        keyMap.put(0x25, "8");
        keyMap.put(0x26, "9");
        keyMap.put(0x27, "0");
        keyMap.put(0x2C, " ");
        keyMap.put(0x2D, "-");
        keyMap.put(0x2E, "=");
        keyMap.put(0x2F, "[");
        keyMap.put(0x30, "]");
        keyMap.put(0x31, "\\");
        keyMap.put(0x33, ";");
        keyMap.put(0x34, "'");
        keyMap.put(0x35, "`");
        keyMap.put(0x36, ",");
        keyMap.put(0x37, ".");
        keyMap.put(0x38, "/");

        // Shifted key mappings
        shiftedKeyMap.put(0x04, "A");
        shiftedKeyMap.put(0x05, "B");
        shiftedKeyMap.put(0x06, "C");
        shiftedKeyMap.put(0x07, "D");
        shiftedKeyMap.put(0x08, "E");
        shiftedKeyMap.put(0x09, "F");
        shiftedKeyMap.put(0x0A, "G");
        shiftedKeyMap.put(0x0B, "H");
        shiftedKeyMap.put(0x0C, "I");
        shiftedKeyMap.put(0x0D, "J");
        shiftedKeyMap.put(0x0E, "K");
        shiftedKeyMap.put(0x0F, "L");
        shiftedKeyMap.put(0x10, "M");
        shiftedKeyMap.put(0x11, "N");
        shiftedKeyMap.put(0x12, "O");
        shiftedKeyMap.put(0x13, "P");
        shiftedKeyMap.put(0x14, "Q");
        shiftedKeyMap.put(0x15, "R");
        shiftedKeyMap.put(0x16, "S");
        shiftedKeyMap.put(0x17, "T");
        shiftedKeyMap.put(0x18, "U");
        shiftedKeyMap.put(0x19, "V");
        shiftedKeyMap.put(0x1A, "W");
        shiftedKeyMap.put(0x1B, "X");
        shiftedKeyMap.put(0x1C, "Y");
        shiftedKeyMap.put(0x1D, "Z");
        shiftedKeyMap.put(0x1E, "!");
        shiftedKeyMap.put(0x1F, "@");
        shiftedKeyMap.put(0x20, "#");
        shiftedKeyMap.put(0x21, "$");
        shiftedKeyMap.put(0x22, "%");
        shiftedKeyMap.put(0x23, "^");
        shiftedKeyMap.put(0x24, "&");
        shiftedKeyMap.put(0x25, "*");
        shiftedKeyMap.put(0x26, "(");
        shiftedKeyMap.put(0x27, ")");
        shiftedKeyMap.put(0x2C, " ");
        shiftedKeyMap.put(0x2D, "_");
        shiftedKeyMap.put(0x2E, "+");
        shiftedKeyMap.put(0x2F, "{");
        shiftedKeyMap.put(0x30, "}");
        shiftedKeyMap.put(0x31, "|");
        shiftedKeyMap.put(0x33, ":");
        shiftedKeyMap.put(0x34, "\"");
        shiftedKeyMap.put(0x35, "~");
        shiftedKeyMap.put(0x36, "<");
        shiftedKeyMap.put(0x37, ">");
        shiftedKeyMap.put(0x38, "?");

        // Additional key mappings for non-printable keys can be added here
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many hex codes do you want to convert? ");
        int count = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String[] hexInputs = new String[count];
        for (int i = 0; i < count; i++) {
            System.out.print("Enter hex code " + (i + 1) + ": ");
            hexInputs[i] = scanner.nextLine();
        }

        StringBuilder result = new StringBuilder();

        for (String hexInput : hexInputs) {
            result.append(decodeHIDReport(hexInput));
        }

        System.out.println("Decoded string: " + result.toString());
        scanner.close();
    }

    private static String decodeHIDReport(String hexInput) {
        byte[] bytes = hexStringToByteArray(hexInput);
        int modifierByte = bytes[0] & 0xFF;

        // Capture decoded characters
        StringBuilder decodedChars = new StringBuilder();

        for (int i = 2; i < bytes.length; i++) {
            int keycode = bytes[i] & 0xFF;
            if (keycode != 0) {
                String key = (modifierByte & 0x02) != 0 ? shiftedKeyMap.get(keycode) : keyMap.get(keycode);
                if (key == null) {
                    key = "[Unknown: " + keycode + "]";
                }
                decodedChars.append(key);
            }
        }

        return decodedChars.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}

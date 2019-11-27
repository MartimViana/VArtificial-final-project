//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::     Antonio Manso                        Luis Correia                   ::
//::     manso@ipt.pt                   Luis.Correia@ciencias.ulisboa.pt     ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::                                                                         ::
//::     Instituto Politécnico de Tomar                                      ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                             (c) 2019    ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package com.utils.problems;

import com.utils.MyString;
import java.math.BigInteger;

/**
 * Created on 30/jan/2017, 12:37:59
 *
 * @author zulu - computer
 */
public class BinaryNumberUtils {

    /**
     * convert binary string to gray representation
     *
     * @param bits binary string
     * @return gray encoded
     */
    public static String grayToBinary(String bits) {
        StringBuilder result = new StringBuilder(bits.substring(0, 1));
        for (int i = 1; i < bits.length(); i++) {
            //bin[i] = gray[i] ^ bin[i-1]
            result.append(bits.charAt(i) != result.charAt(i - 1) ? "1" : "0");
        }
        return result.toString();
    }

    /**
     * convert binary string to gray representation
     *
     * @param bits binary string
     * @return gray encoded
     */
    public static BigInteger grayDecode(String bits) {
        StringBuilder result = new StringBuilder(bits.substring(0, 1));
        for (int i = 1; i < bits.length(); i++) {
            //bin[i] = gray[i] ^ bin[i-1]
            result.append(bits.charAt(i) != result.charAt(i - 1) ? "1" : "0");
        }
        return new BigInteger(result.toString(), 2);
    }

    /**
     * convert binary string to gray representation
     *
     * @param bits binary string
     * @return gray encoded
     */
    public static BigInteger binDecode(String bits) {
        return new BigInteger(bits, 2);
    }

    /* Conevert a boolean array to String
     * @param bits boolean array
     * @return String with '0' and '1'
     */
    public static String booleanArrayToString(boolean[] bits) {//--------------- boolean array to string
        StringBuilder txt = new StringBuilder();
        for (boolean bit : bits) {
            txt.append(bit ? "1" : "0");
        }
        return txt.toString();
    }

    /**
     * long value of a binary boolean bits
     *
     * @param arr boolean bits in binary format
     * @return long value of binary bits
     */
    public static long binaryToInt(boolean[] arr) {
        long number = 0;
        for (boolean b : arr) {
            number = (number << 1) | (b ? 1 : 0);
        }
        return number;
    }

    /**
     * converts a binary value to gray value (gray bits)
     *
     * @param num binary value
     * @return gray value
     */
    public static long binaryToGray(long num) {
        return (num >> 1) ^ num;
    }

    /**
     * converts a gray value to binary value (binary bits)
     *
     * @param num gray value
     * @return binary value
     */
    public static long grayToBinary(long num) {
        long mask;
        for (mask = num >> 1; mask != 0; mask = mask >> 1) {
            num = num ^ mask;
        }
        return num;
    }

    /**
     * converts an integer value to boolean binary array
     *
     * @param number value
     * @param numberOfBits lenght of array
     * @return array of booleans that represents binary value
     */
    public static boolean[] intToBinary(long number, int numberOfBits) {
        boolean[] bits = new boolean[numberOfBits];
        for (int i = 0; i < numberOfBits; i++) {
            bits[numberOfBits - i - 1] = (number & (1 << i)) != 0;
        }
        return bits;
    }

    /**
     * converts an integer value to boolean array in gray code
     *
     * @param number value
     * @param numberOfBits lenght of array
     * @return array of booleans that represents value in gray code
     */
    public static boolean[] intToGray(long number, int numberOfBits) {
        return intToBinary((number >> 1) ^ number, numberOfBits);
    }
    /**
     * gets the value represented by boolean array of gray codes
     * @param arr gray code
     * @return integer value
     */
    public static long grayToInt(boolean[] arr) {
        return grayToBinary(binaryToInt(arr));
    }

    public static void main(String[] args) {
        int size = 8;
        for (int i = 1; i < 60; i++) {
            boolean[] bits = intToBinary(i, size);
            long num = binaryToInt(bits);
            System.out.printf("\nBIN %6d - %s - %6d", i, booleanArrayToString(bits), num);
            boolean[] gray = intToGray(i, size);
            long numg = grayToInt(gray);
            System.out.printf("\tGRAY\t %6d - %s - %6d", binaryToGray(i), booleanArrayToString(gray), numg);
        }
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 20; i++) {
//            String b = Integer.toBinaryString(i);
//            String g = binaryToGray(b);
//            String b2 = grayToBinary(g);
//            System.out.println(i + "\tBin = " + b + "\t Gray " + g + "\t bin2 = " + b2 + " = " + grayDecode(g));
//
//        }
//
//    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201701301237L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}

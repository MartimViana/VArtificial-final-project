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

package com.utils;

import java.util.Locale;

/**
 * Created on 19/jan/2016, 12:33:05
 *
 * @author zulu - computer
 */
public class MyNumber {

    /**
     * convert double to string with size characters
     *
     * @param value double number
     * @param size number characters
     * @return string with number and white spaces if necessary
     */
    public static String numberToString(double value, int size) {
        int val = (int) value;
        if (val == value) {
            return IntegerToString(val, size);
        } else {
            return DoubleToString(value, size);
        }
    }

    /**
     * convert integer to string with size characters
     *
     * @param value number integer
     * @param size number of characters
     * @return string with number and white spaces if necessary
     */
    public static String IntegerToString(int value, int size) {
        StringBuffer str = new StringBuffer(value + "");
        StringBuilder tmp = new StringBuilder(str);
        while (tmp.length() < size) {
            tmp.insert(0, " ");
        }
        return tmp.substring(0, size);
    }

    /**
     * convert double to string with size characters
     *
     * @param value double number
     * @param size number characters
     * @return string with number and white spaces if necessary
     */
    public static String DoubleToString(double value, int size) {
        StringBuilder number2 = new StringBuilder(String.format(Locale.US, "%f", value));

        String number = String.valueOf(value).toUpperCase();
        if (number.length() < size && number.indexOf("E") < 0) {
            return MyString.alignRight(number, size);
        } else {
            return DoubleExpToString(value, size);
        }
    }

    /**
     * convert double to string with size characters
     *
     * @param value double number
     * @param size number characters
     * @return string with number and white spaces if necessary
     */
    public static String DoubleExpToString(double value, int size) {
        size--; //signal
        if (value < 0) {
            return String.format(Locale.US, "%" + size + "." + (size - 5) + "E", value);//signal
        } else {
            return String.format(Locale.US, " %" + size + "." + (size - 5) + "E", value);//space
        }
    }

    public static void main(String[] args) {
        double d = 1.45000024;
        System.out.println("d = " + d);
        System.out.println("d = " + DoubleToString(d, 10));
    }

    /**
     * set the string with size lenght
     *
     * @param str string original
     * @param size new size
     * @return string cuted or with white spaces
     */
    public static String getStringSize(String str, int size, char space) {

        StringBuilder tmp = new StringBuilder();
        int index = 0;
        while (tmp.length() < size && tmp.length() < str.length()) {
            tmp.append(str.charAt(index++));
        }
        while (tmp.length() < size) {
            tmp.append(space);
        }
        return tmp.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191233L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    /**
     * convert a integer to alpha 0-"a" 1-"b" . . . 26- "aa" 27-"ab"
     *
     * @param index index number
     * @return a..z aa...
     */
    public static String convertIntToAlphabet(int index) {
        if (index < 0) {
            return "";
        }
        return convertIntToAlphabet(index / 26 - 1) + (char) (97 + index % 26);
//        StringBuilder str = new StringBuilder();
//        do{
//            str.append((char)(97 + index%26 ));
//            index = index/26  ;
//        }while(index > 0);
//        return str.reverse().toString();
    }

}

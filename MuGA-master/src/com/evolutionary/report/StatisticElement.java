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

package com.evolutionary.report;

import com.utils.MyStatistics;
import com.utils.MyString;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created on 12/abr/2016, 9:28:23
 *
 * @author zulu - computer
 */
public class StatisticElement implements Serializable{

    public final static String[] header = {"MEAN", "STD", "MIN", "MEDIAN", "MAX", "Q1", "Q3"};
    private double[] data;

    public StatisticElement() {
        data = new double[header.length];
    }  

    /**
     * contruct a statistic from an array of values.
     *
     * @param a
     */
    public StatisticElement(double[] a) {
        data = new double[header.length];
        Arrays.sort(a);
        double sum = 0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
            if (max < a[i]) {
                max = a[i];
            }
            if (min > a[i]) {
                min = a[i];
            }
        }
        setMean(sum / a.length);
        setStd(MyStatistics.std(a));
        setMedian(a[a.length / 2]);
        setQ1(a[a.length / 4]);
        setQ3(a[(a.length * 3) / 4]);
        setMin(min);
        setMax(max);
    }

    /**
     * gets the label of statistics elements
     *
     * @return Label of statistics
     */
    public static String getHeaderString() {
        StringBuilder txt = new StringBuilder();
        for (String label : header) {
            txt.append(MyString.alignRight(label, ReportSolver.FIELD_SIZE) + " ");
        }
        return txt.toString();
    }

    /**
     * gets string data os statistics
     *
     * @return
     */
    public String getDataString() {
        StringBuilder txt = new StringBuilder();
        for (double number : data) {
            txt.append(MyString.alignRight(number, ReportSolver.FIELD_SIZE) + " ");
        }
        return txt.toString();
    }
    
    public double[] getData() {
        return data;
    }

    public double getMean() {
        return data[0];
    }

    public void setMean(double mean) {
        data[0] = mean;
    }

    public double getStd() {
        return data[1];
    }

    public void setStd(double std) {
        data[1] = std;
    }

    public double getMin() {
        return data[2];
    }

    public void setMin(double min) {
        data[2] = min;
    }

    public double getMedian() {
        return data[3];
    }

    public void setMedian(double median) {
        data[3] = median;
    }

    public double getMax() {
        return data[4];
    }

    public void setMax(double max) {
        data[4] = max;
    }

    public double getQ1() {
        return data[5];
    }

    public void setQ1(double max) {
        data[5] = max;
    }

    public double getQ3() {
        return data[6];
    }

    public void setQ3(double max) {
        data[6] = max;
    }

    public static int getStatsSize() {
        return header.length;
    }

    public static String getMeanLabel() {
        return header[0];
    }

    public static String getStdLabel() {
        return header[1];
    }

    public static String getMinLabel() {
        return header[2];
    }

    public static String getMedianLabel() {
        return header[3];
    }

    public static String getMaxLabel() {
        return header[4];
    }

    public String toString() {
        return getHeaderString() + "\n" + getDataString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201604120928L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}

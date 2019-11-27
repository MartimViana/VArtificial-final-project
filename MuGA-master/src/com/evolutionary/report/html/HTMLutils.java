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

package com.evolutionary.report.html;

import GUI.utils.MuGASystem;
import com.utils.MyFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 31/jan/2019, 12:49:29
 *
 * @author zulu - computer
 */
public class HTMLutils {

    static int DEFAULT_NUMBER_DECIMALS = 4;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    static String CSV_COMMA = " ; ";
    static String css = MyFile.readResource("/com/evolutionary/report/html/style.css");
    static String pageTemplate = MyFile.readResource("/com/evolutionary/report/html/template.html");
    static String menuTemplate = MyFile.readResource("/com/evolutionary/report/html/menu.html");
    static String indexTemplate = MyFile.readResource("/com/evolutionary/report/html/index.html");
    static String sourceCode = MyFile.readResource("/com/evolutionary/report/html/sourcecode.html");
    static String menuSimulations = MyFile.readResource("/com/evolutionary/report/html/menuSimulationsIndex.html");

    public static String getPageTitle(String pageTitle, String fileName) {
        return getPageTitle(pageTitle, fileName, "");
    }

    public static String getPageTitle(String pageTitle, String fileNameLeft, String fileNameRight) {        
        String downloadRight = "";
        if (!fileNameRight.isEmpty()) {
            downloadRight = "<td width=\"1px\"><li ><a class=\"active\" href=\"" + fileNameRight
                    + "\"  download>" + fileNameRight + "</a></li></td>\n";
        }
        String downloadLeft = "";
        if (!fileNameLeft.isEmpty()) {
            downloadLeft = "<td width=\"1px\"><li ><a class=\"active\" href=\"" + fileNameLeft
                    + "\"  download>" + fileNameLeft + "</a></li></td>\n";
        }

        String title = "<td align=\"center\"> <h1> " + pageTitle + "</h1></td>\n";

        return "<ul>\n<table  style=\"width:100%\"><tr>\n"
                + downloadLeft
                + title
                + downloadRight
                + "</tr></table>\n</ul>";
    }

    public static String getLink(String file, String title, String target) {
        String link = "<a href=\"" + file + "/";
        if (!target.isEmpty()) {
            link += " target=\"" + target;
        }
        return link + ">" + title + "</a>";
    }

    public static String getNumber(double value) {
        return getNumber(value, DEFAULT_NUMBER_DECIMALS, Locale.getDefault());
    }

    public static String getNumber(double value, Locale local) {
        return getNumber(value, DEFAULT_NUMBER_DECIMALS, local);
    }

    public static String getNumber(double value, int decimals) {
        return getNumber(value, decimals, Locale.getDefault());
    }

    public static String getNumber(double value, int decimals, Locale local) {
        if (value == (long) value) {
            return (long) value + "";
        }
        String number = String.format(Locale.ENGLISH, "%." + decimals + "f", value);
        if (Double.valueOf(number) == 0) {
            return String.format("%." + decimals + "e", value);
        }
        return String.format(local, "%." + decimals + "f", value);
    }

    public static void main(String[] args) {
        System.out.println(getNumber(1.23456, 2));
        System.out.println(getNumber(1.00056, 2));
        System.out.println(getNumber(0.00056, 2));
    }

    static String getCopyright() {
        StringBuilder txt = new StringBuilder("<hr>");
        txt.append(MuGASystem.name + " - " + MuGASystem.version + " ");
        txt.append(MuGASystem.copyright + "  " + dateFormat.format(new Date()));
        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201901311249L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}

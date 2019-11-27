//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2019   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.utils;

/**
 * Created on 23/fev/2019, 5:15:04
 *
 * @author zulu - computer
 */
public class Distances {

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static int computeLevenshteinDistance(CharSequence str1,
            CharSequence str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1]
                        + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
                        : 1));
            }
        }

        return distance[str1.length()][str2.length()];
    }

    public static int computeLevenshteinDistance(int[] v1, int[] v2) {
        int[][] distance = new int[v1.length + 1][v2.length + 1];
        for (int i = 0; i <= v1.length; i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= v2.length; j++) {
            distance[0][j] = j;
        }
        for (int i = 1; i <= v1.length; i++) {
            for (int j = 1; j <= v2.length; j++) {
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1]
                        + ((v1[i - 1] == v2[j - 1]) ? 0
                                : 1));
            }
        }
        return distance[v1.length][v2.length];
    }

    public static int editDistance(int[] ind1, int[] ind2) {
        int[][] dp = new int[ind1.length + 1][ind2.length + 1];

        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                dp[i][j] = i == 0 ? j : j == 0 ? i : 0;
                if (i > 0 && j > 0) {
                    if (ind1[i - 1] == ind2[j - 1]) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Math.min(dp[i][j - 1] + 1, Math.min(
                                dp[i - 1][j - 1] + 1, dp[i - 1][j] + 1));
                    }
                }
            }
        }
        return dp[ind1.length][ind2.length];
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201902230515L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}

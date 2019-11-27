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

package com.utils.staticallib.Distributions;

/**
 * Wrapper of functions for the Exponential distribution.
 */

public class exponential {
  
  /**
   * Density of the Exponential distribution.
   */
  public static double density(double x, double scale) {
    if (Double.isNaN(x) || Double.isNaN(scale)) return x + scale;
    if (scale <= 0.0) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    if (x < 0.0) return 0.0;
    return java.lang.Math.exp(-x / scale) / scale;
  }

  /**
   * Distribution function of the Exponential distribution
   *
   */
  public static double cumulative(double x, double scale) {
    if (Double.isNaN(x) || Double.isNaN(scale))
      return x + scale;
    if (scale <= 0.0) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    if (x <= 0.0) return 0.0;
    return 1.0 - java.lang.Math.exp(-x / scale);
  }

  /**
   * quantile function of the Exponential distribution
   */
  public static double quantile(double x, double scale) {
    if (Double.isNaN(x) || Double.isNaN(scale))
      return x + scale;
    if (scale <= 0 || x < 0 || x > 1) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    if (x <= 0.0) return 0.0;
    return - scale * java.lang.Math.log(1.0 - x);
  }
  
  /**
   * Random variates from the Exponential distribution
   */
  public static double random(double scale, uniform PRNG) {
    if (Double.isInfinite(scale) || scale <= 0.0) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    return scale * random(PRNG);
  }
  
  /**
   * Random variates from the standard normal distribution.
   *
   *    Ahrens, J.H. and Dieter, U. (1972).
   *    Computer methods for sampling from the Exponential and
   *    normal distributions.
   *    Comm. ACM, 15, 873-882.
   */

  static private double q[] = {
    0.6931471805599453,
    0.9333736875190459,
    0.9888777961838675,
    0.9984959252914960,
    0.9998292811061389,
    0.9999833164100727,
    0.9999985691438767,
    0.9999998906925558,
    0.9999999924734159,
    0.9999999995283275,
    0.9999999999728814,
    0.9999999999985598,
    0.9999999999999289,
    0.9999999999999968,
    0.9999999999999999,
    1.0000000000000000
  };


  public static double random(uniform PRNG) {
    /* q[k-1] = sum(alog(2.0)**k/k!) k=1,..,n, */
    /* The highest n (here 8) is determined by q[n-1] = 1.0 */
    /* within standard precision */
    double a, u, ustar, umin;
    int i;

    a = 0.0;
    u = PRNG.random();
    for (;;) {
      u = u + u;
      if (u > 1.0)
        break;
      a = a + q[0];
    }
    u = u - 1.0;

    if (u <= q[0])
      return a + u;

    i = 0;
    ustar = PRNG.random();
    umin = ustar;
    do {
      ustar = PRNG.random();
      if (ustar < umin)
        umin = ustar;
      i = i + 1;
    } while (u > q[i]);
    return a + umin * q[0];
  }
}

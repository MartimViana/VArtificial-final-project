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

import java.util.Random;
import com.utils.staticallib.Distributions.rng.WichmannHill;

/**
 * Uniform distribution over an interval.
 */

public class uniform {
  
  /**
   * density of the uniform distribution.
   */
  public static double density(double x, double a, double b) {
    if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b))
      return x + a + b;
    if (b <= a) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    if (a <= x && x <= b)
      return 1.0 / (b - a);
    return 0.0;
  }
  
  /**
   * distribution function of the uniform distribution.
   */
  public static double cumulative(double x, double a, double b) {
    if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b))
      return x + a + b;
    if (b <= a) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    if (x <= a)
      return 0.0;
    if (x >= b)
      return 1.0;
    return (x - a) / (b - a);
  }
  
  /**
   * quantile function of the uniform distribution.
   */
  public static double quantile(double x, double a, double b) {
    if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b))
      return x + a + b;
    if (b <= a || x < 0 || x > 1) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    return a + x * (b - a);
  }
  
  /**
   *  Random variates from the uniform distribution.
   */
  public static double random(double a, double b) {
    if (Double.isInfinite(a) || Double.isInfinite(b) || b < a) {
      throw new java.lang.ArithmeticException("Math Error: DOMAIN");
    }
    if (a == b) 
      return a;
    else 
      return a + (b - a) * random();
  }
  
  /**
   * Generator used during random() call. Can be set.
   */
  public static StdUniformRng uniRng = new WichmannHill();

  public static Random rnd = new Random();
  
  /**
   * generate standard uniform random variate
   */
  public static double random() {
   // return uniRng.random();
      return rnd.nextDouble();
  }

}

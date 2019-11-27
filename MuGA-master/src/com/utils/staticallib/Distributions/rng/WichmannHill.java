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

package com.utils.staticallib.Distributions.rng;

import com.utils.staticallib.Distributions.StdUniformRng;

public class WichmannHill implements StdUniformRng {
  
  int i1_seed;
  int[] i_seed;
  static final int c0 = 30269;
  static final int c1 = 30307;
  static final int c2 = 30323;
  
  public WichmannHill() {
    i1_seed = 123;
    i_seed = new int[2];
    fixupSeeds();
  }
  
  public void fixupSeeds() {
    // exclude 0 as seed
    if (i1_seed==0) i1_seed++;
    for (int j=0; j < i_seed.length; j++) {
      if (i_seed[j]==0) i_seed[j]++;
    }
    if (i1_seed >= c0 ||
         i_seed[0] >= c1 ||
         i_seed[1] >= c2) {
      random();
    }
  }
  
  public double random() {
    i1_seed = i1_seed * 171 % c0;
    i_seed[0] = i_seed[0] * 172 % c1;
    i_seed[1] = i_seed[1] * 170 % c2;
    double value =
      (double)i1_seed / c0 +
      (double)i_seed[0] / c1 +
      (double)i_seed[1] / c2;
    return value - (int) value; // ensure in range [0,1)
  }
  
}

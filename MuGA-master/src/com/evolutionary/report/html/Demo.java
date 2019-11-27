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

import java.lang.reflect.Method;

/**
 * Created on 27/jan/2019, 21:22:28 
 * @author zulu - computer
 */
public class Demo {

    public static void main(String[] args) throws Exception{
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;
        Method method1 = Demo.class.getMethod("method1", parameterTypes);

        Demo demo = new Demo();
        demo.method2(demo, method1, "Hello World");
    }

    public static void method1(String message) {
        System.out.println(message);
    }
    public static void method2(String message) {
        System.out.println(message);
    }

    public static void method2(Object object, Method method, String... message) throws Exception {
        Object[] parameters = new Object[1];
        parameters[0] = message;
        method.invoke(object, parameters);
    }

}
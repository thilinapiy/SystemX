package org.thilina.systemx.core;

/**
 * Created by thilina on 3/13/17.
 */
public class Division {

    public static Double getDivision(Double a, Double b){

        System.out.println("From patch code : patch0001 ... ");
        if (b == 0.0) {
            System.out.println("Argument 'divisor' is 0");
            return 0.0;
        }  
        System.out.println("Divition ... ");
        return a/b;
    }
}

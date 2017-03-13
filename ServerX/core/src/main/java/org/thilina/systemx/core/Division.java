package org.thilina.systemx.core;

/**
 * Created by thilina on 3/13/17.
 */
public class Division {

    public static int getDivision(int a, int b){
        System.out.println("Divition ... ");

        // update for the patch
        int output;
        try {
            output = a/b;
        }catch (Exception e){
            System.out.println(e.toString());
            return -1;
        }
        return output;
    }
}

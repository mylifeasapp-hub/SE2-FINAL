package org.se2final.control;

public class DataInputCheck {
    public static boolean isNumeric(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}

package com.gazman.processor;

/**
 * Created by Ilya gazman on 3/14/15.
 */
public class Assert {


    int currentCase;
    public boolean throwErrors = false;

    public void equals(Object o1, Object o2){
        if(o1 == null && o2 == null){
            return;
        }

        if(o1 == null || o2 == null || !o1.equals(o2)){
            String message = "Not equal\n" + o1 + "\n" + o2;
            postError(message);
        }

    }

    private void postError(String message) {
        System.err.println("\nCase " + currentCase + ": " + message);
        if(throwErrors){
            throw new RuntimeException();
        }
    }
}

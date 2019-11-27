package com.evolutionary.problem.permutation.PancakeSorting;

import java.io.Serializable;

public class Plate implements Serializable {
    private int[] pancakes;

    public Plate(int[]pancakes) {
        this.pancakes = pancakes;
    }

    public void flip(int position) {
        // get sub array
        int sub[] = new int[pancakes.length-position];
        int j = 0;
        for(int i = position; i < pancakes.length; i++) {
            sub[j] = pancakes[i];
            j++;
        }

        // flip array and add it to the main array
        for(int i = 0; i < sub.length; i++) pancakes[position+i] = sub[sub.length-i-1];
    }

    public boolean isSolved() {
        int highest = pancakes[0];
        for (int i = 0; i < pancakes.length; i++) {
            if(highest > pancakes[i]) return false;
            highest = pancakes[i];
        }
        return true;
    }

    @Override
    public Object clone() { return new Plate(pancakes.clone()); }

    @Override
    public String toString() {
        String result = "";
        for (int p: pancakes) result += p+" ";
        return result;
    }
}

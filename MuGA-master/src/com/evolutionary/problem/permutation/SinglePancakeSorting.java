package com.evolutionary.problem.permutation;

import com.evolutionary.problem.permutation.PancakeSorting.Plate;

import javax.xml.crypto.dsig.SignedInfo;
import java.util.Random;

public class SinglePancakeSorting extends Permutations {


    // plate size
    private static int DEFAULT_SIZE = 128;

    // seed that the random number generator has
    private static final int SEED = 0;

    // maximum length that the pancake can have
    private static final int MAX_LENGTH = 1000;

    // Problem that the individuals will attempt to solve
    public Plate plate;


    /**
     * Class constructor.
     */
    public SinglePancakeSorting() {
        super(DEFAULT_SIZE, Optimization.MINIMIZE);
        generateProblem(DEFAULT_SIZE, SEED, MAX_LENGTH);
    }

    protected void generateProblem(int n, long seed, int max_length) {
        Random r = new Random(seed);
        int temp[] = new int[n];
        for (int i = 0; i < n; i++) {
            // generate new pancake
            temp[i] = (int) Math.floor(r.nextDouble() * max_length);
        }
        // create new plate
        this.plate = new Plate(temp);
    }

    @Override
    protected double evaluate(int[] genome) {
            // create a clone of the problem, so as not to solve
            // the original.
            Plate clone = (Plate) this.plate.clone();
            // execute series of flips
            for(int i = 0;i < genome.length; i++) {
                // flip the pancakes!
                clone.flip(
                        Math.abs(genome[i])
                );

                // if the problem was already solved, return this evaluation
                if (clone.isSolved()) return i;
                // if the problem wasn't solved, return this evaluation

            }
            return genome.length;
    }

    @Override
    public boolean isOptimum() {
        return evaluate() <= MAX_LENGTH;
    }
}

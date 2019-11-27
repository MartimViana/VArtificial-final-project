package com.evolutionary.problem.permutation;

import com.evolutionary.problem.permutation.PancakeSorting.Plate;

import java.util.Random;

public class MultiPancakeSorting extends Permutations {
    // plate size
    private static int DEFAULT_SIZE = 100;

    // seed that the random number generator has
    private static final int SEED = 0;

    // maximum length that the pancake can have
    private static final int MAX_LENGTH = 1000;

    // ammount of problems
    private static int PROBLEM_AMMOUNT = 10;

    // list of problems to solve
    private Plate[] plates;

    public MultiPancakeSorting() {
        super(DEFAULT_SIZE, Optimization.MINIMIZE);
        plates = new Plate[PROBLEM_AMMOUNT];
        generateProblem(DEFAULT_SIZE, SEED, MAX_LENGTH);
    }

    protected void generateProblem(int n, long seed, int max_length) {
        Random r = new Random(seed);

        for(int i = 0; i < PROBLEM_AMMOUNT; i++) {
            int temp[] = new int[n];
            for (int j = 0; j < n; j++) {

                // generate new pancake
                temp[j] = (int) Math.floor(r.nextDouble() * max_length);
            }

            // add new plate to plate array
            plates[i] = new Plate(temp);
        }
    }

    public double evaluate(int[] genome) {
        double result = 0;
        for(Plate plate: plates) result += evaluateSinglePancake(plate, genome);
        return result;
    }

    public double evaluateSinglePancake(Plate plate, int[] genome) {
        // create a clone of the problem, so as not to solve
        // the original.
        Plate clone = (Plate) plate.clone();
        // execute series of flips
        for(int i = 0;i < genome.length; i++) {
            // flip the pancakes!
            clone.flip(genome[i]);

            // if the problem was already solved, return this evaluation
            if (clone.isSolved()) return i;
        }
        // if the problem wasn't solved, return this evaluation
        return genome.length;
    }


    @Override
    public boolean isOptimum() { return false; }
}

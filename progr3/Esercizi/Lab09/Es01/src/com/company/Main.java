package com.company;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
	      int[][] matrix = {{1, 2, 3}, {2, 1, 4}, {1, 6, 2}, {12, 12, 3}};
        int[] partials = new int[matrix.length];
        ArrayList<Future<Integer>> tasks = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for(int[] row : matrix) {
          FutureTask<Integer> task = new FutureTask<>(new SingleRowTask(row));
          tasks.add(task);
          executor.execute(task);
        }

        int i = 0;
        for(Future<Integer> task : tasks)
          try {
            partials[i++] = task.get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }

        int max = Integer.MIN_VALUE;
        for(int partial : partials)
          if(partial > max) max = partial;

        System.out.println(max);
        executor.shutdown();
    }
}

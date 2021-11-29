package com.company;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class SingleRowTask implements Callable<Integer> {
  private int[] row;

  public SingleRowTask(int[] row) {
    this.row = row;
  }

  @Override
  public Integer call() throws Exception {
    int ret = Integer.MIN_VALUE;
    for(int elem : row) if(elem > ret) ret = elem;
    return ret;
  }
}

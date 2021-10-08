import java.util.List;

public class Calculator {
  public static <T extends Number> void print(List<T> list) throws IllegalArgumentException {
    if(list == null)
      throw new IllegalArgumentException("list argument can't be null");

    for(T elem : list)
      System.out.print("[" + elem + "]\t");
  }

  public static <T extends Number & Algebra<T>> T sum(List<T> list) throws IllegalArgumentException {
    if(list == null)
      throw new IllegalArgumentException("list argument can't be null");

    T res = list.get(0).getZero();
    for(T elem : list) res = elem.sum(res);
    return res;
  }

  public static <T extends Number & Algebra<T>> T max(List<T> list) throws IllegalArgumentException {
    if(list == null)
      throw new IllegalArgumentException("list argument can't be null");

    T max = list.get(0).getMinimum();
    for(T elem : list)
      if(elem.greaterThan(max)) max = elem;

    return max;
  }
}

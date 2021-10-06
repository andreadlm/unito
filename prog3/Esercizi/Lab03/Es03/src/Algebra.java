public interface Algebra<T extends Number> {
  /**
   * Calculates the sum between two numbers.
   * @param n the number to be summed with-
   * @return the sum with n.
   */
  public T sum(T n);

  /**
   * Checks wheter a number is greater than another.
   * @param n the number to be confronted with.
   * @return true if N > n, false otherwise.
   */
  public boolean greaterThan(T n);

  /**
   * Provides the zero of the numeric type.
   * @return the zero of the numeric type.
   */
  public T getZero();

  /**
   * Provides the minimum number of the numeric type.
   * @return the minimum number of the numeric type.
   */
  public T getMinimum();
}

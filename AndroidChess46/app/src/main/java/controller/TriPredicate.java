package controller;

/**
 * Interface for TriPredicate
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */

public interface TriPredicate<T, U, V> {
	/**
	 * @param t Parameter
	 * @param u Parameter
	 * @param v Parameter
	 * @return True only if TriPredicate criteria is met.
	 */
	boolean test(T t, U u, V v);
}
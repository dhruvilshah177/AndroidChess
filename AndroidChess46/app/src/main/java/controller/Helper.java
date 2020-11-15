package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper class.
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Helper {

	public interface Predicate<T> {
		boolean test(T t);
	}

	public interface BiPredicate<T,U> {
		boolean test(T t, U u);
	}

	public interface TriPredicate<T,U,V> {
		boolean test(T t, U u, V v);
	}

	/**
	 * @param tCollect Input Collection
	 * @param u Parameter
	 * @param v Parameter
	 * @param triPredicate TriPredicate
	 * @param <T> Type Parameter
	 * @param <U> Type Parameter
	 * @param <V> Type Parameter
	 * @return True if predicate is met at least by one in the Collection.
	 */
	public static <T,U,V> boolean findOne(Collection<T> tCollect, U u, V v, TriPredicate<T,U,V> triPredicate) {
		for (T t : tCollect) {
			if (triPredicate.test(t, u, v)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param tCollect Input Collection
	 * @param predicate Predicate
	 * @param <T> Type Parameter
	 * @return True if predicate is met at least by one in the Collection.
	 */
	public static <T> boolean findOne(Collection<T> tCollect, Predicate<T> predicate) {
		for (T t : tCollect) {
			if (predicate.test(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param tCollect Input Collection
	 * @param u Parameter
	 * @param biPredicate BiPredicate
	 * @param <T> Type Parameter
	 * @param <U> Type Parameter
	 * @return List of elements in the Collection that meet criteria.
	 */
	public static <T,U> List<T> filter(Collection<T> tCollect, U u, BiPredicate<T,U> biPredicate) {
		//return tCollection.stream().filter(t -> biPredicate.test(t, u)).collect(Collectors.toList());
		List<T> list = new ArrayList<>();
		for (T t : tCollect) {
			if (biPredicate.test(t, u)) {
				list.add(t);
			}
		}
		return list;
	}
}
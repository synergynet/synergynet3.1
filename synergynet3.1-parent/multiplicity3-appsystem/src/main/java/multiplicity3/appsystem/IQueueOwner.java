package multiplicity3.appsystem;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The Interface IQueueOwner.
 */
public interface IQueueOwner
{

	/**
	 * Enqueue.
	 *
	 * @param <V>
	 *            the value type
	 * @param callable
	 *            the callable
	 * @return the future
	 */
	<V> Future<V> enqueue(Callable<V> callable);
}

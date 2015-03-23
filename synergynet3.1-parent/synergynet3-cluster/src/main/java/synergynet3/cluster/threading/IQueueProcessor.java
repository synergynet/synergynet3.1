package synergynet3.cluster.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The Interface IQueueProcessor.
 */
public interface IQueueProcessor {

	/**
	 * Enqueue.
	 *
	 * @param <V> the value type
	 * @param callable the callable
	 * @return the future
	 */
	<V> Future<V> enqueue(Callable<V> callable);
}

package synergynet3.cluster.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class ClusterThreadManager.
 */
public class ClusterThreadManager {

	/** The instance. */
	public static ClusterThreadManager instance;

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(ClusterThreadManager.class.getName());

	/** The queue processor. */
	private IQueueProcessor queueProcessor;

	/**
	 * Gets the.
	 *
	 * @return the cluster thread manager
	 */
	public static ClusterThreadManager get() {
		synchronized (ClusterThreadManager.class) {
			if (instance == null) {
				instance = new ClusterThreadManager();
				instance.setQueueProcessor(createDefaultQueueProcessor());
			}
			return instance;
		}
	}

	/**
	 * Creates the default queue processor.
	 *
	 * @return the i queue processor
	 */
	private static IQueueProcessor createDefaultQueueProcessor() {
		IQueueProcessor processor = new IQueueProcessor() {
			@Override
			public <V> Future<V> enqueue(Callable<V> callable) {
				try {
					callable.call();
				} catch (Exception e) {
					log.log(Level.WARNING, "A callable item had a problem", e);
				}
				return null;
			}
		};
		return processor;
	}

	/**
	 * Enqueue.
	 *
	 * @param <V> the value type
	 * @param callable the callable
	 * @return the future
	 */
	public <V> Future<V> enqueue(Callable<V> callable) {
		if (queueProcessor == null) {
			log.severe("Queue processor is not set in "
					+ ClusterThreadManager.class.getName()
					+ ". We are likely to crash right now.");
			return null;
		}
		return queueProcessor.enqueue(callable);
	}

	/**
	 * Sets the queue processor.
	 *
	 * @param proc the new queue processor
	 */
	public void setQueueProcessor(IQueueProcessor proc) {
		this.queueProcessor = proc;
	}
}

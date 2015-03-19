package synergynet3.cluster.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterThreadManager {
	private static final Logger log = Logger.getLogger(ClusterThreadManager.class.getName());
	
	public static ClusterThreadManager instance;
	
	public static ClusterThreadManager get() {
		synchronized(ClusterThreadManager.class) {
			if(instance == null) {
				instance = new ClusterThreadManager();
				instance.setQueueProcessor(createDefaultQueueProcessor());
			}
			return instance;
		}
	}

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

	private IQueueProcessor queueProcessor;
	
	public void setQueueProcessor(IQueueProcessor proc) {
		this.queueProcessor = proc;
	}
	
	public <V> Future<V> enqueue(Callable<V> callable) {
		if(queueProcessor == null) {
			log.severe("Queue processor is not set in " + ClusterThreadManager.class.getName() + ". We are likely to crash right now.");
			return null;
		}
		return queueProcessor.enqueue(callable);
	}
}

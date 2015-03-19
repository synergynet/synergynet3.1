package synergynet3.cluster.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface IQueueProcessor {
	<V> Future<V> enqueue(Callable<V> callable);
}

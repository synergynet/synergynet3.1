package multiplicity3.appsystem;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface IQueueOwner {
	<V> Future<V> enqueue(Callable<V> callable);
}

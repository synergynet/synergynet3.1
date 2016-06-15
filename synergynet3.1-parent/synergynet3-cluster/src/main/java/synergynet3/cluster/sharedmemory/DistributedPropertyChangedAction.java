package synergynet3.cluster.sharedmemory;

import com.hazelcast.core.Member;

/**
 * The Interface DistributedPropertyChangedAction.
 *
 * @param <T>
 *            the generic type
 */
public interface DistributedPropertyChangedAction<T>
{

	/**
	 * Distributed property did change.
	 *
	 * @param member
	 *            the member
	 * @param oldValue
	 *            the old value
	 * @param newValue
	 *            the new value
	 */
	void distributedPropertyDidChange(Member member, T oldValue, T newValue);
}

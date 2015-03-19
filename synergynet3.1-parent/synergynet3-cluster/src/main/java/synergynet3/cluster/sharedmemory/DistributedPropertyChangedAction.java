package synergynet3.cluster.sharedmemory;

import com.hazelcast.core.Member;

public interface DistributedPropertyChangedAction <T> {
	void distributedPropertyDidChange(Member member, T oldValue, T newValue);
}

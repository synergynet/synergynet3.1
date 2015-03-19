package synergynet3.cluster.clustereddevice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

public class ClusteredDeviceManager {
	private static final String MAP_DEVICE2MEMBER = "device_to_member";
		
	private Map<String,ClusteredDevice> knownDevices = new HashMap<String,ClusteredDevice>();
	private String identity;
	
	public ClusteredDeviceManager(String identity) {
		this.identity = identity;
		knownDevices = new HashMap<String,ClusteredDevice>();
	}
	
	public void join() {
		Hazelcast.getMap(MAP_DEVICE2MEMBER).put(identity, Hazelcast.getCluster().getLocalMember());
	}

	public List<String> getNamesOfDevicesInCluster(String forType) {
		List<String> list = new ArrayList<String>();
		Set<Member> members = Hazelcast.getCluster().getMembers();
		IMap<String,Member> nameToMember = Hazelcast.getMap(MAP_DEVICE2MEMBER);
		for(Member m : members) {
			String name = getNameForMember(m, nameToMember);
			list.add(name);
		}
		return list;
	}

	private String getNameForMember(Member m, IMap<String,Member> memberMap) {
		for(String name : memberMap.keySet()) {
			Member tm = memberMap.get(name);
			if(tm.equals(m)) {
				return name;
			}
		}
		return null;
	}
	
	public void add(ClusteredDevice d) {
		knownDevices.put(d.getNameOfOwnerDevice(), d);
	}
	
	public ClusteredDevice getClusteredDeviceByName(String name) {
		return knownDevices.get(name);
	}

	public Collection<String> getNames() {
		return knownDevices.keySet();
	}
	
}

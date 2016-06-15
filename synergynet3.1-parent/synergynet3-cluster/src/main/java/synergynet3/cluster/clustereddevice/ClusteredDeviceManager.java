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

/**
 * The Class ClusteredDeviceManager.
 */
public class ClusteredDeviceManager
{

	/** The Constant MAP_DEVICE2MEMBER. */
	private static final String MAP_DEVICE2MEMBER = "device_to_member";

	/** The identity. */
	private String identity;

	/** The known devices. */
	private Map<String, ClusteredDevice> knownDevices = new HashMap<String, ClusteredDevice>();

	/**
	 * Instantiates a new clustered device manager.
	 *
	 * @param identity
	 *            the identity
	 */
	public ClusteredDeviceManager(String identity)
	{
		this.identity = identity;
		knownDevices = new HashMap<String, ClusteredDevice>();
	}

	/**
	 * Adds the.
	 *
	 * @param d
	 *            the d
	 */
	public void add(ClusteredDevice d)
	{
		knownDevices.put(d.getNameOfOwnerDevice(), d);
	}

	/**
	 * Gets the clustered device by name.
	 *
	 * @param name
	 *            the name
	 * @return the clustered device by name
	 */
	public ClusteredDevice getClusteredDeviceByName(String name)
	{
		return knownDevices.get(name);
	}

	/**
	 * Gets the names.
	 *
	 * @return the names
	 */
	public Collection<String> getNames()
	{
		return knownDevices.keySet();
	}

	/**
	 * Gets the names of devices in cluster.
	 *
	 * @param forType
	 *            the for type
	 * @return the names of devices in cluster
	 */
	public List<String> getNamesOfDevicesInCluster(String forType)
	{
		List<String> list = new ArrayList<String>();
		Set<Member> members = Hazelcast.getCluster().getMembers();
		IMap<String, Member> nameToMember = Hazelcast.getMap(MAP_DEVICE2MEMBER);
		for (Member m : members)
		{
			String name = getNameForMember(m, nameToMember);
			list.add(name);
		}
		return list;
	}

	/**
	 * Join.
	 */
	public void join()
	{
		Hazelcast.getMap(MAP_DEVICE2MEMBER).put(identity, Hazelcast.getCluster().getLocalMember());
	}

	/**
	 * Gets the name for member.
	 *
	 * @param m
	 *            the m
	 * @param memberMap
	 *            the member map
	 * @return the name for member
	 */
	private String getNameForMember(Member m, IMap<String, Member> memberMap)
	{
		for (String name : memberMap.keySet())
		{
			Member tm = memberMap.get(name);
			if (tm.equals(m))
			{
				return name;
			}
		}
		return null;
	}

}

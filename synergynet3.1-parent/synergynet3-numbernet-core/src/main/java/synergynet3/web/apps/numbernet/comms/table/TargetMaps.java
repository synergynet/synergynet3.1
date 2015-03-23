package synergynet3.web.apps.numbernet.comms.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.web.apps.numbernet.shared.Edge;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.ExpressionVisibleProperties;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

/**
 * The Class TargetMaps.
 */
public class TargetMaps {

	/** The instance. */
	private static TargetMaps instance;

	/** The target to distributed map. */
	private Map<Double, DistributedMap<String, Expression>> targetToDistributedMap;

	/** The target to edges. */
	private Map<Double, DistributedMap<String, Edge>> targetToEdges;

	/** The target to expression visible properties map. */
	private Map<Double, DistributedMap<String, ExpressionVisibleProperties>> targetToExpressionVisiblePropertiesMap;

	/**
	 * Instantiates a new target maps.
	 */
	private TargetMaps() {
		targetToDistributedMap = new HashMap<Double, DistributedMap<String, Expression>>();
		targetToExpressionVisiblePropertiesMap = new HashMap<Double, DistributedMap<String, ExpressionVisibleProperties>>();
		targetToEdges = new HashMap<Double, DistributedMap<String, Edge>>();
	}

	/**
	 * Gets the.
	 *
	 * @return the target maps
	 */
	public static TargetMaps get() {
		if (instance == null) {
			instance = new TargetMaps();
		}
		return instance;
	}

	/**
	 * Gets the distributed map for target.
	 *
	 * @param target the target
	 * @return the distributed map for target
	 */
	public DistributedMap<String, Expression> getDistributedMapForTarget(
			double target) {
		DistributedMap<String, Expression> map = targetToDistributedMap
				.get(target);
		if (map == null) {
			map = createDistributedMapForTarget(target);
			targetToDistributedMap.put(target, map);
		}
		return map;
	}

	/**
	 * Gets the edges map for target.
	 *
	 * @param target the target
	 * @return the edges map for target
	 */
	public DistributedMap<String, Edge> getEdgesMapForTarget(double target) {
		DistributedMap<String, Edge> map = targetToEdges.get(target);
		if (map == null) {
			map = createEdgesMapForTarget(target);
			targetToEdges.put(target, map);
		}
		return map;
	}

	/**
	 * Gets the expression visible properties map for target.
	 *
	 * @param target the target
	 * @return the expression visible properties map for target
	 */
	public DistributedMap<String, ExpressionVisibleProperties> getExpressionVisiblePropertiesMapForTarget(
			double target) {
		DistributedMap<String, ExpressionVisibleProperties> map = targetToExpressionVisiblePropertiesMap
				.get(target);
		if (map == null) {
			map = createExpressionVisiblePropertiesMapForTarget(target);
			targetToExpressionVisiblePropertiesMap.put(target, map);
		}
		return map;
	}

	/**
	 * Gets the targets list.
	 *
	 * @return the targets list
	 */
	public List<Double> getTargetsList() {
		List<Double> targetList = new ArrayList<Double>();
		targetList.addAll(targetToDistributedMap.keySet());
		return targetList;
	}

	/**
	 * Creates the distributed map.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param name the name
	 * @param keyClass the key class
	 * @param valueClass the value class
	 * @return the distributed map
	 */
	private <K, V> DistributedMap<K, V> createDistributedMap(String name,
			Class<K> keyClass, Class<V> valueClass) {
		IMap<K, V> map = Hazelcast.getMap(name);
		return new DistributedMap<K, V>(map);
	}

	/**
	 * Creates the distributed map for target.
	 *
	 * @param target the target
	 * @return the distributed map
	 */
	private DistributedMap<String, Expression> createDistributedMapForTarget(
			double target) {
		String mapName = getDistributedMapNameForTarget(target);
		return createDistributedMap(mapName, String.class, Expression.class);
	}

	/**
	 * Creates the edges map for target.
	 *
	 * @param target the target
	 * @return the distributed map
	 */
	private DistributedMap<String, Edge> createEdgesMapForTarget(double target) {
		String mapName = getDistributedMapNameForEdgesMapWithTarget(target);
		return createDistributedMap(mapName, String.class, Edge.class);
	}

	/**
	 * Creates the expression visible properties map for target.
	 *
	 * @param target the target
	 * @return the distributed map
	 */
	private DistributedMap<String, ExpressionVisibleProperties> createExpressionVisiblePropertiesMapForTarget(
			double target) {
		String mapName = getDistribtedExpressionVisiblePropertiesMapNameForTarget(target);
		return createDistributedMap(mapName, String.class,
				ExpressionVisibleProperties.class);
	}

	/**
	 * Gets the distribted expression visible properties map name for target.
	 *
	 * @param target the target
	 * @return the distribted expression visible properties map name for target
	 */
	private String getDistribtedExpressionVisiblePropertiesMapNameForTarget(
			double target) {
		return "evp_dmap_" + target;
	}

	/**
	 * Gets the distributed map name for edges map with target.
	 *
	 * @param target the target
	 * @return the distributed map name for edges map with target
	 */
	private String getDistributedMapNameForEdgesMapWithTarget(double target) {
		return "edgesmap_" + target;
	}

	/**
	 * Gets the distributed map name for target.
	 *
	 * @param target the target
	 * @return the distributed map name for target
	 */
	private String getDistributedMapNameForTarget(double target) {
		return "expression_dmap_" + target;
	}
}

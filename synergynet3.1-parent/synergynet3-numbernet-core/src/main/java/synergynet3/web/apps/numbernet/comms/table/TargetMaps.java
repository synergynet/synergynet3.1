package synergynet3.web.apps.numbernet.comms.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.web.apps.numbernet.shared.Edge;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.ExpressionVisibleProperties;

public class TargetMaps {

	private static TargetMaps instance;

	public static TargetMaps get() {
		if(instance == null) instance = new TargetMaps();
		return instance;
	}
	
	private Map<Double,DistributedMap<String,Expression>> targetToDistributedMap;
	private Map<Double,DistributedMap<String,ExpressionVisibleProperties>> targetToExpressionVisiblePropertiesMap;
	private Map<Double,DistributedMap<String,Edge>> targetToEdges;
	
	private TargetMaps() {
		targetToDistributedMap = new HashMap<Double, DistributedMap<String,Expression>>();
		targetToExpressionVisiblePropertiesMap = new HashMap<Double,DistributedMap<String, ExpressionVisibleProperties>>();
		targetToEdges = new HashMap<Double,DistributedMap<String,Edge>>();
	}
	
	public DistributedMap<String,Expression> getDistributedMapForTarget(double target) {
		DistributedMap<String,Expression> map = targetToDistributedMap.get(target);
		if(map == null) {
			map = createDistributedMapForTarget(target);
			targetToDistributedMap.put(target, map);
		}
		return map;
	}
	
	public DistributedMap<String, ExpressionVisibleProperties> getExpressionVisiblePropertiesMapForTarget(double target) {
		DistributedMap<String, ExpressionVisibleProperties> map = targetToExpressionVisiblePropertiesMap.get(target);
		if(map == null) {
			map = createExpressionVisiblePropertiesMapForTarget(target);
			targetToExpressionVisiblePropertiesMap.put(target, map);
		}
		return map;
	}
	
	public DistributedMap<String,Edge> getEdgesMapForTarget(double target) {
		DistributedMap<String,Edge> map = targetToEdges.get(target);
		if(map == null) {
			map = createEdgesMapForTarget(target);
			targetToEdges.put(target, map);
		}
		return map;
	}
	
	private DistributedMap<String, Edge> createEdgesMapForTarget(double target) {
		String mapName = getDistributedMapNameForEdgesMapWithTarget(target);
		return createDistributedMap(mapName, String.class, Edge.class);
	}

	private DistributedMap<String, ExpressionVisibleProperties> createExpressionVisiblePropertiesMapForTarget(double target) {
		String mapName = getDistribtedExpressionVisiblePropertiesMapNameForTarget(target);
		return createDistributedMap(mapName, String.class, ExpressionVisibleProperties.class);
	}

	private DistributedMap<String,Expression> createDistributedMapForTarget(double target) {
		String mapName = getDistributedMapNameForTarget(target);
		return createDistributedMap(mapName, String.class, Expression.class);
	}
	
	private String getDistributedMapNameForTarget(double target) {
		return "expression_dmap_" + target;
	}
	
	private String getDistribtedExpressionVisiblePropertiesMapNameForTarget(
			double target) {
		return "evp_dmap_" + target;
	}
	
	private String getDistributedMapNameForEdgesMapWithTarget(double target) {
		return "edgesmap_" + target;
	}

	private <K,V> DistributedMap<K,V> createDistributedMap(String name, Class<K> keyClass, Class<V> valueClass) {
		IMap<K,V> map = Hazelcast.getMap(name);
		return new DistributedMap<K,V>(map);
	}

	public List<Double> getTargetsList() {
		List<Double> targetList = new ArrayList<Double>();
		targetList.addAll(targetToDistributedMap.keySet());
		return targetList;
	}
}

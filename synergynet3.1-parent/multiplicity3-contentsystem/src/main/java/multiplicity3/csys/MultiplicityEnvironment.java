package multiplicity3.csys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplicity3.csys.stage.IStage;

public class MultiplicityEnvironment {

	private static MultiplicityEnvironment instance;

	public static MultiplicityEnvironment get() {
		synchronized(MultiplicityEnvironment.class) { 
			if(instance == null) {
				instance = new MultiplicityEnvironment();
			}
		}
		return instance;
	}

	private Map<String,IStage> stages;
	
	private MultiplicityEnvironment() {
		stages = new HashMap<String,IStage>();
	}
	
	public int getStageCount() {
		return stages.size();
	}
	
	public boolean addStage(String name, IStage stage) {
		if(stages.containsKey(name)) {
			return false;
		}
		
		stages.put(name, stage);
		return true;
	}
		
	public IStage getStage(String name) {
		
		return null;
	}
	
	public List<IStage> getLocalStages() {
		List<IStage> localStages = new ArrayList<IStage>();
		for(IStage stage : stages.values()) {
			if(stage.isLocal()) {
				localStages.add(stage);
			}
		}
		return localStages;
	}	
}

package multiplicity3.csys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplicity3.csys.stage.IStage;

/**
 * The Class MultiplicityEnvironment.
 */
public class MultiplicityEnvironment
{

	/** The instance. */
	private static MultiplicityEnvironment instance;

	/** The stages. */
	private Map<String, IStage> stages;

	/**
	 * Instantiates a new multiplicity environment.
	 */
	private MultiplicityEnvironment()
	{
		stages = new HashMap<String, IStage>();
	}

	/**
	 * Gets the.
	 *
	 * @return the multiplicity environment
	 */
	public static MultiplicityEnvironment get()
	{
		synchronized (MultiplicityEnvironment.class)
		{
			if (instance == null)
			{
				instance = new MultiplicityEnvironment();
			}
		}
		return instance;
	}

	/**
	 * Adds the stage.
	 *
	 * @param name
	 *            the name
	 * @param stage
	 *            the stage
	 * @return true, if successful
	 */
	public boolean addStage(String name, IStage stage)
	{
		if (stages.containsKey(name))
		{
			return false;
		}

		stages.put(name, stage);
		return true;
	}

	/**
	 * Gets the local stages.
	 *
	 * @return the local stages
	 */
	public List<IStage> getLocalStages()
	{
		List<IStage> localStages = new ArrayList<IStage>();
		for (IStage stage : stages.values())
		{
			if (stage.isLocal())
			{
				localStages.add(stage);
			}
		}
		return localStages;
	}

	/**
	 * Gets the stage.
	 *
	 * @param name
	 *            the name
	 * @return the stage
	 */
	public IStage getStage(String name)
	{

		return null;
	}

	/**
	 * Gets the stage count.
	 *
	 * @return the stage count
	 */
	public int getStageCount()
	{
		return stages.size();
	}
}

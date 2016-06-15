package synergynet3.activitypack1.web.server;

import synergynet3.activitypack1.core.gravitysim.GravitySimDeviceControl;
import synergynet3.activitypack1.web.client.service.GravitySimService;
import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class GravitySimServiceImpl.
 */
public class GravitySimServiceImpl extends RemoteServiceServlet implements GravitySimService
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1090811961083393903L;

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.activitypack1.web.client.service.GravitySimService#clearAllBodies
	 * ()
	 */
	@Override
	public void clearAllBodies()
	{
		Integer value = GravitySimDeviceControl.get().getClearBodiesTrigger().getValue();
		if (null == value)
		{
			GravitySimDeviceControl.get().getClearBodiesTrigger().setValue(0);
		}
		else
		{
			GravitySimDeviceControl.get().getClearBodiesTrigger().setValue(value + 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.activitypack1.web.client.service.GravitySimService#
	 * decreaseGravity()
	 */
	@Override
	public void decreaseGravity()
	{
		Double gravity = GravitySimDeviceControl.get().getGravityControl().getValue();
		gravity = gravity - (gravity * 0.5);
		GravitySimDeviceControl.get().getGravityControl().setValue(gravity);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.activitypack1.web.client.service.GravitySimService#
	 * decreaseSimulationSpeed()
	 */
	@Override
	public void decreaseSimulationSpeed()
	{
		Double simSpeed = GravitySimDeviceControl.get().getGravityControl().getValue();
		simSpeed = simSpeed - (simSpeed * 0.1);
		GravitySimDeviceControl.get().getGravityControl().setValue(simSpeed);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.activitypack1.web.client.service.GravitySimService#
	 * increaseGravity()
	 */
	@Override
	public void increaseGravity()
	{
		Double gravity = GravitySimDeviceControl.get().getGravityControl().getValue();
		gravity = gravity + (gravity * 0.5);
		GravitySimDeviceControl.get().getGravityControl().setValue(gravity);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.activitypack1.web.client.service.GravitySimService#
	 * increaseSimulationSpeed()
	 */
	@Override
	public void increaseSimulationSpeed()
	{
		Double simSpeed = GravitySimDeviceControl.get().getGravityControl().getValue();
		simSpeed = simSpeed + (simSpeed * 0.1);
		GravitySimDeviceControl.get().getGravityControl().setValue(simSpeed);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.activitypack1.web.client.service.GravitySimService#setBodyLimit
	 * (int)
	 */
	@Override
	public void setBodyLimit(int newLimit)
	{
		GravitySimDeviceControl.get().getBodyLimit().setValue(newLimit);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.activitypack1.web.client.service.GravitySimService#setScenario
	 * (synergynet3.activitypack1.web.shared.UniverseScenario)
	 */
	@Override
	public void setScenario(UniverseScenario scenario)
	{
		GravitySimDeviceControl.get().getScenario().setValue(scenario);
	}

}

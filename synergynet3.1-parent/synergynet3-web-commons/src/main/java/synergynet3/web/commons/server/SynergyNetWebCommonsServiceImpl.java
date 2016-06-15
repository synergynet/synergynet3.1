package synergynet3.web.commons.server;

import java.util.List;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.commons.client.service.SynergyNetWebCommonsService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class SynergyNetWebCommonsServiceImpl.
 */
public class SynergyNetWebCommonsServiceImpl extends RemoteServiceServlet implements SynergyNetWebCommonsService
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9068572084864297597L;

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.commons.client.service.SynergyNetWebCommonsService#
	 * getDevicesCurrentlyOnline(java.lang.String)
	 */
	@Override
	public List<String> getDevicesCurrentlyOnline(String deviceType)
	{
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline(deviceType);
	}

}

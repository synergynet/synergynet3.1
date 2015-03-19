package synergynet3.web.commons.server;

import java.util.List;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.commons.client.service.SynergyNetWebCommonsService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SynergyNetWebCommonsServiceImpl extends RemoteServiceServlet implements SynergyNetWebCommonsService {
	private static final long serialVersionUID = 9068572084864297597L;

	@Override
	public List<String> getDevicesCurrentlyOnline(String deviceType) {
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline(deviceType);
	}

}

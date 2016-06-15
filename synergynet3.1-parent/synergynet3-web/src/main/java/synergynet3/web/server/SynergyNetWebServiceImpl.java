package synergynet3.web.server;

import synergynet3.web.client.service.SynergyNetWebService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class SynergyNetWebServiceImpl.
 */
public class SynergyNetWebServiceImpl extends RemoteServiceServlet implements SynergyNetWebService
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5496870039773489395L;

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.client.service.SynergyNetWebService#test(java.lang.String
	 * )
	 */
	@Override
	public void test(String in)
	{

	}

}

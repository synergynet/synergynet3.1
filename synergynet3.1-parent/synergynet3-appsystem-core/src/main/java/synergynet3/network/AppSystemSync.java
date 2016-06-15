package synergynet3.network;

import java.util.ArrayList;

import synergynet3.SynergyNetApp;
import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.web.core.AppSystemDeviceControl;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.hazelcast.core.Member;
import com.jme3.math.Vector2f;

/**
 * Manages the listeners used for default SynergyNetApp extending application
 * network cluster entries.
 */
public class AppSystemSync
{

	/**
	 * Listener for network messages instigating bringing student icons to the
	 * top of an environment.
	 */
	private DistributedPropertyChangedAction<PerformActionMessage> bringStudentsToTopAction = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived() && (newValue.getMessageState() == MESSAGESTATE.ACTIVATE))
			{
				snNode.bringAllStudentsToTop();
			}
		}
	};

	/** The freeze action. */
	private DistributedPropertyChangedAction<PerformActionMessage> freezeAction = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived())
			{
				if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE)
				{
					snNode.toggleFreeze();
				}
			}
		}
	};

	/** Listener for network messages relating to students being logged in. */
	private DistributedPropertyChangedAction<String> loginAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, String oldValue, String newValue)
		{
			if (!newValue.equals(""))
			{
				snNode.login(newValue);
			}
		}
	};

	/** Listener for network messages relating to students being logged out. */
	private DistributedPropertyChangedAction<String> logoutAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, String oldValue, String newValue)
		{
			if (!newValue.equals(""))
			{
				snNode.logout(newValue);
			}
		}
	};

	/** Listener for network messages relating to a class being logged out. */
	private DistributedPropertyChangedAction<String> logoutClassAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, String oldValue, String newValue)
		{
			snNode.logoutAllOfClass(newValue);
		}
	};

	/**
	 * Listener for network messages announcing the arrival of an item
	 * transferred through a network flick.
	 */
	private DistributedPropertyChangedAction<FlickMessage> networkflick = new DistributedPropertyChangedAction<FlickMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, FlickMessage oldValue, FlickMessage newValue)
		{
			if (!newValue.messageAlreadyReceived() && (newValue.getMessageState() == MESSAGESTATE.ACTIVATE))
			{
				if (newValue.getTargetTableID() != null)
				{
					snNode.onFlickArrival(newValue);
				}
			}
		}
	};

	/**
	 * Listener for network messages announcing whether network flicks are
	 * enabled for this device.
	 */
	private DistributedPropertyChangedAction<PerformActionMessage> networkflickState = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived())
			{
				if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE)
				{
					snNode.enableNetworkFlick();
				}
				else if (newValue.getMessageState() == MESSAGESTATE.DEACTIVATE)
				{
					snNode.disableNetworkFlick();
				}
			}
		}
	};

	/**
	 * Listener for network messages initiating the transfer of contents to
	 * projectors.
	 */
	private DistributedPropertyChangedAction<ArrayList<ContentTransferedMessage>> recieveContentsFromProjector = new DistributedPropertyChangedAction<ArrayList<ContentTransferedMessage>>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, ArrayList<ContentTransferedMessage> oldValue, ArrayList<ContentTransferedMessage> newValue)
		{
			if (newValue.size() > 0)
			{
				snNode.onContentFromProjectorArrival(newValue);
			}
		}
	};

	/**
	 * Listener for network messages instigating the recreation of items from a
	 * removable drive.
	 */
	private DistributedPropertyChangedAction<PerformActionMessage> reloadRemovableDriveContentsAction = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived() && (newValue.getMessageState() == MESSAGESTATE.ACTIVATE))
			{
				snNode.reloadRemovableMediaCache();
			}
		}
	};

	/**
	 * Listener for network messages instigating the recreation of items from a
	 * device's directory in the networked shared cache.
	 */
	private DistributedPropertyChangedAction<PerformActionMessage> reloadServerContentsAction = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived() && (newValue.getMessageState() == MESSAGESTATE.ACTIVATE))
			{
				snNode.reloadServerCache();
			}
		}
	};

	/**
	 * Listener for network messages instigating the removal of any items in an
	 * environment not directly created by the application.
	 */
	private DistributedPropertyChangedAction<PerformActionMessage> removeAdditionalMedia = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived() && (newValue.getMessageState() == MESSAGESTATE.ACTIVATE))
			{
				snNode.removeAdditionalMedia();
			}
		}
	};

	/**
	 * Listener for network messages initiating the transfer of contents to
	 * projectors.
	 */
	private DistributedPropertyChangedAction<String[]> sendContentsToProjector = new DistributedPropertyChangedAction<String[]>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, String[] oldValue, String[] newValue)
		{
			snNode.sendContentsToProjectors(newValue);
		}
	};

	/**
	 * Listener for network messages initiating the transfer of a screenshot to
	 * projectors.
	 */
	private DistributedPropertyChangedAction<String[]> sendScreenShotToProjector = new DistributedPropertyChangedAction<String[]>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, String[] oldValue, String[] newValue)
		{
			snNode.sendScreenShotToProjectors(newValue);
		}
	};

	/** An instance of a SynergyNetApp extending application. */
	private SynergyNetApp snNode;

	/**
	 * Manages the network cluster default entries for SynergyNetApp extending
	 * application.
	 */
	private AppSystemDeviceControl synergynetAppControl;

	/**
	 * Listener for network messages announcing the location of another device's
	 * interface position.
	 */
	private DistributedPropertyChangedAction<SynergyNetPosition> tableLocationsUpdate = new DistributedPropertyChangedAction<SynergyNetPosition>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, SynergyNetPosition oldValue, SynergyNetPosition newValue)
		{
			if (!newValue.messageAlreadyReceived())
			{
				if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE)
				{
					snNode.updateVirtualTable(newValue);
				}
				else if (newValue.getMessageState() == MESSAGESTATE.DEACTIVATE)
				{
					snNode.removeVirtualTable(newValue);
				}
			}
		}
	};

	/**
	 * Listener for network messages instigating the creation of a screenshot
	 * item.
	 */
	private DistributedPropertyChangedAction<PerformActionMessage> takeScreenShot = new DistributedPropertyChangedAction<PerformActionMessage>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue)
		{
			if (!newValue.messageAlreadyReceived() && (newValue.getMessageState() == MESSAGESTATE.ACTIVATE))
			{
				snNode.createScreenShotItem(new Vector2f(), 0);
			}
		}
	};

	/**
	 * Initiates the listener management.
	 *
	 * @param synergynetAppControl
	 *            Manager of the network cluster default entries for
	 *            SynergyNetApp extending application.
	 * @param snNode
	 *            An instance of a SynergyNetApp extending application.
	 */
	public AppSystemSync(AppSystemDeviceControl synergynetAppControl, SynergyNetApp snNode)
	{
		this.synergynetAppControl = synergynetAppControl;
		this.snNode = snNode;
		addSync();
	}

	/**
	 * Gets the manager of the network cluster default entries for SynergyNetApp
	 * extending application.
	 *
	 * @return An manager of the network cluster default entries for
	 *         SynergyNetApp extending application.
	 */
	public AppSystemDeviceControl getSynergynetAppControl()
	{
		return synergynetAppControl;
	}

	/**
	 * Changes the application which the listeners interacts with.
	 *
	 * @param snNode
	 *            An instance of a SynergyNetApp extending application.
	 */
	public void reSync(SynergyNetApp snNode)
	{
		this.snNode = snNode;
	}

	/**
	 * Stops the listeners used for default SynergyNetApp extending application
	 * network cluster entries.
	 */
	public void stop()
	{
		synergynetAppControl.getStudentLoginControl().unregisterChangeListener(loginAction);
		synergynetAppControl.getFreezeControlVariable().unregisterChangeListener(freezeAction);
		synergynetAppControl.getStudentLogoutControl().unregisterChangeListener(logoutAction);
		synergynetAppControl.getStudentLogoutOfClassControl().unregisterChangeListener(logoutClassAction);
		synergynetAppControl.getReloadServerContentsControl().unregisterChangeListener(reloadServerContentsAction);
		synergynetAppControl.getReloadRemovableDriveContentsControl().unregisterChangeListener(reloadRemovableDriveContentsAction);
		synergynetAppControl.getStudentsOnTopControl().unregisterChangeListener(bringStudentsToTopAction);
		synergynetAppControl.getTakeScreenshotControl().unregisterChangeListener(takeScreenShot);
		synergynetAppControl.getRemoveAdditionalMediaControl().unregisterChangeListener(removeAdditionalMedia);
		synergynetAppControl.getTablePositions().unregisterChangeListener(tableLocationsUpdate);
		synergynetAppControl.getNetworkFlick().unregisterChangeListener(networkflick);
		synergynetAppControl.getNetworkFlickState().unregisterChangeListener(networkflickState);
		synergynetAppControl.getSendScreenshotsToProjectors().unregisterChangeListener(sendScreenShotToProjector);
		synergynetAppControl.getSendContentsToProjectors().unregisterChangeListener(sendContentsToProjector);
		synergynetAppControl.getContentTransferToTableControl().unregisterChangeListener(recieveContentsFromProjector);
	}

	/**
	 * Initiates the listeners used for default SynergyNetApp extending
	 * application network cluster entries.
	 */
	private void addSync()
	{
		synergynetAppControl.getStudentLoginControl().registerChangeListener(loginAction);
		synergynetAppControl.getFreezeControlVariable().registerChangeListener(freezeAction);
		synergynetAppControl.getStudentLogoutControl().registerChangeListener(logoutAction);
		synergynetAppControl.getStudentLogoutOfClassControl().registerChangeListener(logoutClassAction);
		synergynetAppControl.getReloadServerContentsControl().registerChangeListener(reloadServerContentsAction);
		synergynetAppControl.getReloadRemovableDriveContentsControl().registerChangeListener(reloadRemovableDriveContentsAction);
		synergynetAppControl.getStudentsOnTopControl().registerChangeListener(bringStudentsToTopAction);
		synergynetAppControl.getTakeScreenshotControl().registerChangeListener(takeScreenShot);
		synergynetAppControl.getRemoveAdditionalMediaControl().registerChangeListener(removeAdditionalMedia);
		synergynetAppControl.getTablePositions().registerChangeListener(tableLocationsUpdate);
		synergynetAppControl.getNetworkFlick().registerChangeListener(networkflick);
		synergynetAppControl.getNetworkFlickState().registerChangeListener(networkflickState);
		synergynetAppControl.getSendScreenshotsToProjectors().registerChangeListener(sendScreenShotToProjector);
		synergynetAppControl.getSendContentsToProjectors().registerChangeListener(sendContentsToProjector);
		synergynetAppControl.getContentTransferToTableControl().registerChangeListener(recieveContentsFromProjector);
	}

}

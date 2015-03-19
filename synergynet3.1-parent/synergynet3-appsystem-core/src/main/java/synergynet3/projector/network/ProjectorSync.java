package synergynet3.projector.network;

import java.util.ArrayList;

import com.hazelcast.core.Member;

import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.projector.SynergyNetProjector;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.projector.web.ProjectorDeviceControl;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

/**
 * Manages the listeners used for default SynergyNetApp extending application network cluster entries.
 */
public class ProjectorSync {

	/** An instance of a SynergyNetApp extending application. */
	private SynergyNetProjector snProjectorNode;
	
	/** Manages the network cluster default entries for SynergyNetApp extending application. */
	private ProjectorDeviceControl synergynetProjectorControl;

	/** Listener for network messages relating to clearing projector contents. */
	private DistributedPropertyChangedAction<PerformActionMessage> clearAction = new DistributedPropertyChangedAction<PerformActionMessage>() {
		@Override
		public void distributedPropertyDidChange(Member member,	PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived() && newValue.getMessageState() == MESSAGESTATE.ACTIVATE)snProjectorNode.clearContents();	
		}
	};
	
	/** Listener for network messages relating to aligning projector contents. */
	private DistributedPropertyChangedAction<PerformActionMessage> alignAction = new DistributedPropertyChangedAction<PerformActionMessage>() {
		@Override
		public void distributedPropertyDidChange(Member member,	PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived() && newValue.getMessageState() == MESSAGESTATE.ACTIVATE)snProjectorNode.alignContents();	
		}
	};
	
	/** Listener for network messages relating to sending projector contents. */
	private DistributedPropertyChangedAction<String[]> sendProjectedContentsAction = new DistributedPropertyChangedAction<String[]>() {
		@Override
		public void distributedPropertyDidChange(Member member,	String[] oldValue, String[] newValue) {
			snProjectorNode.sendProjectedContents(newValue);	
		}
	};
	
	
	/** Listener for network messages relating to projector receiving content. */
	private DistributedPropertyChangedAction<ArrayList<ContentTransferedMessage>> receiveContentAction = new DistributedPropertyChangedAction<ArrayList<ContentTransferedMessage>>() {
		@Override
		public void distributedPropertyDidChange(Member member,	ArrayList<ContentTransferedMessage> oldValue, ArrayList<ContentTransferedMessage> newValue) {
			if (newValue.size() > 0)snProjectorNode.onContentArrival(newValue);	
		}
	};
	
	
	/**
	 * Initiates the listener management.
	 * 
	 * @param synergynetAppControl Manager of the network cluster default entries for SynergyNetApp extending application.
	 * @param snNode An instance of a SynergyNetApp extending application.
	 */
	public ProjectorSync(ProjectorDeviceControl synergynetProjectorControl, SynergyNetProjector snProjectorNode) {
		this.synergynetProjectorControl = synergynetProjectorControl;
		this.snProjectorNode = snProjectorNode;
		addSync();
	}
	
	/**
	 * Initiates the listeners used for SynergyNetProjector cluster entries.
	 */
	private void addSync() {	
		synergynetProjectorControl.getProjectorClearControl().registerChangeListener(clearAction);	
		synergynetProjectorControl.getProjectorAlignControl().registerChangeListener(alignAction);	
		synergynetProjectorControl.getSendContentsToTablesControl().registerChangeListener(sendProjectedContentsAction);
		synergynetProjectorControl.getContentTransferToProjectorControl().registerChangeListener(receiveContentAction);
	}
	
	/**
	 * Stops the listeners used for default SynergyNetProjector cluster entries.
	 */
	public void stop(){
		synergynetProjectorControl.getProjectorClearControl().unregisterChangeListener(clearAction);	
		synergynetProjectorControl.getProjectorAlignControl().unregisterChangeListener(alignAction);	
		synergynetProjectorControl.getSendContentsToTablesControl().unregisterChangeListener(sendProjectedContentsAction);
		synergynetProjectorControl.getContentTransferToProjectorControl().unregisterChangeListener(receiveContentAction);
	}

	/**
	 * Gets the manager of the network cluster default entries for SynergyNetApp extending application.
	 * 
	 * @return An manager of the network cluster default entries for SynergyNetApp extending application.
	 */
	public ProjectorDeviceControl getSynergynetProjectorControl() {
		return synergynetProjectorControl;
	}

}

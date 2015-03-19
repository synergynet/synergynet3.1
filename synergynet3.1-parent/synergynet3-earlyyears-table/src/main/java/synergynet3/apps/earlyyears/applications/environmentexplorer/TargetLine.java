package synergynet3.apps.earlyyears.applications.environmentexplorer;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.ColorRGBA;

import synergynet3.additionalitems.ZManager;
import synergynet3.additionalitems.jme.AudioRecorder;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class TargetLine {
	
	private IStage stage;
	private Logger log;
	private ILine line;
	private IItem source;
	private IItem target;
	private int targetIndex = 0;
	
	private ArrayList<IItem>targets = new ArrayList<IItem>();

	public TargetLine(IStage stage, Logger log, IItem source, ArrayList<IItem>targets) {
		this.stage = stage;
		if (log == null){
			this.log = Logger.getLogger(AudioRecorder.class.getName());
		}else{
			this.log = log;
		}		
		this.source = source;	
		createTargetLine(targets);
	}	
	
	private void createTargetLine(ArrayList<IItem>targets){
		try {
			this.targets=targets;
			line = this.stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
			line.setSourceItem(source);
			line.setLineWidth(10f);
			line.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {
					source.setZOrder(line.getZOrder()+1);
					target.setZOrder(line.getZOrder()+1);
				}
			});		
					
			line.setInteractionEnabled(false);
			
			setTarget(source);			
			stage.addItem(line);
			
			hideLine();			
			
		} catch (ContentTypeNotBoundException e) {
			this.log.log(Level.SEVERE, "Content not Bound.", e);
		}	
	}
	
	public void hideLine(){
		if (line.isVisible()){
			line.setVisible(false);
		}
	}
	
	public void setColour(ColorRGBA colour){
		line.setLineColour(colour);
	}
	
	public void showLine(){
		if (!line.isVisible()){
			line.setVisible(true);
		}
	}
	
	public void remove(){
		stage.removeItem(line);
	}
	
	public boolean getNextTarget(){
		if (targets.size() > 1){
			targetIndex++;
			if (targetIndex >= targets.size()){
				targetIndex = 0;
			}
			if (targets.get(targetIndex).equals(line) || targets.get(targetIndex).equals(source) ){
				getNextTarget();
			}else{
				setTarget(targets.get(targetIndex));
				return true;
			}
		}else if (targets.size()==1){
			setTarget(targets.get(0));
			return true;
		}				

		return false;
	}	
	
	public boolean getPreviousTarget(){
		if (targets.size() > 1){
			targetIndex--;
			if (targetIndex < 0){
				targetIndex = targets.size() - 1;
			}
			if (targets.get(targetIndex).equals(line) || targets.get(targetIndex).equals(source) ){
				getPreviousTarget();
			}else{
				setTarget(targets.get(targetIndex));
				return true;
			}
		}else if (targets.size()==1){
			setTarget(targets.get(0));
			return true;
		}	

		return false;
	}	
	
	private void setTarget(IItem target){
		showLine();
		this.target = target;		
		ZManager.manageLineOrderFull(stage, line, line.getSourceItem(), target);
	}
	
	public IItem getCurrentTarget(){
		if (target.equals(source)){
			return null;
		}
		return target;
	}

}

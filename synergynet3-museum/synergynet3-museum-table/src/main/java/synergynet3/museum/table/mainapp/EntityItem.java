package synergynet3.museum.table.mainapp;

import java.util.ArrayList;
import java.util.Collections;

import java.util.UUID;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.museum.table.MuseumApp;
import synergynet3.museum.table.mainapp.userrecorder.UserRecordingPromptLabel;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;

public class EntityItem{	
	
	private static final float ANGLE_OFFSET = 20;
	private static final float SCALE_OFFSET = 0.2f;
	
	private ArrayList<UserRecordingPromptLabel> userRecordingPromptLabels = new ArrayList<UserRecordingPromptLabel>();
	
	private ITextbox nameLabel;
	private ArrayList<IItem> items;
	private ArrayList<ILine> lines = new ArrayList<ILine>();
	
	private ILine lineFromPOI;
	
	private IStage stage;
	
	private boolean isVisible;
	private EntityManager entityManager;

	public EntityItem(IStage stage, String name, ArrayList<IItem> items, ArrayList<IItem> userGeneratedItems, MuseumApp app) {	
		this.items = items;
		this.stage = stage;
		entityManager = app.getEntityManager();
		
		try {
			nameLabel = LabelGenerator.generateName(name, stage, app);
			nameLabel.setVisible(false);	
			stage.addItem(nameLabel);
								
			for (IItem item: items){		
				initialiseItem(item, false);
			}
			for (IItem item: userGeneratedItems){		
				initialiseItem(item, true);
				items.add(item);
			}
			
			regenerate(nameLabel.getRelativeLocation().x, nameLabel.getRelativeLocation().y);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}		
	}
		
	private void initialiseItem(IItem item, boolean userGenerated) throws ContentTypeNotBoundException{
		generateLine(item, stage, userGenerated);		

		item.setVisible(false);		
		stage.addItem(item);	
	}
	
	private void generateLine(final IItem item, IStage stage, boolean userGenerated) throws ContentTypeNotBoundException { 
		ILine line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
		line.setLineWidth(6f);
		line.setInteractionEnabled(false);
		line.getZOrderManager().setAutoBringToTop(false);
		if (userGenerated){
			line.setLineColour(MuseumAppPreferences.getUserGeneratedContentColour());			
		}else{
			line.setLineColour(MuseumAppPreferences.getEntityBorderColour());
		}
		line.setSourceItem(item);
		line.setDestinationItem(nameLabel);		
		line.setVisible(false);		
		stage.addItem(line);
		lines.add(line);			
	}

	public void regenerate(float x, float y){
		setAskew(nameLabel);
		setRandomScale(nameLabel);
		
		float offSetAngle = (float)(Math.random())*360;
		nameLabel.setRelativeLocation(moveAwayFromEdge(getItemPosition(offSetAngle, 60f).add(new Vector2f(x, y)), MuseumAppPreferences.getEntitySpread()));
		
		float angle = (float)(Math.random())*360;
		int angleStep = 0;
		if (items.size() > 0){
			angleStep = 360/items.size();
		}
		Collections.shuffle(items);
		for (IItem item : items){
			setAskew(item);
			Vector2f newLoc = getItemPosition(angle, MuseumAppPreferences.getEntitySpread()).add(new Vector2f(nameLabel.getRelativeLocation().x, nameLabel.getRelativeLocation().y));			
			item.setRelativeLocation(newLoc);
			angle += angleStep;
		}				
	}
	
	private Vector2f moveAwayFromEdge(Vector2f loc, float buffer){
		if (loc.x < -stage.getDisplayWidth()/2 + buffer){
			loc.setX(-stage.getDisplayWidth()/2 + buffer);
		}else if (loc.x > stage.getDisplayWidth()/2 - buffer){
			loc.setX(stage.getDisplayWidth()/2 - buffer);
		}
		if (loc.y < -stage.getDisplayHeight()/2 + buffer){
			loc.setY(-stage.getDisplayHeight()/2 + buffer);
		}else if (loc.y > stage.getDisplayHeight()/2 - buffer){
			loc.setY(stage.getDisplayHeight()/2 - buffer);
		}
		return loc;
	}
	
	private void setAskew(IItem item){
		int angle = (int)(Math.random()*(ANGLE_OFFSET) + 0.5);
		if (Math.random()>0.5)angle = -angle;
		item.setRelativeRotation((float)Math.toRadians(angle));	
	}
	
	private void setRandomScale(IItem item){ 
		int scale = (int)(Math.random()*(SCALE_OFFSET));
		if (Math.random()>0.5)scale = -scale;
		item.setRelativeScale(1 + scale);	
	}
	
	private Vector2f getItemPosition(float angle, float radius) {		
		Vector2f position = new Vector2f(0, radius);
		position.rotateAroundOrigin(FastMath.DEG_TO_RAD * angle, true);
		return position;
	}
	
	public void setVisible(boolean isVisible){		
		if (!(isVisible && isVisible())){	
			this.isVisible = isVisible;			
			for (ILine line: lines){		
				if (!(isVisible && isUserRecordingPromptLabel(line.getSourceItem()) && !entityManager.areRecordingPromptsVisible())){
					line.setVisible(isVisible);						
				}
			}			
			for (IItem item: items){			
				if (!(isVisible && isUserRecordingPromptLabel(item) && !entityManager.areRecordingPromptsVisible())){
					item.setVisible(isVisible);			
				}
			}			
			if (nameLabel != null){
				nameLabel.setVisible(isVisible);
			}			
			if (isVisible){
				regenerate(nameLabel.getRelativeLocation().x, nameLabel.getRelativeLocation().y);				
				for (IItem line: lines){								
					stage.getZOrderManager().bringToTop(line);
				}				
				for (IItem item: items){				
					stage.getZOrderManager().bringToTop(item);	
				}				
				if (nameLabel != null){	
					stage.getZOrderManager().bringToTop(nameLabel);
				}					
			}else{			
				for (IItem item: items){	
					if (item instanceof IMediaPlayer){
						IMediaPlayer player = (IMediaPlayer)item;
						player.pause();
						player.setPosition(0);
					}
				}
				closeRecorder();
			}
			if (lineFromPOI != null){
				if (lineFromPOI.getSourceItem().isVisible() && lineFromPOI.getDestinationItem().isVisible()){
					lineFromPOI.setVisible(true);
				}else{
					lineFromPOI.setVisible(false);
				}
			}
			
		}
	}
	
	private void closeRecorder(){
		for (UserRecordingPromptLabel userRecordingPromptLabel: userRecordingPromptLabels){
			userRecordingPromptLabel.closeRecorder();
		}
	}
	
	private boolean isUserRecordingPromptLabel(IItem possibleUserRecordingPromptLabel){
		for (UserRecordingPromptLabel userRecordingPromptLabel: userRecordingPromptLabels){
			if (userRecordingPromptLabel.getTextItem() == possibleUserRecordingPromptLabel){
				return true;
			}
		}
		return false;
	}
	
	public void addUserRecordingPromptLabel(UserRecordingPromptLabel userRecordingPromptLabel){
		userRecordingPromptLabels.add(userRecordingPromptLabel);
	}
	
	public void setUserRecordingPromptLabelVisibility(boolean visibility){
		if (isVisible){
			for (UserRecordingPromptLabel userRecordingPromptLabel: userRecordingPromptLabels){
				userRecordingPromptLabel.getTextItem().setVisible(visibility);
				for (ILine line: lines){		
					if (userRecordingPromptLabel.getTextItem() == line.getSourceItem()){
						line.setVisible(visibility);						
					}
				}	
			}
		}
	}	
	
	public boolean isVisible(){
		return isVisible; 
	}

	public ITextbox getCentralItem() {
		return nameLabel;
	}

	public void setLineFromPOI(ILine lineFromPOI) {
		this.lineFromPOI = lineFromPOI;
	}

}

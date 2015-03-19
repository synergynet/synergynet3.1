package synergynet3.museum.table.mainapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.mediadetection.mediasearchtypes.AudioSearchType;
import synergynet3.mediadetection.mediasearchtypes.VideoSearchType;
import synergynet3.mediadetection.mediasearchtypes.WavefrontSearchType;
import synergynet3.mediadetection.mediasearchtypes.XMLSearchType;
import synergynet3.museum.table.MuseumApp;
import synergynet3.museum.table.mainapp.ptmViewer.PTMSearchType;
import synergynet3.museum.table.mainapp.ptmViewer.PTMViewerItem;
import synergynet3.museum.table.mainapp.userrecorder.UserRecordingPromptLabel;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.utils.Entity;
import synergynet3.museum.table.utils.EntityType;
import synergynet3.museum.table.utils.LensUtils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class EntityManager {
	
	public static final String RECORDINGS = "Contributions";
	public static final String APPROVED = "Approved";
	public static final String ENTITIES = "Entities";
	
	private static final XMLSearchType XML_CHECK = new XMLSearchType();
	private static final VideoSearchType VIDEO_CHECK = new VideoSearchType();
	private static final AudioSearchType AUDIO_CHECK = new AudioSearchType();
	private static final WavefrontSearchType OBJ_CHECK = new WavefrontSearchType();
	private static final PTMSearchType PTM_CHECK = new PTMSearchType();
	
	public static HashMap<String, EntityItem> entities = new HashMap<String, EntityItem>();
	
	private float imageSizeLimit = 200;	
	
	private IStage stage;

	private POIManager mapManager;
	private MuseumApp app;
	
	private boolean recordingPromptsVisible = true;
	
	public EntityManager(IStage stage, float displayWidth, float displayHeight, Vector2f displayOffset, MuseumApp app){
		this.stage = stage;
		this.app = app;
		imageSizeLimit = stage.getDisplayWidth()/4;
		mapManager = new POIManager(stage, displayWidth, displayHeight, displayOffset, this);
	}
	
	public void loadAllContent(String contentFolderLocation) throws ContentTypeNotBoundException{
		File contentDir = new File(contentFolderLocation + File.separator + ENTITIES);	
		if (contentDir.isDirectory()){				
			for (File entityFolder: contentDir.listFiles()){
				if (entityFolder.isDirectory()){
					loadEntityContent(entityFolder);
				}
			}			
		}	
		
		for (LensVisiblePOI eventPOI : mapManager.getlensVisiblePOIs()){
			stage.getZOrderManager().bringToTop(eventPOI.getPoi());
			eventPOI.getPoi().getZOrderManager().setBringToTopPropagatesUp(false);
		}
		for (IImage poi : mapManager.getPOIs()){
			stage.getZOrderManager().bringToTop(poi);
			stage.getZOrderManager().ignoreItemClickedBehaviour(poi);
			poi.getZOrderManager().setBringToTopPropagatesUp(false);
		}
		
		mapManager.generateBackground();
		if (mapManager.getlensVisiblePOIs().size() > 0 && LensUtils.getLenses().length > 0){
			mapManager.generateLensButton();			
		}
	}
	
	private void loadEntityContent(File folder) throws ContentTypeNotBoundException{				
		ArrayList<IItem> items = loadMedia(folder, false);
		ArrayList<IItem> userGeneratedItems = new ArrayList<IItem>();
		File approvedContentFolder = new File(folder.getAbsolutePath() + File.separator + RECORDINGS + File.separator + APPROVED);
		if (approvedContentFolder.isDirectory()){
			userGeneratedItems.addAll(loadMedia(approvedContentFolder, true));
		}
		parseEntityXmlFile(folder, items, userGeneratedItems);
	}
	
	private ArrayList<IItem> loadMedia(File folder, boolean isUserContent) throws ContentTypeNotBoundException{		
		ArrayList<IItem> items = new ArrayList<IItem>();
		for (File file: folder.listFiles()){
			if(!XML_CHECK.isFileOfSearchType(file)){
				ColorRGBA borderCol = MuseumAppPreferences.getEntityBorderColour();
				if (isUserContent){
					borderCol = MuseumAppPreferences.getUserGeneratedContentColour();
				}
				borderCol.a = 1;
				IItem item = null;
				if (VIDEO_CHECK.isFileOfSearchType(file) || AUDIO_CHECK.isFileOfSearchType(file) || OBJ_CHECK.isFileOfSearchType(file)){
					item = AdditionalSynergyNetUtilities.generateItemFromFile(file, stage, -1, imageSizeLimit * 2, borderCol, 0.3f, 1f);
					//if(item != null)item.setRelativeScale(0.5f);	
				}else if (PTM_CHECK.isFileOfSearchType(file)){
					item = new PTMViewerItem(stage, file, borderCol, imageSizeLimit, 0.5f, 2.2f).asItem();
				}else{
					item = AdditionalSynergyNetUtilities.generateItemFromFile(file, stage, -1, imageSizeLimit, borderCol, 0.5f, 2f);
				}
				if (item != null){
					stage.removeItem(item);
					items.add(item);
					if (item instanceof IMediaPlayer){
						((IMediaPlayer)item).setBackgroundColour(borderCol);
					}
				}
			}
		}	
		return items;
	}
	
	private void parseEntityXmlFile(File folder, ArrayList<IItem> items, ArrayList<IItem> userGenerated) throws ContentTypeNotBoundException{
		
		Entity entity = new Entity(folder.getAbsolutePath());
		if (!entity.getName().equals("")){
		
			String name = entity.getName();
			
			for (String fact: entity.getFacts()){
				items.add(LabelGenerator.generateFact(fact, stage));
			}
			
			for (String link: entity.getLinked()){
				items.add(LabelGenerator.generateLink(link, name, stage, app));
			}			

			ArrayList<UserRecordingPromptLabel> userRecordingPromptLabels = new ArrayList<UserRecordingPromptLabel>();			
			
			if (MuseumAppPreferences.areUserRecordingsEnabled()){	
				int accept = -1;
				if(MuseumAppPreferences.isSinglePrompt()){
					accept = FastMath.nextRandomInt(0, MuseumAppPreferences.getPrompts().length-1);
				}
				for (int i = 0; i < MuseumAppPreferences.getPrompts().length; i++){
					if (accept == -1 || accept == i){
						UserRecordingPromptLabel userRecordingPromptLabel = new UserRecordingPromptLabel(stage, folder, this, MuseumAppPreferences.getPrompts()[i]);
						items.add(userRecordingPromptLabel.getTextItem());
						userRecordingPromptLabels.add(userRecordingPromptLabel);
					}
				}
			}		

			EntityItem entityItem = new EntityItem(stage, name, items, userGenerated, app);
			for (UserRecordingPromptLabel userRecordingPromptLabel : userRecordingPromptLabels){
				userRecordingPromptLabel.setEntityItem(entityItem.getCentralItem());
				entityItem.addUserRecordingPromptLabel(userRecordingPromptLabel);
			}
			
			entities.put(name, entityItem);	
			
			if (entity.getType() == EntityType.LensedPOI){		
				for (String lens: entity.getLensValues()){
					stage.addItem(mapManager.generateLensedPOI(entity.getX(), entity.getY(), lens, name));						
				}		
			}else if (entity.getType() == EntityType.POI){
				if (MuseumAppPreferences.areLocationsEnabled()){
					stage.addItem(mapManager.generatePOI(entity.getX(), entity.getY(), name));					
				}
			}
		}	
	}
	
	public void setUserRecordingPromptLabelsVisibility(boolean visibility){
		recordingPromptsVisible = visibility;
		for (EntityItem entityItem : entities.values()){
			entityItem.setUserRecordingPromptLabelVisibility(visibility);
		}	
	}

	public HashMap<String, EntityItem> getEntities() {
		return entities;
	}
	
	public boolean areRecordingPromptsVisible(){
		return recordingPromptsVisible;
	}

}

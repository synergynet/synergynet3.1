package synergynet3.museum.table;

import java.io.File;

import com.jme3.math.Vector2f;

import synergynet3.SynergyNetApp;
import synergynet3.museum.table.mainapp.EntityItem;
import synergynet3.museum.table.mainapp.EntityManager;
import synergynet3.museum.table.mainapp.EntityTidy;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.general.GeneralConfigPanel;
import synergynet3.museum.table.utils.ShutdownGUI;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.factory.ContentTypeNotBoundException;


public class MuseumApp extends SynergyNetApp{	
	
	private final static float BUTTON_WIDTH = 200f;	
	private final static float TEXT_SCALE = 0.7f;
	
	private float displayWidth;
	private float displayHeight;

	private EntityManager entityManager;
	
	protected void loadDefaultContent() {			
		
		displayWidth = stage.getDisplayWidth();
		displayHeight = stage.getDisplayHeight();
				
		Vector2f displayOffset = new Vector2f(stage.getDisplayWidth()/2, stage.getDisplayHeight()/2);
		
		try {
			new ShutdownGUI(stage, displayWidth, displayHeight, BUTTON_WIDTH, TEXT_SCALE, MuseumAppPreferences.getAdminPIN());			
			
			entityManager = new EntityManager(stage, displayWidth, displayHeight, displayOffset, this);
			EntityTidy.createThread(entityManager);
				
			File f = new File(MuseumAppPreferences.getContentFolder());
			if (f.isDirectory() && !f.getName().equals(GeneralConfigPanel.BACKGROUNDLOC)){
				entityManager.loadAllContent(MuseumAppPreferences.getContentFolder());
			}
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}		
	}	
	
	public void closeTree(String toClose){
		entityManager.getEntities().get(toClose).setVisible(false);
	}
	
	public void focusShift(String from, String to, Vector2f loc){
		closeTree(from);
		
		EntityItem entityTo = entityManager.getEntities().get(to);
		if (entityTo != null){			
			if (!entityTo.isVisible()){
				entityTo.regenerate(loc.x, loc.y);
				entityTo.setVisible(true);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		EntityTidy.stopTidying();
		super.onDestroy();
	};
	
	@Override
	public String getSpecificFriendlyAppName() {
		return "MuseumStart";
	}
	
	public EntityManager getEntityManager(){
		return entityManager;
	}
	
	public static void main(String[] args) {	
		SynergyNetApp.NETWORKING = false;
		SynergyNetApp.ONE_MEDIA_AT_A_TIME = true;
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		MuseumApp app = new MuseumApp();
		client.setCurrentApp(app);
	}
	
}
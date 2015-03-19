package synergynet3.museum.table.mainapp;

import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import multiplicity3.input.events.MultiTouchEvent;

public class EntityTidy {
	
	private static boolean keepTidying = true;
	
	private static long WAIT_TIME_SECONDS = (MuseumAppPreferences.getMaxRecordingTime()/1000) + 30;
	private static long lastTidy = 0;

	public static void createThread(final EntityManager entityManager) {
		Thread timeoutThread = new Thread(new Runnable() {	  
			public void run() {	   
				while (keepTidying){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (lastTidy == 0 || lastTidy != MultiTouchEvent.getLastEvent()){
						if (keepTidying && System.nanoTime() - MultiTouchEvent.getLastEvent() > WAIT_TIME_SECONDS * 1000000000){
							tidyUp(entityManager);
							lastTidy =  MultiTouchEvent.getLastEvent();
						}
					}
				}
			}
		});
		timeoutThread.start();			
	}
	
	public static void tidyUp(EntityManager entityManager){
		for (EntityItem entity : entityManager.getEntities().values()){
			entity.setVisible(false);
		}
	}

	public static void stopTidying() {
		keepTidying = false;		
	}

}

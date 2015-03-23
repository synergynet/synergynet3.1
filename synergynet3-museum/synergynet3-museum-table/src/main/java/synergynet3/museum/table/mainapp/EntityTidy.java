package synergynet3.museum.table.mainapp;

import multiplicity3.input.events.MultiTouchEvent;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

/**
 * The Class EntityTidy.
 */
public class EntityTidy {

	/** The keep tidying. */
	private static boolean keepTidying = true;

	/** The last tidy. */
	private static long lastTidy = 0;

	/** The wait time seconds. */
	private static long WAIT_TIME_SECONDS = (MuseumAppPreferences
			.getMaxRecordingTime() / 1000) + 30;

	/**
	 * Creates the thread.
	 *
	 * @param entityManager the entity manager
	 */
	public static void createThread(final EntityManager entityManager) {
		Thread timeoutThread = new Thread(new Runnable() {
			public void run() {
				while (keepTidying) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if ((lastTidy == 0)
							|| (lastTidy != MultiTouchEvent.getLastEvent())) {
						if (keepTidying
								&& ((System.nanoTime() - MultiTouchEvent
										.getLastEvent()) > (WAIT_TIME_SECONDS * 1000000000))) {
							tidyUp(entityManager);
							lastTidy = MultiTouchEvent.getLastEvent();
						}
					}
				}
			}
		});
		timeoutThread.start();
	}

	/**
	 * Stop tidying.
	 */
	public static void stopTidying() {
		keepTidying = false;
	}

	/**
	 * Tidy up.
	 *
	 * @param entityManager the entity manager
	 */
	public static void tidyUp(EntityManager entityManager) {
		for (EntityItem entity : entityManager.getEntities().values()) {
			entity.setVisible(false);
		}
	}

}

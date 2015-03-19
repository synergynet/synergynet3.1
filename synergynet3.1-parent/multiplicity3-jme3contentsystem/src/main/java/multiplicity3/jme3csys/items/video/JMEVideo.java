package multiplicity3.jme3csys.items.video;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.IUpdateable;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.video.IVideo;
import multiplicity3.csys.items.video.exceptions.CouldNotPlayVideoException;
import multiplicity3.jme3csys.annotations.RequiresUpdate;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.video.Clock;
import com.jme3.video.VFrame;
import com.jme3.video.VQueue;
import com.jme3.video.plugins.jheora.AVThread;

@ImplementsContentItem(target = IVideo.class)
@RequiresUpdate
public class JMEVideo extends JMEItem implements IVideo, IInitable, IUpdateable {
	private static final Logger log = Logger.getLogger(JMEVideo.class.getName());
	
	private CenteredQuad quad;
	private Geometry quadGeometry;
	private Material mat;
	private String resource;
	private boolean playing = false;
	private VQueue videoQueue;
	private AVThread decoder;
	private Thread videoThread;
	private Clock masterClock;
	private AudioNode source;
	private float waitTime = 0;
	private VFrame frameToDraw = null;
	private long lastFrameTime;
	private AudioRenderer audioRenderer;
	private InputStream fis;

	private Renderer renderer;

	public JMEVideo(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		quad = new CenteredQuad(100, 100, true);	
		quadGeometry = new Geometry("_quad_geom", quad);
		mat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
		TextureKey key = new TextureKey("Interface/Logo/Monkey.jpg", true);		
		Texture2D tex = (Texture2D) assetManager.loadTexture(key);
		mat.setColor("m_Color", ColorRGBA.White);
		mat.setTexture("m_Texture", tex);
		quadGeometry.setMaterial(mat);
		ItemMap.register(quadGeometry, this);
		attachChild(quadGeometry);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
	}

	@Override
	public void setSize(float width, float height) {
		quad = new CenteredQuad(width, height, true);
		quadGeometry.setMesh(quad);
	}

	@Override
	public void setResource(String resource) {
		this.resource = resource;		
	}

	@Override
	public void startPlaying() throws CouldNotPlayVideoException {
		if(playing ) return;
		
		log.fine("Playing " + resource);


		try {
			fis = new FileInputStream(resource);
			if(videoQueue == null) videoQueue = new VQueue(5);
			if(decoder == null) decoder = new AVThread(fis, videoQueue);
			videoThread = new Thread(decoder, "Jheora Video Decoder");
			videoThread.setDaemon(true);
			videoThread.start();
			if(masterClock == null) masterClock = decoder.getMasterClock();
			playing = true;
		} catch (FileNotFoundException e) {
			throw new CouldNotPlayVideoException();
		}
	}

	@Override
	public void stopPlaying() {		
		decoder.stop();
		if(source != null) {
			audioRenderer.stopSource(source);
		}
		try {
			fis.close();
		} catch (IOException e) {
			log.warning("Problems closing video stream.");
		}
	}


	private void drawFrame(VFrame frame){
		frame.setImage(frame.getImage());
		mat.setTexture("m_Texture", frame);
		renderer.setTexture(0, frame);
		videoQueue.returnFrame(frame);
		lastFrameTime = frame.getTime();
	}

	public void simpleUpdate(float tpf) {
		if(!isPlaying()) return;
		
		if (source == null){
			if (decoder.getAudioStream() != null){
				source = new AudioNode(decoder.getAudioStream(), null);
				source.setPositional(false);
				source.setReverbEnabled(false);
				audioRenderer.playSource(source);
			}else{
				return;
			}
		}

		if (waitTime > 0){
			waitTime -= tpf;
			if (waitTime > 0)
				return;
			else{
				waitTime = 0;
				drawFrame(frameToDraw);
				frameToDraw = null;
			}
		}else{
			VFrame frame;
			try {
				frame = videoQueue.take();
			} catch (InterruptedException ex){
				stopPlaying();
				return;
			}
			if (frame.getTime() < lastFrameTime){
				videoQueue.returnFrame(frame);
				return;
			}

			if (frame.getTime() == -2){
				System.out.println("End of stream");
				stopPlaying();
				return;
			}

			long AV_SYNC_THRESHOLD = 1 * Clock.MILLIS_TO_NANOS;

			long delay = frame.getTime() - lastFrameTime;
			long diff = frame.getTime() - masterClock.getTime();
			long syncThresh = delay > AV_SYNC_THRESHOLD ? delay : AV_SYNC_THRESHOLD;

			// if difference is more than 1 second, synchronize.
			if (Math.abs(diff) < Clock.SECONDS_TO_NANOS){
				if(diff <= -syncThresh) {
					delay = 0;
				} else if(diff >= syncThresh) {
					delay = 2 * delay;
				}
			}

			if (delay > 0){
				waitNanos(delay);
				drawFrame(frame);
			}else{
				videoQueue.returnFrame(frame);
				lastFrameTime = frame.getTime();
			}
		}
	}

	private void waitNanos(long time){
		long millis = (long) (time / Clock.MILLIS_TO_NANOS);
		int nanos   = (int) (time - (millis * Clock.MILLIS_TO_NANOS));

		try {
			Thread.sleep(millis, nanos);
		}catch (InterruptedException ex){
			//stop(); // would stop app
			return;
		}
	}

	@Override
	public void update(float tpf) {
		simpleUpdate(tpf);
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}
}

package synergynet3.additionalitems.jme;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.sun.jna.Memory;

import synergynet3.additionalitems.interfaces.IActionOnVideoEndListener;
import synergynet3.additionalitems.interfaces.ISimpleMediaPlayer;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

@ImplementsContentItem(target = ISimpleMediaPlayer.class)
public class SimpleMediaPlayer extends JMEItem implements ISimpleMediaPlayer, IInitable, RenderCallback, BufferFormatCallback {
	private static final Logger log = Logger.getLogger(SimpleMediaPlayer.class.getName());
	
	public static ArrayList<SimpleMediaPlayer> mediaPlayers = new ArrayList<SimpleMediaPlayer>();
	
	private IActionOnVideoEndListener actionOnVideoEndListener = null;
	
	public static final String CACHABLE_TYPE = "CACHABLE_VIDEO";
    private static final int WIDTH = 320;
    private static final int HEIGHT = 240;
	
    private static Format TEXTURE_FORMAT = Format.RGBA16;
	
    private static float vidWidth = 12;
    private static float vidHeight = 7;

    private CenteredQuad quad;
    private Geometry quadGeometry;
	private Material mat;
	
	private boolean autostart = false;
	
    private MediaPlayerFactory mediaPlayerFactory;
    public DirectMediaPlayer mediaPlayer;
	
    private Image videoImage;
    private Texture2D videoTexture;
	
	private boolean firstClick = false;
    
    private SimpleMediaPlayer instance;

	private String videoURL = null;
	
	private boolean repeat = false;
	private boolean atEnd = false;	
	
	private boolean hasStarted = false;
	
    static{    	
    	boolean found = false;    	
   		String vlcLib = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("vlc");
    	if (vlcLib != null)found = true;
        if(!found) found = new NativeDiscovery().discover();
        if(!found){
        	log.warning("Cannot play videos.  VLC is either not installed or located in an unexpected directory.  " + 
        			"If VLC is installed in an unexpected directory you can provide the path to its library " + 
        			"location with the argument: '-Dvlc=\"...\""); 
        }
    }    
    
	class VidThread extends Thread {
		
		private ArrayList<MediaPlayerEventAdapter> eventHandlersToAdd = new ArrayList<MediaPlayerEventAdapter>(); 
		
		private boolean playing = false;
		private boolean initiated = false;
		private float startPos = 0;
		public VidThread () {
			super();
		}
		
		public void start () {
			super.start();
		}
		
		public void run () {
			try{
				mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show", "--quiet");
				mediaPlayer = mediaPlayerFactory.newDirectMediaPlayer(instance,instance);
				addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
					@Override
					public void finished(MediaPlayer mediaPlayer) {
						atEnd = true;
						if (vidThread != null){	
							vidThread.atEnd();
							if (repeat){
								vidThread.restart();
							}
						}
					}
				});
				mediaPlayer.setPlaySubItems(true);
				mediaPlayer.mute(true);
				mediaPlayer.prepareMedia(videoURL, "");
				mediaPlayer.play();
				while(!mediaPlayer.isPlaying()){
					Thread.sleep(100);		
				}
				initiated = true;
				playing = true;
				mediaPlayer.setPosition(startPos);
				mediaPlayer.mute(false);
				if (!autostart){
					pauseVid();
					while (!firstClick){
						if (mediaPlayer.isPlaying()){
							Thread.sleep(100);	
							if (!firstClick){
								mediaPlayer.setPause(true);
								mediaPlayer.setPosition(startPos);
							}
						}
					}
				}
				for (MediaPlayerEventAdapter eventHandler : eventHandlersToAdd){
					mediaPlayer.addMediaPlayerEventListener(eventHandler);
				}
			}catch(RuntimeException e){
				log.log(Level.SEVERE, "Video won't play.  VLC may not be installed or the same architecture (32/64bit) as the java platform used).", e);
			} catch (InterruptedException e) {

			}
		}	
		
		public void addMediaPlayerEventListener(MediaPlayerEventAdapter mediaPlayerEventAdapter){
			if (initiated){
				mediaPlayer.addMediaPlayerEventListener(mediaPlayerEventAdapter);				
			}else{
				eventHandlersToAdd.add(mediaPlayerEventAdapter);
			}
		}
		
		public void playVid() {
			if (initiated){ 
				mediaPlayer.play();
				playing = true;
			}
		}	
		
		public void stopVid() {
			if (playing){ 
				mediaPlayer.stop();
				playing = false;
			}
		}		
		
		public void pauseVid() {
			if (playing){ 				
				mediaPlayer.setPause(true);
				playing = false;
			}
		}
		
		public void unpauseVid() {
			if (atEnd){
				restart();
			}else if (!playing){
				mediaPlayer.setPause(false);
				playing = true;
			}
		}
		
		public void atEnd(){
			playing = false;
			if (actionOnVideoEndListener != null){
				actionOnVideoEndListener.onVideoEnd();
			}
		}
		
		public boolean isPlaying(){
			return playing;
		}
		
		public float getPosition(){
			if (!initiated){
				return 0;
			}else{
				return mediaPlayer.getPosition();
			}
		}

		public void setPosition(float pos) {
			if (initiated){
				mediaPlayer.setPosition(pos);	
			}else{
				startPos = pos;
			}
		}
		
		public void restart(){
			mediaPlayer.prepareMedia(videoURL, "");
			mediaPlayer.play();
			playing = true;
			atEnd = false;
		}
		
		public void destroy(){
			initiated = false;
		}

	}
	
	private VidThread vidThread = null;	
	
	public SimpleMediaPlayer(String name, UUID uuid) {
		super(name, uuid);		
	}
	
	@Override
	public void initializeGeometry(AssetManager assetManager) {		
		this.instance = this;
		
		videoImage = new Image(TEXTURE_FORMAT, WIDTH, HEIGHT, null);
        videoTexture = new Texture2D(videoImage);        
		
        quad = new CenteredQuad(vidWidth, vidHeight);	
        quadGeometry = new Geometry("quad_geom", quad);
        mat = new Material(MultiplicityClient.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setTexture("ColorMap", videoTexture);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Front);
        quadGeometry.setMaterial(mat);
        
        Node transformNode = new Node();
        transformNode.attachChild(quadGeometry);     		
        
        transformNode.rotate(0f,0f,FastMath.DEG_TO_RAD * 180f);
        transformNode.rotate(0f,FastMath.DEG_TO_RAD * 180f,0f);
        
        this.setVisible(false);
		
		ItemMap.register(quadGeometry, this);
		log.fine("Attaching image quad geometry!");
		attachChild(transformNode);			
		hasStarted = true;
	}
	
	private void initialise(){		
		vidThread = new VidThread();
		vidThread.start();
    	mediaPlayers.add(this);	    	
	}

	public void setLocalResource(File file, boolean autostart){
		this.autostart = autostart;
		videoURL = file.toString();
	}
	
	public void setLocalResource(String localPath, boolean autostart){
		this.autostart = autostart;
		videoURL = localPath;
	}	
	
	public void setRemoteResource(String remotePath, boolean autostart){
		this.autostart = autostart;
		videoURL = remotePath;
	}
	
	public void setSize(float width, float height) {
		vidWidth = width;
		vidHeight = height;
		quad = new CenteredQuad(width, height);
		quadGeometry.setMesh(quad);
	}
	
	public float getWidth(){
		return vidWidth;
	}
	
	public float getHeight(){
		return vidHeight;
	}
	
	public void addMediaPlayerEventListener(MediaPlayerEventAdapter mediaPlayerEventAdapter){
		if (vidThread != null){
			vidThread.addMediaPlayerEventListener(mediaPlayerEventAdapter);
		}
	}
	
    public void play(){
    	if (vidThread != null){
    		vidThread.playVid();
    	}
    }
    
    public void stop(){   
    	if (vidThread != null){
    		vidThread.stopVid();
    	}
    }
    
    public void pause(){     
    	if (vidThread != null){
    		vidThread.pauseVid();
    	}
    }
    
    public void unpause(){  
    	if (vidThread != null){
	    	vidThread.unpauseVid();
	    	if (!firstClick){
	    		firstClick = true;
	    	}
    	}
    }
    
    public boolean isPlaying(){
    	if (vidThread != null){
    		return vidThread.isPlaying();
    	}else{
    		return false;
    	}
    }	
    
	public float getPosition(){
		if (vidThread != null){
			return vidThread.getPosition();
		}else{
			return 0;
		}
	}
	
	public void setPosition(float pos){
		if (vidThread != null){
			vidThread.setPosition(pos);
		}
	}
    
	public void destroy() {
		if (vidThread != null){
			vidThread.destroy();
			if (mediaPlayer != null){
				unpause();
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			if (mediaPlayerFactory != null){
				mediaPlayerFactory.release();
			}
			if (mediaPlayers.contains(this))mediaPlayers.remove(this);
			vidThread.interrupt();
	
			vidThread = null;
		}
	}	
		
	@Override
	public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
		ByteBuffer buffer = nativeBuffers[0].getByteBuffer(0, (int) bufferFormat.getWidth() * (int) bufferFormat.getHeight() * 4);
		videoImage.setData(buffer);
		videoTexture.setImage(videoImage);		
	}

	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
	}

	@Override
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;		
	}

	@Override
	public boolean getRepeat() {
		return repeat;
	}
	
    @Override
    public void setVisible(boolean isVisible) {
    	if (!hasStarted){
            super.setVisible(isVisible);
            if(!isVisible){
                    if (this.getParentItem() != null){
                            this.setInteractionEnabled(false);
                    }
            }
    	}else{
    		if(!isVisible){
    			super.setVisible(isVisible);
    			destroy();
    		}else{
    			initialise();
    			super.setVisible(isVisible);
    		}
    	}
    }
    
	@Override
	public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
	    return new RV32BufferFormat(WIDTH, HEIGHT);
	}

	@Override
	public void setActionOnVideoEndListener(IActionOnVideoEndListener actionOnVideoEndListener) {
		this.actionOnVideoEndListener = actionOnVideoEndListener;		
	}
}

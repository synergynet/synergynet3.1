package synergynet3.screenshotcontrol;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.Application;
import com.jme3.math.Vector2f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.util.Screenshots;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;

import synergynet3.SynergyNetApp;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.config.web.CacheOrganisation;

public class SNScreenshotAppState extends AbstractAppState implements SceneProcessor {
	
	private boolean capture = false;
	private Renderer renderer;
	private ByteBuffer outBuf;
	private BufferedImage awtImage;
	private IScreenShotter screenShotter;
	private Vector2f loc = new Vector2f();
	private float rot = 0f;
	
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		List<ViewPort> vps = app.getRenderManager().getPostViews();
		ViewPort last = vps.get(vps.size()-1);
		last.addProcessor(this);
    }
    
    public void takeScreenShot(IScreenShotter screenShotter, Vector2f loc, float rot){
    	this.loc = loc;
    	this.rot = rot;
    	this.screenShotter = screenShotter;
	    capture = true;
    }

    public Image getLastScreenShot(){
    	if (awtImage==null){
    		return null;
    	}
    	return awtImage.getScaledInstance(awtImage.getWidth(),awtImage.getHeight(),Image.SCALE_FAST);
    }
    
    public void initialize(RenderManager rm, ViewPort vp) {
        renderer = rm.getRenderer();
        reshape(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
    }
    
    @Override
    public boolean isInitialized() {
        return super.isInitialized() && renderer != null;
    }
    
    public void reshape(ViewPort vp, int w, int h) {
        outBuf = BufferUtils.createByteBuffer(w*h*4);
        awtImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    public void preFrame(float tpf) {}
    public void postQueue(RenderQueue rq) {}
    public void postFrame(FrameBuffer out){
    	if (capture){
    		capture = false;
    		renderer.readFrameBuffer(out, outBuf);
    		Screenshots.convertScreenShot(outBuf, awtImage);
			try{
				SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
				String screenshotID = SynergyNetApp.getTableIdentity() + "_" + yearFormat.format(new Date()); 				
				File file = new File(CacheOrganisation.getScreenshotDir() + File.separator + screenshotID + ".png");
				file.createNewFile();
				ImageIO.write(awtImage, "png", file);
				screenShotter.utiliseScreenshot(file, loc, rot);				
			} catch (IOException ex){ 
				AdditionalSynergyNetUtilities.log(Level.SEVERE, "Error while saving screenshot", ex); 
			} 
			loc = new Vector2f();
			rot = 0;
    	}
    }
 
}
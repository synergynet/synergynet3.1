package multiplicity3.jme3csys.items.keyboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.IKeyboardGraphicsRenderer;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

@ImplementsContentItem(target = IKeyboard.class)
public class JMEKeyboard extends JMEItem implements IKeyboard, IInitable {

	private AssetManager assetManager;
	private CenteredQuad quad;
	private Geometry quadGeometry;
	private Material mat;
	private BufferedImage bufferedImage;
	private IKeyboardGraphicsRenderer renderer;
	private KeyboardDefinition keybDefinition;
	private Graphics2D gfx;
	private Texture2D t;
	
	public JMEKeyboard(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
		quad = new CenteredQuad(100, 100);	
		bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);		
		gfx = (Graphics2D) bufferedImage.getGraphics();
		gfx.setColor(Color.white);
		gfx.fillRect(0, 0, 100, 100);
		quadGeometry = new Geometry("_quad_geom", quad);
		
		// reminder of where to find j3md stuff: jme3/src/core-data
		mat = new Material(this.assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//Texture tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
		Image img = new AWTLoader().load(bufferedImage, true);
		t = new Texture2D(img);
		mat.setTexture("m_ColorMap", t);
		
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
		quad = new CenteredQuad(width, height);
		quadGeometry.setMesh(quad);
	}

	@Override
	public void reDrawKeyboard(boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		if(this.renderer != null) {
			if(gfx != null) {
				renderer.drawKeyboard(gfx, shiftDown, altDown, ctlDown);
				Image img2 = new AWTLoader().load(bufferedImage, true);
				mat.setTexture("m_ColorMap", new Texture2D(img2));				
			}
		}
	}
	

	@Override
	public void setKeyboardDefinition(KeyboardDefinition kd) {
		this.keybDefinition = kd;
		int width = (int)this.keybDefinition.getBounds().getWidth();
		int height = (int)this.keybDefinition.getBounds().getHeight();
		setSize(width, height);
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		gfx = (Graphics2D) bufferedImage.getGraphics();
		reDrawKeyboard(false, false, false);		
		this.updateGeometricState();
	}

	@Override
	public void setKeyboardRenderer(IKeyboardGraphicsRenderer keyboardRenderer) {
		this.renderer = keyboardRenderer;
		reDrawKeyboard(false, false, false);
	}

	@Override
	public void setSize(Vector2f size) {
		setSize(size.x, size.y);
	}

	@Override
	public Vector2f getSize() {
		return new Vector2f(quad.getWidth(), quad.getHeight());
	}

	@Override
	public float getWidth() {
		return quad.getWidth();
	}

	@Override
	public float getHeight() {
		return quad.getHeight();
	}

	@Override
	public KeyboardDefinition getKeyboardDefinition() {
		return this.keybDefinition;
	}

	@Override
	public void reDraw() {
		this.reDrawKeyboard(false, false, false);
	}
}

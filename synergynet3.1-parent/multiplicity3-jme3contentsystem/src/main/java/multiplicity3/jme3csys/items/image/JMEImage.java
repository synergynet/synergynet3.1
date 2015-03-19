package multiplicity3.jme3csys.items.image;


import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

@ImplementsContentItem(target = IImage.class)
public class JMEImage extends JMEItem implements IImage, IInitable {
	private static final Logger log = Logger.getLogger(JMEImage.class.getName());
	
	private CenteredQuad quad;
	private Geometry quadGeometry;
	private Material mat;
	private AssetManager assetManager;
	private boolean wrap = false;

	private String imageResource;
	
	public JMEImage(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
		quad = new CenteredQuad(100, 100);	
		quadGeometry = new Geometry("quad_geom", quad);
		
		// reminder of where to find j3md stuff: jme3/src/core-data
		//mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		mat = new Material(assetManager, "multiplicity3/jme3csys/resources/shaders/Textured.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		setImage("multiplicity3/jme3csys/resources/placeholders/transparent_16.png");
//		Texture tex = assetManager.loadTexture();
//		mat.setTexture("m_ColorMap", tex);
		quadGeometry.setMaterial(mat);
		
		ItemMap.register(quadGeometry, this);
		log.fine("Attaching image quad geometry!");
		attachChild(quadGeometry);
	}
	
	@Override
	public void setSize(Vector2f size) {
		quad = new CenteredQuad(size.x, size.y);
		quadGeometry.setMesh(quad);
	}

	@Override
	public void setImage(String imageResource) {
		this.imageResource = imageResource;
		Texture tex = assetManager.loadTexture(imageResource);
		setTexture(tex);
	}
	
	@Override
	public void setTexture(Texture tex) {
		if(wrap) {
			tex.setWrap(WrapMode.Repeat);
		}else{
			tex.setWrap(WrapMode.EdgeClamp);
		}
		mat.setTexture("m_ColorMap", tex);			
	}
	
	@Override
	public void setWrapping(float xscale, float yscale) {
		this.wrap = true;
		quad.scaleTextureCoordinates(new Vector2f(xscale, yscale));
		setImage(this.imageResource); // force a refresh
	}
	
	@Override
	public void setImage(File imageFile) {
		File parent = imageFile.getParentFile();
		assetManager.registerLocator(parent.getAbsolutePath(),
				FileLocator.class);
		setImage(imageFile.getName());
	}

	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
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
	public void setSize(float width, float height) {
		setSize(new Vector2f(width, height));
	}

	@Override
	public String getImage() {
		return imageResource;
	}

}

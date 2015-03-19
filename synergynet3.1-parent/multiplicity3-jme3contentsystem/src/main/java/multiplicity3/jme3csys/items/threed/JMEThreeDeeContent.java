package multiplicity3.jme3csys.items.threed;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.threed.IThreeDeeContent;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

@ImplementsContentItem(target = IThreeDeeContent.class)
public class JMEThreeDeeContent extends JMEItem implements IThreeDeeContent, IInitable {
	private static final Logger log = Logger.getLogger(JMEThreeDeeContent.class.getName());

	private Geometry geometry;
	private Material material; 

	private AssetManager assetManager;
	private String modelResource = "";
	private String textureResource = "";
	
	private Vector3f extent = new Vector3f();

	public JMEThreeDeeContent(String name, UUID uuid) {
		super(name, uuid);	
	}

	@Override
	public Spatial getManipulableSpatial() {
		return geometry;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {		
		this.assetManager = assetManager;
		
		CenteredQuad quad = new CenteredQuad(100, 100);	
		geometry = new Geometry("quad_geom", quad);
		
		material = new Material(assetManager, "multiplicity3/jme3csys/resources/shaders/Textured.j3md");
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		setTexture("multiplicity3/jme3csys/resources/placeholders/transparent_16.png");
		
		geometry.setMaterial(material);
		ItemMap.register(geometry, this);
		log.fine("Attaching spatial geometry!");
		attachChild(geometry);
	}
	
	@Override
	public void setModel(File modelFile) {
		File parent = modelFile.getParentFile();
		assetManager.registerLocator(parent.getAbsolutePath(), FileLocator.class);
		setModel(modelFile.getName());
	}
	
	@Override
	public void setModel(String modelResource) {
		this.modelResource = modelResource;
		Spatial s = assetManager.loadModel(modelResource);
		((BoundingBox)s.getWorldBound()).getExtent(extent);
		geometry.setMesh(((Geometry)s).getMesh());	
	}
	
	@Override
	public String getModel() {
		return modelResource;
	}
	
	@Override
	public void setTexture(File textureFile) {
		File parent = textureFile.getParentFile();
		assetManager.registerLocator(parent.getAbsolutePath(), FileLocator.class);
		setTexture(textureFile.getName());
	}
	
	@Override
	public void setTexture(String textureResource) {
		this.textureResource = textureResource;
		Texture tex = assetManager.loadTexture(textureResource);
		tex.setWrap(WrapMode.EdgeClamp);  //or edgeclamp?
		material.setTexture("m_ColorMap", tex);	
	}
	
	@Override
	public String getTexture() {
		return textureResource;
	}
	
	@Override
	public void setSize(float width, float height, float depth) {
		((BoundingBox)geometry.getWorldBound()).setXExtent(width);
		((BoundingBox)geometry.getWorldBound()).setYExtent(height);
		((BoundingBox)geometry.getWorldBound()).setZExtent(depth);		
	}

	@Override
	public Vector3f getSize() {
		return extent;		
	}

}

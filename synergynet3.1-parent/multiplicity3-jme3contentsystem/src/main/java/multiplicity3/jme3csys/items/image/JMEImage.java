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

/**
 * The Class JMEImage.
 */
@ImplementsContentItem(target = IImage.class)
public class JMEImage extends JMEItem implements IImage, IInitable
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEImage.class.getName());

	/** The asset manager. */
	private AssetManager assetManager;

	/** The image resource. */
	private String imageResource;

	/** The mat. */
	private Material mat;

	/** The quad. */
	private CenteredQuad quad;

	/** The quad geometry. */
	private Geometry quadGeometry;

	/** The wrap. */
	private boolean wrap = false;

	/**
	 * Instantiates a new JME image.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public JMEImage(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#getHeight()
	 */
	@Override
	public float getHeight()
	{
		return quad.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.image.IImage#getImage()
	 */
	@Override
	public String getImage()
	{
		return imageResource;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial()
	{
		return quadGeometry;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#getSize()
	 */
	@Override
	public Vector2f getSize()
	{
		return new Vector2f(quad.getWidth(), quad.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#getWidth()
	 */
	@Override
	public float getWidth()
	{
		return quad.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.IInitable#initializeGeometry(com.jme3.asset
	 * .AssetManager)
	 */
	@Override
	public void initializeGeometry(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		quad = new CenteredQuad(100, 100);
		quadGeometry = new Geometry("quad_geom", quad);

		// reminder of where to find j3md stuff: jme3/src/core-data
		// mat = new Material(assetManager,
		// "Common/MatDefs/Misc/SimpleTextured.j3md");
		mat = new Material(assetManager, "multiplicity3/jme3csys/resources/shaders/Textured.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		setImage("multiplicity3/jme3csys/resources/placeholders/transparent_16.png");
		// Texture tex = assetManager.loadTexture();
		// mat.setTexture("m_ColorMap", tex);
		quadGeometry.setMaterial(mat);

		ItemMap.register(quadGeometry, this);
		log.fine("Attaching image quad geometry!");
		attachChild(quadGeometry);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.image.IImage#setImage(java.io.File)
	 */
	@Override
	public void setImage(File imageFile)
	{
		File parent = imageFile.getParentFile();
		assetManager.registerLocator(parent.getAbsolutePath(), FileLocator.class);
		setImage(imageFile.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.image.IImage#setImage(java.lang.String)
	 */
	@Override
	public void setImage(String imageResource)
	{
		this.imageResource = imageResource;
		Texture tex = assetManager.loadTexture(imageResource);
		setTexture(tex);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.image.IImage#setSize(float, float)
	 */
	@Override
	public void setSize(float width, float height)
	{
		setSize(new Vector2f(width, height));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.shapes.IRectangularItem#setSize(com.jme3.math
	 * .Vector2f)
	 */
	@Override
	public void setSize(Vector2f size)
	{
		quad = new CenteredQuad(size.x, size.y);
		quadGeometry.setMesh(quad);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.image.IImage#setTexture(com.jme3.texture.Texture
	 * )
	 */
	@Override
	public void setTexture(Texture tex)
	{
		if (wrap)
		{
			tex.setWrap(WrapMode.Repeat);
		}
		else
		{
			tex.setWrap(WrapMode.EdgeClamp);
		}
		mat.setTexture("m_ColorMap", tex);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.image.IImage#setWrapping(float, float)
	 */
	@Override
	public void setWrapping(float xscale, float yscale)
	{
		this.wrap = true;
		quad.scaleTextureCoordinates(new Vector2f(xscale, yscale));
		setImage(this.imageResource); // force a refresh
	}

}

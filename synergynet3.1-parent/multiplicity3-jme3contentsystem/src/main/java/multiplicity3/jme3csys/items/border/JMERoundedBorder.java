package multiplicity3.jme3csys.items.border;

import java.nio.FloatBuffer;
import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;

/**
 * The Class JMERoundedBorder.
 */
@ImplementsContentItem(target = IRoundedBorder.class)
public class JMERoundedBorder extends JMEItem implements IRoundedBorder, IInitable
{

	/** The border geometry. */
	private Geometry borderGeometry;

	/** The border mesh. */
	private BorderMesh borderMesh;

	/** The colour. */
	private ColorRGBA colour = new ColorRGBA(1, 1, 1, 0.5f);

	/** The mat. */
	private Material mat;

	/** The border size. */
	float borderSize = 20;

	/** The inner height. */
	float innerHeight = 150;

	/** The inner width. */
	float innerWidth = 300;

	/**
	 * Instantiates a new JME rounded border.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public JMERoundedBorder(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.border.IRoundedBorder#getBorderWidth()
	 */
	@Override
	public float getBorderWidth()
	{
		return this.borderSize;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial()
	{
		return borderGeometry;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.border.IRoundedBorder#getSize()
	 */
	@Override
	public Vector2f getSize()
	{
		return new Vector2f(this.innerWidth, this.innerHeight);
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
		updateVertices();

		mat = new Material(assetManager, "Common/MatDefs/Misc/VertexColor.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		borderGeometry = new Geometry("border", borderMesh);

		borderGeometry.setMaterial(mat);

		ItemMap.register(borderGeometry, this);

		attachChild(borderGeometry);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.border.IRoundedBorder#setBorderWidth(float)
	 */
	@Override
	public void setBorderWidth(float borderSize)
	{
		this.borderSize = borderSize;
		updateVertices();
		borderGeometry.setMesh(borderMesh);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.border.IRoundedBorder#setColor(com.jme3.math
	 * .ColorRGBA)
	 */
	@Override
	public void setColor(ColorRGBA color)
	{
		this.colour = color;
		FloatBuffer colourBuffer = (FloatBuffer) borderMesh.getBuffer(Type.Color).getData();
		colourBuffer.rewind();
		for (int i = 0; i < borderMesh.getVertexCount(); i++)
		{
			colourBuffer.put(color.r).put(color.g).put(color.b).put(color.a);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.border.IRoundedBorder#setSize(float, float)
	 */
	@Override
	public void setSize(float width, float height)
	{
		this.innerWidth = width;
		this.innerHeight = height;
		updateVertices();
		borderGeometry.setMesh(borderMesh);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.border.IRoundedBorder#setSize(com.jme3.math.
	 * Vector2f)
	 */
	@Override
	public void setSize(Vector2f size)
	{
		setSize(size.x, size.y);
	}

	/**
	 * Update vertices.
	 */
	private void updateVertices()
	{
		float radius = borderSize;
		float outerWidth = innerWidth + (2 * radius);
		float outerHeight = innerHeight + (2 * radius);
		int cornerDivisions = 8;
		borderMesh = new BorderMesh(innerWidth, innerHeight, outerWidth, outerHeight, radius, cornerDivisions);

		int vertexCount = borderMesh.getVertexCount();
		float[] vertexColours = new float[vertexCount * 4];

		for (int i = 0; i < vertexColours.length; i += 4)
		{
			vertexColours[i] = this.colour.r;
			vertexColours[i + 1] = this.colour.g;
			vertexColours[i + 2] = this.colour.b;
			vertexColours[i + 3] = this.colour.a;
		}
		borderMesh.setBuffer(Type.Color, 4, vertexColours);
	}
}

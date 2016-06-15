package multiplicity3.jme3csys.items.container;

import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 * The Class JMEContainer.
 */
@ImplementsContentItem(target = IContainer.class)
public class JMEContainer extends JMEItem implements IContainer, IInitable
{

	/**
	 * Instantiates a new JME container.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public JMEContainer(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial()
	{
		return null;
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
	}

}

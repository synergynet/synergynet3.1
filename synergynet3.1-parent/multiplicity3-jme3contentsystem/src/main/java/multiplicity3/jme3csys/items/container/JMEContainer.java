package multiplicity3.jme3csys.items.container;

import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

@ImplementsContentItem(target = IContainer.class)
public class JMEContainer extends JMEItem implements IContainer, IInitable {
	
	public JMEContainer(String name, UUID uuid) {
		super(name, uuid);		
	}

	@Override
	public Spatial getManipulableSpatial() {
		return null;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {}

}

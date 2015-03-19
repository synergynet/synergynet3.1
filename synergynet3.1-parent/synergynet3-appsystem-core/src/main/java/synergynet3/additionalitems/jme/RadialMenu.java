package synergynet3.additionalitems.jme;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.additionalitems.RadialMenuOption;
import synergynet3.additionalitems.interfaces.IRadialMenu;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;

@ImplementsContentItem(target = IRadialMenu.class)
public class RadialMenu extends JMEContainer implements IRadialMenu, IInitable {
	
	ArrayList<RadialMenuOption> options = new ArrayList<RadialMenuOption>();
	ArrayList<IItem> lines = new ArrayList<IItem>();
	
	private IStage stage;
	private Logger log = Logger.getLogger(RadialMenu.class.getName());
	private boolean optionsVisible = true;
	private float radius = 200;
	private ColorRGBA menuColour;
	
	public RadialMenu(String name, UUID uuid) {
		super(name, uuid);		
	}

	public void setRootItem(IItem rootItem, IStage stage, Logger log, ColorRGBA menuColour){		
		this.stage = stage;
		this.menuColour = menuColour; 

		rootItem.setRelativeLocation(new Vector2f());
		rootItem.setRelativeRotation(0);
		rootItem.setRelativeScale(1);
		
		addItem(rootItem);			
		switchBehaviour(rootItem);	
	}
	
	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		setOptionVisibility(false);
	}
	
	private void switchBehaviour(final IItem item){
		for (IBehaviour behaviour: item.getBehaviours()){
				behaviour.setItemActingOn(this);
		}
		for (IItem child: item.getChildItems()){
			switchBehaviour(child);
		}
		
		final RadialMenu menu = this;
		
		item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {				
				stage.getZOrderManager().bringToTop(menu);
			}
		});		
	}
	
	public void toggleOptionVisibility(){
		setOptionVisibility(!optionsVisible);
	}
	
	public void setOptionVisibility(boolean visibility){
		for (IItem line: lines){
			line.setVisible(visibility);
		}
		
		for (RadialMenuOption option : options){
			option.asItem().setVisible(visibility);
			option.asItem().setInteractionEnabled(visibility);
		}
		optionsVisible = visibility;
	}
	
	public int addOption(RadialMenuOption option) {
		options.add(option);
		updateMenu();
		return options.indexOf(option);
	}
	
	public void removeOption(int optionIndex) {
		options.remove(optionIndex);
		updateMenu();
	}
	
	private void updateMenu(){
		
		Vector2f containerLocation = getRelativeLocation();
		float containerRotation = getRelativeRotation();
		float containerScale = getRelativeScale();
		
		setRelativeLocation(new Vector2f());
		setRelativeRotation(0);
		setRelativeScale(1);
			
		for (IItem line: lines){
			stage.removeItem(line);
		}
		for (RadialMenuOption option: options){
			if (option.asItem().getParentItem() != null){
				if (option.asItem().getParentItem().equals(this)){
					this.removeItem(option.asItem());
				}
			}
		}
		
		lines.clear();
		int angleStep = 360/options.size();
		int angleTotal = 0;
		for (RadialMenuOption option: options){
			Vector2f location = getOptionPosition(angleTotal);
			float rotation = getOptionRotation(angleTotal);
			option.asItem().setRelativeLocation(location);
			option.asItem().setRelativeRotation(rotation);
			
			try {
				ILine line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
				line.setLineWidth(6f);
				line.setStartPosition(new Vector2f());
				line.setEndPosition(option.asItem().getRelativeLocation());
				line.setInteractionEnabled(false);
				line.getZOrderManager().setAutoBringToTop(false);
				line.setLineColour(menuColour);
				addItem(line);				
				lines.add(line);	
			
				addItem(option.asItem());
				
				if (!optionsVisible){					
					line.setVisible(false);
					option.asItem().setVisible(false);
					option.asItem().setInteractionEnabled(false);
				}
				
			} catch (ContentTypeNotBoundException e) {
				log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
			}		
			
			angleTotal += angleStep;
		}
		
		setRelativeLocation(containerLocation);
		setRelativeRotation(containerRotation);
		setRelativeScale(containerScale);
		
	}

	private float getOptionRotation(int angleTotal) {
		return FastMath.DEG_TO_RAD * angleTotal;
	}

	private Vector2f getOptionPosition(int angle) {		
		Vector2f position = new Vector2f(0, radius);
		position.rotateAroundOrigin(FastMath.DEG_TO_RAD * angle, true);
		return position;
	}

	@Override
	public void setRadius(int radius) {
		this.radius = radius;
	}

}

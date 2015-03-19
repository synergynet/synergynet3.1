package synergynet3.activitypack1.table.fishes;

import java.awt.Color;
import java.util.UUID;


import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.events.ItemListenerAdapter;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class LittleFishes implements IMultiplicityApp, IMultiTouchEventListener {
	
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		LittleFishes app = new LittleFishes();
		client.setCurrentApp(app);
	}

	private FlockingSimulator flockingSimulator;
	private IStage stage;
	protected Mode mode = Mode.BlueBirds;


	@Override
	public String getFriendlyAppName() {
		return "LittleFishes";
	}

	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		
		IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		this.stage = stage;
		IContentFactory contentFactory = stage.getContentFactory();
		stage.getZOrderManager().setAutoBringToTop(false);
		
		
		IContainer birdsContainer;
		try {
			IImage bg = contentFactory.create(IImage.class, "bg", UUID.randomUUID());
			bg.setSize(1024,768);
			stage.addItem(bg);
			bg.setImage("synergynet3/activitypack1/table/fishes/grassbg.png");			
			
			birdsContainer = contentFactory.create(IContainer.class, "birdscontainer", UUID.randomUUID());
			stage.addItem(birdsContainer);
			birdsContainer.setRelativeLocation(new Vector2f(-512, -768/2));
			BirdCollection birds = new BirdCollection(birdsContainer);
			flockingSimulator = new FlockingSimulator(birds);
			flockingSimulator.initWithDefaults();
			stage.getAnimationSystem().add(flockingSimulator);
			
			addMenu(contentFactory, stage);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}

		input.registerMultiTouchEventListener(this);
	}

	private void addMenu(IContentFactory contentFactory, IStage stage) throws ContentTypeNotBoundException {
		
		int xindent = 320;
		
		IColourRectangle predButton = contentFactory.create(IColourRectangle.class, "predbutton", UUID.randomUUID());
		stage.addItem(predButton);
		predButton.setSize(60,60);
		predButton.setGradientBackground(new Gradient(ColorRGBA.Red, ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		predButton.setWorldLocation(new Vector2f(xindent, 730));
		predButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				mode = Mode.AddPredators;
			}
		});
		
		IColourRectangle blueButton = contentFactory.create(IColourRectangle.class, "bluebutton", UUID.randomUUID());
		stage.addItem(blueButton);
		blueButton.setSize(60,60);
		blueButton.setGradientBackground(new Gradient(ColorRGBA.Blue, ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		blueButton.setWorldLocation(new Vector2f(xindent+70, 730));
		blueButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				mode = Mode.BlueBirds;
			}
		});
		
		IColourRectangle greenButton = contentFactory.create(IColourRectangle.class, "greenbutton", UUID.randomUUID());
		stage.addItem(greenButton);
		greenButton.setSize(60,60);
		greenButton.setGradientBackground(new Gradient(ColorRGBA.Green, ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		greenButton.setWorldLocation(new Vector2f(xindent + 140, 730));
		greenButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				mode = Mode.GreenBirds;
			}
		});
		
		IColourRectangle yellowButton = contentFactory.create(IColourRectangle.class, "yellowButton", UUID.randomUUID());
		stage.addItem(yellowButton);
		yellowButton.setSize(60,60);
		yellowButton.setGradientBackground(new Gradient(ColorRGBA.Yellow, ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		yellowButton.setWorldLocation(new Vector2f(xindent + 210, 730));
		yellowButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				mode = Mode.YellowBirds;
			}
		});
		
		IColourRectangle magentaButton = contentFactory.create(IColourRectangle.class, "greenbutton", UUID.randomUUID());
		stage.addItem(magentaButton);
		magentaButton.setSize(60,60);
		magentaButton.setGradientBackground(new Gradient(ColorRGBA.Magenta, ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		magentaButton.setWorldLocation(new Vector2f(xindent + 280, 730));
		magentaButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				mode = Mode.Food;
			}
		});
		
		IColourRectangle greyButton = contentFactory.create(IColourRectangle.class, "greenbutton", UUID.randomUUID());
		stage.addItem(greyButton);
		greyButton.setSize(60,60);
		greyButton.setGradientBackground(new Gradient(ColorRGBA.LightGray, ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		greyButton.setWorldLocation(new Vector2f(xindent + 350, 730));
		greyButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				mode = Mode.Obstacle;
			}
		});
	}
	
	private static enum Mode {
		AddPredators,
		GreenBirds,
		BlueBirds,
		YellowBirds,
		Food, 
		Obstacle
	}

	@Override
	public void shouldStop() {}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		
		if(stage.tableToWorld(event.getPosition()).getY() > 700) return;
		
		switch(mode) {
		case AddPredators: {
			flockingSimulator.addPredatorsBirdsAt(stage.tableToWorld(event.getPosition()));
			break;
		}
		case GreenBirds: {
			flockingSimulator.addBirdsAt(Color.green, stage.tableToWorld(event.getPosition()));
			break;
		}
		case BlueBirds: {
			flockingSimulator.addBirdsAt(Color.blue, stage.tableToWorld(event.getPosition()));
			break;
		}
		case YellowBirds: {
			flockingSimulator.addBirdsAt(Color.yellow, stage.tableToWorld(event.getPosition()));
			break;
		}				
		case Food: {
			flockingSimulator.addFoodAt(stage.tableToWorld(event.getPosition()));
			break;
		}
		case Obstacle: {
			flockingSimulator.addObstacleAt(stage.tableToWorld(event.getPosition()));
			break;
		}
		}
		
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void onDestroy() {}

}

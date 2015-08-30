package synergynet3.activitypack1.table.fishes;

import java.awt.Color;
import java.util.UUID;

import synergynet3.activitypack1.table.fishes.birdmodel.Map;
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

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class LittleFishes.
 */
public class LittleFishes implements IMultiplicityApp, IMultiTouchEventListener {

	/**
	 * The Enum Mode.
	 */
	private static enum Mode {

		/** The Add predators. */
		AddPredators,

		/** The Blue birds. */
		BlueBirds,

		/** The Food. */
		Food,

		/** The Green birds. */
		GreenBirds,

		/** The Obstacle. */
		Obstacle,

		/** The Yellow birds. */
		YellowBirds
	}

	/** The flocking simulator. */
	private FlockingSimulator flockingSimulator;

	/** The stage. */
	private IStage stage;

	/** The mode. */
	protected Mode mode = Mode.BlueBirds;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		LittleFishes app = new LittleFishes();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorClicked(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorPressed(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorReleased(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		
		

		if (stage.tableToWorld(event.getPosition()).getY() > stage.getDisplayHeight() - 70 && stage.tableToWorld(event.getPosition()).getX() < (70 * 7) ) {
			return;
		}

		switch (mode) {
			case AddPredators: {
				flockingSimulator.addPredatorsBirdsAt(stage.tableToWorld(event
						.getPosition()));
				break;
			}
			case GreenBirds: {
				flockingSimulator.addBirdsAt(Color.green,
						stage.tableToWorld(event.getPosition()));
				break;
			}
			case BlueBirds: {
				flockingSimulator.addBirdsAt(Color.blue,
						stage.tableToWorld(event.getPosition()));
				break;
			}
			case YellowBirds: {
				flockingSimulator.addBirdsAt(Color.yellow,
						stage.tableToWorld(event.getPosition()));
				break;
			}
			case Food: {
				flockingSimulator.addFoodAt(stage.tableToWorld(event
						.getPosition()));
				break;
			}
			case Obstacle: {
				flockingSimulator.addObstacleAt(stage.tableToWorld(event
						.getPosition()));
				break;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName() {
		return "LittleFishes";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectAdded(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectChanged(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectRemoved(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#onDestroy()
	 */
	@Override
	public void onDestroy() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.appsystem.IMultiplicityApp#shouldStart(multiplicity3.input
	 * .MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {

		IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		this.stage = stage;
		
		Map.setMapDimensions(stage.getDisplayWidth(), stage.getDisplayHeight());
		
		IContentFactory contentFactory = stage.getContentFactory();
		stage.getZOrderManager().setAutoBringToTop(false);

		IContainer birdsContainer;
		try {
			IImage bg = contentFactory.create(IImage.class, "bg",
					UUID.randomUUID());
			bg.setSize(stage.getDisplayWidth(), stage.getDisplayHeight());
			stage.addItem(bg);
			bg.setImage("synergynet3/activitypack1/table/fishes/grassbg.png");

			birdsContainer = contentFactory.create(IContainer.class,
					"birdscontainer", UUID.randomUUID());
			stage.addItem(birdsContainer);
			birdsContainer.setRelativeLocation(new Vector2f(-(stage.getDisplayWidth()/2), -(stage.getDisplayHeight()/2)));
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

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#shouldStop()
	 */
	@Override
	public void shouldStop() {
	}

	/**
	 * Adds the menu.
	 *
	 * @param contentFactory the content factory
	 * @param stage the stage
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void addMenu(IContentFactory contentFactory, IStage stage)
			throws ContentTypeNotBoundException {

		int xindent = 70;
		int yindent = stage.getDisplayHeight() - 70;

		IColourRectangle predButton = contentFactory.create(
				IColourRectangle.class, "predbutton", UUID.randomUUID());
		stage.addItem(predButton);
		predButton.setSize(60, 60);
		predButton.setGradientBackground(new Gradient(ColorRGBA.Red,
				ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		predButton.setWorldLocation(new Vector2f(xindent, yindent));
		predButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item,
					MultiTouchCursorEvent event) {
				mode = Mode.AddPredators;
			}
		});
		
		xindent += 70;

		IColourRectangle blueButton = contentFactory.create(
				IColourRectangle.class, "bluebutton", UUID.randomUUID());
		stage.addItem(blueButton);
		blueButton.setSize(60, 60);
		blueButton.setGradientBackground(new Gradient(ColorRGBA.Blue,
				ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		blueButton.setWorldLocation(new Vector2f(xindent, yindent));
		blueButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item,
					MultiTouchCursorEvent event) {
				mode = Mode.BlueBirds;
			}
		});
		
		xindent += 70;

		IColourRectangle greenButton = contentFactory.create(
				IColourRectangle.class, "greenbutton", UUID.randomUUID());
		stage.addItem(greenButton);
		greenButton.setSize(60, 60);
		greenButton.setGradientBackground(new Gradient(ColorRGBA.Green,
				ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		greenButton.setWorldLocation(new Vector2f(xindent, yindent));
		greenButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item,
					MultiTouchCursorEvent event) {
				mode = Mode.GreenBirds;
			}
		});
		
		xindent += 70;

		IColourRectangle yellowButton = contentFactory.create(
				IColourRectangle.class, "yellowButton", UUID.randomUUID());
		stage.addItem(yellowButton);
		yellowButton.setSize(60, 60);
		yellowButton.setGradientBackground(new Gradient(ColorRGBA.Yellow,
				ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		yellowButton.setWorldLocation(new Vector2f(xindent, yindent));
		yellowButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item,
					MultiTouchCursorEvent event) {
				mode = Mode.YellowBirds;
			}
		});
		
		xindent += 70;

		IColourRectangle magentaButton = contentFactory.create(
				IColourRectangle.class, "greenbutton", UUID.randomUUID());
		stage.addItem(magentaButton);
		magentaButton.setSize(60, 60);
		magentaButton.setGradientBackground(new Gradient(ColorRGBA.Magenta,
				ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		magentaButton.setWorldLocation(new Vector2f(xindent, yindent));
		magentaButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item,
					MultiTouchCursorEvent event) {
				mode = Mode.Food;
			}
		});
		
		xindent += 70;

		IColourRectangle greyButton = contentFactory.create(
				IColourRectangle.class, "greenbutton", UUID.randomUUID());
		stage.addItem(greyButton);
		greyButton.setSize(60, 60);
		greyButton.setGradientBackground(new Gradient(ColorRGBA.LightGray,
				ColorRGBA.DarkGray, GradientDirection.VERTICAL));
		greyButton.setWorldLocation(new Vector2f(xindent, yindent));
		greyButton.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item,
					MultiTouchCursorEvent event) {
				mode = Mode.Obstacle;
			}
		});
	}

}

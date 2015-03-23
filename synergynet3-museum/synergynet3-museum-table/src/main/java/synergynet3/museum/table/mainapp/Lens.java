package synergynet3.museum.table.mainapp;

import java.util.Date;
import java.util.UUID;

import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.fonts.FontUtil;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.utils.ImageUtils;
import synergynet3.museum.table.utils.LensUtils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class Lens.
 */
public class Lens {

	/** The Constant BORDER_WIDTH. */
	private final static float BORDER_WIDTH = 10;

	/** The Constant HEIGHT. */
	private final static int HEIGHT = 360;

	/** The Constant SETTINGS_HEIGHT. */
	private final static int SETTINGS_HEIGHT = 60;

	/** The Constant TIME_OUT. */
	private final static int TIME_OUT = 30000;

	/** The Constant WIDTH. */
	private final static int WIDTH = 360;

	/** The age label. */
	private IMutableLabel ageLabel;

	/** The container. */
	private IContainer container;

	/** The filters. */
	private IColourRectangle[] filters;

	/** The instance. */
	private Lens instance;

	/** The last updated. */
	private Date lastUpdated = new Date();

	/** The lenses. */
	private String[] lenses;

	/** The lens index. */
	private int lensIndex = 0;

	/** The lens value. */
	private String lensValue = "";

	/** The map manager. */
	private POIManager mapManager;

	/** The open. */
	private boolean open = true;

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new lens.
	 *
	 * @param stageIn the stage in
	 * @param lensesIn the lenses in
	 * @param mapManagerIn the map manager in
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	public Lens(IStage stageIn, String[] lensesIn, POIManager mapManagerIn)
			throws ContentTypeNotBoundException {
		stage = stageIn;
		mapManager = mapManagerIn;
		instance = this;
		lenses = lensesIn;

		Thread timeoutThread = new Thread(new Runnable() {
			public void run() {
				while (open) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if ((new Date().getTime() - lastUpdated.getTime()) > TIME_OUT) {
						destroy();
					}
				}
			}
		});
		timeoutThread.start();

		container = stage.getContentFactory().create(IContainer.class,
				"container", UUID.randomUUID());

		filters = new IColourRectangle[lenses.length];
		for (int i = 0; i < lenses.length; i++) {
			String col = LensUtils.getLensColour(lenses[i]);
			ColorRGBA colour = SettingsUtil.getColorRGBA(col);
			colour.set(colour.r, colour.g, colour.b, 0.2f);

			filters[i] = stage.getContentFactory().create(
					IColourRectangle.class, "filter", UUID.randomUUID());
			filters[i].enableTransparency();
			filters[i].setGradientBackground(new Gradient(colour, colour,
					GradientDirection.DIAGONAL));
			filters[i].setSize(WIDTH, HEIGHT + SETTINGS_HEIGHT);
			filters[i]
					.setRelativeLocation(new Vector2f(0, -SETTINGS_HEIGHT / 2));
			filters[i].setInteractionEnabled(false);
			filters[i].setVisible(false);
			container.addItem(filters[i]);
		}

		IImage bgImage = stage.getContentFactory().create(IImage.class,
				"border", UUID.randomUUID());
		bgImage.setImage(getBackgroundImage(MuseumAppPreferences
				.getLensBackgroundColour()));
		bgImage.setSize(WIDTH, HEIGHT + SETTINGS_HEIGHT);
		bgImage.setRelativeLocation(new Vector2f(0, -SETTINGS_HEIGHT / 2));
		bgImage.setInteractionEnabled(false);
		container.addItem(bgImage);

		IRoundedBorder frameBorder = stage.getContentFactory().create(
				IRoundedBorder.class, "containerBorder", UUID.randomUUID());
		frameBorder.setBorderWidth(BORDER_WIDTH);
		frameBorder.setSize(WIDTH, HEIGHT + SETTINGS_HEIGHT);
		frameBorder.setRelativeLocation(new Vector2f(0, -SETTINGS_HEIGHT / 2));
		frameBorder.setColor(MuseumAppPreferences.getLensBorderColour());
		container.addItem(frameBorder);
		frameBorder.setInteractionEnabled(false);
		frameBorder.getZOrderManager().setBringToTopPropagatesUp(false);

		ageLabel = stage.getContentFactory().create(IMutableLabel.class,
				"textLabel", UUID.randomUUID());
		ageLabel.setFont(FontUtil.getFont(MuseumAppPreferences
				.getLensFontColour()));
		ageLabel.setRelativeLocation(new Vector2f(0,
				((-HEIGHT / 2) - (SETTINGS_HEIGHT / 2)) + 2f));
		ageLabel.setBoxSize(WIDTH, SETTINGS_HEIGHT);
		ageLabel.setInteractionEnabled(false);
		ageLabel.setRelativeScale(0.8f);
		container.addItem(ageLabel);

		IImage listener = stage.getContentFactory().create(IImage.class,
				"listener", UUID.randomUUID());
		listener.setSize(WIDTH + (BORDER_WIDTH * 2), HEIGHT + SETTINGS_HEIGHT
				+ (BORDER_WIDTH * 2));
		listener.setRelativeLocation(new Vector2f(0, -SETTINGS_HEIGHT / 2));

		final IgnoreDoubleClick clicker = new IgnoreDoubleClick(500) {
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				stage.getZOrderManager().bringToTop(container);
				onUpdate();

				Vector2f eventLoc = new Vector2f(event.getPosition().x
						* stage.getDisplayWidth(), event.getPosition().y
						* stage.getDisplayHeight());

				for (IImage poi : mapManager.getPOIs()) {

					boolean greaterThanMinX = eventLoc.x > (poi
							.getWorldLocation().x - (poi.getWidth() / 2));
					boolean lessThanMaxX = eventLoc.x < (poi.getWorldLocation().x + (poi
							.getWidth() / 2));
					boolean greaterThanMinY = eventLoc.y > (poi
							.getWorldLocation().y - (poi.getHeight() / 2));
					boolean lessThanMaxY = eventLoc.y < (poi.getWorldLocation().y + (poi
							.getHeight() / 2));

					if (greaterThanMinX && lessThanMaxX && greaterThanMinY
							&& lessThanMaxY) {
						poi.getMultiTouchDispatcher().cursorPressed(event);
					}
				}

				for (LensVisiblePOI poi : mapManager.getlensVisiblePOIs()) {

					boolean greaterThanMinX = eventLoc.x > (poi.getPoi()
							.getWorldLocation().x - (poi.getPoi().getWidth() / 2));
					boolean lessThanMaxX = eventLoc.x < (poi.getPoi()
							.getWorldLocation().x + (poi.getPoi().getWidth() / 2));
					boolean greaterThanMinY = eventLoc.y > (poi.getPoi()
							.getWorldLocation().y - (poi.getPoi().getHeight() / 2));
					boolean lessThanMaxY = eventLoc.y < (poi.getPoi()
							.getWorldLocation().y + (poi.getPoi().getHeight() / 2));

					if (greaterThanMinX && lessThanMaxX && greaterThanMinY
							&& lessThanMaxY) {
						poi.getPoi().getMultiTouchDispatcher()
								.cursorPressed(event);
					}
				}
			}
		};

		listener.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {

					@Override
					public void cursorChanged(MultiTouchCursorEvent event) {
						onUpdate();
					}

					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						clicker.click(event);
					}
				});
		container.addItem(listener);
		container.getZOrderManager().bringToTop(listener);

		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker()
				.addBehaviour(listener, RotateTranslateScaleBehaviour.class);
		rts.setScaleLimits(0.5f, 1.5f);
		rts.setItemActingOn(container);

		lensValue = lenses[0];
		filters[0].setVisible(true);

		if (lenses.length > 1) {

			IImage leftButton = stage.getContentFactory().create(IImage.class,
					"down", UUID.randomUUID());
			leftButton.setImage(getArrowImage(MuseumAppPreferences
					.getLensArrowColour()));
			leftButton.setSize(SETTINGS_HEIGHT / 2, SETTINGS_HEIGHT / 2);
			leftButton.setRelativeLocation(new Vector2f((-WIDTH / 2)
					+ (SETTINGS_HEIGHT / 2), (-HEIGHT / 2)
					- (SETTINGS_HEIGHT / 2)));

			final IgnoreDoubleClick downClicker = new IgnoreDoubleClick(500) {
				@Override
				public void onAction(MultiTouchCursorEvent event) {

					filters[lensIndex].setVisible(false);
					lensIndex--;
					if (lensIndex < 0) {
						lensIndex = lenses.length - 1;
					}
					filters[lensIndex].setVisible(true);
					lensValue = lenses[lensIndex];
					ageLabel.setText(lensValue);

					onUpdate();
				}
			};

			leftButton.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							downClicker.click(event);
						}
					});
			container.addItem(leftButton);

			IImage rightButton = stage.getContentFactory().create(IImage.class,
					"up", UUID.randomUUID());
			rightButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			rightButton.setImage(getArrowImage(MuseumAppPreferences
					.getLensArrowColour()));
			rightButton.setSize(SETTINGS_HEIGHT / 2, SETTINGS_HEIGHT / 2);
			rightButton.setRelativeLocation(new Vector2f((WIDTH / 2)
					- (SETTINGS_HEIGHT / 2), (-HEIGHT / 2)
					- (SETTINGS_HEIGHT / 2)));

			final IgnoreDoubleClick upClicker = new IgnoreDoubleClick(500) {
				@Override
				public void onAction(MultiTouchCursorEvent event) {

					filters[lensIndex].setVisible(false);
					lensIndex++;
					if (lensIndex >= lenses.length) {
						lensIndex = 0;
					}
					filters[lensIndex].setVisible(true);
					lensValue = lenses[lensIndex];
					ageLabel.setText(lensValue);

					onUpdate();
				}
			};

			rightButton.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							upClicker.click(event);
						}
					});
			container.addItem(rightButton);

		}

		IImage closeButton = stage.getContentFactory().create(IImage.class,
				"closeButton", UUID.randomUUID());
		closeButton.setImage(ImageUtils.getImage(
				MuseumAppPreferences.getLensCloseColour(),
				ImageUtils.RESOURCE_DIR + "entitybuttons/close/",
				"_close_button.png"));
		closeButton.setSize(LabelGenerator.TEXT_HEIGHT / 2,
				LabelGenerator.TEXT_HEIGHT / 2);
		closeButton.setRelativeLocation(new Vector2f((WIDTH / 2)
				- (closeButton.getWidth() / 2), (HEIGHT / 2)
				- (closeButton.getHeight() / 2)));
		closeButton.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {
					@Override
					public void cursorClicked(MultiTouchCursorEvent event) {
						destroy();
						POIManager.lenses.remove(instance);
					}
				});
		container.addItem(closeButton);

		container.getZOrderManager().unregisterForZOrdering(listener);

		ageLabel.setText(lensValue);

		stage.addItem(container);

		onUpdate();
	}

	/**
	 * As item.
	 *
	 * @return the i item
	 */
	public IItem asItem() {
		return container;
	}

	/**
	 * Destroy.
	 */
	public void destroy() {
		container.setVisible(false);
		stage.removeItem(container);
		for (LensVisiblePOI eventPOI : mapManager.getlensVisiblePOIs()) {
			eventPOI.removeLens(this);
		}
		open = false;
	}

	/**
	 * @return the startDate
	 */
	public String getLensValue() {
		return lensValue;
	}

	/**
	 * Checks if is within filter.
	 *
	 * @param vec the vec
	 * @return true, if is within filter
	 */
	public boolean isWithinFilter(Vector2f vec) {
		if (container.getRelativeLocation().distance(vec) < ((WIDTH / 2) * container
				.getRelativeScale())) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the arrow image.
	 *
	 * @param colour the colour
	 * @return the arrow image
	 */
	private String getArrowImage(String colour) {
		return ImageUtils.getImage(colour, ImageUtils.RESOURCE_DIR
				+ "sliderarrows/", "_slider_arrow.png");
	}

	/**
	 * Gets the background image.
	 *
	 * @param colour the colour
	 * @return the background image
	 */
	private String getBackgroundImage(String colour) {
		return ImageUtils.getImage(colour, ImageUtils.RESOURCE_DIR
				+ "lensbackgrounds/", "_lens_bg.png");
	}

	/**
	 * On update.
	 */
	private void onUpdate() {
		lastUpdated = new Date();
		for (LensVisiblePOI eventPOI : mapManager.getlensVisiblePOIs()) {
			eventPOI.update(this);
		}
	}

}

package synergynet3.activitypack2.table.mysteries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.IScrollContainer;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;
import synergynet3.mediadetection.mediasearchtypes.XMLSearchType;
import synergynet3.projector.SynergyNetProjector;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/** Class to be run to produce a projection environment. */
public class MysteriesProjectorMenu {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(MysteriesProjectorMenu.class.getName());

	/** The Constant TEXT_WIDTH_LIMIT. */
	private static final float TEXT_WIDTH_LIMIT = 500;

	/** The Constant TITLE_WIDTH_LIMIT. */
	private static final float TITLE_WIDTH_LIMIT = 800;

	/** The Constant XML_CHECK. */
	private static final XMLSearchType XML_CHECK = new XMLSearchType();

	/** The Constant Y_OFFSET. */
	private static final int Y_OFFSET = 85;

	/** The Constant OFFSET. */
	protected static final float BUTTON_SIZE = 40f, CONTROL_BORDER_WIDTH = 5f,
			OFFSET = 10f;

	/** The Constant CORNER_ICON_LOC_OFF. */
	protected static final String CORNER_ICON_LOC_OFF = "synergynet3/projector/corner_button.png";

	/** The Constant CORNER_ICON_LOC_ON. */
	protected static final String CORNER_ICON_LOC_ON = "synergynet3/projector/corner_button_on.png";

	/** The bottom leftcorner button. */
	private IImage bottomLeftcornerButton;

	/** The mystery menu. */
	private IScrollContainer mysteryMenu;

	/** The projector. */
	private SynergyNetProjector projector;

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new mysteries projector menu.
	 *
	 * @param projector the projector
	 */
	public MysteriesProjectorMenu(SynergyNetProjector projector) {
		this.stage = projector.getStage();
		this.projector = projector;
		try {
			generateMysteriesMenuControlButton();
			generateMysteriesMenu();
		} catch (ContentTypeNotBoundException e) {
			log.warning("ContentTypeNotBoundException: " + e);
		}
	}

	/**
	 * Adds the content from directory.
	 *
	 * @param f the f
	 */
	private void addContentFromDirectory(File f) {
		projector.clearContents();
		ArrayList<IItem> items = new ArrayList<IItem>();
		File[] files = f.listFiles();
		for (File file : files) {
			try {
				if (XML_CHECK.isFileOfSearchType(file)) {
					items.addAll(parseXmlFile(file));
				} else {
					IItem item = AdditionalSynergyNetUtilities
							.generateItemFromFile(file, stage, -1, 250f,
									ColorRGBA.White);
					if (item != null) {
						items.add(item);
					}
				}
			} catch (ContentTypeNotBoundException ex) {
			}
			mysteryMenu.setVisibility(false);
			bottomLeftcornerButton.setImage(CORNER_ICON_LOC_OFF);
		}

		Collections.shuffle(items);
		for (IItem item : items) {
			stage.getZOrderManager().bringToTop(item);
			item.setRelativeScale(FastMath.nextRandomInt(60, 90) / 100f);
		}
		IItem[] itemArray = new IItem[items.size()];
		items.toArray(itemArray);
		AdditionalSynergyNetUtilities.pile(itemArray, 0, 0, 20, 0);
	}

	/**
	 * Generate mysteries menu.
	 *
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void generateMysteriesMenu() throws ContentTypeNotBoundException {

		Logger log = Logger.getLogger(MysteriesProjectorMenu.class.getName());

		mysteryMenu = stage.getContentFactory().create(IScrollContainer.class,
				"menu", UUID.randomUUID());
		mysteryMenu.setDimensions(stage, log, 250, 350);
		stage.addItem(mysteryMenu);
		mysteryMenu.getBackground().setSolidBackgroundColour(ColorRGBA.Blue);
		mysteryMenu.getBorder().setBorderWidth(5);
		mysteryMenu.setArrowWidthOverride(30f);
		mysteryMenu.setArrowHeightOverride(50f);
		mysteryMenu.setArrowYOverride(-145);
		mysteryMenu.setRelativeLocation(new Vector2f(
				(-stage.getDisplayWidth() / 2) + (250 / 2)
						+ (BUTTON_SIZE - OFFSET),
				(-stage.getDisplayHeight() / 2) + (350 / 2)
						+ (BUTTON_SIZE - OFFSET)));
		mysteryMenu.setActive(false);
		mysteryMenu.setVisibility(false);

		IMutableLabel textLabel = stage.getContentFactory().create(
				IMutableLabel.class, "textLabel", UUID.randomUUID());
		textLabel.setFont(FontUtil.getFont(FontColour.White));
		textLabel.setText("Select Mystery:");
		textLabel.setBoxSize(350, 50);
		textLabel.setRelativeScale(0.9f);
		mysteryMenu.addToAllFrames(textLabel, 0, 150);

		int y = 175;
		int frame = 0;

		File mysteriesDir = new File(MysteriesProjector.mysteriesLocation);
		for (File file : mysteriesDir.listFiles()) {
			if (file.isDirectory()) {
				y = y - Y_OFFSET;
				if (y <= -150) {
					y = 175 - Y_OFFSET;
					frame++;
					mysteryMenu.addFrame();
				}
				mysteryMenu
						.addToFrame(generateMysteryButton(file), frame, 0, y);
				mysteryMenu.getZOrderManager().setAutoBringToTop(false);
			}
		}
	}

	/**
	 * Generate mysteries menu control button.
	 *
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void generateMysteriesMenuControlButton()
			throws ContentTypeNotBoundException {
		bottomLeftcornerButton = stage.getContentFactory().create(IImage.class,
				"cornerButton", UUID.randomUUID());
		bottomLeftcornerButton.setImage(CORNER_ICON_LOC_OFF);
		bottomLeftcornerButton.setSize(BUTTON_SIZE * 2, BUTTON_SIZE * 2);
		bottomLeftcornerButton.setRelativeLocation(new Vector2f(-stage
				.getDisplayWidth() / 2, -stage.getDisplayHeight() / 2));
		bottomLeftcornerButton.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {
					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						if (mysteryMenu.isVisible()) {
							mysteryMenu.setVisibility(false);
							bottomLeftcornerButton
									.setImage(CORNER_ICON_LOC_OFF);
						} else {
							mysteryMenu.setVisibility(true);
							stage.getZOrderManager().bringToTop(mysteryMenu);
							bottomLeftcornerButton.setImage(CORNER_ICON_LOC_ON);
						}
					}
				});
		stage.addItem(bottomLeftcornerButton);
	}

	/**
	 * Generate mystery button.
	 *
	 * @param file the file
	 * @return the i item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IItem generateMysteryButton(final File file)
			throws ContentTypeNotBoundException {
		IButtonbox button = stage.getContentFactory().create(IButtonbox.class,
				"button", UUID.randomUUID());
		button.setText(file.getName(), ColorRGBA.Gray, ColorRGBA.White,
				FontColour.Black, 200, 60, stage);
		button.getListener().getMultiTouchDispatcher()
				.addListener(new MultiTouchEventAdapter() {
					@Override
					public void cursorClicked(MultiTouchCursorEvent event) {
						addContentFromDirectory(file);
					}
				});
		return button;
	}

	/**
	 * Gets the text item.
	 *
	 * @param el the el
	 * @return the text item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IItem getTextItem(Element el) throws ContentTypeNotBoundException {

		String text = getTextValue(el, "contents");

		ITextbox textItem = stage.getContentFactory().create(ITextbox.class,
				text, UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setScaleLimits(0.5f, 1.5f);
		textItem.setColours(ColorRGBA.White, ColorRGBA.Gray, FontColour.Black);
		textItem.setWidth(TEXT_WIDTH_LIMIT);
		textItem.setHeight(60f);
		textItem.setText(text, stage);
		FeedbackSystem.registerAsFeedbackEligible(textItem, TEXT_WIDTH_LIMIT,
				textItem.getHeight(), stage);

		stage.addItem(textItem);
		return textItem;
	}

	/**
	 * Gets the text value.
	 *
	 * @param ele the ele
	 * @param tagName the tag name
	 * @return the text value
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if ((nl != null) && (nl.getLength() > 0)) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	/**
	 * Gets the title item.
	 *
	 * @param el the el
	 * @return the title item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void getTitleItem(Element el) throws ContentTypeNotBoundException {

		String text = getTextValue(el, "contents");

		ITextbox textItem = stage.getContentFactory().create(ITextbox.class,
				text, UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setScaleLimits(0.5f, 1.5f);
		textItem.setColours(ColorRGBA.Black, ColorRGBA.Green, FontColour.Green);
		textItem.setWidth(TITLE_WIDTH_LIMIT);
		textItem.setText(text, stage);
		FeedbackSystem.registerAsFeedbackEligible(textItem, TITLE_WIDTH_LIMIT,
				textItem.getHeight(), stage);

		textItem.setRelativeLocation(new Vector2f(0,
				(stage.getDisplayHeight() / 2) - textItem.getHeight()));
		stage.addItem(textItem);
	}

	/**
	 * Parses the xml file.
	 *
	 * @param file the file
	 * @return the array list
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private ArrayList<IItem> parseXmlFile(File file)
			throws ContentTypeNotBoundException {
		ArrayList<IItem> items = new ArrayList<IItem>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(file);

			Element docEle = dom.getDocumentElement();

			NodeList nl = docEle.getElementsByTagName("Text");
			if ((nl != null) && (nl.getLength() > 0)) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element el = (Element) nl.item(i);
					String type = el.getAttribute("type");
					if (type.equalsIgnoreCase("title")) {
						getTitleItem(el);
					} else {
						items.add(getTextItem(el));
					}
				}
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return items;
	}

}
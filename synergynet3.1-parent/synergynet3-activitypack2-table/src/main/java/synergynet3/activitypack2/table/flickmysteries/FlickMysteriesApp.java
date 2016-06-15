package synergynet3.activitypack2.table.flickmysteries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import synergynet3.SynergyNetApp;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.fonts.FontColour;
import synergynet3.mediadetection.mediasearchtypes.XMLSearchType;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class TestVideoApp.
 */
public class FlickMysteriesApp extends SynergyNetApp
{

	/** The Constant TEXT_WIDTH_LIMIT. */
	private static final float TEXT_WIDTH_LIMIT = 500;

	/** The Constant TITLE_WIDTH_LIMIT. */
	private static final float TITLE_WIDTH_LIMIT = 800;

	/** The Constant XML_CHECK. */
	private static final XMLSearchType XML_CHECK = new XMLSearchType();

	/** The media source folder. */
	private static String mediaSource = "";

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		if (args.length == 1)
		{

			mediaSource = args[0];

			MultiplicityClient client = MultiplicityClient.get();
			client.start();
			FlickMysteriesApp app = new FlickMysteriesApp();
			client.setCurrentApp(app);

		}
		else
		{
			AdditionalSynergyNetUtilities.logInfo("One mysteries location expected.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.projector.SynergyNetProjector#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName()
	{
		return "Flick Mysteries";
	}

	/**
	 * Adds the content from directory.
	 *
	 * @param f
	 *            the File
	 */
	private void addContentFromDirectory(File f)
	{
		ArrayList<IItem> items = new ArrayList<IItem>();
		File[] files = f.listFiles();
		for (File file : files)
		{
			try
			{
				if (XML_CHECK.isFileOfSearchType(file))
				{
					items.addAll(parseXmlFile(file));
				}
				else
				{
					IItem item = AdditionalSynergyNetUtilities.generateItemFromFile(file, stage, -1, 250f, ColorRGBA.White);
					if (item != null)
					{
						items.add(item);

						// Make flickable
						NetworkFlickBehaviour nf = stage.getBehaviourMaker().addBehaviour(item, NetworkFlickBehaviour.class);
						nf.setMaxDimension(250f);
						nf.setItemActingOn(item);
						nf.setDeceleration(deceleration);

					}
				}
			}
			catch (ContentTypeNotBoundException ex)
			{
			}
		}

		Collections.shuffle(items);
		for (IItem item : items)
		{
			stage.getZOrderManager().bringToTop(item);
			item.setRelativeScale(FastMath.nextRandomInt(60, 90) / 100f);
		}
		IItem[] itemArray = new IItem[items.size()];
		items.toArray(itemArray);
		AdditionalSynergyNetUtilities.pile(itemArray, 0, 0, 20, 0);
	}

	/**
	 * Gets the text item.
	 *
	 * @param el
	 *            the el
	 * @return the text item
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private IItem getTextItem(Element el) throws ContentTypeNotBoundException
	{

		String text = getTextValue(el, "contents");

		ITextbox textItem = stage.getContentFactory().create(ITextbox.class, text, UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setScaleLimits(0.5f, 1.5f);
		textItem.setColours(ColorRGBA.White, ColorRGBA.Gray, FontColour.Black);
		textItem.setWidth(TEXT_WIDTH_LIMIT);
		textItem.setHeight(60f);
		textItem.setText(text, stage);

		FeedbackSystem.registerAsFeedbackEligible(textItem, TEXT_WIDTH_LIMIT, textItem.getHeight(), stage);

		// Make flickable
		NetworkFlickBehaviour nf = stage.getBehaviourMaker().addBehaviour(textItem.getListenBlock(), NetworkFlickBehaviour.class);
		nf.setMaxDimension(TEXT_WIDTH_LIMIT);
		nf.setItemActingOn(textItem);
		nf.setDeceleration(deceleration);

		stage.addItem(textItem);
		return textItem;
	}

	/**
	 * Gets the text value.
	 *
	 * @param ele
	 *            the ele
	 * @param tagName
	 *            the tag name
	 * @return the text value
	 */
	private String getTextValue(Element ele, String tagName)
	{
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if ((nl != null) && (nl.getLength() > 0))
		{
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	/**
	 * Gets the title item.
	 *
	 * @param el
	 *            the el
	 * @return the title item
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void getTitleItem(Element el) throws ContentTypeNotBoundException
	{

		String text = getTextValue(el, "contents");

		ITextbox textItem = stage.getContentFactory().create(ITextbox.class, text, UUID.randomUUID());
		textItem.setMovable(false);
		textItem.setScaleLimits(0.5f, 1.5f);
		textItem.setColours(ColorRGBA.Black, ColorRGBA.Green, FontColour.Green);
		textItem.setWidth(TITLE_WIDTH_LIMIT);
		textItem.setText(text, stage);

		textItem.setRelativeLocation(new Vector2f(0, (stage.getDisplayHeight() / 2) - textItem.getHeight()));
		stage.addItem(textItem);
	}

	/**
	 * Parses the xml file.
	 *
	 * @param file
	 *            the file
	 * @return the array list
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private ArrayList<IItem> parseXmlFile(File file) throws ContentTypeNotBoundException
	{
		ArrayList<IItem> items = new ArrayList<IItem>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(file);

			Element docEle = dom.getDocumentElement();

			NodeList nl = docEle.getElementsByTagName("Text");
			if ((nl != null) && (nl.getLength() > 0))
			{
				for (int i = 0; i < nl.getLength(); i++)
				{
					Element el = (Element) nl.item(i);
					String type = el.getAttribute("type");
					if (type.equalsIgnoreCase("title"))
					{
						getTitleItem(el);
					}
					else
					{
						items.add(getTextItem(el));
					}
				}
			}
		}
		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch (SAXException se)
		{
			se.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return items;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#loadDefaultContent()
	 */
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException
	{

		// ProjectorTransferUtilities.get().setDecelerationOnArrival(-1);
		// feedbackTypes.add(SimpleTrafficLightFeedback.class);
		// feedbackTypes.add(AudioFeedback.class);
		// feedbackTypes.add(SmilieFeedback.class);
		// feedbackTypes.add(YesOrNoFeedback.class);

		this.enableNetworkFlick();

		try
		{
			// Load content from mysteries folder
			File mysteriesDir = new File(mediaSource);
			addContentFromDirectory(mysteriesDir);
		}
		catch (Exception ex)
		{
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Unable to open the folder provided in the arguments.", ex);
		}

	}

}

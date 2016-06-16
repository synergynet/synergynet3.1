package synergynet3.museum.table.settingsapp.appearance;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class AppearanceXmlParser.
 */
public class AppearanceXmlParser extends DefaultHandler
{

	/** The appearance values. */
	private HashMap<String, String> appearanceValues = new HashMap<String, String>();

	/** The current flag. */
	private String currentFlag = "";

	/** The link text. */
	private boolean linkText = false;

	/**
	 * Instantiates a new appearance xml parser.
	 */
	public AppearanceXmlParser()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char ch[], int start, int length) throws SAXException
	{
		if (AppearanceConfigPrefsItem.getDefaults().containsKey(currentFlag))
		{
			String previous = "";
			if (appearanceValues.containsKey(currentFlag))
			{
				previous = appearanceValues.get(currentFlag);
			}
			appearanceValues.put(currentFlag, previous + new String(ch, start, length));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		currentFlag = "";
		if (linkText)
		{
			if (!appearanceValues.containsKey(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT))
			{
				appearanceValues.put(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT, "");
			}
			linkText = false;
		}
	}

	/**
	 * @return the appearanceValues
	 */
	public HashMap<String, String> getAppearanceValues()
	{
		return appearanceValues;
	}

	/**
	 * @param appearanceValues
	 *            the appearanceValues to set
	 */
	public void setAppearanceValues(HashMap<String, String> appearanceValues)
	{
		this.appearanceValues = appearanceValues;
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		currentFlag = qName;
		if (currentFlag.equals(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT))
		{
			linkText = true;
		}
	}

}

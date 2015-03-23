package synergynet3.museum.table.settingsapp.lensmanager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class LensXmlParser.
 */
public class LensXmlParser extends DefaultHandler {

	/** The colour. */
	private String colour = "";

	/** The current flag. */
	private String currentFlag = "";

	/** The name. */
	private String name = "";

	/**
	 * Instantiates a new lens xml parser.
	 */
	public LensXmlParser() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (currentFlag.equalsIgnoreCase(LensXmlFields.NAME)) {
			name = new String(ch, start, length);
		} else if (currentFlag.equalsIgnoreCase(LensXmlFields.COLOUR)) {
			colour = new String(ch, start, length);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentFlag = "";
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentFlag = qName;
	}

}
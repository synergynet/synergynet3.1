package synergynet3.museum.table.settingsapp.lensmanager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LensXmlParser extends DefaultHandler{
	
	private String currentFlag = "";
	
	private String name = "";
	private String colour = "";
	
	public LensXmlParser(){
		super();
	}
 
	@Override
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		currentFlag = qName;			 
	}
 
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentFlag = ""; 
	}
 
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (currentFlag.equalsIgnoreCase(LensXmlFields.NAME)){
			name = new String(ch, start, length);
		}else if (currentFlag.equalsIgnoreCase(LensXmlFields.COLOUR)){
			colour = new String(ch, start, length);
		}		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}	
	
}
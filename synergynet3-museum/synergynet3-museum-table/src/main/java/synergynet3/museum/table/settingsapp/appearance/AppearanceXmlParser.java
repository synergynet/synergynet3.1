package synergynet3.museum.table.settingsapp.appearance;


import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class AppearanceXmlParser extends DefaultHandler{
	
	private String currentFlag = "";
	private boolean linkText = false;

	private HashMap<String, String> appearanceValues = new HashMap<String, String>();
	
	public AppearanceXmlParser(){
		super();
	}
 
	@Override
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		currentFlag = qName;			 
		if (currentFlag.equals(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT)){
			linkText = true;
		}
	}
 
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentFlag = ""; 
		if (linkText){
			if(!appearanceValues.containsKey(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT)){
				appearanceValues.put(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT, "");
			}
			linkText = false;
		}
	}
 
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (AppearanceConfigPrefsItem.getDefaults().containsKey(currentFlag)){
			String previous = "";
			if (appearanceValues.containsKey(currentFlag)){
				previous = appearanceValues.get(currentFlag);
			}
			appearanceValues.put(currentFlag, previous + new String(ch, start, length));
		}
	}

	/**
	 * @return the appearanceValues
	 */
	public HashMap<String, String> getAppearanceValues() {
		return appearanceValues;
	}

	/**
	 * @param appearanceValues the appearanceValues to set
	 */
	public void setAppearanceValues(HashMap<String, String> appearanceValues) {
		this.appearanceValues = appearanceValues;
	}	
	
}
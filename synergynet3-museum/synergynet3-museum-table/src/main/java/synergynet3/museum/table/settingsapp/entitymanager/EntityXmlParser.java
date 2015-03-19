package synergynet3.museum.table.settingsapp.entitymanager;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import synergynet3.museum.table.utils.EntityType;
import synergynet3.museum.table.utils.EntityXmlFields;

public class EntityXmlParser extends DefaultHandler{
	
	private String currentFlag = "";

	private ArrayList<String> facts = new ArrayList<String>();	
	private ArrayList<String> lenses = new ArrayList<String>();		
	private ArrayList<String> linked = new ArrayList<String>();
	private String name = "";
	private String image = "";
	private float x = 0;
	private float y = 0;
	private EntityType entityType = EntityType.Free;
	
	public EntityXmlParser(){
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
		if (currentFlag.equalsIgnoreCase(EntityXmlFields.NAME)){
			name = new String(ch, start, length);
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.X)){
			x = Float.parseFloat(new String(ch, start, length));
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.Y)){
			y = Float.parseFloat(new String(ch, start, length));						
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.LENS)){
			lenses.add(new String(ch, start, length));
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.FACT)){
			facts.add(new String(ch, start, length));						
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.LINK)){
			linked.add(new String(ch, start, length));						
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.IMAGE)){
			image = new String(ch, start, length);
		}else if (currentFlag.equalsIgnoreCase(EntityXmlFields.ENTITY_TYPE)){
			entityType = EntityType.valueOf(new String(ch, start, length));							
		}
	}	
	
	public String getName() {
		return name;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public String getImage() {
		return image;
	}
	
	public ArrayList<String> getFacts() {
		return facts;
	}
	
	public ArrayList<String> getLenses() {
		return lenses;
	}
	
	public ArrayList<String> getLinked() {
		return linked;
	}
	
	public EntityType getEntityType(){
		return entityType;
	}

}
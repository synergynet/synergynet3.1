package synergynet3.museum.table.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.SAXException;

import synergynet3.museum.table.settingsapp.entitymanager.EntityXmlParser;

public class Entity {
	
	public static String ENTITY_FILE = "entity.xml";
	
	private String location = "";
	private EntityType type = EntityType.Free;
	
	private String name = "";
	private float x = 0.5f;
	private float y = 0.5f;
	private int endDate = 0;
	private int startDate = 0;

	private ArrayList<String> facts = new ArrayList<String>();
	private ArrayList<String> linked = new ArrayList<String>();
	private ArrayList<String> lenses = new ArrayList<String>();
	
	public Entity(String location){
		this.setLocation(location);
		regenerate();
	}
	
	public void regenerate(){
		File f = new File(location);
		if (f.isDirectory()){
			File entityFile = new File(location + File.separator + ENTITY_FILE);
			if (entityFile.exists()){	
				try {
					SAXParserFactory factory = SAXParserFactory.newInstance();
					SAXParser saxParser = factory.newSAXParser();
					
					EntityXmlParser handler = new EntityXmlParser();
					saxParser.parse(entityFile, handler);
					
					setType(handler.getEntityType());
					
					setName(handler.getName());
					setX(handler.getX());
					setY(handler.getY());
					
					setLinked(handler.getLinked());
					setFacts(handler.getFacts());	
					setLenses(handler.getLenses());							
					
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveXML() {
		File entityFile = new File(location + File.separator + ENTITY_FILE);
		try {
			if (!entityFile.exists()){
				entityFile.createNewFile();
			}				

			OutputStream outputStream = new FileOutputStream(entityFile);

			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(outputStream, "utf-8"));
			out.writeStartDocument();
			out.writeCharacters("\n");
			out.writeStartElement(EntityXmlFields.ENTITY_NODE);
			out.writeCharacters("\n");
			
			out.writeStartElement(EntityXmlFields.NAME);
			out.writeCharacters(name);
			out.writeEndElement();
			out.writeCharacters("\n");
			
			out.writeStartElement(EntityXmlFields.ENTITY_TYPE);
			out.writeCharacters(type.toString());
			out.writeEndElement();
			out.writeCharacters("\n");
			
			switch (type){
				case LensedPOI:					
					out.writeStartElement(EntityXmlFields.X);
					out.writeCharacters("" + x);
					out.writeEndElement();
					out.writeCharacters("\n");
					
					out.writeStartElement(EntityXmlFields.Y);
					out.writeCharacters("" + y);
					out.writeEndElement();
					out.writeCharacters("\n");
					
					for (String lens: lenses){
						out.writeStartElement(EntityXmlFields.LENS);
						out.writeCharacters(lens);
						out.writeEndElement();
						out.writeCharacters("\n");
					}						
									
					break;
				case POI:		
				
					out.writeStartElement(EntityXmlFields.X);
					out.writeCharacters("" + x);
					out.writeEndElement();
					out.writeCharacters("\n");
					
					out.writeStartElement(EntityXmlFields.Y);
					out.writeCharacters("" + y);
					out.writeEndElement();
					out.writeCharacters("\n");
			
					break;
			default:
				break;
			}
			
			for (String fact: facts){
				out.writeStartElement(EntityXmlFields.FACT);
				out.writeCharacters(fact);
				out.writeEndElement();
				out.writeCharacters("\n");
			}
						
			for (String link: linked){
				out.writeStartElement(EntityXmlFields.LINK);
				out.writeCharacters(link);
				out.writeEndElement();
				out.writeCharacters("\n");
			}
			
			out.writeEndElement();
			out.writeEndDocument();

			out.close();			
			
			outputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the type
	 */
	public EntityType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EntityType type) {
		this.type = type;
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
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the endDate
	 */
	public int getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startDate
	 */
	public int getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the linked
	 */
	public ArrayList<String> getLinked() {
		return linked;
	}

	/**
	 * @param linked the linked to set
	 */
	public void setLinked(ArrayList<String> linked) {
		this.linked = linked;
	}

	/**
	 * @return the facts
	 */
	public ArrayList<String> getFacts() {
		return facts;
	}

	/**
	 * @param facts the facts to set
	 */
	public void setFacts(ArrayList<String> facts) {
		this.facts = facts;
	}
	
	/**
	 * @param lenses the lenses to set
	 */
	public void setLenses(ArrayList<String> lenses) {
		this.lenses = lenses;
	}

	/**
	 * @return the lenses
	 */
	public ArrayList<String> getLensValues() {
		return lenses;
	}		

}

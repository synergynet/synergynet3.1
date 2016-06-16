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

/**
 * The Class Entity.
 */
public class Entity
{

	/** The entity file. */
	public static String ENTITY_FILE = "entity.xml";

	/** The end date. */
	private int endDate = 0;

	/** The facts. */
	private ArrayList<String> facts = new ArrayList<String>();

	/** The lenses. */
	private ArrayList<String> lenses = new ArrayList<String>();

	/** The linked. */
	private ArrayList<String> linked = new ArrayList<String>();

	/** The location. */
	private String location = "";

	/** The name. */
	private String name = "";

	/** The start date. */
	private int startDate = 0;

	/** The type. */
	private EntityType type = EntityType.Free;

	/** The x. */
	private float x = 0.5f;

	/** The y. */
	private float y = 0.5f;

	/**
	 * Instantiates a new entity.
	 *
	 * @param location
	 *            the location
	 */
	public Entity(String location)
	{
		this.setLocation(location);
		regenerate();
	}

	/**
	 * @return the endDate
	 */
	public int getEndDate()
	{
		return endDate;
	}

	/**
	 * @return the facts
	 */
	public ArrayList<String> getFacts()
	{
		return facts;
	}

	/**
	 * @return the lenses
	 */
	public ArrayList<String> getLensValues()
	{
		return lenses;
	}

	/**
	 * @return the linked
	 */
	public ArrayList<String> getLinked()
	{
		return linked;
	}

	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the startDate
	 */
	public int getStartDate()
	{
		return startDate;
	}

	/**
	 * @return the type
	 */
	public EntityType getType()
	{
		return type;
	}

	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * Regenerate.
	 */
	public void regenerate()
	{
		File f = new File(location);
		if (f.isDirectory())
		{
			File entityFile = new File(location + File.separator + ENTITY_FILE);
			if (entityFile.exists())
			{
				try
				{
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

				}
				catch (ParserConfigurationException e)
				{
					e.printStackTrace();
				}
				catch (SAXException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Save xml.
	 */
	public void saveXML()
	{
		File entityFile = new File(location + File.separator + ENTITY_FILE);
		try
		{
			if (!entityFile.exists())
			{
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

			switch (type)
			{
				case LensedPOI:
					out.writeStartElement(EntityXmlFields.X);
					out.writeCharacters("" + x);
					out.writeEndElement();
					out.writeCharacters("\n");

					out.writeStartElement(EntityXmlFields.Y);
					out.writeCharacters("" + y);
					out.writeEndElement();
					out.writeCharacters("\n");

					for (String lens : lenses)
					{
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

			for (String fact : facts)
			{
				out.writeStartElement(EntityXmlFields.FACT);
				out.writeCharacters(fact);
				out.writeEndElement();
				out.writeCharacters("\n");
			}

			for (String link : linked)
			{
				out.writeStartElement(EntityXmlFields.LINK);
				out.writeCharacters(link);
				out.writeEndElement();
				out.writeCharacters("\n");
			}

			out.writeEndElement();
			out.writeEndDocument();

			out.close();

			outputStream.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(int endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @param facts
	 *            the facts to set
	 */
	public void setFacts(ArrayList<String> facts)
	{
		this.facts = facts;
	}

	/**
	 * @param lenses
	 *            the lenses to set
	 */
	public void setLenses(ArrayList<String> lenses)
	{
		this.lenses = lenses;
	}

	/**
	 * @param linked
	 *            the linked to set
	 */
	public void setLinked(ArrayList<String> linked)
	{
		this.linked = linked;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(int startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(EntityType type)
	{
		this.type = type;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y)
	{
		this.y = y;
	}

}

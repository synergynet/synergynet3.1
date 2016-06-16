package synergynet3.museum.table.settingsapp.entitymanager;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import synergynet3.museum.table.utils.EntityType;
import synergynet3.museum.table.utils.EntityXmlFields;

/**
 * The Class EntityXmlParser.
 */
public class EntityXmlParser extends DefaultHandler
{

	/** The current flag. */
	private String currentFlag = "";

	/** The entity type. */
	private EntityType entityType = EntityType.Free;

	/** The facts. */
	private ArrayList<String> facts = new ArrayList<String>();

	/** The image. */
	private String image = "";

	/** The lenses. */
	private ArrayList<String> lenses = new ArrayList<String>();

	/** The linked. */
	private ArrayList<String> linked = new ArrayList<String>();

	/** The name. */
	private String name = "";

	/** The x. */
	private float x = 0;

	/** The y. */
	private float y = 0;

	/**
	 * Instantiates a new entity xml parser.
	 */
	public EntityXmlParser()
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
		if (currentFlag.equalsIgnoreCase(EntityXmlFields.NAME))
		{
			name = new String(ch, start, length);
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.X))
		{
			x = Float.parseFloat(new String(ch, start, length));
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.Y))
		{
			y = Float.parseFloat(new String(ch, start, length));
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.LENS))
		{
			lenses.add(new String(ch, start, length));
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.FACT))
		{
			facts.add(new String(ch, start, length));
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.LINK))
		{
			linked.add(new String(ch, start, length));
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.IMAGE))
		{
			image = new String(ch, start, length);
		}
		else if (currentFlag.equalsIgnoreCase(EntityXmlFields.ENTITY_TYPE))
		{
			entityType = EntityType.valueOf(new String(ch, start, length));
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
	}

	/**
	 * Gets the entity type.
	 *
	 * @return the entity type
	 */
	public EntityType getEntityType()
	{
		return entityType;
	}

	/**
	 * Gets the facts.
	 *
	 * @return the facts
	 */
	public ArrayList<String> getFacts()
	{
		return facts;
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public String getImage()
	{
		return image;
	}

	/**
	 * Gets the lenses.
	 *
	 * @return the lenses
	 */
	public ArrayList<String> getLenses()
	{
		return lenses;
	}

	/**
	 * Gets the linked.
	 *
	 * @return the linked
	 */
	public ArrayList<String> getLinked()
	{
		return linked;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public float getY()
	{
		return y;
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
	}

}

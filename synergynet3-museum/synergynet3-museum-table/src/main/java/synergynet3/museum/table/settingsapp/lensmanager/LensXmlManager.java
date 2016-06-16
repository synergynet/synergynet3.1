package synergynet3.museum.table.settingsapp.lensmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class LensXmlManager.
 */
public class LensXmlManager extends DefaultHandler
{

	/** The colour. */
	private String colour = "";

	/** The file. */
	private File file;

	/** The name. */
	private String name = "";

	/** The valid workspace. */
	private boolean validWorkspace = true;

	/**
	 * Instantiates a new lens xml manager.
	 *
	 * @param file
	 *            the file
	 */
	public LensXmlManager(File file)
	{
		this.file = file;
	}

	/**
	 * @return the colour
	 */
	public String getColour()
	{
		return colour;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Regenerate.
	 */
	public void regenerate()
	{
		if (file.exists())
		{
			try
			{
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();

				LensXmlParser handler = new LensXmlParser();
				saxParser.parse(file, handler);

				name = handler.getName();
				colour = handler.getColour();

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

	/**
	 * Save xml.
	 */
	public void saveXML()
	{
		try
		{
			if (validWorkspace)
			{
				if (!file.exists())
				{
					file.createNewFile();
				}

				OutputStream outputStream = new FileOutputStream(file);

				XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(outputStream, "utf-8"));
				out.writeStartDocument();
				out.writeCharacters("\n");
				out.writeStartElement(LensXmlFields.LENS_NODE);
				out.writeCharacters("\n");

				out.writeStartElement(LensXmlFields.NAME);
				out.writeCharacters(name);
				out.writeEndElement();
				out.writeCharacters("\n");

				out.writeStartElement(LensXmlFields.COLOUR);
				out.writeCharacters(colour);
				out.writeEndElement();
				out.writeCharacters("\n");

				out.writeEndElement();
				out.writeEndDocument();

				out.close();

				outputStream.close();

			}

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
	 * @param colour
	 *            the colour to set
	 */
	public void setColour(String colour)
	{
		this.colour = colour;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}

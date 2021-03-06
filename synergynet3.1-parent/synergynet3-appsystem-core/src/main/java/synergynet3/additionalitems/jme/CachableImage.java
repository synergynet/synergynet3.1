package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;
import java.util.regex.Pattern;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.image.JMEImage;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;

import com.jme3.math.ColorRGBA;

/**
 * The Class CachableImage.
 */
@ImplementsContentItem(target = ICachableImage.class)
public class CachableImage extends JMEImage implements ICachableImage, IInitable, IItemCachable
{

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_IMAGE";

	/** The border. */
	private IRoundedBorder border;

	/** The cached. */
	private String cached = "";

	/** The resource location. */
	private File resourceLocation = null;

	/**
	 * Instantiates a new cachable image.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public CachableImage(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/**
	 * Reconstruct.
	 *
	 * @param galleryItem
	 *            the gallery item
	 * @param stage
	 *            the stage
	 * @param loc
	 *            the loc
	 * @return the cachable image
	 */
	public static CachableImage reconstruct(GalleryItemDatabaseFormat galleryItem, IStage stage, String loc)
	{
		try
		{
			CachableImage image = stage.getContentFactory().create(ICachableImage.class, (String) galleryItem.getValues().get(0), UUID.randomUUID());
			try
			{
				image.setImage(new File(CacheOrganisation.getSpecificDir(loc) + File.separator + (String) galleryItem.getValues().get(0)));
			}
			catch (Exception e)
			{
				image.setImage(CacheOrganisation.getSpecificDir(loc) + File.separator + (String) galleryItem.getValues().get(0));
			}
			image.setSize(galleryItem.getWidth(), galleryItem.getHeight());
			image.setCached(loc);
			return image;
		}
		catch (ContentTypeNotBoundException e)
		{
			return null;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.cachecontrol.IItemCachable#deconstruct(java.lang.String)
	 */
	@Override
	public GalleryItemDatabaseFormat deconstruct(String loc)
	{
		GalleryItemDatabaseFormat galleryItem = new GalleryItemDatabaseFormat();
		galleryItem.setType(CACHABLE_TYPE);
		if (!cached.equalsIgnoreCase(loc))
		{
			if (resourceLocation != null)
			{
				ItemCaching.cacheFile(resourceLocation, loc);
			}
			else
			{
				ItemCaching.cacheImage(getImage(), loc);
			}
		}
		String newLocation = ItemCaching.getFullImageName(getImage(), loc);
		String[] pathToNewLoc = newLocation.split(Pattern.quote(loc));
		newLocation = pathToNewLoc[pathToNewLoc.length - 1];
		newLocation = newLocation.substring(1);
		galleryItem.addValue(newLocation);
		return galleryItem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ICachableImage#generateBorder(
	 * multiplicity3.csys.stage.IStage, com.jme3.math.ColorRGBA, float)
	 */
	@Override
	public void generateBorder(IStage stage, ColorRGBA borderColour, float borderWidth)
	{
		try
		{
			border = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			border.setBorderWidth(borderWidth);
			border.setSize(getWidth(), getHeight());
			border.setColor(borderColour);
			border.setInteractionEnabled(false);
			this.addItem(border);
		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the cached
	 */
	public String isCached()
	{
		return cached;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ICachableImage#removeBorder()
	 */
	@Override
	public void removeBorder()
	{
		if (border != null)
		{
			this.removeItem(border);
		}
	}

	/**
	 * @param cached
	 *            the cached to set
	 */
	public void setCached(String cached)
	{
		this.cached = cached;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.jme3csys.items.image.JMEImage#setImage(java.io.File)
	 */
	@Override
	public void setImage(File imageFile)
	{
		resourceLocation = imageFile;
		super.setImage(imageFile);
	}

}

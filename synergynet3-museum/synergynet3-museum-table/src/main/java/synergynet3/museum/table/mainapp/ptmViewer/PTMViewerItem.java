package synergynet3.museum.table.mainapp.ptmViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import jpview.gui.PTMCanvas;
import jpview.gui.PTMCanvasBufferedImage;
import jpview.io.PTMIO;
import jpview.ptms.PTM;
import jpview.transforms.DirectionalLightOp;
import jpview.transforms.PixelTransformOp;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

/**
 * The Class PTMViewerItem.
 */
public class PTMViewerItem implements PTMEventDispatcher
{

	/** Size for Border. */
	private static final float BORDER_SIZE = 35f;

	/** The border colour. */
	private ColorRGBA borderColour;

	/** The canvas. */
	private PTMCanvasBufferedImage canvas;

	/** The image. */
	private IImage image;

	/** The pixel transform op. */
	private PixelTransformOp pixelTransformOp = new DirectionalLightOp();

	/** The ptm. */
	private PTM ptm = null;

	/** The size limit. */
	private float sizeLimit;

	/** The wrapper frame. */
	private IContainer wrapperFrame;

	/**
	 * Instantiates a new PTM viewer item.
	 *
	 * @param stage
	 *            the stage
	 * @param file
	 *            the file
	 * @param borderColour
	 *            the border colour
	 * @param sizeLimit
	 *            the size limit
	 * @param minScale
	 *            the min scale
	 * @param maxScale
	 *            the max scale
	 */
	public PTMViewerItem(IStage stage, File file, ColorRGBA borderColour, float sizeLimit, float minScale, float maxScale)
	{
		this.borderColour = borderColour;
		this.sizeLimit = sizeLimit;
		// Generate PTMCanvas with file
		try
		{
			ptm = PTMIO.getPTMParser(new FileInputStream(file)).readPTM();
			canvas = (PTMCanvasBufferedImage) PTMCanvas.createPTMCanvas(ptm.getWidth(), ptm.getHeight(), PTMCanvas.BUFFERED_IMAGE);
			pixelTransformOp.transformPixels(canvas.getPixels(), ptm, ptm.getWidth() / 2, ptm.getHeight() / 2);

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		generatePTMViewerItem(stage, canvas, file.getName(), minScale, maxScale);
	}

	/**
	 * As item.
	 *
	 * @return the i item
	 */
	public IItem asItem()
	{
		return wrapperFrame;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.museum.table.mainapp.ptmViewer.PTMEventDispatcher#generateImage
	 * ()
	 */
	@Override
	public void generateImage()
	{
		Texture2D tex = new Texture2D(new AWTLoader().load(canvas.getImage(), false));
		image.setTexture(tex);
	}

	/**
	 * Generate ptm viewer item.
	 *
	 * @param stage
	 *            the stage
	 * @param canvas
	 *            the canvas
	 * @param name
	 *            the name
	 * @param minScale
	 *            the min scale
	 * @param maxScale
	 *            the max scale
	 */
	private void generatePTMViewerItem(IStage stage, PTMCanvas canvas, String name, float minScale, float maxScale)
	{

		try
		{
			image = stage.getContentFactory().create(IImage.class, name + "Canvas", UUID.randomUUID());

			float width = canvas.getDisplayWidth();
			float height = canvas.getDisplayHeight();

			if ((width > sizeLimit) || (height > sizeLimit))
			{
				if (width < height)
				{
					width *= (sizeLimit / height);
					height = sizeLimit;
				}
				else
				{
					height *= (sizeLimit / width);
					width = sizeLimit;
				}
			}
			image.setSize(width, height);

			IRoundedBorder objectBorder = stage.getContentFactory().create(IRoundedBorder.class, name + "Border", UUID.randomUUID());
			objectBorder.setBorderWidth(BORDER_SIZE);
			objectBorder.setSize(width, height);
			objectBorder.setColor(borderColour);

			wrapperFrame = stage.getContentFactory().create(IContainer.class, name + "Wrapper", UUID.randomUUID());

			PTMListener ptmListener = new PTMListener();
			ptmListener.setValues(ptm, pixelTransformOp, canvas, width, height, stage, wrapperFrame, this);
			image.getMultiTouchDispatcher().addListener(ptmListener);

			RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(objectBorder, RotateTranslateScaleBehaviour.class);
			rts.setItemActingOn(wrapperFrame);
			rts.setScaleLimits(minScale, maxScale);

			wrapperFrame.addItem(image);
			wrapperFrame.addItem(objectBorder);

			generateImage();

		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}

	}

}

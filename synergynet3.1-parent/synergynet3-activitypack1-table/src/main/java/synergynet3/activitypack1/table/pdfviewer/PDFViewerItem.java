package synergynet3.activitypack1.table.pdfviewer;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.behaviours.inertia.InertiaBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * The Class PDFViewerItem.
 */
public class PDFViewerItem
{

	/** The converted pages. */
	private static HashMap<String, File> convertedPages = new HashMap<String, File>();

	/** The Constant FONT_LOC. */
	private static final String FONT_LOC = "synergynet3/activitypack1/table/common/arial64_white.fnt";

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PDFViewerItem.class.getName());

	/** The pdf sizes. */
	private static HashMap<String, Vector2f> pdfSizes = new HashMap<String, Vector2f>();

	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = "synergynet3/activitypack1/table/pdfviewer/";

	/** The arrows present. */
	private boolean arrowsPresent = false;

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The current frame. */
	private int currentFrame = 0;

	/** The frames. */
	private ArrayList<ArrayList<IItem>> frames = new ArrayList<ArrayList<IItem>>();

	/** The pdf dimensions. */
	private Vector2f pdfDimensions = new Vector2f();

	/** The scroll down. */
	private IImage scrollDown;

	/** The scroll up. */
	private IImage scrollUp;

	/** The stage. */
	private IStage stage;

	/** The wrapper frame. */
	private IContainer wrapperFrame;

	/**
	 * Instantiates a new PDF viewer item.
	 *
	 * @param stage
	 *            the stage
	 * @param pdfFile
	 *            the pdf file
	 */
	public PDFViewerItem(IStage stage, File pdfFile)
	{
		this.stage = stage;

		try
		{
			generateImages(pdfFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds the to stage.
	 */
	public void addToStage()
	{
		stage.addItem(wrapperFrame);
	}

	/**
	 * Destroy.
	 */
	public void destroy()
	{
		for (int i = 0; i < frames.size(); i++)
		{
			tidyAwayFrameContents(i);
		}
		frames.clear();
		if (wrapperFrame.getParentItem() != null)
		{
			wrapperFrame.getParentItem().removeItem(wrapperFrame);
		}
	}

	/**
	 * Sets the location.
	 *
	 * @param loc
	 *            the new location
	 */
	public void setLocation(Vector2f loc)
	{
		wrapperFrame.setRelativeLocation(loc);
	}

	/**
	 * Adds the frame.
	 *
	 * @return the int
	 */
	private int addFrame()
	{
		if (frames.size() == 1)
		{
			showScrollButtons();
		}
		frames.add(new ArrayList<IItem>());
		return frames.size() - 1;
	}

	/**
	 * Adds the to frame.
	 *
	 * @param item
	 *            the item
	 * @param frame
	 *            the frame
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	private void addToFrame(IItem item, int frame, int x, int y)
	{
		if ((frame >= 0) && (frame < frames.size()))
		{
			frames.get(frame).add(item);
			positionCorrectlyOnFrame(item, x, y);
			if (frame != currentFrame)
			{
				hideItem(item);
			}
		}
	}

	/**
	 * Creates the arrows.
	 */
	private void createArrows()
	{
		try
		{

			String scrollButtonImage = RESOURCE_PATH + "scrollButton.png";

			scrollUp = contentFactory.create(IImage.class, "scrollUp", UUID.randomUUID());
			scrollUp.setImage(scrollButtonImage);
			scrollUp.setSize(120, 490);
			scrollUp.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			scrollUp.setRelativeLocation(new Vector2f((pdfDimensions.getX() / 2) + 70, 0));

			scrollUp.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					scrollForward();
				}
			});

			scrollDown = contentFactory.create(IImage.class, "scrollDown", UUID.randomUUID());
			scrollDown.setImage(scrollButtonImage);
			scrollDown.setSize(120, 490);
			scrollDown.setRelativeLocation(new Vector2f(-(pdfDimensions.getX() / 2) - 70, 0));

			scrollDown.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					scrollBack();
				}
			});

			wrapperFrame.addItem(scrollUp);
			wrapperFrame.addItem(scrollDown);

			arrowsPresent = true;

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}

	/**
	 * Creates the exit button.
	 */
	private void createExitButton()
	{
		try
		{
			IImage exitButton = contentFactory.create(IImage.class, "destroy", UUID.randomUUID());
			exitButton.setImage(RESOURCE_PATH + "destroyButton.png");
			exitButton.setSize(128, 128);
			exitButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			exitButton.setRelativeLocation(new Vector2f(0, -(pdfDimensions.getY() / 2) - 74));
			wrapperFrame.addItem(exitButton);

			exitButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					destroy();
				}
			});
		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}

	/**
	 * Generate image.
	 *
	 * @param imageName
	 *            the image name
	 * @param imageFile
	 *            the image file
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return the i image
	 */
	private IImage generateImage(String imageName, File imageFile, int width, int height)
	{
		try
		{
			IImage image = contentFactory.create(IImage.class, "", UUID.randomUUID());
			image.setImage(imageFile);
			image.setSize(width, height);
			if (!convertedPages.containsKey(imageName))
			{
				convertedPages.put(imageName, imageFile);
			}

			return image;

		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generate images.
	 *
	 * @param file
	 *            the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void generateImages(File file) throws IOException
	{

		contentFactory = stage.getContentFactory();

		// load a pdf from a byte buffer
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);

		int numPgs = pdffile.getNumPages();

		ArrayList<IImage> images = new ArrayList<IImage>();

		for (int i = 0; i < numPgs; i++)
		{

			String pdfName = file.getName();
			String imageName = pdfName + i;

			if (convertedPages.containsKey(imageName))
			{
				images.add(generateImage(imageName, convertedPages.get(imageName), (int) pdfSizes.get(pdfName).getX(), (int) pdfSizes.get(pdfName).getY()));
				pdfDimensions = pdfSizes.get(pdfName);
			}
			else
			{

				// draw the page to an image
				PDFPage page = pdffile.getPage(i);

				// get the width and height for the doc at the default zoom
				Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());

				double pw = page.getWidth() * 2;
				double ph = page.getHeight() * 2;

				// generate the image
				Image img = page.getImage((int) pw, (int) ph, rect, null, true, true);

				if (!pdfSizes.containsKey(pdfName))
				{
					Vector2f dimensions = new Vector2f((int) pw, (int) ph);
					pdfDimensions = dimensions;
					pdfSizes.put(pdfName, dimensions);
				}

				// save it as a file
				BufferedImage bImg = toBufferedImage(img, (int) pw, (int) ph);

				File imageFile = File.createTempFile(imageName, ".png");
				imageFile.deleteOnExit();
				ImageIO.write(bImg, "png", imageFile);

				images.add(generateImage(imageName, imageFile, (int) pw, (int) ph));
			}
		}
		raf.close();
		generatePDFViewer(file.getName(), images);

	}

	/**
	 * Generate pdf label.
	 *
	 * @param name
	 *            the name
	 */
	private void generatePDFLabel(String name)
	{
		try
		{
			IMutableLabel pdfLabel = this.stage.getContentFactory().create(IMutableLabel.class, "pdfLabel", UUID.randomUUID());
			pdfLabel.setFont(FONT_LOC);
			pdfLabel.setText(name);
			pdfLabel.setBoxSize(pdfDimensions.getY(), 50);
			pdfLabel.setFontScale(1.5f);
			pdfLabel.setRelativeLocation(new Vector2f(0, (pdfDimensions.getY() / 2) + 35));
			wrapperFrame.addItem(pdfLabel);
		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}

	/**
	 * Generate pdf viewer.
	 *
	 * @param pdfName
	 *            the pdf name
	 * @param images
	 *            the images
	 */
	private void generatePDFViewer(String pdfName, ArrayList<IImage> images)
	{
		try
		{
			frames.add(new ArrayList<IItem>());

			wrapperFrame = contentFactory.create(IContainer.class, "wrapper", UUID.randomUUID());

			createExitButton();

			generatePDFLabel(pdfName);

			for (int i = 0; i < images.size(); i++)
			{
				if (i > 0)
				{
					addFrame();
				}
				addToFrame(images.get(i), i, 0, 0);
			}

			wrapperFrame.setRelativeScale(0.25f);

			InertiaBehaviour ib = stage.getBehaviourMaker().addBehaviour(wrapperFrame, InertiaBehaviour.class);
			ib.setDeceleration(200f);

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}

	/**
	 * Hide current frame contents.
	 */
	private void hideCurrentFrameContents()
	{
		if (currentFrame < frames.size())
		{
			for (IItem item : frames.get(currentFrame))
			{
				hideItem(item);
			}
		}
	}

	/**
	 * Hide item.
	 *
	 * @param item
	 *            the item
	 */
	private void hideItem(IItem item)
	{
		if (item != null)
		{
			item.setVisible(false);
			item.setInteractionEnabled(false);
		}
	}

	/**
	 * Position correctly on frame.
	 *
	 * @param item
	 *            the item
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	private void positionCorrectlyOnFrame(IItem item, int x, int y)
	{
		float rotation = wrapperFrame.getRelativeRotation();
		Vector2f position = wrapperFrame.getRelativeLocation();
		wrapperFrame.setRelativeRotation(0);
		wrapperFrame.setRelativeLocation(new Vector2f());
		item.setRelativeLocation(new Vector2f(x, y));
		wrapperFrame.addItem(item);
		wrapperFrame.setRelativeRotation(rotation);
		wrapperFrame.setRelativeLocation(position);
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(item, RotateTranslateScaleBehaviour.class);
		rts.setItemActingOn(wrapperFrame);
		rts.setScaleLimits(0.25f, 1.5f);
	}

	/**
	 * Scroll back.
	 */
	private void scrollBack()
	{
		int targetFrame = currentFrame - 1;
		if (targetFrame < 0)
		{
			targetFrame = frames.size() - 1;
		}
		scrollToFrame(targetFrame);
	}

	/**
	 * Scroll forward.
	 */
	private void scrollForward()
	{
		int targetFrame = currentFrame + 1;
		if (targetFrame >= frames.size())
		{
			targetFrame = 0;
		}
		scrollToFrame(targetFrame);
	}

	/**
	 * Scroll to frame.
	 *
	 * @param frame
	 *            the frame
	 */
	private void scrollToFrame(int frame)
	{
		hideCurrentFrameContents();
		currentFrame = frame;
		showCurrentFrameContents();
	}

	/**
	 * Show current frame contents.
	 */
	private void showCurrentFrameContents()
	{
		for (IItem item : frames.get(currentFrame))
		{
			showItem(item);
		}
	}

	/**
	 * Show item.
	 *
	 * @param item
	 *            the item
	 */
	private void showItem(IItem item)
	{
		if (item != null)
		{
			item.setVisible(true);
			item.setInteractionEnabled(true);
		}
	}

	/**
	 * Show scroll buttons.
	 */
	private void showScrollButtons()
	{
		if (!arrowsPresent)
		{
			createArrows();
		}
		showItem(scrollUp);
		showItem(scrollDown);
	}

	/**
	 * Tidy away frame contents.
	 *
	 * @param toRemove
	 *            the to remove
	 */
	private void tidyAwayFrameContents(int toRemove)
	{
		for (IItem item : frames.get(currentFrame))
		{
			wrapperFrame.removeItem(item);
		}
	}

	/**
	 * To buffered image.
	 *
	 * @param image
	 *            the image
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return the buffered image
	 */
	private BufferedImage toBufferedImage(Image image, int width, int height)
	{
		if (image instanceof BufferedImage)
		{
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		try
		{
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(width, height, transparency);
		}
		catch (HeadlessException e)
		{
			System.out.println("The system does not have a screen");
		}

		if (bimage == null)
		{
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(width, height, type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}
}

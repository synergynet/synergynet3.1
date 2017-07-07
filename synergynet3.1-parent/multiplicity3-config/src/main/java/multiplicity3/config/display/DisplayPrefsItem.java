package multiplicity3.config.display;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.PreferencesItem;
import multiplicity3.input.tuio.TUIOPrefsItem;

/**
 * The Class DisplayPrefsItem.
 */
public class DisplayPrefsItem implements PreferencesItem
{

	/** The Constant INPUT_TYPES. */
	public static final String[] INPUT_TYPES =
	{ "Mouse and Keyboard", "TUIO", "Evoluce MIM", "Windows Touch", "Windows Touch (64bit Java)", "Alt Windows Touch (1.8 Java)"};

	/** The Constant DISPLAY_ALPHA_BITS. */
	private static final String DISPLAY_ALPHA_BITS = "DISPLAY_ALPHA_BITS";

	/** The Constant DISPLAY_BITS_PER_PIXEL. */
	private static final String DISPLAY_BITS_PER_PIXEL = "DISPLAY_BITS_PER_PIXEL";

	/** The Constant DISPLAY_DEPTH_BITS. */
	private static final String DISPLAY_DEPTH_BITS = "DISPLAY_DEPTH";

	/** The Constant DISPLAY_FREQ. */
	private static final String DISPLAY_FREQ = "DISPLAY_FREQ";

	/** The Constant DISPLAY_FULLSCREEN. */
	private static final String DISPLAY_FULLSCREEN = "DISPLAY_FULLSCREEN";

	/** The Constant DISPLAY_HEIGHT. */
	private static final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";

	/** The Constant DISPLAY_MIN_AA_SAMPLES. */
	private static final String DISPLAY_MIN_AA_SAMPLES = "DISPLAY_MIN_AA_SAMPLES";

	/** The Constant DISPLAY_REAL_WIDTH. */
	private static final String DISPLAY_REAL_WIDTH = "DISPLAY_REAL_WIDTH";

	/** The Constant DISPLAY_RENDERER. */
	private static final String DISPLAY_RENDERER = "DISPLAY_RENDERER";

	/** The Constant DISPLAY_STENCIL_BITS. */
	private static final String DISPLAY_STENCIL_BITS = "DISPLAY_STENCIL_BITS";

	/** The Constant DISPLAY_WIDTH. */
	private static final String DISPLAY_WIDTH = "DISPLAY_WIDTH";

	/** The prefs. */
	private static Preferences prefs = Preferences.userNodeForPackage(DisplayPrefsItem.class);

	/** The Constant PREFS_INPUT_TYPE. */
	private static final String PREFS_INPUT_TYPE = "INPUT_DEVICE_TYPE";

	/** The tuio prefs. */
	private static TUIOPrefsItem tuioPrefs = new TUIOPrefsItem();

	/**
	 * Instantiates a new display prefs item.
	 */
	public DisplayPrefsItem()
	{
	}

	/**
	 * Gets the alpha bits.
	 *
	 * @return the alpha bits
	 */
	public int getAlphaBits()
	{
		return prefs.getInt(DISPLAY_ALPHA_BITS, 0);
	}

	/**
	 * Gets the bits per pixel.
	 *
	 * @return the bits per pixel
	 */
	public int getBitsPerPixel()
	{
		return prefs.getInt(DISPLAY_BITS_PER_PIXEL, 16);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel()
	{
		DisplayConfigPanel dp2 = new DisplayConfigPanel(this);
		return dp2;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanelName()
	 */
	@Override
	public String getConfigurationPanelName()
	{
		return "Interface";
	}

	/**
	 * Gets the depth bits.
	 *
	 * @return the depth bits
	 */
	public int getDepthBits()
	{
		return prefs.getInt(DISPLAY_DEPTH_BITS, 8);
	}

	/**
	 * Gets the display renderer.
	 *
	 * @return the display renderer
	 */
	public String getDisplayRenderer()
	{
		return prefs.get(DISPLAY_RENDERER, "LWJGL");
	}

	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public int getFrequency()
	{
		return prefs.getInt(DISPLAY_FREQ, -1);
	}

	/**
	 * Gets the full screen.
	 *
	 * @return the full screen
	 */
	public boolean getFullScreen()
	{
		return prefs.getBoolean(DISPLAY_FULLSCREEN, false);
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight()
	{
		return prefs.getInt(DISPLAY_HEIGHT, 768);
	}

	/**
	 * Gets the input type.
	 *
	 * @return the input type
	 */
	public String getInputType()
	{
		return prefs.get(PREFS_INPUT_TYPE, INPUT_TYPES[0]);
	}

	/**
	 * Gets the minimum anti alias samples.
	 *
	 * @return the minimum anti alias samples
	 */
	public int getMinimumAntiAliasSamples()
	{
		return prefs.getInt(DISPLAY_MIN_AA_SAMPLES, 0);
	}

	/**
	 * Gets the real width.
	 *
	 * @return the real width
	 */
	public float getRealWidth()
	{
		return prefs.getFloat(DISPLAY_REAL_WIDTH, 1.5f);
	}

	/**
	 * Gets the stencil bits.
	 *
	 * @return the stencil bits
	 */
	public int getStencilBits()
	{
		return prefs.getInt(DISPLAY_STENCIL_BITS, 8);
	}

	/**
	 * Gets the tuio port.
	 *
	 * @return the tuio port
	 */
	public int getTuioPort()
	{
		return tuioPrefs.getTuioPort();
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth()
	{
		return prefs.getInt(DISPLAY_WIDTH, 1024);
	}

	/**
	 * Sets the alpha bits.
	 *
	 * @param bits
	 *            the new alpha bits
	 */
	public void setAlphaBits(int bits)
	{
		prefs.putInt(DISPLAY_ALPHA_BITS, bits);
	}

	/**
	 * Sets the bits per pixel.
	 *
	 * @param bpp
	 *            the new bits per pixel
	 */
	public void setBitsPerPixel(int bpp)
	{
		prefs.putInt(DISPLAY_BITS_PER_PIXEL, bpp);
	}

	/**
	 * Sets the depth bits.
	 *
	 * @param b
	 *            the new depth bits
	 */
	public void setDepthBits(int b)
	{
		prefs.putInt(DISPLAY_DEPTH_BITS, b);
	}

	/**
	 * Sets the display renderer.
	 *
	 * @param renderer
	 *            the new display renderer
	 */
	public void setDisplayRenderer(String renderer)
	{
		prefs.put(DISPLAY_RENDERER, renderer);
	}

	/**
	 * Sets the frequency.
	 *
	 * @param f
	 *            the new frequency
	 */
	public void setFrequency(int f)
	{
		prefs.putInt(DISPLAY_FREQ, f);
	}

	/**
	 * Sets the full screen.
	 *
	 * @param fs
	 *            the new full screen
	 */
	public void setFullScreen(boolean fs)
	{
		prefs.putBoolean(DISPLAY_FULLSCREEN, fs);
	}

	/**
	 * Sets the height.
	 *
	 * @param h
	 *            the new height
	 */
	public void setHeight(int h)
	{
		prefs.putInt(DISPLAY_HEIGHT, h);
	}

	/**
	 * Sets the input type.
	 *
	 * @param type
	 *            the new input type
	 */
	public void setInputType(String type)
	{
		prefs.put(PREFS_INPUT_TYPE, type);
	}

	/**
	 * Sets the minimum anti alias samples.
	 *
	 * @param samples
	 *            the new minimum anti alias samples
	 */
	public void setMinimumAntiAliasSamples(int samples)
	{
		prefs.putInt(DISPLAY_MIN_AA_SAMPLES, samples);
	}

	/**
	 * Sets the real width.
	 *
	 * @param w
	 *            the new real width
	 */
	public void setRealWidth(float w)
	{
		prefs.putFloat(DISPLAY_REAL_WIDTH, w);
	}

	/**
	 * Sets the stencil bits.
	 *
	 * @param bits
	 *            the new stencil bits
	 */
	public void setStencilBits(int bits)
	{
		prefs.putInt(DISPLAY_STENCIL_BITS, bits);
	}

	/**
	 * Sets the tuio port.
	 *
	 * @param port
	 *            the new tuio port
	 */
	public void setTuioPort(int port)
	{
		tuioPrefs.setTuioPort(port);
	}

	/**
	 * Sets the width.
	 *
	 * @param w
	 *            the new width
	 */
	public void setWidth(int w)
	{
		prefs.putInt(DISPLAY_WIDTH, w);
	}

}

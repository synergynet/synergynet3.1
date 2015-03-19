package multiplicity3.config.display;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.PreferencesItem;
import multiplicity3.input.tuio.TUIOPrefsItem;

public class DisplayPrefsItem implements PreferencesItem {
	
	private static Preferences prefs = Preferences.userNodeForPackage(DisplayPrefsItem.class);
	private static TUIOPrefsItem tuioPrefs = new TUIOPrefsItem();

	private static final String DISPLAY_WIDTH = "DISPLAY_WIDTH";
	private static final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";
	private static final String DISPLAY_FREQ = "DISPLAY_FREQ";
	private static final String DISPLAY_BITS_PER_PIXEL = "DISPLAY_BITS_PER_PIXEL";
	private static final String DISPLAY_FULLSCREEN = "DISPLAY_FULLSCREEN";
	private static final String DISPLAY_MIN_AA_SAMPLES = "DISPLAY_MIN_AA_SAMPLES";
	private static final String DISPLAY_STENCIL_BITS = "DISPLAY_STENCIL_BITS";
	private static final String DISPLAY_ALPHA_BITS = "DISPLAY_ALPHA_BITS";	
	private static final String DISPLAY_DEPTH_BITS = "DISPLAY_DEPTH";
	private static final String DISPLAY_RENDERER = "DISPLAY_RENDERER";
	private static final String DISPLAY_REAL_WIDTH = "DISPLAY_REAL_WIDTH";
	private static final String PREFS_INPUT_TYPE = "INPUT_DEVICE_TYPE";
	
	public static final String[] INPUT_TYPES = {"Mouse and Keyboard", "TUIO", "Evoluce MIM", "Windows Touch", "Windows Touch (64bit Java)"};
	
	public DisplayPrefsItem() {}
	
	@Override
	public JPanel getConfigurationPanel() {
		DisplayConfigPanel dp2 = new DisplayConfigPanel(this);
		return dp2;
	}

	@Override
	public String getConfigurationPanelName() {
		return "Interface";
	}
	
	public void setRealWidth(float w) {
		prefs.putFloat(DISPLAY_REAL_WIDTH, w);
	}

	public float getRealWidth() {
		return prefs.getFloat(DISPLAY_REAL_WIDTH, 1.5f);
	}
	
	public void setWidth(int w) {
		prefs.putInt(DISPLAY_WIDTH, w);
	}

	public int getWidth() {
		return prefs.getInt(DISPLAY_WIDTH, 1024);
	}

	public int getHeight() {
		return prefs.getInt(DISPLAY_HEIGHT, 768);
	}

	public void setHeight(int h) {
		prefs.putInt(DISPLAY_HEIGHT, h);
	}

	public int getDepthBits() {
		return prefs.getInt(DISPLAY_DEPTH_BITS, 8);
	}

	public void setDepthBits(int b) {
		prefs.putInt(DISPLAY_DEPTH_BITS, b);
	}
	
	public void setBitsPerPixel(int bpp) {
		prefs.putInt(DISPLAY_BITS_PER_PIXEL, bpp);
	}
	
	public int getBitsPerPixel() {
		return prefs.getInt(DISPLAY_BITS_PER_PIXEL, 16);
	}
	
	public int getStencilBits() {
		return prefs.getInt(DISPLAY_STENCIL_BITS, 8);
	}
	
	public void setStencilBits(int bits) {
		prefs.putInt(DISPLAY_STENCIL_BITS, bits);
	}

	public int getAlphaBits() {
		return prefs.getInt(DISPLAY_ALPHA_BITS, 0);
	}
	
	public void setAlphaBits(int bits) {
		prefs.putInt(DISPLAY_ALPHA_BITS, bits);
	}
	
	public int getFrequency() {
		return prefs.getInt(DISPLAY_FREQ, -1);
	}

	public void setFrequency(int f) {
		prefs.putInt(DISPLAY_FREQ, f);
	}

	public boolean getFullScreen() {
		return prefs.getBoolean(DISPLAY_FULLSCREEN, false);
	}

	public void setFullScreen(boolean fs) {
		prefs.putBoolean(DISPLAY_FULLSCREEN, fs);
	}

	public String getDisplayRenderer() {
		return prefs.get(DISPLAY_RENDERER, "LWJGL");
	}
	
	public int getMinimumAntiAliasSamples() {
		return prefs.getInt(DISPLAY_MIN_AA_SAMPLES, 0);
	}
	
	public void setMinimumAntiAliasSamples(int samples) {
		prefs.putInt(DISPLAY_MIN_AA_SAMPLES, samples);
	}

	public void setDisplayRenderer(String renderer) {
		prefs.put(DISPLAY_RENDERER, renderer);
	}
	
	public void setInputType(String type) {
		prefs.put(PREFS_INPUT_TYPE, type);
	}

	public String getInputType() {			
		return prefs.get(PREFS_INPUT_TYPE, INPUT_TYPES[0]);
	}
	
	public void setTuioPort(int port) {
		tuioPrefs.setTuioPort(port);
	}

	public int getTuioPort() {			
		return tuioPrefs.getTuioPort();
	}
	
}

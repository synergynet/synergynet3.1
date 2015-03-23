package multiplicity3.jme3csys.items.mutablelabel;

import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

/**
 * The Class JMEMutableLabel.
 */
@ImplementsContentItem(target = IMutableLabel.class)
public class JMEMutableLabel extends JMEItem implements IMutableLabel,
		IInitable {

	/** The asset manager. */
	private AssetManager assetManager;

	/** The bounding box. */
	private Rectangle boundingBox;

	/** The current text. */
	private String currentText = "text";

	/** The fnt. */
	private BitmapFont fnt;

	/** The font scale. */
	private float fontScale = 1;

	/** The text. */
	private String text = "";

	/** The txt. */
	private BitmapText txt;

	/**
	 * Instantiates a new JME mutable label.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public JMEMutableLabel(String name, UUID uuid) {
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.mutablelabel.IMutableLabel#appendChar(char)
	 */
	@Override
	public void appendChar(char theChar) {
		text = text + theChar;
		setText(text);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial() {
		return txt;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.mutablelabel.IMutableLabel#getText()
	 */
	@Override
	public String getText() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.mutablelabel.IMutableLabel#getTextSize()
	 */
	@Override
	public Vector2f getTextSize() {
		return new Vector2f(fnt.getLineWidth(currentText), txt.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.IInitable#initializeGeometry(com.jme3.asset
	 * .AssetManager)
	 */
	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
		fnt = FontCache.get().getFont("Interface/Fonts/Default.fnt",
				assetManager);
		txt = new BitmapText(fnt, false);
		doUpdate();
		attachChild(txt);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.mutablelabel.IMutableLabel#isBoxSizeSet()
	 */
	@Override
	public boolean isBoxSizeSet() {
		return boundingBox != null;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.mutablelabel.IMutableLabel#removeChar()
	 */
	@Override
	public void removeChar() {
		if (text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
		setText(text);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.mutablelabel.IMutableLabel#setBoxSize(float,
	 * float)
	 */
	@Override
	public void setBoxSize(float width, float height) {
		this.boundingBox = new Rectangle(0, 0, width, height);
		doUpdate();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.mutablelabel.IMutableLabel#setFont(java.lang
	 * .String)
	 */
	@Override
	public void setFont(String resourcePath) {
		fnt = FontCache.get().getFont(resourcePath, assetManager);

		detachChild(txt);

		for (Spatial c : txt.getChildren()) {
			ItemMap.unregister(c, this);
		}
		txt = new BitmapText(fnt, false);
		attachChild(txt);
		doUpdate();
		ItemMap.register(txt, this);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.mutablelabel.IMutableLabel#setFontScale(float)
	 */
	@Override
	public void setFontScale(float scale) {
		this.fontScale = scale;
		doUpdate();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.mutablelabel.IMutableLabel#setText(java.lang
	 * .String)
	 */
	@Override
	public void setText(String text) {
		this.currentText = text;
		this.text = text;
		this.setName(text);
		this.setItemName(text);
		doUpdate();
	}

	/**
	 * Do update.
	 */
	private void doUpdate() {

		for (Spatial c : txt.getChildren()) {
			ItemMap.unregister(c, this);
		}

		if (boundingBox != null) {
			txt.setBox(boundingBox);
			txt.setAlignment(Align.Center);
			txt.setVerticalAlignment(VAlign.Center);
			txt.setLineWrapMode(LineWrapMode.Word);
		}
		txt.setSize(32 * fontScale);
		txt.setText(currentText);

		for (Spatial c : txt.getChildren()) {
			ItemMap.register(c, this);
		}

		if (boundingBox != null) {
			txt.setLocalTranslation(-txt.getLineWidth() / 2f,
					txt.getHeight() / 2f, 0);
		} else {
			float lineWidth = fnt.getLineWidth(currentText);
			float textHeight = txt.getLineHeight();
			txt.setLocalTranslation(-lineWidth / 2f, textHeight / 2f, 0);
		}

		updateGeometricState();
	}
}

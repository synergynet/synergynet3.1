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

@ImplementsContentItem(target = IMutableLabel.class)
public class JMEMutableLabel extends JMEItem implements IMutableLabel, IInitable {
	private BitmapFont fnt;
	private BitmapText txt;
	private String currentText = "text";
	private AssetManager assetManager;
	private Rectangle boundingBox;
	private float fontScale = 1;
	
	public JMEMutableLabel(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
		fnt = FontCache.get().getFont("Interface/Fonts/Default.fnt", assetManager);
        txt = new BitmapText(fnt, false);
        doUpdate();
        attachChild(txt);
	}
	
	@Override
	public Vector2f getTextSize() {
		return new Vector2f(fnt.getLineWidth(currentText), txt.getHeight());
	}
	
	
	@Override
	public Spatial getManipulableSpatial() {
		return txt;
	}

	@Override
	public void setText(String text) {
		this.currentText = text;
		this.text = text;
		this.setName(text);
		this.setItemName(text);
		doUpdate();
	}


	@Override
	public void setFont(String resourcePath) {
		fnt = FontCache.get().getFont(resourcePath, assetManager);
		
		detachChild(txt);
		
		for(Spatial c : txt.getChildren()) {
        	ItemMap.unregister(c, this);
        }
		txt = new BitmapText(fnt, false);
		attachChild(txt);
		doUpdate();        
        ItemMap.register(txt, this);
	}
	
	private void doUpdate() {
		
		for(Spatial c : txt.getChildren()) {
        	ItemMap.unregister(c, this);
        }
		
		if(boundingBox != null) {
			txt.setBox(boundingBox);
			txt.setAlignment(Align.Center);
			txt.setVerticalAlignment(VAlign.Center);
			txt.setLineWrapMode(LineWrapMode.Word);
		}
		txt.setSize(32 * fontScale);
        txt.setText(currentText);
        
        for(Spatial c : txt.getChildren()) {
        	ItemMap.register(c, this);
        }
        
        if(boundingBox != null) {
        	txt.setLocalTranslation(-txt.getLineWidth()/2f, txt.getHeight()/2f, 0);
        }else{
        	float lineWidth = fnt.getLineWidth(currentText);
        	float textHeight = txt.getLineHeight();
        	txt.setLocalTranslation(-lineWidth/2f, textHeight/2f, 0);
        }
        
        updateGeometricState();
	}

	@Override
	public void setBoxSize(float width, float height) {
		this.boundingBox = new Rectangle(0, 0, width, height);
		doUpdate();
	}


	@Override
	public void setFontScale(float scale) {
		this.fontScale = scale;
		doUpdate();
	}

	@Override
	public boolean isBoxSizeSet() {
		return boundingBox != null;
	}

	private String text = "";
	
	@Override
	public void removeChar() {
		if(text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
		setText(text);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void appendChar(char theChar) {
		text = text + theChar;
		setText(text);
	}
}

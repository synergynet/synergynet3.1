package synergynet3.additionalitems.interfaces;

import java.util.logging.Logger;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;

public interface IScrollContainer extends IItem {

	public void setDimensions(IStage stage, Logger log, int width, int height);
	public void setFrameColour(ColorRGBA studentColour);
	public IColourRectangle getBackground();
	public void addToAllFrames(IItem userIcon, int x, int y);
	public int getWidth();
	public int getHeight();
	public void addToFrame(IItem audioRecorder, int frame, int x, int y);
	public void setArrowHeightOverride(float newHeight);
	public int addFrame();
	public void setVisibility(final Boolean isVisible);
	public void hideScrollButtons();
	public void setArrowYOverride(float newY);
	public void addListenerToArrows(MultiTouchEventAdapter multiTouchEventAdapter);
	public void showCurrentFrameContents();
	public void scrollToFrame(int frame);
	public int getCurrentFrame();
	public void removeFrame(int toRemove);
	public IRoundedBorder getBorder();
	public void setArrowWidthOverride(float width);
	public void setActive(boolean active);
	
}

package synergynet3.additionalitems.jme;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IScrollContainer;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

@ImplementsContentItem(target = IScrollContainer.class)
public class ScrollContainer extends JMEContainer implements IScrollContainer, IInitable {

	protected int width = 512;
	protected int height = 300;
	
	private static final String SCROLL_BUTTON_IMAGE  = "synergynet3/additionalitems/scrollButton.png";
	protected IContentFactory contentFactory;
	
	private ArrayList<ArrayList<IItem>> frames = new ArrayList<ArrayList<IItem>>();
	private int currentFrame = 0;
	
	private IColourRectangle background;
	protected IStage stage;
	protected Logger log;
	protected ICachableImage scrollUp;
	protected ICachableImage scrollDown;
	private IRoundedBorder frameBorder;
	protected boolean arrowsPresent = false;	
	
	private RotateTranslateScaleBehaviour rts;
	private RotateTranslateScaleBehaviour rtsbg;	
	private boolean scalable = false;
	
	private float arrowHeight = 0;
	private float arrowWidth = 60;
	private float arrowY = 0;

	private String owner = "";
	private float borderWidth = 15f;
	
	public ScrollContainer(String name, UUID uuid) {
		super(name, uuid);		
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setVisibility(final Boolean isVisible) {
		
		if (!isVisible){
			new PerformActionOnAllDescendents(this, false, false){
				@Override
				protected void actionOnDescendent(IItem child){	
					child.setVisible(isVisible);
				}
			};	
		}else{
			setVisible(isVisible);
			for (int i = 0; i < frames.size(); i++){
				scrollForward();
			}
		}
	}
	
	public void setBackgroundColour(ColorRGBA colour){
		background.setSolidBackgroundColour(colour);
	}
	
	public void setFrameColour(ColorRGBA colour){
		frameBorder.setColor(colour);
	}
	
	public void setDimensions(IStage stage, Logger log, int width, int height){
		
		this.width = width;
		this.height = height;
		
		try{
			contentFactory = stage.getContentFactory();
			
			frames.add(new ArrayList<IItem>());
			
			background = contentFactory.create(IColourRectangle.class, "bg", UUID.randomUUID());
			background.setSolidBackgroundColour(ColorRGBA.Black);
			background.setSize(width, height);			
					
			frameBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());		
			frameBorder.setBorderWidth(borderWidth );
			frameBorder.setSize(width, height);
			frameBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));
			
			arrowHeight = height - frameBorder.getBorderWidth()*2;
			arrowY = 0;
			
			addItem(background);
			addItem(frameBorder);	
			
			this.getZOrderManager().setAutoBringToTop(false);	
			
			rts = stage.getBehaviourMaker().addBehaviour(frameBorder, RotateTranslateScaleBehaviour.class);		
			rts.setItemActingOn(this);
			rtsbg = stage.getBehaviourMaker().addBehaviour(background, RotateTranslateScaleBehaviour.class);		
			rtsbg.setItemActingOn(this);
			applyScaleability();	
					
		}catch(ContentTypeNotBoundException e){
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e );
		}
	}
	
	public void setScalable(boolean scalable){
		this.scalable = scalable;
		applyScaleability();
	}
	
	private void applyScaleability(){
		if (rts != null)rts.setScaleEnabled(scalable);
		if (rtsbg != null)rtsbg.setScaleEnabled(scalable);
	}
	
	public void setActive(boolean active){
		if (rts != null)rts.setActive(active);
		if (rtsbg != null)rtsbg.setActive(active);
	}
	
	public void setArrowWidthOverride(float newWidth){
		arrowWidth = newWidth;
	}
	
	public void setArrowHeightOverride(float newHeight){
		arrowHeight = newHeight;
	}
	
	public void setArrowYOverride(float newY){
		arrowY = newY;
	}
	
	public void addListenerToArrows(MultiTouchEventAdapter mTEA){
		if (scrollUp != null){
			scrollUp.getMultiTouchDispatcher().addListener(mTEA);
		}
		if (scrollDown != null){
			scrollDown.getMultiTouchDispatcher().addListener(mTEA);
		}
	}
	
	protected void createArrows(){
		try{
			scrollUp = contentFactory.create(ICachableImage.class, "scrollUp", UUID.randomUUID());
			scrollUp.setImage(SCROLL_BUTTON_IMAGE);				
			scrollUp.setSize(arrowWidth, arrowHeight);	
			scrollUp.setRelativeRotation(FastMath.DEG_TO_RAD*180);
			scrollUp.setRelativeLocation(new Vector2f((width/2)-((scrollUp.getWidth()/2+frameBorder.getBorderWidth())), arrowY));
			
			scrollUp.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {					
					scrollForward();
				}
			});
					
			scrollDown = contentFactory.create(ICachableImage.class, "scrollDown", UUID.randomUUID());
			scrollDown.setImage(SCROLL_BUTTON_IMAGE);				
			scrollDown.setSize(arrowWidth, arrowHeight);	
			scrollDown.setRelativeLocation(new Vector2f(-((width/2)-((scrollDown.getWidth()/2+frameBorder.getBorderWidth()))), arrowY));
			
			scrollDown.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {
					scrollBack();
				}
			});
			
			addItem(scrollUp);	
			addItem(scrollDown);
			
			arrowsPresent = true;
			
		}catch(ContentTypeNotBoundException e){
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e );
		}
	}
	
	public int addFrame(){
		if (frames.size() == 1){
			showScrollButtons();
		}
		frames.add(new ArrayList<IItem>());
		return frames.size()-1;
	}
	
	public void removeFrame(int toRemove){
		if (toRemove >= 0){
			tidyAwayFrameContents(toRemove);
			if (frames.size() == 1){
				frames.get(0).clear();
			}else if(frames.size() > 1){
				frames.remove(toRemove);
				if (frames.size() == 1){
					hideScrollButtons();
				}
				if (currentFrame == toRemove){
					scrollBack();
				}
			}
		}
	}
	
	private void showScrollButtons() {	
		if (!arrowsPresent)createArrows();
		showItem(scrollUp);
		showItem(scrollDown);
	}
	
	public void hideScrollButtons() {
		hideItem(scrollUp);
		hideItem(scrollDown);	
	}
	
	
	public void addToAllFrames(IItem item, int x, int y){
			addItem(item);
			positionCorrectlyOnFrame(item, x, y);
	}

	public void addToFrame(IItem item, int frame, int x, int y){
		if ((frame >= 0) && (frame < frames.size())){
			frames.get(frame).add(item);
			positionCorrectlyOnFrame(item, x, y);
			if (frame != currentFrame){
				hideItem(item);
			}
		}
	}
	
	@Override
	public void removeItem(IItem item){
		for (ArrayList<IItem> items: frames){
			for (IItem isItem: items){
				if (isItem.equals(item)){
					super.removeItem(item);
				}
				items.remove(isItem);
				break;
			}
		}
	}
	
	private void positionCorrectlyOnFrame(IItem item, int x, int y){
		float rotation = getRelativeRotation();
		Vector2f position = getRelativeLocation();
		float scale = getRelativeScale();
		setRelativeRotation(0);
		setRelativeLocation(new Vector2f());
		setRelativeScale(1);
		item.setRelativeLocation(new Vector2f(x, y));
		addItem(item);
		setRelativeRotation(rotation);
		setRelativeLocation(position);
		setRelativeScale(scale);
	}

	private void tidyAwayFrameContents(int toRemove) {
		for (IItem item: frames.get(toRemove)){
			super.removeItem(item);
		}		
	}
	
	private void scrollForward(){
		int targetFrame = currentFrame + 1;
		if (targetFrame >= frames.size()){targetFrame = 0;}
		scrollToFrame(targetFrame);
	}
	
	private void scrollBack(){
		int targetFrame = currentFrame - 1;
		if (targetFrame < 0){targetFrame = frames.size()-1;}
		scrollToFrame(targetFrame);
	}
	
	public void scrollToFrame(int frame){		
		hideCurrentFrameContents();
		currentFrame = frame;
		showCurrentFrameContents();		
	}

	public void showCurrentFrameContents() {
		for (IItem item : frames.get(currentFrame)){
			showItem(item);
		}		
	}

	private void hideCurrentFrameContents() {
		if (currentFrame < frames.size()){
			for (IItem item : frames.get(currentFrame)){
				hideItem(item);
			}		
		}
	}
	
	private void hideItem(IItem item){
		if (item != null){
			item.setVisible(false);
			item.setInteractionEnabled(false);
		}
	}
	
	private void showItem(IItem item){
		if (item != null){
			item.setVisible(true);
			item.setInteractionEnabled(true);
		}
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public IRoundedBorder getBorder() {
		return frameBorder;		
	}

	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public int getNumberOfFrames(){
		return frames.size();
	}

	public IColourRectangle getBackground() {
		return background;
	}
}

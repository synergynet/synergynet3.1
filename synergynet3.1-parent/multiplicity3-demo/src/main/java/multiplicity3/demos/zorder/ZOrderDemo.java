package multiplicity3.demos.zorder;

import java.util.UUID;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class ZOrderDemo implements IMultiplicityApp {
	
	public static final String FONT_ARIAL_32_WHITE  = "multiplicity3/demos/fonts/arial64_white.fnt";

	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		ZOrderDemo app = new ZOrderDemo();
		client.setCurrentApp(app);
	}

	private IContentFactory factory;
	private Gradient expressionBackgroundGradient;
	private IStage stage;

	@Override
	public String getFriendlyAppName() {
		return "ZOrder Demo";
	}

	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		stage = MultiplicityEnvironment.get().getLocalStages().get(0);		
		factory = stage.getContentFactory();
		
		setupExpressionBackgroundGradient(); 
		
		try {			
			IContainer rectanglesContainer = getContainerWithRectangles();	
			stage.addItem(rectanglesContainer);			
						
			IContainer abc = createExpressionVisualRepresentation("abc");
			stage.addItem(abc);

			IContainer def = createExpressionVisualRepresentation("def");
			stage.addItem(def);			
			
			stage.getZOrderManager().setAutoBringToTop(true);
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}

	}
	
	private IContainer getContainerWithRectangles() throws ContentTypeNotBoundException {
		IContainer container = stage.getContentFactory().create(IContainer.class, "RectangleContainer", UUID.randomUUID());
		
		IColourRectangle rectangleGreen = getGreenRectangle();
		rectangleGreen.setSize(300, 100);
		rectangleGreen.setRelativeLocation(new Vector2f(0, 0));
		
		IColourRectangle rectangleRed = getRedRectangle();
		rectangleRed.setSize(300, 100);
		rectangleRed.setRelativeLocation(new Vector2f(50, 50));
		
		container.addItem(rectangleGreen);
		container.addItem(rectangleRed);
		
		container.getZOrderManager().setAutoBringToTop(true);
		container.getZOrderManager().setBringToTopPropagatesUp(false);
		
		return container;
	}

	private IColourRectangle getRedRectangle() throws ContentTypeNotBoundException {		
		ColorRGBA topRed = new ColorRGBA(0.8f, 0.3f, 0.3f, 1f);
		ColorRGBA bottomRed = new ColorRGBA(0.6f, 0.3f, 0.3f, 1f);		
		return getRectangleWithGradient(topRed, bottomRed, "red");
	}

	private IColourRectangle getGreenRectangle() throws ContentTypeNotBoundException {		
		ColorRGBA topGreen = new ColorRGBA(0.3f, 0.8f, 0.3f, 1f);
		ColorRGBA bottomGreen = new ColorRGBA(0.2f, 0.6f, 0.2f, 1f);		
		return getRectangleWithGradient(topGreen, bottomGreen, "green");
	}
	
	public IColourRectangle getRectangleWithGradient(ColorRGBA top, ColorRGBA bottom, String name) throws ContentTypeNotBoundException {
		IColourRectangle rectangle = factory.create(IColourRectangle.class, name, UUID.randomUUID());
		Gradient greenGradient = new Gradient(top, bottom, GradientDirection.VERTICAL);
		rectangle.setGradientBackground(greenGradient);		
		return rectangle;
	}

	@Override
	public void shouldStop() {}
	
	
	@Override
	public void onDestroy() {}

	
	private void setupExpressionBackgroundGradient() {
		ColorRGBA gradientTop = new ColorRGBA(0.6f, 0.6f, 0.6f, 0.5f);
		ColorRGBA gradientBottom = new ColorRGBA(0.3f, 0.3f, 0.3f, 0.5f);
		expressionBackgroundGradient = new Gradient(gradientTop, gradientBottom, GradientDirection.VERTICAL);
	}
	
	
	
	public IContainer createExpressionVisualRepresentation(String expression) throws ContentTypeNotBoundException {
		
		UUID idToUse = UUID.randomUUID();
		
		final IContainer container = factory.create(IContainer.class, "container_" + expression, idToUse);
		IColourRectangle background = factory.create(IColourRectangle.class, "bg_" + expression, idToUse);
		
		background.setGradientBackground(expressionBackgroundGradient);
		container.addItem(background);
		
		IMutableLabel expl = factory.create(IMutableLabel.class, expression, idToUse);
		expl.setFont(FONT_ARIAL_32_WHITE);
		expl.setText(expression);
				
		container.addItem(expl);

		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(expl, RotateTranslateScaleBehaviour.class);
		rts.setScaleEnabled(false);
		rts.setItemActingOn(container);
		
		background.getMultiTouchDispatcher().addListener(rts);
		
		Vector2f textSize = expl.getTextSize();
		Vector2f bgSize = new Vector2f(textSize.x + 20f, textSize.y+5f);
		background.setSize(bgSize);
		
		IRoundedBorder border = factory.create(IRoundedBorder.class, "border_" + expression, idToUse);		
		border.setBorderWidth(2f);
		border.setSize(bgSize);
		
		
		border.setColor(new ColorRGBA(1, 1, 1, 1f));
		
		container.addItem(border);
		
		RotateTranslateScaleBehaviour rtsb = stage.getBehaviourMaker().addBehaviour(border, RotateTranslateScaleBehaviour.class);
		rtsb.setScaleEnabled(false);
		rtsb.setItemActingOn(container);
		
		stage.getDragAndDropSystem().registerDragSource(border);
		stage.getDragAndDropSystem().registerDragSource(background);
		stage.getDragAndDropSystem().registerDragSource(expl);		
		
		background.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				container.getMultiTouchDispatcher().cursorPressed(event);
			}

			@Override
			public void cursorReleased(MultiTouchCursorEvent event) {
				container.getMultiTouchDispatcher().cursorReleased(event);
			}
		});
		
		expl.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				container.getMultiTouchDispatcher().cursorPressed(event);
			}

			@Override
			public void cursorReleased(MultiTouchCursorEvent event) {
				container.getMultiTouchDispatcher().cursorReleased(event);
			}			
		});
		
		return container;
	}
	
}
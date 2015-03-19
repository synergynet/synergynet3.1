package multiplicity3.demos.contentdemo;

import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

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
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity3.csys.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;

public class ContentDemo implements IMultiplicityApp {
	
	private static final Logger log = Logger.getLogger(ContentDemo.class.getName());

	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		ContentDemo app = new ContentDemo();
		client.setCurrentApp(app);
	}

	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		final IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0); // get first local stage		
		IContentFactory cf = stage.getContentFactory();
		
		stage.getZOrderManager().setAutoBringToTop(true); // it is true by default, but just showing its existence!
		
		// solid and gradient rectangles
		try {
			IContainer keyboardWithFrame = cf.create(IContainer.class, "keywf", UUID.randomUUID());			
			
			IKeyboard kb = cf.create(IKeyboard.class, "kb", UUID.randomUUID());
			kb.setKeyboardDefinition(new SimpleAlphaKeyboardDefinition());
			kb.setKeyboardRenderer(new SimpleAlphaKeyboardRenderer(kb.getKeyboardDefinition()));
			keyboardWithFrame.addItem(kb);
			kb.reDraw();
			KeyboardBehaviour kbb = stage.getBehaviourMaker().addBehaviour(kb, KeyboardBehaviour.class);
			kbb.addListener(new IMultiTouchKeyboardListener() {
				
				@Override
				public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
						boolean ctlDown) {
					log.fine("Key pressed: " + k.getKeyStringRepresentation());
				}
				
				@Override
				public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
						boolean ctlDown) {
					// TODO Auto-generated method stub
					
				}
			});			
			
			// support different sizes...
			IRoundedBorder border = cf.create(IRoundedBorder.class, "border", UUID.randomUUID());
			keyboardWithFrame.addItem(border);
			border.setSize((float)kb.getKeyboardDefinition().getBounds().getWidth(), (float)kb.getKeyboardDefinition().getBounds().getHeight());
			border.setColor(new ColorRGBA(1, 1, 1, 0.5f));
			RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(border, RotateTranslateScaleBehaviour.class);
			rts.setItemActingOn(keyboardWithFrame);
			keyboardWithFrame.setRelativeLocation(new Vector2f(50, -200));
			stage.addItem(keyboardWithFrame);
			
			
			IColourRectangle rect = cf.create(IColourRectangle.class, "solidbox", UUID.randomUUID());
			rect.setRelativeLocation(new Vector2f(0, 0));
			rect.setSolidBackgroundColour(ColorRGBA.Blue);
			rect.setRelativeLocation(new Vector2f(200, 300));
			stage.getBehaviourMaker().addBehaviour(rect, RotateTranslateScaleBehaviour.class);
			stage.addItem(rect);
			rect.setSize(200, 100);
			
			IColourRectangle rect2 = cf.create(IColourRectangle.class, "gradientbox", UUID.randomUUID());
			rect2.setRelativeLocation(new Vector2f(400, 190));
			Gradient g = new Gradient(ColorRGBA.White, ColorRGBA.Gray, GradientDirection.VERTICAL);
			rect2.setGradientBackground(g);
			stage.getBehaviourMaker().addBehaviour(rect2, RotateTranslateScaleBehaviour.class);
			stage.addItem(rect2);
			rect2.setSize(200, 100);
			
			ILine line = cf.create(ILine.class, "line", UUID.randomUUID());
			line.setSourceItem(rect);
			line.setDestinationItem(rect2);
			line.setLineWidth(3f);
			stage.addItem(line);
			
			IMutableLabel label = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label.setText("multiplicity");
			label.setRelativeLocation(new Vector2f(-200, 250));
			label.setFontScale(2f);
			stage.getBehaviourMaker().addBehaviour(label, RotateTranslateScaleBehaviour.class);
			stage.addItem(label);
			
			IMutableLabel longTextLabel = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			longTextLabel.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			longTextLabel.setText("multiplicity3\n...is a platform for multi-touch interactive goodness");
			stage.getBehaviourMaker().addBehaviour(longTextLabel, RotateTranslateScaleBehaviour.class);
			stage.addItem(longTextLabel);
			
			IMutableLabel longTextLabel2 = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			longTextLabel2.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			longTextLabel2.setText("word wrapping is not about keeping characters warm...");
			longTextLabel2.setFontScale(0.5f);
			longTextLabel2.setBoxSize(200, 100);
			longTextLabel2.setRelativeLocation(new Vector2f(-100, 100));
			stage.getBehaviourMaker().addBehaviour(longTextLabel2, RotateTranslateScaleBehaviour.class);
			stage.addItem(longTextLabel2);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shouldStop() {}
	

	@Override
	public void onDestroy() {}


	@Override
	public String getFriendlyAppName() {
		return "Content Demo";
	}


}

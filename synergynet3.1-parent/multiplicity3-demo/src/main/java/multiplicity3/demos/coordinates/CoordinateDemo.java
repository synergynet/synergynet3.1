package multiplicity3.demos.coordinates;

import java.util.UUID;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.events.ItemListenerAdapter;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;

import com.jme3.math.Vector2f;

public class CoordinateDemo implements IMultiplicityApp {

	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		CoordinateDemo app = new CoordinateDemo();
		client.setCurrentApp(app);
	}

	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		final IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0); // get first local stage
		IContentFactory cf = stage.getContentFactory();

		try {
			IMutableLabel label_0_0 = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_0_0.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_0_0.setText("(0,0) world");
			label_0_0.setRelativeScale(0.5f);
			stage.addItem(label_0_0);
			label_0_0.setWorldLocation(new Vector2f(0, 0));	
			
			IMutableLabel label_0_0l = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_0_0l.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_0_0l.setText("(0,0) relative to stage");
			label_0_0l.setRelativeScale(0.5f);
			label_0_0l.setRelativeLocation(new Vector2f(0, 0));			
			stage.addItem(label_0_0l);
		
			IMutableLabel label_100_100 = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_100_100.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_100_100.setText("(100,100) world");	
			label_100_100.setRelativeScale(0.5f);
			stage.addItem(label_100_100);
			label_100_100.setWorldLocation(new Vector2f(100, 100));
		
			IMutableLabel label_100_100s = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_100_100s.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_100_100s.setRelativeScale(0.5f);
			label_100_100s.setText("(200,200) screen");
			stage.addItem(label_100_100s);
			label_100_100s.setWorldLocation(stage.screenToWorld(new Vector2f(200, 200)));						
			
			IMutableLabel label_1024_768s = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_1024_768s.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_1024_768s.setText("(900,700) screen");
			label_1024_768s.setRelativeScale(0.5f);
			stage.addItem(label_1024_768s);
			label_1024_768s.setWorldLocation(stage.screenToWorld(new Vector2f(900, 700)));

			IMutableLabel label_1_1_t = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_1_1_t.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_1_1_t.setText("(0.5f,0.95f) table");
			label_1_1_t.setRelativeScale(0.5f);
			stage.addItem(label_1_1_t);
			label_1_1_t.setWorldLocation(stage.tableToWorld(new Vector2f(0.5f,0.95f)));			

			IMutableLabel label_corner_w = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_corner_w.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_corner_w.setText("(400, 350) world");
			label_corner_w.setRelativeScale(0.5f);
			stage.addItem(label_corner_w);
			label_corner_w.setWorldLocation(new Vector2f(400, 350));

					
			final IMutableLabel cursorLabel = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			cursorLabel.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");						
			cursorLabel.setRelativeScale(0.5f);
			cursorLabel.setText("Move me!");
			stage.addItem(cursorLabel);
			cursorLabel.setRelativeLocation(new Vector2f(50, 50));			
			stage.getBehaviourMaker().addBehaviour(cursorLabel, RotateTranslateScaleBehaviour.class).setScaleEnabled(false);
			
			cursorLabel.addItemListener(new ItemListenerAdapter() {
				@Override
				public void itemMoved(IItem item) {
					cursorLabel.setText("World: " + item.getWorldLocation() + "\nRelative: " + item.getRelativeLocation());
				}
			});
			
			//stage.getZOrderManager().setAutoBringToTop(false);
			
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
		return "Coordinate Demo";
	}
}

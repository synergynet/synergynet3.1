package synergynet3.activitypack1.table.pdfviewer;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class PDFViewerApp implements IMultiplicityApp {

	private static final String FONT_LOC= "synergynet3/activitypack1/table/common/arial64_white.fnt";
	private static final Logger log = Logger.getLogger(PDFViewerApp.class.getName());
	
	private static String fileDir = null;

	private IStage stage;
	
	private float yIncrement = 0;
	private float yCurrent = 0;

	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		stage.getZOrderManager().setAutoBringToTop(false);
		try {
			loadDefaultContent();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	private void loadDefaultContent() throws IOException, ContentTypeNotBoundException {

		File directory = new File(fileDir);		
		if (directory.isDirectory()){
			File[] files = directory.listFiles();
			
			yIncrement = stage.getDisplayHeight()/(files.length + 1);
			yCurrent = yIncrement;
			
			for (File file: files){					
				try {
					createPDFgenerationButton(file.getName(), file);

					PDFViewerItem pdfItem = new PDFViewerItem(stage, file);		
					pdfItem.destroy();
				}catch (Exception e) {
					log.log(Level.SEVERE, "Exception: " + e );
				}
			}
		}
	}	
	
	private void createPDFgenerationButton(final String pdfName, final File file){
		try {
			
			final IMutableLabel generatePDFbutton = this.stage.getContentFactory().create(IMutableLabel.class, "pdfLabel", UUID.randomUUID());
			generatePDFbutton.setFont(FONT_LOC);
			generatePDFbutton.setText(pdfName);
			generatePDFbutton.setBoxSize((stage.getDisplayWidth()/3)*2, 50);
			generatePDFbutton.setFontScale(0.75f);	
			generatePDFbutton.setRelativeLocation(new Vector2f(0, -stage.getDisplayHeight()/2 + yCurrent));					
			
			yCurrent+= yIncrement;
			
			generatePDFbutton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {					
					PDFViewerItem pdfItem = new PDFViewerItem(stage, file);
					pdfItem.addToStage();
					pdfItem.setLocation(generatePDFbutton.getRelativeLocation());
				}
			});
			
			generatePDFbutton.getZOrderManager().setAutoBringToTop(true);
			generatePDFbutton.getZOrderManager().setBringToTopPropagatesUp(false);
			
			stage.addItem(generatePDFbutton);
		}catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "Exception: " + e );
		}
	}


	@Override
	public String getFriendlyAppName() {
		return "PDFViewer";
	}

	@Override
	public void shouldStop() {}

	@Override
	public void onDestroy() {}

	public static void main(String[] args) {
		
		try{
			fileDir = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("directory");			
		}catch(Exception e){
			log.info("No valid directory address given in arguments, no pdfs to read.");
		}
		
		if (fileDir != null){
			if (new File(fileDir).isDirectory()){
				log.info("Using directory at: " + fileDir);
				MultiplicityClient client = MultiplicityClient.get();
				client.start();
				PDFViewerApp app = new PDFViewerApp();
				client.setCurrentApp(app);
			}else{
				log.info(fileDir + " cannot be found, no pdfs to read.");
			}
		}
	}
	
}

package synergynet3.activitypack2.table.flickstudy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import synergynet3.SynergyNetApp;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.behaviours.BehaviourUtilities;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.behaviours.networkflick.NetworkFlickLogging;
import synergynet3.behaviours.networkflick.NetworkFlickLogging.FLICKTYPE;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.Vector2f;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class FlickStudyApp extends SynergyNetApp implements IMultiTouchEventListener {
	
	private static final float OFFSET = 200f;
	
	private static final String RESOURCES_DIR = "synergynet3/activitypack2/table/flickstudy/";	
	private static final String[] tables = {"", "green", "blue", "yellow", "red"};
	
	private float xPos = -OFFSET;
	
	private static float DECELERATION = 7.5f;
	
	private static FLICKTYPE flickMode = FLICKTYPE.PROPORTIONAL;
	private String id = "";	
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm ss.SS");
	private static final SimpleDateFormat LOG_NAME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	private Date startTime = new Date();
	
	private static int bounceLimit = 0;	
	private Date firstTouch = null;
	private Date lastTouch = null;	
	private int touchCount = 0;
	
	private static String logAddress = "";
	
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		input.registerMultiTouchEventListener(this);
		super.shouldStart(input, iqo);
	}

	protected void loadDefaultContent() {			
		id = getTableIdentity();		
		if (id.equals("green") || id.equals("blue") || id.equals("yellow") || id.equals("red")){
			
			deceleration = DECELERATION;
			
			enableNetworkFlick();			
			BehaviourUtilities.FLICK_TYPE = flickMode;			
			NetworkFlickLogging.LOGGING_ENABLED = true;			
			NetworkFlickLogging.BOUNCE_LIMIT = bounceLimit;
			
			for (int i = 1; i <= 4; i++){
				generateFlickItem(i);
			}
			
			generatePrompt("Flick each item to the table displaying the corresponding number.");
			
		}	
	}
	
	private void generateFlickItem(int targetTable){
		if (!id.equals(tables[targetTable])){
			try{
				String fileName = targetTable + ".png";
				ICachableImage flickItem = contentFactory.create(ICachableImage.class, fileName, UUID.randomUUID());
				flickItem.setImage(RESOURCES_DIR + fileName);	
				flickItem.setSize(200f, 200f);
				flickItem.setRelativeScale(0.5f);
				RotateTranslateScaleBehaviour rt = behaviourMaker.addBehaviour(flickItem, RotateTranslateScaleBehaviour.class);
				rt.setScaleLimits(0.5f, 1f);
				NetworkFlickBehaviour nf = behaviourMaker.addBehaviour(flickItem, NetworkFlickBehaviour.class);
				nf.setDeceleration(DECELERATION);
				nf.setMaxDimension(200f);
				flickItem.setRelativeLocation(new Vector2f(getXPos(), 0));
				stage.addItem(flickItem);
				FeedbackSystem.registerAsFeedbackEligible(flickItem, 200, 200, stage);
			} catch (ContentTypeNotBoundException e) {
				AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type Not Bound", e);
			}
		}
	}
	
	
	@Override
	public void onFlickArrival(synergynet3.behaviours.networkflick.messages.FlickMessage message) {
		IItem item = BehaviourUtilities.onFlickArrival(message, stage, tableIdentity, deceleration);
		for (RotateTranslateScaleBehaviour rts: item.getBehaviours(RotateTranslateScaleBehaviour.class)){
			rts.setScaleLimits(0.5f, 1f);
		}
		
	};
	
	@Override
	public void shouldStop() {
		writeToLog();
		super.shouldStop();
	}
	
	private void writeToLog(){
		if (firstTouch == null || lastTouch == null)return;
		if (logAddress != null){
			if (!logAddress.equals("")){
				String address = logAddress +  File.separator + LOG_NAME_FORMAT.format(startTime) + "_" + id + "_FlickingLog.csv";
				File logFile = new File(address);
				if (!logFile.isFile()){
					AdditionalSynergyNetUtilities.logInfo("No log file found, creating new one.");
					try {
						if (!logFile.createNewFile()){
							AdditionalSynergyNetUtilities.logInfo("Unable to create new log file.");
							return;
						}
					} catch (IOException e1) {
						AdditionalSynergyNetUtilities.logInfo("Unable to create new log file.");
						return;
					}
				}
				AdditionalSynergyNetUtilities.logInfo("Saving data to " + address);
				try	{
					FileWriter out = new FileWriter(address, true);
					BufferedWriter writer = new BufferedWriter(out);
				
					writer.write("Table," + id);
					writer.newLine();
					writer.write("Date," + DATE_FORMAT.format(startTime));
					writer.newLine();
					writer.write("Start," + TIME_FORMAT.format(startTime));
					writer.newLine();
					writer.write("End," + TIME_FORMAT.format(new Date()));
					writer.newLine();
					writer.newLine();
					
					if (flickMode == FLICKTYPE.INSTANT){
						writer.write("Mode,Instant");
					}else{
						writer.write("Mode,Proportional");					
					}
					writer.newLine();
					writer.newLine();
					
					writer.write("Deceleration," + DECELERATION);
					writer.newLine();
					writer.newLine();					
					
					writer.write("First Touch," + TIME_FORMAT.format(firstTouch));
					writer.newLine();
					writer.write("Last Touch," + TIME_FORMAT.format(lastTouch));
					writer.newLine();
					writer.newLine();
					
					writer.write("Total Number of Touches," + touchCount);
					writer.newLine();
					writer.newLine();			
					
					if (bounceLimit > 0){
						writer.write("Bounce Limit," + bounceLimit);
					}else{
						writer.write("No Bounce Limit Set.");
					}
					writer.newLine();
					writer.newLine();	
					
					writer.write("Bounces due to innacuracy," + NetworkFlickLogging.INACCURATE_BOUNCE_COUNT);
					writer.newLine();
					writer.write("Bounces due to lack of momentum," + NetworkFlickLogging.LACK_OF_MOMENTUM_BOUNCE_COUNT);
					writer.newLine();
					writer.newLine();
					
					writer.write("Flick Departures," + NetworkFlickLogging.DEPARTURE_COUNT);
					writer.newLine();
					writer.write("Flick Arrivals," + NetworkFlickLogging.ARRIVAL_COUNT);
					writer.newLine();
					writer.newLine();
					
					String[] bounceLogMessages = NetworkFlickLogging.BOUNCE_LOG.split(NetworkFlickLogging.LOG_PARSE_TOKEN);
					for (String message : bounceLogMessages){
						writer.write(message);
						writer.newLine();
					}
					writer.newLine();
					

					String[] flickLogMessages = NetworkFlickLogging.FLICK_LOG.split(NetworkFlickLogging.LOG_PARSE_TOKEN);
					for (String message : flickLogMessages){
						writer.write(message);
						writer.newLine();
					}
	
					writer.close();
				} catch (IOException e1) {
					AdditionalSynergyNetUtilities.logInfo("Unable to write to log file.");
				}
			}
		}
	}
	
	private void generatePrompt(String message){
		try{
			IMutableLabel promptLabel = this.stage.getContentFactory().create(IMutableLabel.class, "positionLabel", UUID.randomUUID());
			promptLabel.setFont(FontUtil.getFont(FontColour.White));
			promptLabel.setText(message);
			promptLabel.setBoxSize((stage.getDisplayWidth()), 50);
			promptLabel.setFontScale(0.75f);	
			promptLabel.setRelativeLocation(new Vector2f(0, -stage.getDisplayHeight()/2 + 25));
			promptLabel.setInteractionEnabled(false);
			
			stage.addItem(promptLabel);
			promptLabel.getZOrderManager().setAutoBringToTop(true);
			promptLabel.getZOrderManager().setBringToTopPropagatesUp(false);
		} catch (ContentTypeNotBoundException e) {
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type Not Bound", e);
		}
	}
	
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {		
		if (firstTouch == null)firstTouch = new Date();
		touchCount++;
	}

	
	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		lastTouch = new Date();
	}	
	
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}
	
	private float getXPos(){
		float toReturn = xPos;
		xPos += OFFSET;
		return toReturn;
	}

	@Override
	public String getSpecificFriendlyAppName() {
		return "FlickTest";
	}
	
	public static void main(String[] args) {
		if(args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}
		
		try{
			logAddress = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("log");
			AdditionalSynergyNetUtilities.logInfo("Using log at: " + logAddress );
		}catch(Exception e){
			AdditionalSynergyNetUtilities.logInfo( "No valid log address in arguments, no logging will take place.");
		}
		
		try{
			if (Boolean.parseBoolean(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("instant"))){
				flickMode = FLICKTYPE.INSTANT;
			}			
			AdditionalSynergyNetUtilities.logInfo("Flick mode set to " + flickMode);
		}catch(Exception e){
			AdditionalSynergyNetUtilities.logInfo("No flick mode setting given, defaulting to PROPORTIONAL.");
		}
		
		try{
			DECELERATION = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("deceleration"));
		}catch(Exception e){
			AdditionalSynergyNetUtilities.logInfo( "No valid deceleration value given, using default.");
		}
		AdditionalSynergyNetUtilities.logInfo("Using deceleration of: " + DECELERATION);
		
		try{
			bounceLimit = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("limit"));
			AdditionalSynergyNetUtilities.logInfo("Limit of bounces set to: " + bounceLimit);
		}catch(Exception e){
			AdditionalSynergyNetUtilities.logInfo( "No limit for bounces set.");
		}
		
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		FlickStudyApp app = new FlickStudyApp();
		client.setCurrentApp(app);
	}

}
package synergynet3;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import com.db4o.ext.Db4oIOException;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.AdditionalItemUtilities;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.jme.SimpleMediaPlayer;
import synergynet3.behaviours.BehaviourUtilities;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.behaviours.networkflick.VirtualTableUtilities;
import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.cachecontrol.CacheTidy;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.config.web.WebConfigPrefsItem;
import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackSystem;

import synergynet3.mediadetection.IMediaSearcher;
import synergynet3.mediadetection.MediaDetection;
import synergynet3.mediadetection.MediaDetection.SearchType;
import synergynet3.network.AppSystemSync;
import synergynet3.personalcontentcontrol.StudentRepresentation;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.positioning.SynergyNetPositioning;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.projector.web.ProjectorControlComms;
import synergynet3.screenshotcontrol.IScreenShotter;
import synergynet3.screenshotcontrol.SNScreenshotAppState;
import synergynet3.screenshotcontrol.ScreenshotUtilities;
import synergynet3.studentmenucontrol.StudentMenu;
import synergynet3.studentmenucontrol.StudentMenuUtilities;
import synergynet3.web.core.AppSystemDeviceControl;
import synergynet3.web.shared.DevicesSelected;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.ColourUtils;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;

/** Class to be extended by all applications which wish to utilise SynergyNet 3's database, network flick, 
* screenshot, audio playback, media detection and student menu systems.*/
abstract public class SynergyNetApp implements IMultiplicityApp, IScreenShotter, IMediaSearcher {
	
	/**If set to false no calls to SynergyNetCluster will be made, allowing for apps to be used without OpenFire running. */
	public static boolean NETWORKING = true;
	
	/**If set to false no threads for searching identified drives will be started. */
	public static boolean MEDIA_SEARCH = true;
	
	/**If set to true only one piece of media will be able play at a time. */
	public static boolean ONE_MEDIA_AT_A_TIME= false;
	
	/**The position of the current device's interface in its physical environment. */
	public static SynergyNetPosition localDevicePosition = null;
	
	/** Manages network cluster listeners.*/
	public static AppSystemSync sync = null;
	
	/** ID of the device in the network cluster.*/
	protected static String tableIdentity;
	
	/** Indicates whether items on the display can be transmitted to other tables through the network flick feature.*/
	public static boolean networkFlickEnabled = false;
		
	/** Manager for thread checking removable media for new files.*/
	protected MediaDetection removableMediaDetector = new MediaDetection();
	
	/** Manager for thread checking the current device's corresponding directory in the shared cache for new files.*/
	protected MediaDetection serverMediaDetector = new MediaDetection();
	
	/** Used to generate Items.*/
	protected IContentFactory contentFactory;
	
	/** Representation of the JME environment.*/
	protected IStage stage;
	
	/** Used to create and attach different behaviours to items.*/
	protected BehaviourMaker behaviourMaker;
	
	/** Rendering state used to capture images of the current environment.*/
	protected SNScreenshotAppState screenCaptor;
	
	/** Used to scale the size of imported media.  Can be overwritten by applications extending SynergyNetApp.*/
	protected int importImageSizeLimit = 250;
	
	/** Array of file paths used to record when a file has been made into an item.*/
	protected ArrayList<String> filePaths = new ArrayList<String>();
	
	/** The feedback types used in an application extending SynergyNetApp.*/
	protected ArrayList<Class<? extends FeedbackItem>> feedbackTypes = new ArrayList<Class<? extends FeedbackItem>>();
	
	/** Roster of students to be logged in when an application extending SynergyNetApp is started.*/
	protected ArrayList<String[]> joiningIDs = new ArrayList<String[]>();
	
	/** Indicates whether an item representing the last screenshot made has been created yet.*/
	protected boolean screenShotAwaiting = false;
	
	/** The rate at which content by default will slow down **/
	protected float deceleration = 100f;
		
	/** Array of projector IDs to send next screenshot to. */
	protected String[] toTransferScreenShotsTo = new String[0];
	
	/** Table border to show colour of table if possible. */
	protected IRoundedBorder tableBorder = null;
	
	/** Image used to show the table has been frozen. */
	protected IColourRectangle freezeFrame;
	
	/** Is the table frozen? */
	protected boolean frozen = false;
	
	/** Flag to stop screenshots being generated while network variable are being initialised. */
	protected boolean initialised = false;
	
	/** Colour for generated items to use for borders. */
	protected ColorRGBA borderColour = new ColorRGBA(1, 1, 1, 0.75f);
		
	
	///////////////// App Control System //////////////////////////
	
	
	/**
	* Executed at the start of each application which extended the SynergyNetApp.
	* This method first tidies any unused files from the synergynet cache.
	* The method then sets up the default feedback types to be used in SynergyNetApps and records the display dimensions.
	* The screen capture function is then initialised and login menus are created for any waiting students.
	* Following this the controller which listens for appropriate changes in the HazelCast Cluster is initialised.
	* The application's main environment construction is then called and finally the media searching threads are started.
	*
	* @param input
	*          The multi-touch input providing component.
	* @param iqo
	*          Queue owner.
	**/
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {		
		initiateDefaultVariables();					
        initiateScreenShotState();           
        loginOnStartup();		
		initiateSync();			        		
		protectedLoadDefaultContent();				
		afterContentLoad();		
		initiateMediaDetectors();		
		buildDisplayBorder();
		addFreezeFrame();
		initialisationThread();
	}

	/**
	* Establishes the values of variables used throughout the app.
	**/
	private void initiateDefaultVariables(){
		networkFlickEnabled = false;
		
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		this.behaviourMaker = this.stage.getBehaviourMaker();
		this.contentFactory = this.stage.getContentFactory();
		
		if (NETWORKING){
			localDevicePosition = SynergyNetPositioning.getLocalDeviceLocationFull();
		}
		
		AdditionalItemUtilities.loadAdditionalItems(stage);		
	}
	
	/**
	* Sets up the app state used to take screenshots of the environment.
	**/
	private void initiateScreenShotState(){
		screenCaptor = new SNScreenshotAppState();
        MultiplicityClient.get().getStateManager().attach(screenCaptor);
	}
	
	/**
	* Log in any students who were still present when the previous application was closed.
	**/
	private void loginOnStartup(){
		for (String[] ID: joiningIDs){
			StudentMenu menu = login(ID[0]);
			if (menu != null){
				menu.getRadialMenu().setRelativeLocation(new Vector2f(Float.parseFloat(ID[1]), Float.parseFloat(ID[2])));
				menu.getRadialMenu().setRelativeRotation(Float.parseFloat(ID[3]));     	
			}
		}
		setAbilityToAddContentFromGallery(true);
	}
	
	/**
	* Begin or update listeners for changes to variables in the network cluster.
	**/
	private void initiateSync(){
		if (sync == null){
			tableIdentity = new WebConfigPrefsItem().getClusterUserName();
			if (NETWORKING){
				AppSystemDeviceControl appSystemDeviceController = new AppSystemDeviceControl(tableIdentity);
				sync = new AppSystemSync(appSystemDeviceController, this);
				
				try{
					CacheTidy.removeUnusedStudentFiles(tableIdentity);
				}catch(Db4oIOException e){
					AdditionalSynergyNetUtilities.logInfo("Cannot clean user resources - database not online.");
				}
				
			}
			
		}else{
			if (NETWORKING){
				sync.reSync(this);
			}
		}
	}
	
	/**
	* Starts threads for listening for new media.
	**/
	private void initiateMediaDetectors(){
		
		if (MEDIA_SEARCH){
					
			removableMediaDetector.addSearchTypeTarget(SearchType.IMAGE);
			removableMediaDetector.addSearchTypeTarget(SearchType.VIDEO);
			removableMediaDetector.addSearchTypeTarget(SearchType.AUDIO);
			removableMediaDetector.initialiseRemovableMediaListener(this);      
			
			serverMediaDetector.addSearchTypeTarget(SearchType.IMAGE);
			serverMediaDetector.addSearchTypeTarget(SearchType.VIDEO);
			serverMediaDetector.addSearchTypeTarget(SearchType.AUDIO);
			serverMediaDetector.initialiseDirectoryListener(new File(CacheOrganisation.getSpecificTableDir(tableIdentity)), this);	
			
		}
	}
	
	/**
	* Creates a border the colour of the table's ID (if possible) which shows when taking some screenshots.
	**/
	private void buildDisplayBorder(){
		Color colour = ColourUtils.colorFromString(tableIdentity);
		if (colour != null){
			ColorRGBA crgba = ColourUtils.getColorRGBAFromColor(colour);
			try {
				int displayWidth = (int) (stage.getWorldLocation().x * 2);
				int displayHeight = (int) (stage.getWorldLocation().y * 2);
				
				tableBorder = stage.getContentFactory().create(IRoundedBorder.class, "tabelBorder", UUID.randomUUID());
				tableBorder.setBorderWidth(30f);
				tableBorder.setSize(displayWidth-30, displayHeight-30);
				tableBorder.setColor(crgba);
				stage.addItem(tableBorder);
				tableBorder.setInteractionEnabled(false);
				tableBorder.setVisible(false);
			} catch (ContentTypeNotBoundException e) {
				e.printStackTrace();
			}					
		}	
	}
	
	/**
	* Creates an item to represent the table being frozen.
	**/
	private void addFreezeFrame() {
		try {
			freezeFrame = stage.getContentFactory().create(IColourRectangle.class, "test", UUID.randomUUID());
			freezeFrame.setSize(stage.getDisplayWidth(), stage.getDisplayHeight());	
			freezeFrame.enableTransparency();
			freezeFrame.setGradientBackground(new Gradient(new ColorRGBA(0.5f, 0.5f, 1, 0.25f), new ColorRGBA(0, 0, 1, 0.3f), GradientDirection.DIAGONAL));
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	* Stops screenshots being generated while network variable are being initialised.
	**/
	private void initialisationThread() {
		Thread initialisationThread = new Thread(new Runnable() {	  
			public void run() {	  
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				initialised = true;
			}    					
		});	
		initialisationThread.start();
	}
	
	/**
	* Surrounds the environment construction with a catch to print any errors committed by extending applications with the logger.
	**/
	private void protectedLoadDefaultContent(){
		try {
			loadDefaultContent();
		} catch (IOException e) {
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "IO Exception.", e);
		} catch (ContentTypeNotBoundException e) {
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content not Bound.", e);
		}	
	}
	
	/**
	* The main environment construction should be performed in this method for any applications extending SynergyNetApp.
	**/
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException {}
	
	/**
	* This brings student menus to the top after they may have been obscured by the main environment construction of an application.
	**/
	protected void afterContentLoad() {
		StudentMenuUtilities.bringAllStudentsToTop(stage);
	}
	
	/**
	* Called when a SynergyNetApp extending application is closed through traditional means.
	* All students present art logged out, the media searches are stopped and the table announces its removal to the network.
	* Any unused files are then removed from the cache.
	*
	**/
	@Override
	public void shouldStop() {
		logoutAll();		
		stopMediaSearching();		
		removeLocalTable();
		ProjectorTransferUtilities.get().clearContents();
	}
	
	/**
	* Sets the name of an application.
	*
	* @return String representing the application's full name.
	*
	**/
	@Override
	public String getFriendlyAppName() {
		String specificFriendlyAppName = getSpecificFriendlyAppName();
		if (!specificFriendlyAppName.equals("")){
			specificFriendlyAppName = "-" + specificFriendlyAppName;
		}
		return "SynergyNet" + specificFriendlyAppName;
	}
	
	
	/**
	* Function to be overwritten so applications can append their name to the SynergyNetApp environment name.
	*
	* @return String representing the application's name.
	*
	**/
	protected String getSpecificFriendlyAppName(){
		return "";
	}
	
	///////////////// Screenshot Control System //////////////////////////
	
	/**
	* Initiates the screenshot renderer to capture an image of the current environment and call utiliseScreenshot 
	* with the image file it creates.
	*
	* @param loc
	*          Location at which the screenshot created should appear.
	* @param rot
	*          Rotation at which the screenshot created should appear.
	**/
	public void createScreenShotItem(Vector2f loc, float rot){
		if (!screenShotAwaiting && initialised){
			screenCaptor.takeScreenShot(this, loc, rot);
			screenShotAwaiting = true;
		}
	}
	
	/**
	* Creates a manipulable screenshot item using an image file and adds it to the environment.
	* The screenshot item is registered as feedback eligible and made capable of being network flicked when created.
	*
	* @param screenShotFile
	*          Image file to create the screenshot item from.
	* @param loc
	*          Location at which the screenshot created should appear.
	* @param rot
	*          Rotation at which the screenshot created should appear.
	**/
	@Override
	public void utiliseScreenshot(File screenShotFile, Vector2f loc, float rot){
		screenShotAwaiting = false;
		

		if (tableBorder != null)tableBorder.setVisible(false);
		
		if(toTransferScreenShotsTo.length > 0){
			transferScreenShot(screenShotFile);
		}else{
			AdditionalSynergyNetUtilities.buildScreenshotItem(screenShotFile, loc, stage, rot);
		}
	}
	

	///////////////// Media Control System //////////////////////////
	
	/**
	* Sets whether students representations present in the environment can add content from their personal galleries
	* to the applications.
	*
	* @param allowGalleryAdd
	*          true = items can be added from personal galleries.
	*          false = items cannot be added from personal galleries.    
	**/
	protected void setAbilityToAddContentFromGallery(boolean allowGalleryAdd) {
		StudentMenuUtilities.setAbilityToAddContentFromGallery(allowGalleryAdd);
	}
	
	/**
	* Called by the media searching threads when images or audio recordings are found on removable devices
	* or the device's corresponding cache folder.  Items for each discovered files are created.
	* If the items are not already present they are passed on.
	*
	* @param files
	*          The files discovered to be used in the app if not already present.   
	**/
	@Override
	public void onFind(File[] files) {
		IItem[] items = new IItem[files.length];
		for (int i = 0; i < files.length; i++){			
			if (notAlreadyPresent(files[i])){
				items[i] = AdditionalSynergyNetUtilities.generateItemFromFile(files[i], stage, deceleration, importImageSizeLimit, borderColour);
				filePaths.add(files[i].getAbsolutePath());
			}			
		}	
		onAddingAdditionalMaterials(items);
	}
	
	/**
	* When new items are discovered they are passed through this method which can be overridden by applications.
	*
	* @param itmes
	*          The items discovered to be piled together.   
	**/
	protected void onAddingAdditionalMaterials(IItem[] items){
		AdditionalSynergyNetUtilities.pile(items, 0, 0, 20, 0);	
	}

	/**
	* Forces media searching threads to recreate items for any previously discovered image or audio files
	* found on the current device's corresponding cache folder.
	* 
	**/
	public void reloadServerCache(){
		ArrayList<File[]> filesCollection = serverMediaDetector.reloadDisoveredContent();
		for (File[] files: filesCollection){
			createItemsFromFiles(files);
		}
	}
	
	/**
	* Forces media searching threads to recreate items for any previously discovered image or audio files
	* found on any removable media drives.
	* 
	**/
	public void reloadRemovableMediaCache(){
		ArrayList<File[]> filesCollection = removableMediaDetector.reloadDisoveredContent();
		for (File[] files: filesCollection){
			createItemsFromFiles(files);
		}
	}
	
	/**
	* Removes any items which are not part of the current application from the environment.
	**/
	public void removeAdditionalMedia(){
		FeedbackSystem.removeAdditionalMedia(stage);
	}
	
	/**
	* Creates items for image and audio files found from an array of files.
	* The create items are then piled in the centre of the environment.
	* 	
	* @param files
	*          The files to used to create items.
	**/
	private void createItemsFromFiles(File[] files) {
		IItem[] items = new IItem[files.length];
		for (int i = 0; i < files.length; i++){			
			items[i] = AdditionalSynergyNetUtilities.generateItemFromFile(files[i], stage, deceleration, importImageSizeLimit, borderColour);
			filePaths.add(files[i].getAbsolutePath());
		}
		AdditionalSynergyNetUtilities.pile(items, 0, 0, 20, 0);			
	}	
	
	/**
	* Checks whether an item already exists in the environment which has been made from the supplied file.
	* 
	* @param file
	*          The file to check.
	*          
	* @return Boolean value representing whether the item already exists.
	*          
	**/
	private boolean notAlreadyPresent(File file) {
		String filePath = file.getAbsolutePath();
		for (String existingPath: filePaths){
			if (existingPath.equals(filePath)){
				return false;
			}
		}
		return true;
	}

	/**
	* Stop the media detection threads.
	* 
	**/
	protected void stopMediaSearching(){
		removableMediaDetector.stopListener();
		serverMediaDetector.stopListener();
	}
	
	
	
	///////////////// Student Control System //////////////////////////
	
	/**
	* Creates a student menu for a given student representation.
	* 
	* @param student
	*          The student representation to create a student menu for.
	*          
	* @return The student menu item created for the corresponding student.
	* 
	**/
	public StudentMenu generateStudentMenu(StudentRepresentation student){
		StudentMenu menu = new StudentMenu(student, stage, null, this);
		try {
			modifyMenus(menu);
		} catch (ContentTypeNotBoundException e) {
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content not Bound", e);
		}		
		student.getGallery().setMenu(menu);
		return menu;
	}

	/**
	* Can be overridden by applications extending SynergyNetApp to modify the options made available by student menus.
	**/
	public void modifyMenus(final StudentMenu menu) throws ContentTypeNotBoundException{}
	
	/**
	* Adds a list of student IDs to the start up roster.  When a SynergyNetApp extending application
	* starts up it will login all students current on the roster.
	* 
	* @param IDs
	*          Array of the student IDs.
	* 
	**/
	public void loginAll(ArrayList<String[]> IDs){
		for (String[] ID: IDs){
			joiningIDs.add(ID);
		}        				
	}
	
	/**
	* Will log in a student with the supplied ID.  When a student is logged in their representation is retrieved from the database service
	* using their ID and a menu for the student is created and positioned randomly in the environment. 
	* When a student logs in their personal gallery is also created and populated using details from their database entry.
	* 
	* @param studentID
	*         ID of the student to be logged in.
	*         
	* @return The student menu item created for the corresponding student.
	* 
	**/
	public StudentMenu login(String studentID) {		
		return StudentMenuUtilities.login(studentID, stage, this);
	}
	
	/**
	* Logs out all students in the environment and removes their menus. 
	* 
	**/
	public void logoutAll(){
		StudentMenuUtilities.logoutAll(stage);	
	}
	
	/**
	* Logs out a specific student from the environment and removes their menu.  When logging out their details, such as items and
	* feedback stored in their personal gallery, are updated in the database.
	* 
	* @param studentID
	*         ID of the student to be logged out.
	* 
	**/
	public void logout(String studentID) {
		StudentMenuUtilities.logout(studentID, stage);		
	}
	
	/**
	* Logs out all students belonging to a specific class.
	* 
	* @param className
	*         Name of the class for which all students belonging to it should be logged out.
	* 
	*/
	public void logoutAllOfClass(String className) {
		StudentMenuUtilities.logoutAllOfClass(className, stage);
	}
	
	/**
	* Brings all student menus and menu icons to the top of the environment.
	* 
	*/
	public void bringAllStudentsToTop(){
		StudentMenuUtilities.bringAllStudentsToTop(stage);	
	}
	
	/**
	* Returns an array of the feedback types current used in an application extending SynergyNetApp.
	* 
	* @return ArrayList of the feedback type classes.
	* 
	**/
	public ArrayList<Class<? extends FeedbackItem>> getFeedbackTypes() {
		return feedbackTypes;
	}
	
	
	///////////////// Network Flick System //////////////////////////
	
	/**
	* Retrieves details from the supplied message to recreate the flicked item and to ensure it appears onscreen
	* in the manner expected, i.e. aligning with its flick vector from its source device.
	* 
	* @param message
	*         Structured message detailing the details of an item's arrival.
	* 
	*/
	public void onFlickArrival(final FlickMessage message){					
		BehaviourUtilities.onFlickArrival(message, stage, tableIdentity, deceleration);
	}	
		
	/**
	* Retrieves details from the supplied message to create a virtual representation of the device of screen in
	* relation to its real life position.  This virtual device can then be used to detect when a flicked item
	* should be transferred.
	* 
	* @param message
	*         Structured message detailing a device's position.
	* 
	*/
	public void updateVirtualTable(SynergyNetPosition message){	
		VirtualTableUtilities.addTable(message.getTableID(), 
				new Vector2f(SynergyNetPositioning.getPixelValue(message.getXinMetres()), SynergyNetPositioning.getPixelValue(message.getYinMetres())), 
				message.getOrientation(), 
				new Vector2f(SynergyNetPositioning.getPixelValue(message.getWidthinMetres()), SynergyNetPositioning.getPixelValue(message.getHeightinMetres())), 
				message.getPixelWidth(), message.getWidthinMetres(), stage);
	}
	
	/**
	* Removes a device which should no longer be accessible.
	* 
	* @param message
	*         Structured message detailing the device representation to be removed.
	* 
	*/
	public void removeVirtualTable(SynergyNetPosition message){	
		VirtualTableUtilities.removeTable(message.getTableID());
	}
	
	/**
	* Initiates the announcement to the network that it is no longer accepting items from network flicks.
	* 
	*/
	private void removeLocalTable() {
		if (NETWORKING){
			VirtualTableUtilities.removeTableFromAll();	
		}
	}
	
	/**
	* Initiates the announcement to the network that it is now accepting items from network flicks.
	* Enables network flick listeners from initiating a transfer.
	* 
	*/
	public void enableNetworkFlick(){	
		networkFlickEnabled = true;
		VirtualTableUtilities.announceTablePositionToAll();
	}
	
	/**
	* Disables network flick listeners from initiating a transfer.
	* 
	*/
	public void disableNetworkFlick(){
		if (networkFlickEnabled){			
			networkFlickEnabled = false;
			removeLocalTable();
		}
	}
	
	/**
	 * Gets the string representing the device's identity in the network cluster.
	 * 
	 * @return String representing the identity of the device.
	 * 
	 */
	public static String getTableIdentity() {
		return tableIdentity;
	}
	
	/**
	 * Actions to be performed when the SynergyNetApp window is closed.
	 */
	@Override
	public void onDestroy() {
		ArrayList<SimpleMediaPlayer> mediaPlayers = new ArrayList<SimpleMediaPlayer>(SimpleMediaPlayer.mediaPlayers);
		for (SimpleMediaPlayer mediaPlayer : mediaPlayers){
			mediaPlayer.destroy();
		}
		if (NETWORKING){
			try{
				CacheTidy.removeUnusedStudentFiles(tableIdentity);
			}catch(Db4oIOException e){
				AdditionalSynergyNetUtilities.logInfo("Cannot clean user resources - database not online.");
			}
		}
		if (sync != null)sync.stop();
		if (NETWORKING){
			SynergyNetCluster.get().shutdown();
		}
	}

	
	///////////////// Projector Interaction //////////////////////////
	
	/**
	 * Send screenshot of current contents of the app to a the list of projectors provided.
	 * 
	 * 	* @param projectorsToSendTo
	 *         List of projectors identities to send screenshots to.
	 */
	public void sendScreenShotToProjectors(String[] projectorsToSendTo) {		
		toTransferScreenShotsTo = projectorsToSendTo;		
		createScreenShotItem(new Vector2f(), importImageSizeLimit);
		if (tableBorder != null){
			tableBorder.setVisible(true);
			stage.getZOrderManager().bringToTop(tableBorder);
		}
	}	
	
	/**
	 * Send screenshot of current contents of the app to a the list of projectors provided.
	 * 
	 * 	* @param screenShotFile
	 *        File to generate screenshot from.
	 */
	private void transferScreenShot(File screenShotFile){
		String[] projectorsToSendTo = toTransferScreenShotsTo;
		toTransferScreenShotsTo = new String[0];
		ScreenshotUtilities.transferScreenShot(screenShotFile, projectorsToSendTo, stage);
	}

	/**
	 * Send current contents of the app to a the list of projectors provided.
	 * 
	 * 	* @param projectorsToSendTo
	 *         List of projectors identities to send contents to.
	 */
	public void sendContentsToProjectors(final String[] projectorsToSendTo) {
		Thread cachingThread = new Thread(new Runnable() {	  
			public void run() {	  
				ArrayList<ContentTransferedMessage> messages = ProjectorTransferUtilities.get().prepareToTransferAllContents(projectorsToSendTo);
				
				for (String projector: projectorsToSendTo){
					if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)){
						ProjectorControlComms.get().allProjectorsReceiveContent(messages);
					}else{
						ProjectorControlComms.get().specificProjectorsReceiveContent(messages, projector);
					}
				}
			}    					
		});	
		cachingThread.start();
	}
	

	/**
	* Retrieves details from the supplied message to recreate the transfered item
	* 
	* @param message
	*         Structured message detailing the details of an item's arrival.
	* 
	*/
	public void onContentFromProjectorArrival(ArrayList<ContentTransferedMessage> messages){	
		ProjectorTransferUtilities.get().onContentArrival(messages);
	}	
	
	/**
	* Adds or removes the blue item to represent a frozen workspace.
	*/
	public void toggleFreeze() {
		if (!frozen){
			if (freezeFrame.getParentItem() == null){
				stage.addItem(freezeFrame);
				stage.getZOrderManager().bringToTop(freezeFrame);
			}			
			for (IItem item : stage.getChildItems()){
				new PerformActionOnAllDescendents(item, false, false){
					@Override
					protected void actionOnDescendent(IItem child){	
						for(NetworkFlickBehaviour behaviour : child.getBehaviours(NetworkFlickBehaviour.class)){
							behaviour.reset();
						}
					}
				};
			}	
			frozen = true;
		}else{
			if (freezeFrame.getParentItem() != null){
				stage.removeItem(freezeFrame);
			}
			frozen = false;
		}
	}
	
}
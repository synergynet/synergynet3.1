package synergynet3.apps.numbernet.controller.numbernettable;

import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.apps.numbernet.graphing.builders.SpringGraphBuilder;
import synergynet3.apps.numbernet.model.ExpressionSession;
import synergynet3.apps.numbernet.network.CalculatorCollectionSync;
import synergynet3.apps.numbernet.network.CalculatorKeySynchronizer;
import synergynet3.apps.numbernet.network.ExpressionDisplaySync;
import synergynet3.apps.numbernet.network.ExpressionPositionSync;
import synergynet3.apps.numbernet.network.MultiTouchEnabledSync;
import synergynet3.apps.numbernet.ui.expression.ExpressionDisplay;
import synergynet3.apps.numbernet.validation.DefaultValidationChecker;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;

import com.hazelcast.core.Member;

public class NumberNetController {
	private static final Logger log = Logger.getLogger(NumberNetController.class.getName());

	private IStage stage;

	private Double currentTargetSet = 10.0;

	private CalculatorEventProcessor calculatorEventProcessor;

	private NumberNetStudentTableClusteredData studentTableDataCluster;
	private CalculatorCollectionManager calculatorCollectionManager;
	private ExpressionPositionSync expressionPositionSynchronizer;	
	private MultiTouchEnabledSync multiTouchEnabledSynchronizer;
	private CalculatorCollectionSync calculatorCollectionSynchronizer;	
	private ExpressionDisplay expressionDisplay;
	private DefaultValidationChecker validationChecker;
	private ExpressionSession expressionSession;
	private CalculatorKeySynchronizer calculatorKeySynchronizer;
	private String tableIdentity;
	private ExpressionDisplaySync expressionDisplaySynchronizer;
	private SpringGraphBuilder springGraphBuilder;
	private IContainer graphingLinesContainer;

	private IContainer calculatorsAndExpressionsContainer;

	public NumberNetController(MultiTouchInputComponent input) throws ContentTypeNotBoundException {

		try {

			this.tableIdentity = SynergyNetCluster.get().getIdentity();
			log.info("Starting controller for numbernet");
			initDisplay();

			validationChecker = new DefaultValidationChecker();		
			studentTableDataCluster = new NumberNetStudentTableClusteredData(tableIdentity);
			expressionSession = new ExpressionSession(studentTableDataCluster);		
			calculatorEventProcessor = new CalculatorEventProcessor(tableIdentity, validationChecker);

			log.info("private inits");
			initCalculatorCollection();
			initCalculatorSync();	
			initMultiTouchInputSync(input);
			initExpressionDisplay();
			initExpressionDisplaySync();
			setExpressionSession(expressionSession);
			
			//Adds a dummy to avoid java error
			calculatorCollectionManager.addCalculator("dummy", -100, 0);

			log.info("Populating from network");
			expressionSession.addAllExpressionsFromDistributedMap();

			log.info("Creating SGB");
			springGraphBuilder = new SpringGraphBuilder(stage, 70, 300, 100);
			log.info("Done");
			
			//enterTestMode();


			studentTableDataCluster.getGraphingModeControl().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {			
				@Override
				public void distributedPropertyDidChange(Member member, Boolean oldValue,
						Boolean newValue) {
					log.fine("Graphing mode control change to " + newValue);					
					setGraphingModeEnabled(newValue);
				}
			});		

			Boolean graphModeEnabled = studentTableDataCluster.getGraphingModeControl().getValue();
			if(graphModeEnabled != null && graphModeEnabled == true) {
				setGraphingModeEnabled(true);
			}
			
			calculatorCollectionManager.removeCalculator("dummy");


		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}

	protected void setGraphingModeEnabled(boolean enabled) {
		if(enabled) {
			log.fine("Setting graph builder up");		
			calculatorCollectionManager.setAllCalculatorsVisible(false);
			springGraphBuilder.setExpressionSessionAndDisplay(expressionSession, expressionDisplay, graphingLinesContainer);
			try{
				springGraphBuilder.setActive(true, expressionSession.getTargetValue());
			}catch (Exception e){}
		}else{
			log.fine("Shutting down graphbuilder");
			calculatorCollectionManager.setAllCalculatorsVisible(true);
			springGraphBuilder.setActive(false, 0);
		}
	}

	public void shutdown() {
		expressionPositionSynchronizer.stop();
		calculatorCollectionSynchronizer.stop();
		multiTouchEnabledSynchronizer.stop();
	}	

	public void setCurrentTargetSet(Double currentTargetSet) {
		this.currentTargetSet = currentTargetSet;
	}

	public Double getCurrentTargetSet() {
		return currentTargetSet;
	}

	public void setExpressionSession(ExpressionSession session) {
		expressionDisplay.setExpressionSession(session);
		calculatorEventProcessor.setExpressionSession(session);
		validationChecker.setCurrentExpressionSession(session);		
	}


	// ******* private ********

	protected void enterTestMode() {
		log.info("Entering test mode");
//		calculatorCollectionManager.addCalculator("iyad", -300, 0);
		calculatorCollectionManager.addCalculator("andrew", -100, 0);
//		calculatorCollectionManager.addCalculator("steve", 100, 0);
//		calculatorCollectionManager.addCalculator("phyo", 300, 0);
		expressionSession.setTarget(35);
		springGraphBuilder.setExpressionSessionAndDisplay(expressionSession, expressionDisplay, graphingLinesContainer);
		this.setGraphingModeEnabled(true);
		calculatorCollectionManager.setAllCalculatorsVisible(true);
	}


	private void initExpressionDisplaySync() {		
		expressionPositionSynchronizer = new ExpressionPositionSync(expressionDisplay, studentTableDataCluster);
		expressionPositionSynchronizer.start();

		expressionDisplaySynchronizer = new ExpressionDisplaySync(expressionDisplay, studentTableDataCluster);
		expressionDisplaySynchronizer.start();
	}

	private void initCalculatorCollection() {
		
		calculatorCollectionManager = new CalculatorCollectionManager(calculatorsAndExpressionsContainer, calculatorEventProcessor);
	}

	private void initCalculatorSync() {
		calculatorCollectionSynchronizer = new CalculatorCollectionSync(calculatorCollectionManager, studentTableDataCluster);
		calculatorCollectionSynchronizer.start();

		calculatorKeySynchronizer = new CalculatorKeySynchronizer(calculatorCollectionManager, studentTableDataCluster);
		calculatorKeySynchronizer.start();
	}	

	private void initMultiTouchInputSync(MultiTouchInputComponent input) {
		multiTouchEnabledSynchronizer = new MultiTouchEnabledSync(studentTableDataCluster, input);
		multiTouchEnabledSynchronizer.start();
	}

	private void initDisplay() throws ContentTypeNotBoundException {
		stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		
		IImage background = stage.getContentFactory().create(IImage.class, "bg", UUID.randomUUID());
		stage.addItem(background);
		
		background.setImage("/synergynet3/apps/numbernet/backgrounds/blue.png");
		background.setSize(stage.getDisplayWidth(), stage.getDisplayHeight());
		float wrapScale = 100f;
		background.setWrapping(wrapScale, wrapScale);		

		graphingLinesContainer = stage.getContentFactory().create(IContainer.class, "glines", UUID.randomUUID());
		stage.addItem(graphingLinesContainer);
		stage.getZOrderManager().ignoreItemClickedBehaviour(background);

		
		calculatorsAndExpressionsContainer = stage.getContentFactory().create(IContainer.class, "calcexpr", UUID.randomUUID());
		stage.addItem(calculatorsAndExpressionsContainer);
				
		stage.getZOrderManager().setAutoBringToTop(false);
		stage.getZOrderManager().bringToTop(calculatorsAndExpressionsContainer);
	}

	private void initExpressionDisplay() throws ContentTypeNotBoundException {
		expressionDisplay = new ExpressionDisplay(tableIdentity, stage, calculatorsAndExpressionsContainer, calculatorCollectionManager);	
	}

}

package synergynet3.activitypack1.table.pdfviewer;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.jme3.math.Vector2f;

/**
 * The Class PDFViewerApp.
 */
public class PDFViewerApp implements IMultiplicityApp {

	/** The file dir. */
	private static String fileDir = null;

	/** The Constant FONT_LOC. */
	private static final String FONT_LOC = "synergynet3/activitypack1/table/common/arial64_white.fnt";

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PDFViewerApp.class
			.getName());

	/** The stage. */
	private IStage stage;

	/** The y current. */
	private float yCurrent = 0;

	/** The y increment. */
	private float yIncrement = 0;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		try {
			fileDir = ManagementFactory.getRuntimeMXBean()
					.getSystemProperties().get("directory");
		} catch (Exception e) {
			log.info("No valid directory address given in arguments, no pdfs to read.");
		}

		if (fileDir != null) {
			if (new File(fileDir).isDirectory()) {
				log.info("Using directory at: " + fileDir);
				MultiplicityClient client = MultiplicityClient.get();
				client.start();
				PDFViewerApp app = new PDFViewerApp();
				client.setCurrentApp(app);
			} else {
				log.info(fileDir + " cannot be found, no pdfs to read.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName() {
		return "PDFViewer";
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#onDestroy()
	 */
	@Override
	public void onDestroy() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.appsystem.IMultiplicityApp#shouldStart(multiplicity3.input
	 * .MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#shouldStop()
	 */
	@Override
	public void shouldStop() {
	}

	/**
	 * Creates the pd fgeneration button.
	 *
	 * @param pdfName the pdf name
	 * @param file the file
	 */
	private void createPDFgenerationButton(final String pdfName, final File file) {
		try {

			final IMutableLabel generatePDFbutton = this.stage
					.getContentFactory().create(IMutableLabel.class,
							"pdfLabel", UUID.randomUUID());
			generatePDFbutton.setFont(FONT_LOC);
			generatePDFbutton.setText(pdfName);
			generatePDFbutton.setBoxSize((stage.getDisplayWidth() / 3) * 2, 50);
			generatePDFbutton.setFontScale(0.75f);
			generatePDFbutton.setRelativeLocation(new Vector2f(0, (-stage
					.getDisplayHeight() / 2) + yCurrent));

			yCurrent += yIncrement;

			generatePDFbutton.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							PDFViewerItem pdfItem = new PDFViewerItem(stage,
									file);
							pdfItem.addToStage();
							pdfItem.setLocation(generatePDFbutton
									.getRelativeLocation());
						}
					});

			generatePDFbutton.getZOrderManager().setAutoBringToTop(true);
			generatePDFbutton.getZOrderManager().setBringToTopPropagatesUp(
					false);

			stage.addItem(generatePDFbutton);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "Exception: " + e);
		}
	}

	/**
	 * Load default content.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void loadDefaultContent() throws IOException,
			ContentTypeNotBoundException {

		File directory = new File(fileDir);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();

			yIncrement = stage.getDisplayHeight() / (files.length + 1);
			yCurrent = yIncrement;

			for (File file : files) {
				try {
					createPDFgenerationButton(file.getName(), file);

					PDFViewerItem pdfItem = new PDFViewerItem(stage, file);
					pdfItem.destroy();
				} catch (Exception e) {
					log.log(Level.SEVERE, "Exception: " + e);
				}
			}
		}
	}

}

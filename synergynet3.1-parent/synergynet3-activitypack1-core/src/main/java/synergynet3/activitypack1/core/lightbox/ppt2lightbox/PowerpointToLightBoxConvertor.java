package synergynet3.activitypack1.core.lightbox.ppt2lightbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.SlideShow;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.LightBox;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;
import synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements.ImageItemConvertor;
import synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements.TextShapeConvertor;

/**
 * The Class PowerpointToLightBoxConvertor.
 */
public class PowerpointToLightBoxConvertor {

	/** The light box. */
	private LightBox lightBox;

	/** The light box directory. */
	private File lightBoxDirectory;

	/** The slideshow. */
	private SlideShow slideshow;

	/**
	 * Instantiates a new powerpoint to light box convertor.
	 *
	 * @param lightBoxName the light box name
	 * @param slideshow the slideshow
	 * @param storageDirectory the storage directory
	 */
	public PowerpointToLightBoxConvertor(String lightBoxName,
			SlideShow slideshow, File storageDirectory) {
		this.slideshow = slideshow;
		this.lightBoxDirectory = storageDirectory;
		lightBox = new LightBox(lightBoxName);
	}

	/**
	 * Convert slide from stream.
	 *
	 * @param slideIndex the slide index
	 * @param inputStream the input stream
	 * @param lightBoxStorageDirectory the light box storage directory
	 * @param lightBoxName the light box name
	 * @return the light box
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static LightBox convertSlideFromStream(int slideIndex,
			InputStream inputStream, File lightBoxStorageDirectory,
			String lightBoxName) throws IOException {
		SlideShow powerpointFile = new SlideShow(inputStream);
		PowerpointToLightBoxConvertor convertor = new PowerpointToLightBoxConvertor(
				lightBoxName, powerpointFile, lightBoxStorageDirectory);
		return convertor.convertFromSlide(slideIndex);
	}

	/**
	 * Populate light box with images from slide.
	 *
	 * @param slide the slide
	 */
	private void populateLightBoxWithImagesFromSlide(Slide slide) {
		for (Shape shape : slide.getShapes()) {
			if (shape instanceof Picture) {
				ImageItem imageItem = ImageItemConvertor.convertPictureShape(
						(Picture) shape, slideshow.getPageSize(),
						lightBoxDirectory);
				lightBox.addImageItem(imageItem);
			}
		}
	}

	/**
	 * Convert from slide.
	 *
	 * @param slideIndex the slide index
	 * @return the light box
	 */
	LightBox convertFromSlide(int slideIndex) {
		Slide slide = getSlideFromPowerpointDocumentForIndex(slideIndex);
		populateLightBoxWithTextShapesFromSlide(slide);
		populateLightBoxWithImagesFromSlide(slide);
		return lightBox;
	}

	/**
	 * Gets the slide from powerpoint document for index.
	 *
	 * @param slideIndex the slide index
	 * @return the slide from powerpoint document for index
	 */
	Slide getSlideFromPowerpointDocumentForIndex(int slideIndex) {
		return slideshow.getSlides()[slideIndex];
	}

	/**
	 * Gets the text shapes from slide.
	 *
	 * @param slide the slide
	 * @return the text shapes from slide
	 */
	TextShape[] getTextShapesFromSlide(Slide slide) {
		List<TextShape> textShapes = new ArrayList<TextShape>();
		for (Shape shape : slide.getShapes()) {
			if (shape instanceof TextShape) {
				textShapes.add((TextShape) shape);
			}
		}
		return textShapes.toArray(new TextShape[0]);
	}

	/**
	 * Populate light box with text shapes from slide.
	 *
	 * @param slide the slide
	 */
	void populateLightBoxWithTextShapesFromSlide(Slide slide) {
		for (TextShape textBox : getTextShapesFromSlide(slide)) {
			TextItem textItem = TextShapeConvertor.convertTextShape(textBox,
					slideshow.getPageSize());
			lightBox.addTextItem(textItem);
		}
	}
}

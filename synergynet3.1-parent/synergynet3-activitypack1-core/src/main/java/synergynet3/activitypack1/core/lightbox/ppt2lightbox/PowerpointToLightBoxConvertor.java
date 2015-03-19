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

public class PowerpointToLightBoxConvertor {
	
	private LightBox lightBox;
	private SlideShow slideshow;
	private File lightBoxDirectory;

	public static LightBox convertSlideFromStream(int slideIndex, InputStream inputStream, File lightBoxStorageDirectory, String lightBoxName) throws IOException {		
		SlideShow powerpointFile = new SlideShow(inputStream);
		PowerpointToLightBoxConvertor convertor = new PowerpointToLightBoxConvertor(lightBoxName, powerpointFile, lightBoxStorageDirectory);
		return convertor.convertFromSlide(slideIndex);
	}

	public PowerpointToLightBoxConvertor(String lightBoxName, SlideShow slideshow, File storageDirectory) {
		this.slideshow = slideshow;
		this.lightBoxDirectory = storageDirectory;
		lightBox = new LightBox(lightBoxName);
	}

	LightBox convertFromSlide(int slideIndex) {
		Slide slide = getSlideFromPowerpointDocumentForIndex(slideIndex);
		populateLightBoxWithTextShapesFromSlide(slide);
		populateLightBoxWithImagesFromSlide(slide);
		return lightBox;
	}
	
	Slide getSlideFromPowerpointDocumentForIndex(int slideIndex) {
		return slideshow.getSlides()[slideIndex];
	}
	
	private void populateLightBoxWithImagesFromSlide(Slide slide) {
		for(Shape shape : slide.getShapes()) {
			if(shape instanceof Picture) {
				ImageItem imageItem = ImageItemConvertor.convertPictureShape((Picture) shape, slideshow.getPageSize(), lightBoxDirectory);
				lightBox.addImageItem(imageItem);
			}
		}		
	}

	void populateLightBoxWithTextShapesFromSlide(Slide slide) {
		for(TextShape textBox : getTextShapesFromSlide(slide)) { 
			TextItem textItem = TextShapeConvertor.convertTextShape(textBox, slideshow.getPageSize());
			lightBox.addTextItem(textItem);
		}
	}

	TextShape[] getTextShapesFromSlide(Slide slide) {
		List<TextShape> textShapes = new ArrayList<TextShape>();
		for(Shape shape : slide.getShapes()) {	
			if(shape instanceof TextShape) {
				textShapes.add((TextShape) shape);
			}
		}
		return textShapes.toArray(new TextShape[0]);
	}
}

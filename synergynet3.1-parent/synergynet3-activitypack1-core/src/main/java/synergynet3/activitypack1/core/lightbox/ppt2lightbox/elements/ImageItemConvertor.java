package synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements;

import java.awt.Dimension;
import java.awt.geom.Point2D.Float;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.Picture;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;



public class ImageItemConvertor extends Convertor {
	private static final Logger log = Logger.getLogger(ImageItemConvertor.class.getName());

	public static ImageItem convertPictureShape(Picture shape, Dimension slideSize, File lightboxDirectory) {
		return new ImageItemConvertor(shape, slideSize, lightboxDirectory).convert();
	}

	private Picture shape;
	private File lightboxDirectory;
	private File imageFile;
	private Float imageSize;
	private Float imagePosition;
	private boolean isMoveable;
	
	public ImageItemConvertor(Picture shape, Dimension slideSize, File lightboxDirectory) {
		super(shape, slideSize);
		this.shape = shape;
		this.lightboxDirectory = lightboxDirectory;
		this.lightboxDirectory.mkdirs();
	}

	private ImageItem convert() {
		extractData();		
		ImageItem imageItem = new ImageItem();
		populateImageItemWithData(imageItem);
		return imageItem;
	}

	private void extractData() {
		try {
			extractImageData();
		} catch (IOException e) {
			log.log(Level.WARNING, "Error extracting image data", e);
		}
		this.isMoveable = interpretMoveablePropertyFromBackgroundFillColour();
		this.imageSize = super.getSizeFromShapeBounds();
		this.imagePosition = super.getPositionFromShapeBounds();
	}
	
	private void populateImageItemWithData(ImageItem imageItem) {
		imageItem.setImageFileName(this.imageFile.getName());
		imageItem.setSize(this.imageSize);
		imageItem.setPosition(this.imagePosition);
		imageItem.setMoveable(this.isMoveable);
	}
	
	private void extractImageData() throws IOException {
		String fileExtension = getExtension();
		String imageFileName = getUniqueFileName() + fileExtension;
		this.imageFile = new File(lightboxDirectory, imageFileName);
		OutputStream imageFileOutputStream = new FileOutputStream(this.imageFile);
		imageFileOutputStream.write(shape.getPictureData().getData());
		imageFileOutputStream.close();
	}
	
	private String getExtension() {
		String extension = ".unknown";
		switch (shape.getPictureData().getType()){
	      case Picture.JPEG: extension=".jpg"; break;
	      case Picture.PNG: extension=".png"; break;
	      case Picture.WMF: extension=".wmf"; break;
	      case Picture.EMF: extension=".emf"; break;
	      case Picture.PICT: extension=".pict"; break;
	    }
		return extension;
	}

	private String getUniqueFileName() {
		UUID randomUUID = UUID.randomUUID();
		return randomUUID.toString();
	}



}

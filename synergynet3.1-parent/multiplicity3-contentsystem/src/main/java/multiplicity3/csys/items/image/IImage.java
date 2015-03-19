package multiplicity3.csys.items.image;

import java.io.File;

import com.jme3.texture.Texture;

import multiplicity3.csys.items.shapes.IRectangularItem;

public interface IImage extends IRectangularItem {
	public void setImage(String imageResoure);
	public void setTexture(Texture tex);
	public String getImage();
	public void setSize(float width, float height);
	public void setImage(File imageFile);
	public void setWrapping(float xscale, float yscale);
}

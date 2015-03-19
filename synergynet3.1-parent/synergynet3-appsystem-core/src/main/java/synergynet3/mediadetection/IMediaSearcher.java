package synergynet3.mediadetection;

import java.io.File;

//This is intended to be implemented by any class intending utilise a search listener.
public interface IMediaSearcher {
	
	public void onFind(File[] files); 

}

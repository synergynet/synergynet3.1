package synergynet3.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import synergynet3.SynergyNetApp;
import synergynet3.config.web.CacheOrganisation;
import multiplicity3.appsystem.MultiplicityClient;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;

public class SNAudioController {
	
	private Logger log;
	private boolean audioSet;
	private boolean isRecording;
	private Clip audio;	
	private AudioNode audioNode;
	private TargetDataLine targetDataLine;	
	public static boolean recordingConcurrently = false;	
	private IAudioItem audioItem;	
	private boolean isSuccess;
	private double duration = 0;
	private boolean isLocal = false;	
	private int maxRecordingTime = 20000;
	
	private File audioFile;
	
	public SNAudioController(){
		this.log = Logger.getLogger(SNAudioController.class.getName());
	}
	
	public SNAudioController(Logger log){
		this.log = log;
	}
	
	public SNAudioController(Logger log, IAudioItem audioItem){
		this.log = log;
		this.audioItem = audioItem;
	}
	
	public void setAudioSource(AssetManager assetManager, String fileLocation) {		
		audioNode = new AudioNode(assetManager, fileLocation);
		duration = audioNode.getAudioData().getDuration() * 1000f;
		isLocal = true;
		audioSet = true;
	}
	
	public void setAudioSource(File f){	
		audioFile = f;
		try{
			setAudioSource(f.toURI().toString());
		}catch(NullPointerException e){}
	}
	
	public void setAudioSource(File f, String s){	
		audioFile = f;
	    setAudioSource(s);
	}
	
	public void setAudioSource(String recordingAddress){
		try{
			recordingAddress = recordingAddress.replace("file:/", "");
			recordingAddress = recordingAddress.replace("%20", " ");
			if (!isWindows()){
				recordingAddress = File.separator + recordingAddress; 
			}
			InputStream in = new FileInputStream(recordingAddress);
			InputStream bufferedIn = new BufferedInputStream(in);
			audio = AudioSystem.getClip();
	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
	        audio.open(inputStream);
	        duration = TimeUnit.MICROSECONDS.toMillis(audio.getMicrosecondLength());	    
			audioSet = true;
		}catch (Exception e){
			log.log(Level.SEVERE, "Unable to open recording.", e);			
			audioSet = false;
			stopAudioItemRecording(false);
		}
	}
	
	private static boolean isWindows(){
		return System.getProperty("os.name").startsWith("Windows");
	}
	
	public boolean hasAudioBeenSet(){
		return audioSet;
	}
	
	public void startAudioCapture() {
		if (!audioSet && !recordingConcurrently){
			recordingConcurrently = true;
			try {
				final AudioFormat format = getAudioFormat();
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
				targetDataLine = (TargetDataLine)AudioSystem.getLine(info);
			    final AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;	
			    
				SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
				String audioID = SynergyNetApp.getTableIdentity() + "_" + yearFormat.format(new Date()); 
			    audioFile = new File(CacheOrganisation.getAudioDir() + File.separator + audioID + ".wav");
			    audioFile.createNewFile();
			     
		      	Runnable recorderRunner = new Runnable() {	  
					public void run() {
					    try{			
					    	isRecording = true;
					    	recordingConcurrently = true;
						    targetDataLine.open(format);
						    targetDataLine.start();
						    AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);	
						    recordingConcurrently = false;
						} catch (IOException ie) {
							log.log(Level.SEVERE, "IO Exception.", ie);
							recordingConcurrently = false;
							stopAudioCapture(false);		
						} catch (LineUnavailableException le) {
							log.log(Level.SEVERE, "Line unavailable.", le);
							recordingConcurrently = false;
							stopAudioCapture(false);	
						}						
					}		      		
		      	};
		      	Thread captureThread = new Thread(recorderRunner);
		      	captureThread.start();
		      	
		      	Runnable timeOutRunner = new Runnable() {	  
					public void run() {
					    try{		
					    	Thread.sleep(maxRecordingTime);
					    	if (isRecording) {
					    		recordingConcurrently = false;
					    		stopAudioCapture(true);	
					    	}
			    		  } catch (InterruptedException e) {
			    			  log.log(Level.SEVERE, "Interrupted Exception.", e);
			    		  }						
					}		      		
		      	};
		      	Thread timeOutThead = new Thread(timeOutRunner);
		      	timeOutThead.start();
		      	
			} catch (LineUnavailableException e) {
				log.log(Level.SEVERE, "Line unavailable.", e);
  				recordingConcurrently = false;
  				stopAudioCapture(false);
		  	} catch (IOException ie){
				log.log(Level.SEVERE, "Recording File unavailable.", ie);
  				recordingConcurrently = false;
  				audioSet = false;
  				stopAudioCapture(false);
		  	}
		}else{
			stopAudioItemRecording(false);
		}
	}
	
	private AudioFormat getAudioFormat(){
		float sampleRate = 16000;		    //8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;		    //8,16
		int channels = 2;				    //1,2
		boolean signed = true;			    //true,false
		boolean bigEndian = true;		    //true,false
		return new AudioFormat(sampleRate,sampleSizeInBits,	channels, signed, bigEndian);
	}
	
	public void stopAudioCapture(boolean success) {		
		isRecording = false;
		if (targetDataLine!=null){
	        targetDataLine.stop();
	        targetDataLine.close();
		}
		
		isSuccess = success;
		
		Runnable runner = new Runnable() { 
			public void run() {
				try {
					Thread.sleep(100);
					if (isSuccess){
						setAudioSource(audioFile);
					}else{
						audioSet = false;
					}
					stopAudioItemRecording(isSuccess);
				} catch (InterruptedException e) {
					log.log(Level.SEVERE, "Interrupted Exception.", e);
				}
			}
	    };
	    Thread waitThread = new Thread(runner);
	    waitThread.start();			

	}
	
	private void stopAudioItemRecording(boolean success){
		if (audioItem != null){
			audioItem.stopRecord(isSuccess);
		}
	}
	
	public void startAudioPlayBack() {
		if (audioSet){
			if (isLocal){
				MultiplicityClient.get().getAudioRenderer().playSource(audioNode);				
			}else{
				audio.start(); 			  
			}
			Runnable runner = new Runnable() { 
				public void run() {
					try {						
						Thread.sleep((int)Math.ceil(duration));						
						stopAudioPlayBack();
					} catch (InterruptedException e) {
						log.log(Level.SEVERE, "Interrupted Exception.", e);
					}
				}
		    };
		    Thread playThread = new Thread(runner);
		    playThread.start();
		}
	}
	  
	public void stopAudioPlayBack(){
		if (isLocal){
			MultiplicityClient.get().getAudioRenderer().stopSource(audioNode);
		}else{
			try{
				audio.stop();
				audio.setFramePosition(0);
			}catch(NullPointerException e){}
		}
		if (audioItem != null){
			audioItem.stopPlay();
		}
	}
	
	public void resetAudioRecording(){	    
		audioSet = false;
	}
	
	public File getAudioFile(){
		return audioFile;
	}

	public int getMaxRecordingTime() {
		return maxRecordingTime;
	}

	public void setMaxRecordingTime(int maxRecordingTime) {
		this.maxRecordingTime = maxRecordingTime;
	}
}

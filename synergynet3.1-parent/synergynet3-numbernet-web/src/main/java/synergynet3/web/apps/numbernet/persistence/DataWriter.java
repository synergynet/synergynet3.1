package synergynet3.web.apps.numbernet.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.comms.teachercontrol.TeacherControlComms;
import synergynet3.web.apps.numbernet.shared.Edge;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.ExpressionVisibleProperties;
import synergynet3.web.apps.numbernet.shared.TableTarget;

import com.thoughtworks.xstream.XStream;

public class DataWriter implements Runnable {
	private XStream xstream;
	private int timeBetweenWritesSeconds = 30;

	public DataWriter() {
		xstream = new XStream();
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				writeAllExpressionData();
				writeAllGraphingData();
				writeAllPositionData();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(timeBetweenWritesSeconds  * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void writeAllPositionData() throws IOException {
		FileOutputStream outputStream = new FileOutputStream(getPositionDataHandleForWriting());
		for(TableTarget tt : TeacherControlComms.get().getTableTargets()) {
			if(tt.getTarget() != null) {
				double target = tt.getTarget();
				DistributedMap<String, ExpressionVisibleProperties> vismap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(target);
				List<ExpressionVisibleProperties> visProps = new ArrayList<ExpressionVisibleProperties>();
				visProps.addAll(vismap.values());
				writeVisProperties(visProps, outputStream);
			}
		}
		outputStream.close();
	}



	private void writeAllGraphingData() throws IOException {
		FileOutputStream outputStream = new FileOutputStream(getEdgeMapHandleForWriting());
		for(TableTarget tt : TeacherControlComms.get().getTableTargets()) {
			if(tt.getTarget() != null) {
				double target = tt.getTarget();
				Collection<Edge> edges = TargetMaps.get().getEdgesMapForTarget(target).values();
				List<Edge> edgesToWrite = new ArrayList<Edge>();
				edgesToWrite.addAll(edges);
				writeEdges(edgesToWrite, outputStream);
			}
		}
		outputStream.close();
	}




	private void writeAllExpressionData() throws IOException {
		FileOutputStream outputStream = new FileOutputStream(getExpressionFileHandleForWriting());
		for(TableTarget tt : TeacherControlComms.get().getTableTargets()) {
			if(tt.getTarget() != null) {
				writeExpressionDataForTarget(tt.getTarget(), outputStream);
			}
		}
		outputStream.close();
	}

	private void writeExpressionDataForTarget(Double target, FileOutputStream outputStream) {
		List<Expression> expressions = TeacherControlComms.get().getExpressionsForTarget(target);
		writeExpressionsForTarget(target, expressions, outputStream);
	}
	
	private void writeVisProperties(List<ExpressionVisibleProperties> visProps,
			FileOutputStream outputStream) {
		xstream.toXML(visProps);
	}


	private void writeExpressionsForTarget(Double target,
			List<Expression> expressions, FileOutputStream outputStream) {
			xstream.toXML(expressions, outputStream);		
	}
	
	private void writeEdges(Collection<Edge> edges,
			FileOutputStream outputStream) {
		xstream.toXML(edges, outputStream);
	}

	private File getExpressionFileHandleForWriting() throws IOException {
		String dateStr = getDateString();
		return File.createTempFile(dateStr + "_expr", ".xml");
	}
	
	private File getEdgeMapHandleForWriting() throws IOException {
		String dateStr = getDateString();
		return File.createTempFile(dateStr + "_edge", ".xml");
	}
	
	private File getPositionDataHandleForWriting() throws IOException {
		String dateStr = getDateString();
		return  File.createTempFile(dateStr + "_pos", ".xml");
	}
	
	private String getDateString() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMddHH.mmss"); 
		return sdf.format(cal.getTime());
		
	}


}

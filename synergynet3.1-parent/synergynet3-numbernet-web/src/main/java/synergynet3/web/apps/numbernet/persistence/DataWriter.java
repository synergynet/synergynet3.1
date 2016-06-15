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

/**
 * The Class DataWriter.
 */
public class DataWriter implements Runnable
{

	/** The time between writes seconds. */
	private int timeBetweenWritesSeconds = 30;

	/** The xstream. */
	private XStream xstream;

	/**
	 * Instantiates a new data writer.
	 */
	public DataWriter()
	{
		xstream = new XStream();
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				writeAllExpressionData();
				writeAllGraphingData();
				writeAllPositionData();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			try
			{
				Thread.sleep(timeBetweenWritesSeconds * 1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the date string.
	 *
	 * @return the date string
	 */
	private String getDateString()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMddHH.mmss");
		return sdf.format(cal.getTime());

	}

	/**
	 * Gets the edge map handle for writing.
	 *
	 * @return the edge map handle for writing
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File getEdgeMapHandleForWriting() throws IOException
	{
		String dateStr = getDateString();
		return File.createTempFile(dateStr + "_edge", ".xml");
	}

	/**
	 * Gets the expression file handle for writing.
	 *
	 * @return the expression file handle for writing
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File getExpressionFileHandleForWriting() throws IOException
	{
		String dateStr = getDateString();
		return File.createTempFile(dateStr + "_expr", ".xml");
	}

	/**
	 * Gets the position data handle for writing.
	 *
	 * @return the position data handle for writing
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File getPositionDataHandleForWriting() throws IOException
	{
		String dateStr = getDateString();
		return File.createTempFile(dateStr + "_pos", ".xml");
	}

	/**
	 * Write all expression data.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeAllExpressionData() throws IOException
	{
		FileOutputStream outputStream = new FileOutputStream(getExpressionFileHandleForWriting());
		for (TableTarget tt : TeacherControlComms.get().getTableTargets())
		{
			if (tt.getTarget() != null)
			{
				writeExpressionDataForTarget(tt.getTarget(), outputStream);
			}
		}
		outputStream.close();
	}

	/**
	 * Write all graphing data.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeAllGraphingData() throws IOException
	{
		FileOutputStream outputStream = new FileOutputStream(getEdgeMapHandleForWriting());
		for (TableTarget tt : TeacherControlComms.get().getTableTargets())
		{
			if (tt.getTarget() != null)
			{
				double target = tt.getTarget();
				Collection<Edge> edges = TargetMaps.get().getEdgesMapForTarget(target).values();
				List<Edge> edgesToWrite = new ArrayList<Edge>();
				edgesToWrite.addAll(edges);
				writeEdges(edgesToWrite, outputStream);
			}
		}
		outputStream.close();
	}

	/**
	 * Write all position data.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeAllPositionData() throws IOException
	{
		FileOutputStream outputStream = new FileOutputStream(getPositionDataHandleForWriting());
		for (TableTarget tt : TeacherControlComms.get().getTableTargets())
		{
			if (tt.getTarget() != null)
			{
				double target = tt.getTarget();
				DistributedMap<String, ExpressionVisibleProperties> vismap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(target);
				List<ExpressionVisibleProperties> visProps = new ArrayList<ExpressionVisibleProperties>();
				visProps.addAll(vismap.values());
				writeVisProperties(visProps, outputStream);
			}
		}
		outputStream.close();
	}

	/**
	 * Write edges.
	 *
	 * @param edges
	 *            the edges
	 * @param outputStream
	 *            the output stream
	 */
	private void writeEdges(Collection<Edge> edges, FileOutputStream outputStream)
	{
		xstream.toXML(edges, outputStream);
	}

	/**
	 * Write expression data for target.
	 *
	 * @param target
	 *            the target
	 * @param outputStream
	 *            the output stream
	 */
	private void writeExpressionDataForTarget(Double target, FileOutputStream outputStream)
	{
		List<Expression> expressions = TeacherControlComms.get().getExpressionsForTarget(target);
		writeExpressionsForTarget(target, expressions, outputStream);
	}

	/**
	 * Write expressions for target.
	 *
	 * @param target
	 *            the target
	 * @param expressions
	 *            the expressions
	 * @param outputStream
	 *            the output stream
	 */
	private void writeExpressionsForTarget(Double target, List<Expression> expressions, FileOutputStream outputStream)
	{
		xstream.toXML(expressions, outputStream);
	}

	/**
	 * Write vis properties.
	 *
	 * @param visProps
	 *            the vis props
	 * @param outputStream
	 *            the output stream
	 */
	private void writeVisProperties(List<ExpressionVisibleProperties> visProps, FileOutputStream outputStream)
	{
		xstream.toXML(visProps);
	}

}

package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class TableTarget.
 */
public class TableTarget implements Serializable, IsSerializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -536053704733381831L;

	/** The table. */
	private String table;

	/** The target. */
	private Double target;

	/**
	 * Instantiates a new table target.
	 */
	public TableTarget()
	{
		this.table = "";
		this.target = null;
	}

	/**
	 * Instantiates a new table target.
	 *
	 * @param table
	 *            the table
	 * @param target
	 *            the target
	 */
	public TableTarget(String table, Double target)
	{
		this.table = table;
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof TableTarget)
		{
			TableTarget tt = (TableTarget) obj;
			return tt.getTable().equals(getTable()) && (tt.getTarget() == getTarget());
		}
		return false;
	}

	/**
	 * Gets the table.
	 *
	 * @return the table
	 */
	public String getTable()
	{
		return table;
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public Double getTarget()
	{
		return target;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return table + "->" + target;
	}
}

package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TableTarget implements Serializable, IsSerializable {
	private static final long serialVersionUID = -536053704733381831L;
	private String table;
	private Double target;
	
	public TableTarget() {
		this.table = "";
		this.target = null;
	}

	public TableTarget(String table, Double target) {
		this.table = table;
		this.target = target;
	}

	public String getTable() {
		return table;
	}

	public Double getTarget() {
		return target;
	}
	
	public String toString() {
		return table + "->" + target;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof TableTarget) {
			TableTarget tt = (TableTarget)obj;
			return tt.getTable().equals(getTable()) && tt.getTarget() == getTarget();
		}
		return false;
	}
}

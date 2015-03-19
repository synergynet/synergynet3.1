package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Expression implements Serializable, IsSerializable {
	private static final Logger log = Logger.getLogger(Expression.class.getName());
	
	private static final long serialVersionUID = -8512137832767638454L;
	
	private String id;
	private String expression;
	private double value;
	private double target;
	private String error;
	private Date createdDate;
	private String createdBy;
	private String createdOnTable;
	private boolean isEdit;
	
	public Expression() {}

	public Expression(String id, String expr, double value, double target, String error, String createdBy, String onTable, boolean isEdit) {
		this.setId(id);
		this.setCreatedBy(createdBy);
		this.setCreatedOnTable(onTable);
		this.setCreatedDate(new Date());
		this.setExpression(expr);
		this.setValue(value);
		this.setTarget(target);
		this.setError(error);
		this.setEdit(isEdit);
		log.fine("Created " + this.getFullString());
	}

	public String getFullString() {
		return
		"Expression: " +
		"\n   id: " + id +
		"\n   by: " + createdBy +
		"\n   on: " + createdOnTable +
		"\n    e: " + expression +
		"\n    v: " + value +
		"\n    t: " + target +
		"\n  err: " + error +
		"\n   ed: " + isEdit +
		"\n";
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public double getTarget() {
		return target;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	
	public String toString() {
		return getValue() + " [" + getExpression() + "] target: " + target;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedOnTable(String onTable) {
		this.createdOnTable = onTable;
	}

	public String getCreatedOnTable() {
		return createdOnTable;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Expression)) return false;
		Expression e = (Expression) obj;
		if(e.getId().equals(getId())) return true;
 		if(!e.getExpression().equals(getExpression())) return false;
		if(!(e.getTarget() == (getTarget()))) return false;
		if(!(e.getValue() == (getValue()))) return false;
		if(!e.getCreatedBy().equals(getCreatedBy())) return false;
		if(!e.getCreatedOnTable().equals(getCreatedOnTable())) return false;
		return true;
	}

	public boolean isCorrect() {
		return Math.abs(value - target) < 0.00001;
	}

	public boolean isErroneous() {
		return error != null;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}

package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Expression.
 */
public class Expression implements Serializable, IsSerializable {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(Expression.class
			.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8512137832767638454L;

	/** The created by. */
	private String createdBy;

	/** The created date. */
	private Date createdDate;

	/** The created on table. */
	private String createdOnTable;

	/** The error. */
	private String error;

	/** The expression. */
	private String expression;

	/** The id. */
	private String id;

	/** The is edit. */
	private boolean isEdit;

	/** The target. */
	private double target;

	/** The value. */
	private double value;

	/**
	 * Instantiates a new expression.
	 */
	public Expression() {
	}

	/**
	 * Instantiates a new expression.
	 *
	 * @param id the id
	 * @param expr the expr
	 * @param value the value
	 * @param target the target
	 * @param error the error
	 * @param createdBy the created by
	 * @param onTable the on table
	 * @param isEdit the is edit
	 */
	public Expression(String id, String expr, double value, double target,
			String error, String createdBy, String onTable, boolean isEdit) {
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Expression)) {
			return false;
		}
		Expression e = (Expression) obj;
		if (e.getId().equals(getId())) {
			return true;
		}
		if (!e.getExpression().equals(getExpression())) {
			return false;
		}
		if (!(e.getTarget() == (getTarget()))) {
			return false;
		}
		if (!(e.getValue() == (getValue()))) {
			return false;
		}
		if (!e.getCreatedBy().equals(getCreatedBy())) {
			return false;
		}
		if (!e.getCreatedOnTable().equals(getCreatedOnTable())) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Gets the created on table.
	 *
	 * @return the created on table
	 */
	public String getCreatedOnTable() {
		return createdOnTable;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Gets the expression.
	 *
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Gets the full string.
	 *
	 * @return the full string
	 */
	public String getFullString() {
		return "Expression: " + "\n   id: " + id + "\n   by: " + createdBy
				+ "\n   on: " + createdOnTable + "\n    e: " + expression
				+ "\n    v: " + value + "\n    t: " + target + "\n  err: "
				+ error + "\n   ed: " + isEdit + "\n";
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public double getTarget() {
		return target;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Checks if is correct.
	 *
	 * @return true, if is correct
	 */
	public boolean isCorrect() {
		return Math.abs(value - target) < 0.00001;
	}

	/**
	 * Checks if is edits the.
	 *
	 * @return true, if is edits the
	 */
	public boolean isEdit() {
		return isEdit;
	}

	/**
	 * Checks if is erroneous.
	 *
	 * @return true, if is erroneous
	 */
	public boolean isErroneous() {
		return error != null;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy the new created by
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Sets the created on table.
	 *
	 * @param onTable the new created on table
	 */
	public void setCreatedOnTable(String onTable) {
		this.createdOnTable = onTable;
	}

	/**
	 * Sets the edits the.
	 *
	 * @param isEdit the new edits the
	 */
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Sets the expression.
	 *
	 * @param expression the new expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(double target) {
		this.target = target;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getValue() + " [" + getExpression() + "] target: " + target;
	}
}

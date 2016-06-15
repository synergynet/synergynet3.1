package synergynet3.activitypack1.table.gravitysim.model;

/**
 * The Class MassReference.
 */
public class MassReference
{

	/** The mass. */
	private double mass;

	/**
	 * Instantiates a new mass reference.
	 *
	 * @param mass
	 *            the mass
	 */
	public MassReference(double mass)
	{
		this.setMass(mass);
	}

	/**
	 * Gets the mass.
	 *
	 * @return the mass
	 */
	public double getMass()
	{
		return mass;
	}

	/**
	 * Sets the mass.
	 *
	 * @param mass
	 *            the new mass
	 */
	public void setMass(double mass)
	{
		this.mass = mass;
	}
}

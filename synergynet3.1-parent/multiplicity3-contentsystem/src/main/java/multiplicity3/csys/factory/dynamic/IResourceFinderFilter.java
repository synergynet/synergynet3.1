package multiplicity3.csys.factory.dynamic;

/**
 * The Interface IResourceFinderFilter.
 */
public interface IResourceFinderFilter
{

	/**
	 * Accept.
	 *
	 * @param dottedResourcePathname
	 *            the dotted resource pathname
	 * @return true, if successful
	 */
	boolean accept(String dottedResourcePathname);

}

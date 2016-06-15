package multiplicity3.csys.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import multiplicity3.csys.items.item.IItem;

/**
 * The Interface ImplementsContentItem.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementsContentItem
{

	/**
	 * Target.
	 *
	 * @return the class<? extends i item>
	 */
	Class<? extends IItem> target();
}

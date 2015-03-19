package multiplicity3.csys.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import multiplicity3.csys.items.item.IItem;

@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementsContentItem {
	Class<? extends IItem> target();
}

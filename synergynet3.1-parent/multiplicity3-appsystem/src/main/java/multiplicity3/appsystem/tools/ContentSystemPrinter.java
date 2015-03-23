package multiplicity3.appsystem.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * The Class ContentSystemPrinter.
 */
public class ContentSystemPrinter {

	/**
	 * Log content system by z order from root.
	 *
	 * @param log the log
	 * @param level the level
	 * @param root the root
	 */
	public static void logContentSystemByZOrderFromRoot(Logger log,
			Level level, IItem root) {
		int indent = 0;
		printContentSystemFromItemWithIndent(log, level, root, indent);
	}

	/**
	 * Log jme structure.
	 *
	 * @param log the log
	 * @param level the level
	 * @param root the root
	 */
	public static void logJMEStructure(Logger log, Level level, Spatial root) {
		int indent = 0;
		printJMEStructureFromNodeWithIndent(log, level, root, indent);
	}

	/**
	 * Gets the item string.
	 *
	 * @param item the item
	 * @return the item string
	 */
	private static String getItemString(IItem item) {
		return item.getZOrder() + ": " + item.toString() + '\n';
	}

	/**
	 * Gets the item string.
	 *
	 * @param item the item
	 * @return the item string
	 */
	private static String getItemString(Spatial item) {
		return item.getName() + ":" + item.getClass().getName() + '@'
				+ item.getWorldTranslation() + '\n';
	}

	/**
	 * Gets the n spaces.
	 *
	 * @param indent the indent
	 * @return the n spaces
	 */
	private static String getNSpaces(int indent) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	/**
	 * Items children.
	 *
	 * @param item the item
	 * @param indent the indent
	 * @return the string
	 */
	private static String itemsChildren(IItem item, int indent) {
		StringBuffer sb = new StringBuffer();
		String indentSpaces = getNSpaces(indent * 3);
		for (IItem i : item.getChildItems()) {
			sb.append(indentSpaces + getItemString(i));
			sb.append(itemsChildren(i, indent + 1));
		}
		return sb.toString();
	}

	/**
	 * Items children.
	 *
	 * @param item the item
	 * @param indent the indent
	 * @return the string
	 */
	private static String itemsChildren(Spatial item, int indent) {
		String indentSpaces = getNSpaces(indent * 3);
		StringBuffer sb = new StringBuffer();

		if (item instanceof Node) {
			for (Spatial i : ((Node) item).getChildren()) {
				sb.append(indentSpaces + getItemString(i));
				sb.append(itemsChildren(i, indent + 1));
			}
		}

		return sb.toString();
	}

	/**
	 * Prints the content system from item with indent.
	 *
	 * @param log the log
	 * @param level the level
	 * @param item the item
	 * @param indent the indent
	 */
	private static void printContentSystemFromItemWithIndent(Logger log,
			Level level, IItem item, int indent) {
		StringBuffer sb = new StringBuffer();
		sb.append('\n');
		String indentSpaces = getNSpaces(indent * 3);
		sb.append(indentSpaces + getItemString(item));
		sb.append(itemsChildren(item, indent + 1));

		log.log(level, sb.toString());
	}

	/**
	 * Prints the jme structure from node with indent.
	 *
	 * @param log the log
	 * @param level the level
	 * @param root the root
	 * @param indent the indent
	 */
	private static void printJMEStructureFromNodeWithIndent(Logger log,
			Level level, Spatial root, int indent) {
		StringBuffer sb = new StringBuffer();
		sb.append('\n');
		String indentSpaces = getNSpaces(indent * 3);
		sb.append(indentSpaces + getItemString(root));
		sb.append(itemsChildren(root, indent + 1));

		log.log(level, sb.toString());
	}

}

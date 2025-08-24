/**
 * 
 */
package es.shiftplanner.interfaces;

import java.util.List;

/**
 * 
 */
public interface IDataStructure {
	void load(String line);
	List<String> exportAsCsv();
	boolean isEmpty();
}

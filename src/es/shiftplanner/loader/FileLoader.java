/**
 * 
 */
package es.shiftplanner.loader;

import java.util.Scanner;

import es.shiftplanner.interfaces.IDataStructure;

/**
 * 
 */
public final class FileLoader {
	private final Scanner _reader;

	public FileLoader(Readable reader) {
		this._reader = new Scanner(reader);
	}

	public void loadData(IDataStructure structure) {
		while (this._reader.hasNextLine()) {
			String data = this._reader.nextLine();
			structure.load(data);
		}
		this._reader.close();
	}

}

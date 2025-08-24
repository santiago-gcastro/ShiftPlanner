/**
 * 
 */
package es.shiftplanner.loader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import es.shiftplanner.interfaces.IDataStructure;
/**
 * 
 */
public class FileExporter {
	private final BufferedWriter _writer;

	public FileExporter(Writer writer) {
		this._writer = new BufferedWriter(writer);
	}

	public void saveAsCsv(IDataStructure dataStructure) throws IOException {
		for (String line : dataStructure.exportAsCsv()) {
			this._writer.write(line);
			this._writer.newLine();
		}
		this._writer.close();
	}
}

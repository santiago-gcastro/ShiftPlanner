package es.main;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import es.main.exception.DataErrorException;
import es.main.logger.Logger;
import es.shiftplanner.configuration.Configuration;
import es.shiftplanner.data.ShiftTable;
import es.shiftplanner.loader.FileExporter;
import es.shiftplanner.loader.FileLoader;

public class ShiftPlanner {

	public static void main(String[] args) {
		Logger.getInstance().log("INICIANDO PLANIFICADOR DE CARTELERAS. Versión 1.5");
		try {
			FileLoader configLoader = new FileLoader(new InputStreamReader(new FileInputStream(new File(CONFIG_FILE)), FILES_CHARSET_NAME));
			Configuration configuration = new Configuration();
			configLoader.loadData(configuration);
			Logger.getInstance().log("Datos importados desde " + configuration.getInputFile());
			FileLoader inputLoader = new FileLoader(new InputStreamReader(new FileInputStream(new File(configuration.getInputFile())), FILES_CHARSET_NAME));
			ShiftTable shiftTable = new ShiftTable().setConfiguration(configuration);
			inputLoader.loadData(shiftTable);
			if (shiftTable.isEmpty()) {
				throw new DataErrorException("No se han encontrado turnos en el archivo de entrada");
			}
			FileExporter exporter = new FileExporter(new FileWriter(new File(configuration.getOutputFile()), Charset.forName(FILES_CHARSET_NAME)));
			exporter.saveAsCsv(shiftTable);
			Logger.getInstance().log("Datos exportados en " + configuration.getOutputFile());
			System.exit(EXIT_SUCCESS);
		} catch (FileNotFoundException e) {
			Logger.getInstance().log(e);
			System.exit(EXIT_FAILURE);
		} catch (DataErrorException exception) {
			Logger.getInstance().log(exception.getMessage());
			System.exit(EXIT_FAILURE);
		} catch (UnsupportedEncodingException exception) {
			Logger.getInstance().log(exception);
			System.exit(EXIT_FAILURE);
		} catch (IOException ioException) {
			Logger.getInstance().log(ioException);
			System.exit(EXIT_FAILURE);
		} catch (Exception exception) {
			Logger.getInstance().log(exception.getMessage());
			System.exit(EXIT_FAILURE);
		}
	}
	private static final String CONFIG_FILE = "config.cfg";
	private static final String FILES_CHARSET_NAME = "windows-1252";
	private static final int EXIT_FAILURE = 1;
	private static final int EXIT_SUCCESS = 0;

}

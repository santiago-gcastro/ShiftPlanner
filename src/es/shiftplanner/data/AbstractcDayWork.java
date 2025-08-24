/**
 * 
 */
package es.shiftplanner.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.main.common.CommonUtils;
import es.main.exception.DataErrorException;
import es.shiftplanner.configuration.Configuration;

/**
 * 
 */
public abstract class AbstractcDayWork {
	protected Configuration configuration;
	private Map<String, String> indexData;
	public AbstractcDayWork(String pLine, String[] pHeader, Configuration pConfiguration) {
		this.configuration = pConfiguration;
		if (pHeader != null && pHeader.length >= 0) {
			this.indexData = new HashMap<String, String>();
			String[] splitedLine = CommonUtils.split(pLine, CommonUtils.CSV_DELIM);
			for (int i = 0; i < splitedLine.length; i++) {
				indexData.put(pHeader[i], this.normalizeField(pHeader[i], splitedLine[i]));
			}
		} else {
			throw new DataErrorException("No se ha encontrado la cabecera en el archivo o la cabecera no contiene los campos correctos. La cabecera del CSV debe de ser la primera línea.");
		}
	}
	public final void setStatus(String pStatus) {
		String statusField = this.getConfiguration().getStatusField();
		this.indexData.put(statusField, pStatus);
	}
	public final Date getDate() {
		String dateField = this.getConfiguration().getDateField();
		return CommonUtils.parseDate(this.getIndexData().get(dateField));
	}
	public final String getJob() {
		String jobField = this.getConfiguration().getJobField();
		return this.getIndexData().get(jobField);
	}
	public final String getShift() {
		String shiftField = this.getConfiguration().getShiftField();
		return this.getIndexData().get(shiftField);
	}
	public final String getStaffNumber() {
		String staffField = this.getConfiguration().getStaffField();
		return this.getIndexData().get(staffField);
	}
	public final String dayOfTheWeek() {
		Date dateField = this.getDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE", Locale.of("es", "ES"));
		return simpleDateFormat.format(dateField);
	}
	public final String getDateFormatted() {
		String dateField = this.getConfiguration().getDateField();
		return this.getIndexData().get(dateField);
	}
	public final String key() {
		String outKey = "";
		for (String key : this.getConfiguration().getUniqueKeyFields()) {
			if (indexData.containsKey(key)) {
				outKey = String.join("", outKey, indexData.get(key));
			}
		}
		return outKey;
	}
	public final String getLine() {
		String[] header = this.getConfiguration().getOutputColumns();
		List<String> outLine = new ArrayList<String>();
		for (String field : header) {
			outLine.add(this.getIndexData().get(field));
		}
		return String.join(CommonUtils.CSV_DELIM, outLine);
	}
	protected final Configuration getConfiguration() {
		if (this.configuration != null) {
			return this.configuration;
		}
		return new Configuration();
	}
	protected final Map<String, String> getIndexData() {
		return this.indexData;
	}
	private String normalizeField(String field, String value) {
		if (this.getConfiguration().getShiftField().equals(field)) {
			if (Arrays.asList(this.getConfiguration().getFixedMorningShiftTag()).contains(value)) {
				return Shift.M.getName();
			}
		}
		return value;
	}
}

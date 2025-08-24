/**
 * 
 */
package es.shiftplanner.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.main.common.CommonUtils;
import es.shiftplanner.interfaces.IDataStructure;

/**
 * 
 */
public final class Configuration implements IDataStructure {
	private Map<String, String> table;
	
	public Configuration() {
		this.table = new HashMap<String, String>();
	}
	
	@Override
	public List<String> exportAsCsv() {
		return new ArrayList<String>();
	}
	@Override
	public void load(String line) {
		if (line != null && line.contains(CONFIG_FIELD_DELIM) && !line.startsWith(CONFIG_FIELD_COMMENT)) {
			table.put(line.substring(0, line.indexOf(CONFIG_FIELD_DELIM)), line.substring(line.indexOf(CONFIG_FIELD_DELIM) + 1, line.length()));
		}
	}
	
	@Override
	public boolean isEmpty() {
		return this.table.isEmpty();
	}

	public final String getHeadlineIdentifier() {
		if (table.containsKey(HEADLINE_IDENTIFIER)) {
			return table.get(HEADLINE_IDENTIFIER);
		}
		return "Fecha";
	}
	public final String[] getUniqueKeyFields() {
		if (table.containsKey(UNIQUE_KEY_FIELDS)) {
			return CommonUtils.split(table.get(UNIQUE_KEY_FIELDS), CONFIG_DATA_DELIM);
		}
		return new String[] {"Fecha", "Abrev.objeto", "Abreviatura de turno"};
	}
	public final String[] getOrderFields() {
		if (table.containsKey(ORDER_FIELDS)) {
			Map<String, String> fieldsMapping = new LinkedHashMap<String, String>();
			fieldsMapping.put(this.getDateField(), DATE);
			fieldsMapping.put(this.getJobField(), JOB);
			fieldsMapping.put(this.getShiftField(), SHIFT);
			fieldsMapping.put(this.getStaffField(), STAFF);
			List<String> orderFields = Arrays.asList(CommonUtils.split(table.get(ORDER_FIELDS), CONFIG_DATA_DELIM));
			List<String> outOrder = new ArrayList<String>();
			for (String field : orderFields) {
				outOrder.add(fieldsMapping.get(field));
			}
			return outOrder.toArray(new String[outOrder.size()]);
		}
		return new String[] {DATE, JOB, SHIFT, STAFF};
	}
	public final String[] getOutputColumns() {
		if (table.containsKey(OUTPUT_COLUMNS)) {
			return CommonUtils.split(table.get(OUTPUT_COLUMNS), CONFIG_DATA_DELIM);
		}
		return new String[] {"Fecha", "Abrev.objeto", "Número de personal", "Nombre", "Abreviatura de turno", "Estado"};
	}
	public final String[] getDaysOfWeek() {
		if (table.containsKey(DAYS_OF_WEEK)) {
			return CommonUtils.split(table.get(DAYS_OF_WEEK), CONFIG_DATA_DELIM);
		}
		return new String[] {"lun","mar","mié","jue","vie","sáb","dom"};
	}
	public final String getDateField() {
		if (table.containsKey(DATE)) {
			return table.get(DATE);
		}
		return "Fecha";
	}
	public final String getJobField() {
		if (table.containsKey(JOB)) {
			return table.get(JOB);
		}
		return "Abrev.objeto";
	}
	public final String getShiftField() {
		if (table.containsKey(SHIFT)) {
			return table.get(SHIFT);
		}
		return "Abreviatura de turno";
	}
	public final String getStaffField() {
		if (table.containsKey(STAFF)) {
			return table.get(STAFF);
		}
		return "Número de personal";
	}
	public final String getStatusField() {
		if (table.containsKey(STATUS)) {
			return table.get(STATUS);
		}
		return "Estado";
	}
	public final String getRowTemplate() {
		if (table.containsKey(ROW_TEMPLATE)) {
			return table.get(ROW_TEMPLATE);
		}
		return "#date#;#job#;99999;\"XXXXX XXXXX XXXXX\";#shift#";
	}
	public final String[] getJobsForTemplate() {
		if (table.containsKey(JOBS_FOR_TEMPLATE)) {
			return CommonUtils.split(table.get(JOBS_FOR_TEMPLATE), CONFIG_DATA_DELIM);
		}
		return new String[] {"ATS/DUE","Aux.Enf."};
	}
	public final String[] getShiftsForTemplate() {
		if (table.containsKey(SHIFTS_FOR_TEMPLATE)) {
			return CommonUtils.split(table.get(SHIFTS_FOR_TEMPLATE), CONFIG_DATA_DELIM);
		}
		return new String[] {"M","T","N"};
	}
	public final String[] getStaffMembers() {
		if (table.containsKey(STAFF_LIST)) {
			return CommonUtils.split(table.get(STAFF_LIST), CONFIG_DATA_DELIM);
		}
		return new String[] {};
	}
	public final String getStatusOk() {
		if (table.containsKey(STATUS_OK_MSG)) {
			return table.get(STATUS_OK_MSG);
		}
		return "Ok";
	}
	public final String getStatusOverdue() {
		if (table.containsKey(STATUS_OVERDUE_MSG)) {
			return table.get(STATUS_OVERDUE_MSG);
		}
		return "ESTOY de MÁS";
	}
	public final String getStatusGap() {
		if (table.containsKey(STATUS_GAP_MSG)) {
			return table.get(STATUS_GAP_MSG);
		}
		return "NADIE AQUÍ";
	}
	public final String[] getFixedMorningShiftTag() {
		if (table.containsKey(FIXED_MORNING_SHIFT_TAG)) {
			return CommonUtils.split(table.get(FIXED_MORNING_SHIFT_TAG), CONFIG_DATA_DELIM);
		}
		return new String[] {};
	}
	public final Integer getJobsShiftsToCoverPerday(String job, String shift) {
		String key = job + "_" + shift;
		if (table.containsKey(key)) {
			return Integer.valueOf(table.get(key));
		}
		return Integer.valueOf("3");
	}
	public final String getInputFile() {
		if (table.containsKey(INPUT_FILE)) {
			return table.get(INPUT_FILE);
		}
		return "input.csv";
	}
	public final String getOutputFile() {
		if (table.containsKey(OUTPUT_FILE)) {
			return table.get(OUTPUT_FILE);
		}
		return "output.csv";
	}
	public static final String DATE_TOKEN = "#date#";
	public static final String JOB_TOKEN = "#job#";
	public static final String SHIFT_TOKEN = "#shift#";
	private static final String HEADLINE_IDENTIFIER = "headline_identifier";
	private static final String UNIQUE_KEY_FIELDS = "unique_key_fields";
	private static final String ORDER_FIELDS = "order_by_fields";
	private static final String OUTPUT_COLUMNS = "output_columns";
	private static final String STATUS = "status";
	public static final String DATE = "date";
	public static final String JOB = "job";
	public static final String SHIFT = "shift";
	private static final String DAYS_OF_WEEK = "days_of_week";
	private static final String ROW_TEMPLATE = "row_template";
	private static final String JOBS_FOR_TEMPLATE = "jobs_for_template";
	private static final String SHIFTS_FOR_TEMPLATE = "shifts_for_template";
	private static final String STAFF_LIST = "staff_members";
	public static final String STAFF = "staff";
	private static final String STATUS_OK_MSG = "status_ok_msg";
	private static final String STATUS_GAP_MSG = "status_gap_msg";
	private static final String STATUS_OVERDUE_MSG = "status_overdue_msg";
	private static final String FIXED_MORNING_SHIFT_TAG = "fixed_morning_shift_tag";
	private static final String INPUT_FILE = "input_file";
	private static final String OUTPUT_FILE = "output_file";
	private static final String CONFIG_DATA_DELIM = ",";
	private static final String CONFIG_FIELD_DELIM = "=";
	private static final String CONFIG_FIELD_COMMENT = "#";
}

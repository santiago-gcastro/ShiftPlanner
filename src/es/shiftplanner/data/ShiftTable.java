/**
 * 
 */
package es.shiftplanner.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.main.common.CommonUtils;
import es.main.common.DateIterator;
import es.main.logger.Logger;
import es.shiftplanner.configuration.Configuration;
import es.shiftplanner.interfaces.IDataStructure;

/**
 * 
 */
public final class ShiftTable implements IDataStructure {
	private String[] headerFields;
	private Configuration configuration;
	private Map<String, DayWork> indexShifts;
	private List<DayWork> shifts;
	public ShiftTable() {
		this.indexShifts = new HashMap<String, DayWork>();
		this.shifts = new ArrayList<DayWork>();
	}
	@Override
	public void load(String line) {
		if (!line.isEmpty()) {
			if (this.isHeader(line)) {
				this.headerFields = CommonUtils.split(line, CommonUtils.CSV_DELIM);
			} else {
				DayWork dayWork = new DayWork(line, headerFields, this.getConfiguration());
				dayWork.setStatus(this.getConfiguration().getStatusOk());
				indexShifts.put(dayWork.key(), dayWork);
				shifts.add(dayWork);
			}
		}
	}
	
	@Override
	public boolean isEmpty() {
		return this.shifts.isEmpty();
	}
	public ShiftTable setConfiguration(Configuration pConfiguration) {
		this.configuration = pConfiguration;
		return this;
	}
	@Override
	public List<String> exportAsCsv() {
		List<String> out = new ArrayList<String>();
		out.add(this.getOutputHeader());
		String dayOfTheWeek = null;
		for (DayWork dayWork : this.generateCompleteShift()) {
			if (dayOfTheWeek != null && !dayWork.dayOfTheWeek().equals(dayOfTheWeek)) {
				out.add(CommonUtils.generateBlankLine(Integer.valueOf(configuration.getOutputColumns().length)));
			}
			out.add(dayWork.getLine());
			dayOfTheWeek = dayWork.dayOfTheWeek();
		}
		return out;
	}
	private List<DayWork> generateCompleteShift() {
		List<DayWork> outList = new ArrayList<DayWork>();
		Map<String, List<DayWork>> classifiedShifts =  this.classifyOutputShifts();
		
		List<DayWorkTemplate> template = this.generateShiftTemplates();
		for (DayWorkTemplate dayWorkTemplate : template) {
			int gaps = dayWorkTemplate.getNumber();
			if (classifiedShifts.containsKey(dayWorkTemplate.key())) {
				gaps = dayWorkTemplate.getNumber().intValue() - classifiedShifts.get(dayWorkTemplate.key()).size();
				if (gaps < 0) {
					Logger.getInstance().log("**** WARNING **** Tienes personal " + dayWorkTemplate.getJob() + " de MÁS para el día " + dayWorkTemplate.getDateFormatted() + " en el turno " + dayWorkTemplate.getShift() );
					int flag = 1;
					for (DayWork dayWork : classifiedShifts.get(dayWorkTemplate.key())) {
						if (flag > dayWorkTemplate.getNumber().intValue()) {
							dayWork.setStatus(this.getConfiguration().getStatusOverdue());
						}
						flag = flag + 1;
					}
				}	
			} else {
				classifiedShifts.put(dayWorkTemplate.key(), new ArrayList<DayWork>());
			}
			for (int i = 0; i < gaps; i++) {
				classifiedShifts.get(dayWorkTemplate.key()).add(new DayWork(dayWorkTemplate.getLine(), configuration.getOutputColumns(),this.getConfiguration()));
			}
		} //for
		for (List<DayWork> dayWorkPerDate : classifiedShifts.values()) {
			outList.addAll(dayWorkPerDate);
		}
		outList.sort(new DayWorkComparator(this.getConfiguration()));
		return outList;
	
	}
	private List<DayWork> getOutputList() {
		List<DayWork> outList = new ArrayList<DayWork>();
		for (DayWork dayWork : new ArrayList<DayWork>(this.shifts)) {
			if (dayWork.isValid()) {
				outList.add(dayWork);
			}
		}
		outList.sort(new DayWorkComparator(this.getConfiguration()));
		return outList;
	}
	private String getOutputHeader() {
		String[] header = configuration.getOutputColumns();
		return String.join(CommonUtils.CSV_DELIM, Arrays.asList(header));
	}
	private Boolean isHeader(String line) {
		return line.contains(this.getConfiguration().getHeadlineIdentifier());
	}
	private Configuration getConfiguration() {
		if (this.configuration != null) {
			return this.configuration;
		}
		return new Configuration();
	}
	private DayWork getFirst() {
		List<DayWork> outList = this.getOutputList();
		if (outList.size() >= 1) {
			return outList.get(0);
		}
		throw new RuntimeException("Los campos requeridos para cada fila del archivo de entrada son: "
				+ this.configuration.getDateField() + ", " + this.configuration.getJobField() + " y "
				+ this.configuration.getShiftField() + ". Al menos alguno de los puestos tiene que ser de esta lista "
				+ String.join(", ", this.getConfiguration().getJobsForTemplate()));
	}
	private DayWork getLast() {
		List<DayWork> outList = this.getOutputList();
		if (outList.size() > 1) {
			return outList.get(outList.size() - 1);
		}
		return this.getFirst();
	}
	private Map<String,List<DayWork>> classifyOutputShifts() {
		Map<String, List<DayWork>> out = new HashMap<String, List<DayWork>>();
		for (DayWork dayWork : this.getOutputList()) {
			if (!out.containsKey(dayWork.key())) {
				out.put(dayWork.key(), new ArrayList<DayWork>());
			}
			out.get(dayWork.key()).add(dayWork);
		}
		return out;
	}
	private List<DayWorkTemplate> generateShiftTemplates() {
		String[] header = configuration.getOutputColumns();
		String rowTemplate = null;
		Integer times;
		DayWorkTemplate dayWork;
		List<String> daysOfTheWeek = Arrays.asList(configuration.getDaysOfWeek());
		List<DayWorkTemplate> out = new ArrayList<DayWorkTemplate>();
		for (Date date : new DateIterator(this.getFirst().getDate(), this.getLast().getDate())) {
			for (String job : configuration.getJobsForTemplate()) {
				for (String shift : configuration.getShiftsForTemplate()) {
					times = configuration.getJobsShiftsToCoverPerday(job, shift);
					rowTemplate = configuration.getRowTemplate();
					rowTemplate = rowTemplate.replaceAll(Configuration.DATE_TOKEN, CommonUtils.formatDate(date));
					rowTemplate = rowTemplate.replaceAll(Configuration.JOB_TOKEN, job);
					rowTemplate = rowTemplate.replaceAll(Configuration.SHIFT_TOKEN, shift);
					dayWork = new DayWorkTemplate(rowTemplate, header, this.getConfiguration(), times);
					dayWork.setStatus(this.getConfiguration().getStatusGap());
					if (daysOfTheWeek.contains(dayWork.dayOfTheWeek())) {
						out.add(dayWork);
					}
				}
			}
			
		}
		return out;
	}
}

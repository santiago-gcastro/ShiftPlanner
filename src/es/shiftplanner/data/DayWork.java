/**
 * 
 */
package es.shiftplanner.data;

import java.util.Arrays;

import es.shiftplanner.configuration.Configuration;

/**
 * 
 */
public class DayWork extends AbstractcDayWork {
	public DayWork(String pLine, String[] pHeader, Configuration pConfiguration) {
		super(pLine, pHeader, pConfiguration);
	}
	public Boolean isValid() {
		return this.getDate() != null 
				&& !Job.BLANK.equals(Job.jobValueOf(this.getJob())) 
				&&  Arrays.asList(this.getConfiguration().getJobsForTemplate()).contains(this.getJob())
				&& !Shift.BLANK.equals(Shift.shiftValueOf(this.getShift()));
	}
}

/**
 * 
 */
package es.shiftplanner.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import es.main.logger.Logger;
import es.shiftplanner.configuration.Configuration;

/**
 * 
 */
public class DayWorkComparator implements Comparator<DayWork> {
	private Configuration configuration;
	public DayWorkComparator(Configuration pConfiguration) {
		this.configuration = pConfiguration;
	}

	@Override
	public int compare(DayWork dw1, DayWork dw2) {
		Logger.getInstance().log(Arrays.asList(configuration.getOrderFields()).iterator());
		List<String> orderFields = Arrays.asList(configuration.getOrderFields());
		for (String field : orderFields) {
			if (this.compareField(field, dw1, dw2) != 0) {
				return this.compareField(field, dw1, dw2);
			}
		} // for
		return 0;
	}
	
	private int compareStaff(DayWork dw1, DayWork dw2) {
		List<String> staffMembers = Arrays.asList(configuration.getStaffMembers()); 
		if (dw1.getStaffNumber().equals(dw2.getStaffNumber())) {
			return 0;
		} else if (staffMembers.contains(dw1.getStaffNumber()) && staffMembers.contains(dw2.getStaffNumber())) {
			return 0;
		} else if (!staffMembers.contains(dw1.getStaffNumber())) {
			return 1;
		} else if (!staffMembers.contains(dw2.getStaffNumber())) {
			return -1;
		}
		return 0;
	}
	
	private int compareField(String field, DayWork dw1, DayWork dw2) {
		if (Configuration.DATE.equals(field)) {
			return dw1.getDate().compareTo(dw2.getDate());
		}
		if (Configuration.JOB.equals(field)) {
			return Job.jobValueOf(dw1.getJob()).compareTo(Job.jobValueOf(dw2.getJob()));
		}
		if (Configuration.SHIFT.equals(field)) {
			return Shift.shiftValueOf(dw1.getShift()).compareTo(Shift.shiftValueOf(dw2.getShift()));
		}
		if (Configuration.STAFF.equals(field)) {
			return this.compareStaff(dw1, dw2);
		}
		return 0;
	}

}

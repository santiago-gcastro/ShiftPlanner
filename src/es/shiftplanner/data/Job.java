/**
 * 
 */
package es.shiftplanner.data;

import java.util.Comparator;

/**
 * 
 */
public enum Job implements Comparator<Job> {
	ATS_DUE("ATS/DUE"), AUX_ENF("Aux.Enf.") , SUP_UNIDAD("Sup.Unidad"), BLANK("");
	private String name;
	
	private Job(String pName) {
		this.name = pName;
	}
	@Override
	public int compare(Job job1, Job job2) {
		return job1.ordinal() - job2.ordinal();
	}
	public String getName() {
		return this.name;
	}
	public static Job jobValueOf(String name) {
		Job outJob = null;
		for (Job jobElement : Job.values()) {
			if (jobElement.getName().equals(name)) {
				outJob = jobElement;
			}
		} // for
		if (outJob == null) {
			return Job.BLANK;
		}
		return outJob;
	}
}

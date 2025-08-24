/**
 * 
 */
package es.shiftplanner.data;

import es.shiftplanner.configuration.Configuration;

/**
 * 
 */
public class DayWorkTemplate extends AbstractcDayWork {
	private Integer number;
	public DayWorkTemplate(String pLine, String[] pHeader, Configuration pConfiguration, Integer pNumber) {
		super(pLine, pHeader, pConfiguration);
		this.number = pNumber;
	}
	public final Integer getNumber() {
		return this.number;
	}
}

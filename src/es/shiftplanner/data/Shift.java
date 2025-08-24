/**
 * 
 */
package es.shiftplanner.data;

import java.util.Comparator;

/**
 * 
 */
public enum Shift implements Comparator<Shift> {
	M("M"), T("T") ,N("N"), BLANK("");
	private String name;
	
	private Shift(String pName) {
		this.name = pName;
	}
	@Override
	public int compare(Shift shift1, Shift shift2) {
		return shift1.ordinal() - shift2.ordinal();
	}
	public String getName() {
		return this.name;
	}
	public static Shift shiftValueOf(String name) {
		Shift outShift = null;
		for (Shift shiftElement : Shift.values()) {
			if (shiftElement.getName().equals(name)) {
				outShift = shiftElement;
			}
		}
		if (outShift == null) {
			return Shift.BLANK;
		}
		return outShift;
	}
}

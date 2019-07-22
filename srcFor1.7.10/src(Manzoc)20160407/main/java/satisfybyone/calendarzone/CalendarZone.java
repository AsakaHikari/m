package satisfybyone.calendarzone;

import java.util.List;

import com.ibm.icu.util.Calendar;

public abstract class CalendarZone {
	public String[][] cities;

	public CalendarZone(String[][] cities) {
		this.cities = cities;
	}

	public abstract List<String> getManzocCitiesOnThirtyMinutes(
			Calendar calendar, int thirtyMinutes);
}

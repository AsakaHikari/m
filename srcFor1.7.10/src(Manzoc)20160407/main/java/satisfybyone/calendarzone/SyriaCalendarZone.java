package satisfybyone.calendarzone;

import java.util.ArrayList;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

import java.util.List;


public class SyriaCalendarZone extends CalendarZone {
	public SyriaCalendarZone(String[][] cities) {
		super(cities);
		System.out.println("europe:" + cities.length);
	}

	@Override
	public List<String> getManzocCitiesOnThirtyMinutes(Calendar calendar,
			int thirtyMinutes) {
		List<String> list = new ArrayList<String>();
		Calendar summerTimeStart = new GregorianCalendar(
				calendar.get(Calendar.YEAR), Calendar.MARCH, 1, 0, 0);
		summerTimeStart.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		summerTimeStart
				.set(Calendar.DAY_OF_WEEK_IN_MONTH, summerTimeStart
						.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH));

		Calendar summerTimeEnd = new GregorianCalendar(
				calendar.get(Calendar.YEAR), Calendar.OCTOBER, 1, 0, 0);
		summerTimeEnd.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		summerTimeEnd
				.set(Calendar.DAY_OF_WEEK_IN_MONTH, summerTimeStart
						.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH));

		if (calendar.after(summerTimeStart) && calendar.before(summerTimeEnd)) {
			if (thirtyMinutes < 46) {
				for (String s : this.cities[thirtyMinutes + 2]) {
					list.add(s);
				}
			} else {
				for (String s : this.cities[thirtyMinutes + -46]) {
					list.add(s);
				}
			}
		} else {
			for (String s : this.cities[thirtyMinutes]) {
				list.add(s);
			}
		}
		return list;
	}

}

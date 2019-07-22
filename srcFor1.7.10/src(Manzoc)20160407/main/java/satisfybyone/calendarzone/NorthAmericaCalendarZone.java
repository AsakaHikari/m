package satisfybyone.calendarzone;

import java.util.ArrayList;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

import java.util.List;

public class NorthAmericaCalendarZone extends CalendarZone {
	public NorthAmericaCalendarZone(String[][] cities) {
		super(cities);
		System.out.println("NA:" + cities.length);
	}

	@Override
	public List<String> getManzocCitiesOnThirtyMinutes(Calendar calendar,
			int thirtyMinutes) {
		List<String> list = new ArrayList<String>();
		Calendar summerTimeStart = new GregorianCalendar(
				calendar.get(Calendar.YEAR), Calendar.MARCH, 1, 2, 0);
		summerTimeStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		summerTimeStart.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);

		Calendar summerTimeEnd = new GregorianCalendar(
				calendar.get(Calendar.YEAR), Calendar.NOVEMBER, 1, 2, 0);
		summerTimeEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		summerTimeEnd.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);

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

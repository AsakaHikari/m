package satisfybyone.calendarzone;

import java.util.ArrayList;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

import java.util.List;

import com.ibm.icu.util.PersianCalendar;

public class IranCalendarZone extends CalendarZone {
	public IranCalendarZone(String[][] cities) {
		super(cities);
		System.out.println("iran:" + cities.length);
	}

	@Override
	public List<String> getManzocCitiesOnThirtyMinutes(Calendar calendar,
			int thirtyMinutes) {
		List<String> list = new ArrayList<String>();
		Calendar summerTimeStart = new PersianCalendar(
				calendar.get(Calendar.YEAR), Calendar.JANUARY, 2, 2, 0, 0);

		Calendar summerTimeEnd = new PersianCalendar(
				calendar.get(Calendar.YEAR), Calendar.JUNE, 30, 23, 0, 0);

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

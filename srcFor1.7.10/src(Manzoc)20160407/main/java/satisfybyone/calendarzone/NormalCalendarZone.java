package satisfybyone.calendarzone;

import java.util.ArrayList;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import java.util.List;


public class NormalCalendarZone extends CalendarZone {
	public NormalCalendarZone(String[][] cities) {
		super(cities);
		System.out.println("normal:" + cities.length);
	}

	@Override
	public List<String> getManzocCitiesOnThirtyMinutes(Calendar calendar,
			int thirtyMinutes) {
		List<String> list = new ArrayList<String>();
		for (String s : this.cities[thirtyMinutes]) {
			list.add(s);
		}
		return list;
	}

}

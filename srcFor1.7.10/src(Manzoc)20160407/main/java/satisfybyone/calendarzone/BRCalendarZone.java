package satisfybyone.calendarzone;

import java.util.ArrayList;
import java.util.Date;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

import java.util.List;

import net.minecraft.util.MathHelper;


public class BRCalendarZone extends CalendarZone {
	public BRCalendarZone(String[][] cities) {
		super(cities);
		// System.out.println("OG:"+cities.length);
	}

	@Override
	public List<String> getManzocCitiesOnThirtyMinutes(Calendar calendar,
			int thirtyMinutes) {
		List<String> list = new ArrayList<String>();
		Calendar summerTimeStart = new GregorianCalendar(
				calendar.get(Calendar.YEAR), Calendar.OCTOBER, 1, 0, 0);
		summerTimeStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		summerTimeStart.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);

		Calendar summerTimeEnd = new GregorianCalendar(
				calendar.get(Calendar.YEAR), Calendar.FEBRUARY, 1, 0, 0);
		summerTimeEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		summerTimeEnd.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
		
		if(summerTimeEnd.equals(this.getCarnivalDay(summerTimeEnd)))

		if (!(calendar.after(summerTimeStart) && calendar.before(summerTimeEnd))) {
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
	
	public Calendar getCarnivalDay(Calendar calendar){
		int year=calendar.get(Calendar.YEAR);
		Calendar VE=new GregorianCalendar
				(year,3,MathHelper.floor_double(20.8431 + 0.242194 * ( year - 1980)) - MathHelper.floor_double((year - 1980)/4),0,0);
		int moonPhase=MoonCalc.getMoonPhase(VE.get(Calendar.YEAR),VE.get(Calendar.MONTH),VE.get(Calendar.DAY_OF_MONTH));
		moonPhase=14-moonPhase;
		if(moonPhase<0)moonPhase+=30;
		VE.add(Calendar.DAY_OF_MONTH,moonPhase-57);
		return VE;
	}

}

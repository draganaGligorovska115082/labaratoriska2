package mk.ukim.finki.jmm.todolist.utils;

import java.util.Calendar;
import java.util.Date;

import android.widget.DatePicker;

public class DatePickerUtils {

	public static Date getDate(DatePicker datePicker) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		c.set(Calendar.MONTH, datePicker.getMonth());
		c.set(Calendar.YEAR, datePicker.getYear());

		return c.getTime();
	}

}

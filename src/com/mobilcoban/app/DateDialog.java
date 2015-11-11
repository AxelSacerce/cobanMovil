package com.mobilcoban.app;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class DateDialog extends DialogFragment  implements DatePickerDialog.OnDateSetListener {
	 
	EditText txtDate;
	
	
	public DateDialog(View v)
	{
		txtDate = (EditText) v;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		
		
		return new DatePickerDialog(getActivity(), this, year, month, day); 
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month,
			int day) {
		
		String date = year + "-" + (month+1) + "-" + day ;
		
		txtDate.setText(date);
		
		
	}
	
	

	

	
}

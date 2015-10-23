package com.mobilcoban.fragment;
		
import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;


public class ErrorDialogoFragment extends DialogFragment 
{
	private Dialog dialog = null;
	public ErrorDialogoFragment()
	{
		super();
	}
	
	public void setDialog(Dialog dialog)
	{
		this.dialog = dialog;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstance)
	{
		return dialog;
	}

	
}

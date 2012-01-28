package com.osesm.randy.lab;

import android.content.Context;
import android.widget.TextView;

public class FPSView extends TextView {

	public FPSView(Context context) {
		super(context);
		
		setText("Status bar initialized...");
		setTextColor(0xFFF06D2F);
		setTextSize(25.0f);			
	}

}

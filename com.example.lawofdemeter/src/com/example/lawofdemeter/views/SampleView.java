package com.example.lawofdemeter.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;


public class SampleView extends ViewPart {

	private Text text;

	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
		text.append("Hello Kitty!");
	}

	@Override
	public void setFocus() {
		
	}

	public void append(String string)  {
		text.append(string);
		text.append("\n");
	};
	
}
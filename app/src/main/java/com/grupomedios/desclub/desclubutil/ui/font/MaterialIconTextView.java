package com.grupomedios.desclub.desclubutil.ui.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MaterialIconTextView extends TextView {

	private static Typeface sMaterialIcon;

	public MaterialIconTextView(Context context) {
		super(context);
		if (isInEditMode()) return; //Won't work in Eclipse graphical layout
		setTypeface();
	}

	public MaterialIconTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		setTypeface();
	}

	public MaterialIconTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		setTypeface();
	}
	
	private void setTypeface() {
		if (sMaterialIcon == null) {
			sMaterialIcon = Typeface.createFromAsset(getContext().getAssets(), "fonts/materialdesignicons-webfont.ttf");
		}
		setTypeface(sMaterialIcon);
	}
}

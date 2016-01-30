package com.nuttwarunyu.blankbook;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dell-NB on 31/1/2559.
 */
public class CustomFont extends TextView {
    public CustomFont(Context context) {
        super(context);
        initTypeFace(null);
    }

    public CustomFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace(attrs);
    }

    public CustomFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTypeFace(attrs);
    }

    private void initTypeFace(AttributeSet attrs) {

        int attrTextStyle = Typeface.NORMAL;
        if (attrs != null) {
            int[] attributes = new int[]{android.R.attr.textStyle};
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, attributes);
            attrTextStyle = typedArray.getInt(0, 0);
            typedArray.recycle();
        }

        String fontPath = "CSPraJad.otf";
        Typeface typeface = getTypeface();
        if (typeface != null) {
            if ((typeface.getStyle() & Typeface.BOLD) == Typeface.BOLD ||
                    (attrTextStyle & Typeface.BOLD) == Typeface.BOLD) {
                fontPath = "CSPraJad-bold.otf";
            } else if ((typeface.getStyle() & Typeface.ITALIC) == Typeface.ITALIC ||
                    (attrTextStyle & Typeface.ITALIC) == Typeface.ITALIC) {
                fontPath = "CSPraJad-Italic";
            }
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontPath));
        }

    }


}

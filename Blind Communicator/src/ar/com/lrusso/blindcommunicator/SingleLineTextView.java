package ar.com.lrusso.blindcommunicator;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.text.TextUtils.*;
import android.text.*;

public class SingleLineTextView extends TextView
	{
	public SingleLineTextView(Context context, AttributeSet attrs, int defStyle)
		{
		super(context, attrs, defStyle);
		setSingleLine();
		setEllipsize(TruncateAt.START);
		}
	
	public SingleLineTextView(Context context, AttributeSet attrs)
		{
		super(context, attrs);
		setSingleLine();
		setEllipsize(TruncateAt.START);
		}
	
	public SingleLineTextView(Context context)
		{
		super(context);
		setSingleLine();
		setEllipsize(TruncateAt.START);
		}
	
	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final Layout layout = getLayout();
		if (layout != null)
			{
			final int lineCount = layout.getLineCount();
			if (lineCount > 0)
				{
				final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
				if (ellipsisCount > 0)
					{
					final float textSize = getTextSize();
					setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize - 1));
					super.onMeasure(widthMeasureSpec, heightMeasureSpec);
					}
				}
			}
		}
	}

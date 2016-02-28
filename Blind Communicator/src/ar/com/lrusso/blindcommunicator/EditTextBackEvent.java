package ar.com.lrusso.blindcommunicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EditTextBackEvent extends EditText
	{
    public EditTextBackEvent(Context context)
    	{
        super(context);
    	}

    public EditTextBackEvent(Context context, AttributeSet attrs)
    	{
        super(context, attrs);
    	}

    public EditTextBackEvent(Context context, AttributeSet attrs, int defStyle)
    	{
        super(context, attrs, defStyle);
    	}

    @Override public boolean onKeyPreIme(int keyCode, KeyEvent event)
    	{
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
        	{
        	try
        		{
        		GlobalVars.inputModeKeyboardTalkbackActivity.finish();
        		}
        		catch(NullPointerException e)
        		{
        		}
        		catch(Exception e)
        		{
        		}
        	}
        return super.dispatchKeyEvent(event);
    	}
	}
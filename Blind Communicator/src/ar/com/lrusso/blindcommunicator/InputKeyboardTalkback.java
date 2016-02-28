package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class InputKeyboardTalkback extends Activity
	{
	EditTextBackEvent textResult;
	
	@Override protected void onCreate(Bundle savedInstanceState)
		{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.inputkeyboardtalkback);
		GlobalVars.lastActivity = InputKeyboardTalkback.class;
		GlobalVars.inputModeKeyboardTalkbackActivity = this;
		textResult = (EditTextBackEvent) findViewById(R.id.textResult);
		//FORCE SOFT KEYBOARD TO SHOW UP, BESIDES THE ANDROIDMANIFEST.XML SPECIFICATION
		try
			{
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}

		TextWatcher fieldValidatorTextWatcher = new TextWatcher()
			{
			@Override public void afterTextChanged(Editable s)
				{
				try
					{
					GlobalVars.tts.stop();
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				}
		
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
				}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count)
				{
				try
					{
					GlobalVars.tts.stop();
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				}
			};
		textResult.addTextChangedListener(fieldValidatorTextWatcher);
		}
	
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = InputKeyboardTalkback.class;
		GlobalVars.talk(getResources().getString(R.string.layoutInputKeyboardTalkbackOnResume));
		}
	    
	public boolean onKeyUp(int keyCode, KeyEvent event)
		{
		try
			{
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
				{
				try
					{
					InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				this.finish();
				}
			else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0)
				{
				if (textResult.getText().toString()!=null)
					{
					if (textResult.getText().toString().length()>0)
						{
						GlobalVars.inputModeResult = textResult.getText().toString();
						}
						else
						{
						GlobalVars.inputModeResult = null;
						}
					}
					else
					{
					GlobalVars.inputModeResult = null;
					}
				GlobalVars.inputModeKeyboardOnlyNumbers = false;
				try
					{
					InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				this.finish();
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return super.onKeyUp(keyCode, event);
		}
	}
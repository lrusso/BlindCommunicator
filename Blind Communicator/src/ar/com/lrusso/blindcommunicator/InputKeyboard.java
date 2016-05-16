package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.widget.TextView;
import android.view.*;
import java.util.*;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class InputKeyboard extends Activity
	{
	private float x1,x2,y1;
	private float y2 = -1;
	public int location = -1;
	private int limit;
	private TextView message;
	private List<String> keyList;
	
	@Override protected void onCreate(Bundle savedInstanceState)
		{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.inputkeyboard);
		GlobalVars.lastActivity = InputKeyboard.class;
		message = (TextView) findViewById(R.id.message);
		String[] original = null;
		if (GlobalVars.inputModeKeyboardOnlyNumbers==true)
			{
			original = getResources().getStringArray(R.array.keysOnlyNumbers);
			}
			else
			{
			original = getResources().getStringArray(R.array.keysAll);
			}
		keyList = new ArrayList<String>(Arrays.asList(original));
		limit = keyList.size() -1;
		location = limit;
		}
		
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = InputKeyboard.class;
		GlobalVars.talk(getResources().getString(R.string.layoutInputKeysOnResume));
		}
		
	@Override public boolean onTouchEvent(MotionEvent event)
		{
		switch(event.getAction())
			{
			case MotionEvent.ACTION_MOVE:
			y1 = event.getY();
			if (y2==-1)
				{
				y2=y1;
				}
			float deltaY = y2 - y1;
			if (Math.abs(deltaY) > GlobalVars.MIN_DISTANCE)
				{
				if (y2 < y1)
					{
					if (location+1>limit)
						{
						location=0;
						}
						else
						{
						location=location+1;
						}
					}
					else 
					{
					if (location-1<0)
						{
						location=limit;
						}
						else
						{
						location=location-1;
						}
					}
				changeCharacter();
				y2=-1;
				}
			break;
			
			case MotionEvent.ACTION_DOWN:
			x1 = event.getX();
			break;

			case MotionEvent.ACTION_UP:
			x2 = event.getX();
			y2=-1;
			float deltaX = x2 - x1;
			if (Math.abs(deltaX) > GlobalVars.MIN_DISTANCE)
				{
				if (x2 > x1)
					{
					nextCharacter();
					}
					else
					{
					deleteLastCharacter();
					}
				}
			break;
			}
		return super.onTouchEvent(event);
		}
		
	public boolean onKeyUp(int keyCode, KeyEvent event)
		{
		try
			{
			String value = null;
			if (keyCode == KeyEvent.KEYCODE_BACK)
				{
				GlobalVars.inputModeResult = null;
				GlobalVars.inputModeKeyboardOnlyNumbers = false;
				this.finish();
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
				removeSpecialKeyText();
				value = message.getText().toString() + getResources().getString(R.string.layoutInputKeysKeyEnter);
				executeEnterKey(value);
				this.finish();
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_SPACE)
				{
				removeSpecialKeyText();
				value = message.getText().toString();
				GlobalVars.talk(value);
				message.setText(value + " _");
				location=limit;
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_DEL)
				{
				removeSpecialKeyText();
				if (message.getText().toString().length()>0)
					{
					value = message.getText().toString();
					message.setText(value.substring(0, value.length()-1) + "_");
					if (value.substring(0,value.length()-1).length()==0)
						{
						GlobalVars.talk(getResources().getString(R.string.layoutInputKeysEmptyField));
						}
						else
						{
						GlobalVars.talk(value.substring(0,value.length()-1));
						}
					location=limit;
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutInputKeysEmptyField));
					}
				if (message.getText().toString().length()==0)
					{
					message.setText("_");
					location=limit;
					}
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
				{
				if (location-1<0)
					{
					location=limit;
					}
					else
					{
					location=location-1;
					}
				changeCharacter();
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
				{
				if (location+1>limit)
					{
					location=0;
					}
					else
					{
					location=location+1;
					}
				changeCharacter();
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
				{
				deleteLastCharacter();
				return true;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
				nextCharacter();
				return true;
				}
			else
				{
				char initialValue = (char) event.getUnicodeChar();
				String finalValue = Character.toString(initialValue).toUpperCase();
				if (keyList.contains(finalValue))
					{
					removeSpecialKeyText();
					message.setText(message.getText().toString() + finalValue + "_");
					GlobalVars.talk(finalValue);
					location=limit;
					}
				return super.onKeyUp(keyCode, event);
				}
			}
			catch(StringIndexOutOfBoundsException e)
			{
			return super.onKeyUp(keyCode, event);
			}
			catch(Exception e)
			{
			return super.onKeyUp(keyCode, event);
			}
		}

	public boolean onKeyDown(int keyCode, KeyEvent event)
		{ 
		if (keyCode == KeyEvent.KEYCODE_BACK)
			{
			return true;
			}
			else
			{
			return super.onKeyDown(keyCode, event);
			}
		}
		
	public void changeCharacter()
		{
		String value = message.getText().toString();
		if (value.endsWith(getResources().getString(R.string.layoutInputKeysKeyEnter)))
			{
			message.setText(value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyEnter).length()) + keyList.get(location));
			}
		else if (value.endsWith(getResources().getString(R.string.layoutInputKeysKeyRead)))
			{
			message.setText(value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyRead).length()) + keyList.get(location));
			}
		else
			{
			message.setText(value.substring(0,value.length()-1) + keyList.get(location));
			}
		if (keyList.get(location).contains(getResources().getString(R.string.layoutInputKeysKeyEnter)))
			{
			GlobalVars.talk(getResources().getString(R.string.layoutInputKeysKeyEnter2));
			}
		else if (keyList.get(location).contains(getResources().getString(R.string.layoutInputKeysKeyRead)))
			{
			if (GlobalVars.inputModeKeyboardOnlyNumbers==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputKeysKeyReadNumber));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputKeysKeyReadText));
				}
			}
		else if (keyList.get(location).contains("_"))
			{
			GlobalVars.talk(getResources().getString(R.string.layoutInputKeysKeySpace));
			}
		else
			{
			GlobalVars.talk(keyList.get(location));	
			}
		}
		
	public void deleteLastCharacter()
		{
		removeSpecialKeyText();
		String value = message.getText().toString();
		if (value.endsWith("_"))
			{
			value = value.substring(0,value.length()-1);
			}
		if (value.length()==0)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutInputKeysEmptyField));
			}
			else
			{
			value = value.substring(0,value.length()-1);
			location=limit;
			if (value.length()==0)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputKeysEmptyField));
				}
				else
				{
				GlobalVars.talk(value);
				}
			}
		message.setText(value + "_");
		}

	public void nextCharacter()
		{
		String value = message.getText().toString();
		if (value.endsWith("_"))
			{
			if (GlobalVars.inputModeKeyboardOnlyNumbers==false)
				{
				if (value.substring(0,value.length()-1).length()>0)
					{
					GlobalVars.talk(value.substring(0,value.length()-1));
					}
				message.setText(value.substring(0,value.length()-1) + " _");
				}
				else
				{
				message.setText(value.substring(0,value.length()-1) + "_");
				}
			
			}
		else if (value.endsWith(getResources().getString(R.string.layoutInputKeysKeyEnter)))
			{
			executeEnterKey(value);
			this.finish();
			}
		else if (value.endsWith(getResources().getString(R.string.layoutInputKeysKeyRead)))
			{
			if (value.length()==getResources().getString(R.string.layoutInputKeysKeyRead).length())
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputKeysNothingToRead));
				}
				else
				{
				if (GlobalVars.inputModeKeyboardOnlyNumbers==true)
					{
					GlobalVars.talk(GlobalVars.divideNumbersWithBlanks(value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyRead).length())));
					}
					else
					{
					GlobalVars.talk(value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyRead).length()));
					}
				}
			}
		else
			{
			GlobalVars.talk(value.substring(value.length()-1,value.length()) + getResources().getString(R.string.layoutInputKeysEntered));
			message.setText(value + "_");
			location=limit;
			}
		}
		
	public void removeSpecialKeyText()
		{
		String value = message.getText().toString();
		if (value.endsWith("_"))
			{
			message.setText(value.substring(0,value.length()-1));
			}
		else if (value.endsWith(getResources().getString(R.string.layoutInputKeysKeyRead)))
			{
			message.setText(value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyRead).length()));
			}
		else if (value.endsWith(getResources().getString(R.string.layoutInputKeysKeyEnter)))
			{
			message.setText(value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyEnter).length()));
			}
		}
		
	public void executeEnterKey(String value)
		{
		try
			{
			if (value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyEnter).length()).length()>0)
				{
				GlobalVars.inputModeResult = value.substring(0,value.length()-getResources().getString(R.string.layoutInputKeysKeyEnter).length());
				}
				else
				{
				GlobalVars.inputModeResult = null;
				}
			GlobalVars.inputModeKeyboardOnlyNumbers = false;
			}
			catch(StringIndexOutOfBoundsException e)
			{
			GlobalVars.inputModeKeyboardOnlyNumbers = false;
			}
			catch(Exception e)
			{
			GlobalVars.inputModeKeyboardOnlyNumbers = false;
			}	
		}
	}
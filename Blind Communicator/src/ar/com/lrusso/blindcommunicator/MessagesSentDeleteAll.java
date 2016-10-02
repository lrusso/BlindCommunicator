package ar.com.lrusso.blindcommunicator;

import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.app.Activity;
import android.database.Cursor;
import ar.com.lrusso.blindcommunicator.R;

public class MessagesSentDeleteAll extends Activity
	{
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.messagessentdeleteall);
		GlobalVars.lastActivity = MessagesSentDeleteAll.class;
		delete = (TextView) findViewById(R.id.messagedelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.messagesSentWereDeleted = false;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
    	}
		
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = MessagesSentDeleteAll.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentDeleteAllOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentDeleteAllDelete));
			break;

			case 2: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			deleteAllSentMessages();
			GlobalVars.messagesSentWereDeleted = true;
			this.finish();
			break;

			case 2: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
		
	@Override public boolean onTouchEvent(MotionEvent event)
		{
		int result = GlobalVars.detectMovement(event);
		switch (result)
			{
			case GlobalVars.ACTION_SELECT:
			select();
			break;

			case GlobalVars.ACTION_EXECUTE:
			execute();
			break;
			}
		return super.onTouchEvent(event);
		}

	public boolean onKeyUp(int keyCode, KeyEvent event)
		{
		int result = GlobalVars.detectKeyUp(keyCode);
		switch (result)
			{
			case GlobalVars.ACTION_SELECT:
			select();
			break;
			
			case GlobalVars.ACTION_EXECUTE:
			execute();
			break;
			}
		return super.onKeyUp(keyCode, event);
		}

	public boolean onKeyDown(int keyCode, KeyEvent event)
		{
		return GlobalVars.detectKeyDown(keyCode);
		}

	private void deleteAllSentMessages()
		{
		try
			{
			Uri sentUri = Uri.parse("content://sms/sent");
			Cursor c = getContentResolver().query(sentUri , null, null, null, null);
			while (c.moveToNext())
				{
			    try
			    	{
			        // Delete the SMS
			        String pid = c.getString(0); // Get id;
			        String uri = "content://sms/" + pid;
			        getContentResolver().delete(Uri.parse(uri), null, null);
			    	}
			    	catch (Exception e)
			    	{
			    	}
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
	}
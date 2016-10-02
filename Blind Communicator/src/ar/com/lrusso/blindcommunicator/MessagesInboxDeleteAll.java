package ar.com.lrusso.blindcommunicator;

import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.app.Activity;
import android.database.Cursor;
import ar.com.lrusso.blindcommunicator.R;

public class MessagesInboxDeleteAll extends Activity
	{
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.messagesinboxdeleteall);
		GlobalVars.lastActivity = MessagesInboxDeleteAll.class;
		delete = (TextView) findViewById(R.id.messagedelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.messagesInboxWereDeleted = false;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
    	}
		
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = MessagesInboxDeleteAll.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxDeleteAllOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxDeleteAllDelete));
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
			deleteAllReceivedMessages();
			GlobalVars.messagesInboxWereDeleted = true;
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

	private void deleteAllReceivedMessages()
		{
		try
			{
			Uri inboxUri = Uri.parse("content://sms/inbox");
			Cursor c = getContentResolver().query(inboxUri , null, null, null, null);
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
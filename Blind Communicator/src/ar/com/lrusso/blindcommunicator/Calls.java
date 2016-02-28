package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Calls extends Activity
	{
	private TextView callslist;
	private TextView deletecalls;
	private TextView makeacall;
	private TextView goback;
	   
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.calls);
		GlobalVars.lastActivity = Calls.class;
		callslist = (TextView) findViewById(R.id.callslist);
		deletecalls = (TextView) findViewById(R.id.deletecalllogs);
		makeacall = (TextView) findViewById(R.id.makeacall);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
        }
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = Calls.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(callslist,false);
		GlobalVars.selectTextView(deletecalls,false);
		GlobalVars.selectTextView(makeacall,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.callLogsDeleted==true)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutCallsOnResume2));
			GlobalVars.callLogsDeleted=false;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutCallsOnResume));
			}
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST CALL LOGS
			GlobalVars.selectTextView(callslist,true);
			GlobalVars.selectTextView(deletecalls,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsMissedCallsList));
			break;

			case 2: //DELETE ALL CALL LOGS
			GlobalVars.selectTextView(deletecalls,true);
			GlobalVars.selectTextView(callslist,false);
			GlobalVars.selectTextView(makeacall,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsDeleteCallLogs2));
			break;
			
			case 3: //MAKE A CALL
			GlobalVars.selectTextView(makeacall, true);
			GlobalVars.selectTextView(deletecalls,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCall));
			break;

			case 4: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(callslist,false);
			GlobalVars.selectTextView(makeacall,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST CALL LOGS
			GlobalVars.startActivity(CallsLogs.class);
			break;
			
			case 2: //DELETE ALL CALL LOGS
			GlobalVars.startActivity(CallsLogsDelete.class);
			break;

			case 3: //MAKE A CALL
			GlobalVars.startActivity(CallsMake.class);
			break;

			case 4: //GO BACK TO THE MAIN MENU
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
	}
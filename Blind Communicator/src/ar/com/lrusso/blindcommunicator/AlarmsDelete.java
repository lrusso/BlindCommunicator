package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class AlarmsDelete extends Activity
	{
	private TextView alarmname;
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarmsdelete);
		GlobalVars.lastActivity = AlarmsDelete.class;
		alarmname = (TextView) findViewById(R.id.alarmsname);
		delete = (TextView) findViewById(R.id.alarmsdelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		
		GlobalVars.setText(alarmname, false,
							GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) + " - " +
							GlobalVars.getAlarmHours(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) + ":" +
							GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) + "\n" +
							GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)));
    	}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = AlarmsDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(alarmname,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ ALARM
			GlobalVars.selectTextView(alarmname,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteSelected) +
							GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsListAt) +
							GlobalVars.getAlarmHours(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
							GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
							GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)));
			break;

			case 2: //DELETE ALARM
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(alarmname,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteDelete));
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(alarmname,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ ALARM
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteSelected) +
							GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsListAt) +
							GlobalVars.getAlarmHours(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
							GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
							GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)));
			break;

			case 2: //DELETE ALARM
			GlobalVars.deleteAlarm(GlobalVars.alarmToDeleteIndex);
			GlobalVars.alarmToDeleteIndex = -1;
			GlobalVars.alarmWasDeleted=true;
			this.finish();
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.alarmToDeleteIndex = -1;
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

package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class AlarmsList extends Activity
	{
	private TextView alarmlist;
	private TextView delete;
	private TextView deleteall;
	private TextView goback;
	private int selectedAlarm = -1;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarmslist);
		GlobalVars.lastActivity = AlarmsList.class;
		alarmlist = (TextView) findViewById(R.id.alarmslist);
		delete = (TextView) findViewById(R.id.alarmsdelete);
		deleteall = (TextView) findViewById(R.id.alarmsdeleteall);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		selectedAlarm = -1;
		
		GlobalVars.setText(alarmlist, false, getResources().getString(R.string.layoutAlarmsListList) + " (" + GlobalVars.alarmList.size() + ")");
    	}
		
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = AlarmsList.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(alarmlist,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(deleteall,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.alarmWasDeleted==true)
			{
			selectedAlarm = -1;
			GlobalVars.setText(alarmlist, false, getResources().getString(R.string.layoutAlarmsListList) + " (" + GlobalVars.alarmList.size() + ")");
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListDeleted));
			GlobalVars.alarmWasDeleted=false;
			}
		else if (GlobalVars.alarmWereDeleted==true)
			{
			selectedAlarm = -1;
			GlobalVars.setText(alarmlist, false, getResources().getString(R.string.layoutAlarmsListList) + " (" + GlobalVars.alarmList.size() + ")");
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListOnResume2));
			GlobalVars.alarmWereDeleted=false;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListOnResume));
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ALARM LIST
			GlobalVars.selectTextView(alarmlist,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedAlarm==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListList) + ". " + GlobalVars.alarmList.size() + getResources().getString(R.string.layoutAlarmsListAlarmAlarmsCount) + " . " + GlobalVars.getPendingAlarmsForTodayCount() + " " + getResources().getString(R.string.layoutAlarmsListForToday));
				}
				else
				{
				GlobalVars.talk(GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(selectedAlarm)) +
								getResources().getString(R.string.layoutAlarmsListAt) +
								GlobalVars.getAlarmHours(GlobalVars.alarmList.get(selectedAlarm)) +
								getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
								GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(selectedAlarm)) +
								getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
								GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(selectedAlarm)));
				}
			break;

			case 2: //DELETE ALARM
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(alarmlist,false);
			GlobalVars.selectTextView(deleteall,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListDelete));
			break;

			case 3: //DELETE ALL ALARMS
			GlobalVars.selectTextView(deleteall, true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteAllDeleteDelete));
			break;

			case 4: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(alarmlist,false);
			GlobalVars.selectTextView(deleteall,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
    
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ALARM LIST
			try
				{
				if (GlobalVars.alarmList.size()>0)
					{
					if (selectedAlarm+1==GlobalVars.alarmList.size())
						{
						selectedAlarm = -1;
						}
					selectedAlarm = selectedAlarm + 1;
					GlobalVars.setText(alarmlist, true, GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(selectedAlarm)) + " - " +
														GlobalVars.getAlarmHours(GlobalVars.alarmList.get(selectedAlarm)) + ":" +
														GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(selectedAlarm)) + "\n" +
														GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(selectedAlarm)));
					GlobalVars.talk(GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(selectedAlarm)) +
									getResources().getString(R.string.layoutAlarmsListAt) +
									GlobalVars.getAlarmHours(GlobalVars.alarmList.get(selectedAlarm)) +
									getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
									GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(selectedAlarm)) +
									getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
									GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(selectedAlarm)));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListNoAlarms));
					}
				}
				catch(Exception e)
				{
				}
			break;

			case 2: //DELETE ALARM
			if (selectedAlarm==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListDeleteSelectError));
				}
				else
				{
				GlobalVars.alarmWasDeleted=false;
				GlobalVars.alarmToDeleteIndex = selectedAlarm;
				GlobalVars.startActivity(AlarmsDelete.class);
				}
			break;

			case 3: //DELETE ALL ALARMS
			GlobalVars.startActivity(AlarmsDeleteAll.class);
			break;

			case 4: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}	
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ALARM LIST
			try
				{
				if (GlobalVars.alarmList.size()>0)
					{
					if (selectedAlarm-1<0)
						{
						selectedAlarm = GlobalVars.alarmList.size();
						}
					selectedAlarm = selectedAlarm - 1;
					GlobalVars.setText(alarmlist, true, GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(selectedAlarm)) + " - " +
														GlobalVars.getAlarmHours(GlobalVars.alarmList.get(selectedAlarm)) + ":" +
											   			GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(selectedAlarm)) + "\n" +
											   			GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(selectedAlarm)));
					GlobalVars.talk(GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(selectedAlarm)) +
									getResources().getString(R.string.layoutAlarmsListAt) +
									GlobalVars.getAlarmHours(GlobalVars.alarmList.get(selectedAlarm)) +
									getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
									GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(selectedAlarm)) +
									getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
									GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(selectedAlarm)));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutAlarmsListNoAlarms));
					}
				}
				catch(Exception e)
				{
				}
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
			
			case GlobalVars.ACTION_SELECT_PREVIOUS:
			previousItem();
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

			case GlobalVars.ACTION_SELECT_PREVIOUS:
			previousItem();
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

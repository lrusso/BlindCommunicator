package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.widget.TextView;
import android.view.*;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class VoiceRecorder extends Activity
	{
	private TextView list;
	private TextView create;
	private TextView goback;

	@Override protected void onCreate(Bundle savedInstanceState)
    	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicerecorder);
		GlobalVars.lastActivity = VoiceRecorder.class;
		list = (TextView) findViewById(R.id.voicerecorderlist);
		create = (TextView) findViewById(R.id.voicerecordercreate);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
    	}

	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = VoiceRecorder.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(list,false);
		GlobalVars.selectTextView(create,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.voiceRecorderAudioWasSaved==true)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderOnResume2));
			GlobalVars.voiceRecorderAudioWasSaved=false;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderOnResume));
			}
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			GlobalVars.selectTextView(list,true);
			GlobalVars.selectTextView(create,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderList));
			break;

			case 2: //CREATE VOICE RECORD
			GlobalVars.selectTextView(create, true);
			GlobalVars.selectTextView(list,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreate));
			break;

			case 3: //GO BACK TO MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(list,false);
			GlobalVars.selectTextView(create,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			GlobalVars.startActivity(VoiceRecorderList.class);
			break;

			case 2: //CREATE VOICE RECORD
			GlobalVars.startActivity(VoiceRecorderCreate.class);
			break;
			
			case 3: //GO BACK TO MAIN MENU
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

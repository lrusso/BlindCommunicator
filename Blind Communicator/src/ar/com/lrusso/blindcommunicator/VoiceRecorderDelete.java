package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;

public class VoiceRecorderDelete extends Activity
	{
	private TextView todelete;
	private TextView delete;
	private TextView goback;
	private MediaPlayer mp = new MediaPlayer();

	@Override protected void onCreate(Bundle savedInstanceState)
    	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicerecorderdelete);
		GlobalVars.lastActivity = VoiceRecorderDelete.class;
		todelete = (TextView) findViewById(R.id.todelete);
		delete = (TextView) findViewById(R.id.delete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.setText(todelete, false, getResources().getString(R.string.layoutVoiceRecorderDeleteToDelete) + (GlobalVars.voiceRecorderToDelete + 1));
    	}

	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = VoiceRecorderDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(todelete,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderDeleteOnResume));
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ VOICE RECORD TO DELETE
			GlobalVars.selectTextView(todelete,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderDeleteDelete) + (GlobalVars.voiceRecorderToDelete + 1) + getResources().getString(R.string.layoutVoiceRecorderDeleteDelete2));
			break;

			case 2: //DELETE VOICE RECORD
			GlobalVars.selectTextView(delete,true);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderDeleteConfirm));
			break;
			
			case 3: //GO BACK TO PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ VOICE RECORD TO DELETE
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
			try
				{
				mp.stop();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp.release();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp = new MediaPlayer();
				mp.setDataSource(GlobalVars.voiceRecorderListFiles.get(GlobalVars.voiceRecorderToDelete));
				mp.prepare();
				mp.setLooping(false);
				mp.start();
				}
				catch(NullPointerException e)
				{
				}
				catch (Exception e)
				{
				}
			break;

			case 2: //DELETE VOICE RECORD
			try
				{
				File toDeleteFile = new File(GlobalVars.voiceRecorderListFiles.get(GlobalVars.voiceRecorderToDelete));
				toDeleteFile.delete();
				}
				catch(IllegalArgumentException e)
				{
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			GlobalVars.voiceRecorderAudioWasDeleted = true;
			this.finish();
			break;

			case 3: //GO BACK TO PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ VOICE RECORD TO DELETE
			try
				{
				mp.stop();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp.release();
				}
				catch(NullPointerException e)
				{
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

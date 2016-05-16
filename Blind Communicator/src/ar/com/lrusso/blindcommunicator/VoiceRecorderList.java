package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.widget.TextView;
import android.view.*;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;
import android.media.*;

public class VoiceRecorderList extends Activity
	{
	private TextView list;
	private TextView play;
	private TextView delete;
	private TextView goback;
	private MediaPlayer mp = new MediaPlayer();
	private int selectedVoiceRecord = -1;

	@Override protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicerecorderlist);
		GlobalVars.lastActivity = VoiceRecorderList.class;
		list = (TextView) findViewById(R.id.voicerecorderlist);
		play = (TextView) findViewById(R.id.voicerecorderplay);
		delete = (TextView) findViewById(R.id.voicerecorderdelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		selectedVoiceRecord = -1;
		new VoiceRecorderListThread().execute("");
		}

	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = VoiceRecorderList.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(list,false);
		GlobalVars.selectTextView(play,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		selectedVoiceRecord = -1;
		GlobalVars.setText(list, false, getResources().getString(R.string.layoutVoiceRecorderListList));
		if (GlobalVars.voiceRecorderAudioWasDeleted==true)
			{
			GlobalVars.voiceRecorderToDelete = -1;
			GlobalVars.voiceRecorderAudioWasDeleted = false;
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListOnResume2));
			new VoiceRecorderListThread().execute("");
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListOnResume));
			}
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			GlobalVars.selectTextView(list,true);
			GlobalVars.selectTextView(play,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListList2));
				}
				else
				{
				if (GlobalVars.voiceRecorderListReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPleaseWait));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + getResources().getString(R.string.layoutVoiceRecorderListItemOf) + GlobalVars.voiceRecorderListFiles.size());
					}
				}
			break;

			case 2: //PLAY VOICE RECORD
			GlobalVars.selectTextView(play, true);
			GlobalVars.selectTextView(list,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPlay2));
			break;

			case 3: //DELETE VOICE RECORD
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(play,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListDelete2));
			break;
			
			case 4: //GO BACK TO PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(list,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			if (GlobalVars.voiceRecorderListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPleaseWait));
				}
				else
				{
				if (GlobalVars.voiceRecorderListFiles.size()>0)
					{
					if (selectedVoiceRecord+1==GlobalVars.voiceRecorderListFiles.size())
						{
						selectedVoiceRecord = -1;
						}
					selectedVoiceRecord = selectedVoiceRecord + 1;
					GlobalVars.setText(list, true, getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + "/" + GlobalVars.voiceRecorderListFiles.size());
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + getResources().getString(R.string.layoutVoiceRecorderListItemOf) + GlobalVars.voiceRecorderListFiles.size());
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListNoVoiceRecords));
					}	
				}
			break;

			case 2: //PLAY VOICE RECORD
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListError));
				}
				else
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
					mp.setDataSource(GlobalVars.voiceRecorderListFiles.get(selectedVoiceRecord));
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
				}
			break;

			case 3: //DELETE VOICE RECORD
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListError));
				}
				else
				{
				GlobalVars.voiceRecorderToDelete = selectedVoiceRecord;
				GlobalVars.startActivity(VoiceRecorderDelete.class);
				}
			break;
			
			case 4: //GO BACK TO PREVIOUS MENU
			this.finish();
			break;
			}
		}
		
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			if (GlobalVars.voiceRecorderListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPleaseWait));
				}
				else
				{
				if (GlobalVars.voiceRecorderListFiles.size()>0)
					{
					if (selectedVoiceRecord-1<0)
						{
						selectedVoiceRecord = GlobalVars.voiceRecorderListFiles.size();
						}
					selectedVoiceRecord = selectedVoiceRecord - 1;
					GlobalVars.setText(list, true, getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + "/" + GlobalVars.voiceRecorderListFiles.size());
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + getResources().getString(R.string.layoutVoiceRecorderListItemOf) + GlobalVars.voiceRecorderListFiles.size());
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListNoVoiceRecords));
					}	
				}
			break;
			
			case 2: //PLAY VOICE RECORD
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListError));
				}
				else
				{
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

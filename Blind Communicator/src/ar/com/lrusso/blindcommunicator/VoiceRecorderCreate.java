package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.content.*;
import android.media.*;
import android.media.MediaPlayer.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import android.content.res.*;

public class VoiceRecorderCreate extends Activity
	{
	private TextView create;
	private TextView goback;
	private MediaRecorder recorder = new MediaRecorder();
	private boolean recording = false;

	@Override protected void onCreate(Bundle savedInstanceState)
    	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicerecordercreate);
		GlobalVars.lastActivity = VoiceRecorderCreate.class;
		create = (TextView) findViewById(R.id.record);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
		GlobalVars.voiceRecorderAudioWasSaved = false;
    	}

	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = VoiceRecorderCreate.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
		GlobalVars.selectTextView(create,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateOnResume));
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CREATE VOICE RECORD
			GlobalVars.selectTextView(create,true);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateCreate2));
			break;

			case 2: //GO BACK TO PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(create,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CREATE VOICE RECORD
			if (recording==false)
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
				recordAudio();
				}
				else
				{
				stopRecording();
				}
			break;

			case 2: //GO BACK TO PREVIOUS MENU
			if (recording==true)
				{
				stopRecording();
				}
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

	private void recordAudio()
		{
		MediaPlayer mp = new MediaPlayer();
		try
			{
			AssetFileDescriptor descriptor = getAssets().openFd("beep.wav");
			mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mp.prepare();
			mp.setVolume(1f, 1f);
			mp.setLooping(false);
			mp.setOnCompletionListener(new OnCompletionListener()
				{
				@Override public void onCompletion(MediaPlayer mp)
					{
					recordAudioStart();
					}
				});
			mp.start();
			}
			catch(NullPointerException e)
			{
			}
			catch (Exception e)
			{
			}
		}
		
	private void recordAudioStart()
		{
		if (isExternalStorageReadable()==true && isExternalStorageWritable()==true)
			{
			recording = true;
			File externalStoragePath = Environment.getExternalStorageDirectory();
			String finalPath = "";
			if (externalStoragePath.toString().endsWith("/"))
				{
				finalPath = externalStoragePath + "BlindCommunicator/Audio";
				}
				else
				{
				finalPath = externalStoragePath + "/BlindCommunicator/Audio";
				}
			File pathChecker = new File(finalPath);
			pathChecker.mkdirs();
			if (pathChecker.exists()==true)
				{
				String year = GlobalVars.getYear();
				String month = GlobalVars.getMonth();
				String day = getDay();
				String hour = getHour();
				String minutes = getMinutes();
				String seconds = getSeconds();
				String fileName = year + "-" + month + "-" + day + " " + hour + "-" + minutes + "-" + seconds + ".mp3";
				ContentValues values = new ContentValues(3);
				values.put(MediaStore.MediaColumns.TITLE, fileName);
				try
					{
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					recorder.setOutputFile(finalPath + "/" + fileName);
					try
						{
						recorder.prepare();
						}
						catch(NullPointerException e)
						{
						}
						catch (Exception e)
						{
						}
					try
						{
						recorder.start();
						}
						catch(NullPointerException e)
						{
						}
						catch(Exception e)
						{
						}
					
					}
					catch(NullPointerException e)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateError));
					}
					catch(IllegalStateException e)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateError));
					}
					catch(Exception e)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateError));
					}
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateError));
				}
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderCreateError));
			}
		}
		
	private void stopRecording()
		{
		recording = false;
		try
			{
			recorder.stop();
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		try
			{
			recorder.release();
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		GlobalVars.voiceRecorderAudioWasSaved = true;
		this.finish();
		}
		
	private boolean isExternalStorageWritable()
		{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
			{
			return true;
			}
		return false;
		}

	private boolean isExternalStorageReadable()
		{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
			{
			return true;
			}
		return false;
		}
		
	public static String getDay()
		{
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(Calendar.getInstance().getTime());
		}
		
	public static String getHour()
		{
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		return sdf.format(Calendar.getInstance().getTime());
		}
		
	public static String getMinutes()
		{
		SimpleDateFormat sdf = new SimpleDateFormat("mm");
		return sdf.format(Calendar.getInstance().getTime());
		}
		
	public static String getSeconds()
		{
		SimpleDateFormat sdf = new SimpleDateFormat("ss");
		return sdf.format(Calendar.getInstance().getTime());
		}
	}

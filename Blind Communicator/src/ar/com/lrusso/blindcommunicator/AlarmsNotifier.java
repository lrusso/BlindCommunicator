package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class AlarmsNotifier extends Activity implements TextToSpeech.OnInitListener
	{
	private static MediaPlayer mediaPlayer;
	public static TextToSpeech tts;
	private TextView alarmmessage;
	private boolean stopWaiting = false;
	
	@Override protected void onCreate(Bundle savedInstanceState)
		{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarmsnotifier);
		GlobalVars.lastActivity = AlarmsNotifier.class;
		
		alarmmessage = (TextView) findViewById(R.id.alarmmessage);
		alarmmessage.setText(GlobalVars.alarmMessage);
		
		GlobalVars.tts.stop();
		
		PowerManager pm = (PowerManager) getSystemService(GlobalVars.context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		if (isScreenOn==false)
			{
			try
				{
				PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
														  PowerManager.ACQUIRE_CAUSES_WAKEUP |
														  PowerManager.ON_AFTER_RELEASE, "TurnOnTheScreenTag");
				wl.acquire();
				wl.release(); 
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			}
		
		//ALLOWS THE ALARM TO BE AT LEAST 2 SECONDS IN THE SCREEN
		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
			{
			@Override public void run()
					{
					stopWaiting = true;
					}
				}, 2000);
		
		//AFTER 60 SECONDS, THE ALARM IS CANCELLED
		Handler handler2 = new Handler();
		handler2.postDelayed(new Runnable()
				{
				@Override public void run()
					{
					try
						{
						cancelAlarm();
						}
						catch (Exception e)
						{
						}
					}
				}, 60000);
				
		playAlarmTone();
		GlobalVars.startTTS(tts);
		tts = new TextToSpeech(this,this);
		tts.setPitch((float) 1.0);
		}
	
	@Override public void onPause()
		{
	    super.onPause();
		try
			{
			GlobalVars.alarmVibrator.cancel();
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
	
    @Override protected void onDestroy()
		{
    	super.onDestroy();
		try
			{
			tts.shutdown();
			}
			catch(Exception e)
			{
			}
		try
			{
			GlobalVars.alarmVibrator.cancel();
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
		
	public void onInit(int status)
		{
		if (status == TextToSpeech.SUCCESS)
			{
			sayAlarm();
			tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener()
				{
				@Override public void onUtteranceCompleted(String utteranceId)
					{
					sayAlarmWithDelay();
					}
				});
			}
		}
		
	public void sayAlarm()
		{
		try
			{
			AudioManager mAudioManager = (AudioManager) GlobalVars.context.getSystemService(GlobalVars.context.AUDIO_SERVICE);
			if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
				{
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
				tts.speak(GlobalVars.alarmMessage,TextToSpeech.QUEUE_FLUSH, params);
				}
			}
			catch(Exception e)
			{
			}
		try
			{
			GlobalVars.alarmVibrator.vibrate(1000);
			}
			catch(Exception e)
			{
			}
		}

	public void sayAlarmWithDelay()
		{
		new Thread()
			{  
			public void run()
				{ 
				try
					{
					sleep(5000);
					}
					catch (InterruptedException e)
					{
						
					}
				sayAlarm();
				}  
			}.start();  
		}
		
	public void cancelAlarm()
		{
		deleteValues();
		try
			{
			GlobalVars.alarmVibrator.cancel();
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		this.finish();
		}
		
	public static void deleteValues()
		{
		GlobalVars.alarmMessage="";
		GlobalVars.alarmDay="";
		GlobalVars.alarmHours="";
		GlobalVars.alarmMinutes="";
		try
			{
			mediaPlayer.stop();
			mediaPlayer = null;
			}
			catch(Exception e)
			{
			}
		}
		
	public static void playAlarmTone()
		{
		try
			{
			mediaPlayer.stop();
			mediaPlayer = null;
			}
			catch(Exception e)
			{
			}
		try
			{
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			mediaPlayer = MediaPlayer.create(GlobalVars.context, notification);
			mediaPlayer.start();
			}
			catch (Exception e)
			{
			}
		}
		
	@Override public boolean onTouchEvent(MotionEvent event)
		{
		int result = GlobalVars.detectMovement(event);
		switch (result)
			{
			case GlobalVars.ACTION_SELECT:
			if (stopWaiting==true)
				{
				cancelAlarm();
				}
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
			if (stopWaiting==true)
				{
				cancelAlarm();
				}
			break;
			}
		return super.onKeyUp(keyCode, event);
		}

	public boolean onKeyDown(int keyCode, KeyEvent event)
		{
		return GlobalVars.detectKeyDown(keyCode);
		}	
	}
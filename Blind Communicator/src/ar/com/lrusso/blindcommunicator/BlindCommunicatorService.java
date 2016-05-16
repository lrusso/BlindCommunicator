package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.net.NetworkInfo.*;
import android.net.wifi.*;
import android.os.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.telephony.*;
import android.view.*;
import android.view.View.*;

import com.android.internal.telephony.*;

import java.lang.reflect.*;
import java.util.*;

public class BlindCommunicatorService extends Service implements TextToSpeech.OnInitListener
	{
	private static boolean started = false;
	private static boolean callInProgress = false;
	
	public static final int ACTION_ANSWER = 1;
	public static final int ACTION_REJECT = 2;
	public static float x1,x2,y1,y2;
	
	private static WindowManager.LayoutParams params;
	private static WindowManager wm;
	private static LayoutInflater inflater;
	private static View myView;
	
	private static String who = "";
	private static boolean repeatWho = false;
	private static TextToSpeech tts;

	@Override public IBinder onBind(Intent intent)
		{
		return null;
		}
	
	@Override public void onCreate()
		{
		started=false;
		GlobalVars.startTTS(tts);
		tts = new TextToSpeech(this,this);
		tts.setPitch((float) 1.0);
		tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener()
			{
			@Override public void onUtteranceCompleted(String utteranceId)
				{
				if (repeatWho==true)
					{
					sayWhoIsCallingWithDelay();
					}
				}
			});
		try
			{
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
			registerReceiver(mBroadcastReceiver, new IntentFilter("android.intent.action.TIME_SET"));
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
			registerReceiver(mBroadcastReceiver, new IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));
			registerReceiver(mBroadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
			registerReceiver(mBroadcastReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
			registerReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
			registerReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
			registerReceiver(mBroadcastReceiver, new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION));
			registerReceiver(mBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
			}
			catch(NullPointerException e)
			{
			}
			catch(OutOfMemoryError e)
			{
			}
			catch(Exception e)
			{
			}
			
		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
			{
			@Override public void run()
				{
				started = true;
				}
			}, 1000);
		
		try
			{
			params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,PixelFormat.TRANSLUCENT);
			wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			myView = inflater.inflate(R.layout.callsanswerreject, null);
			myView.setOnTouchListener(new OnTouchListener()
				{
				@Override public boolean onTouch(View v, MotionEvent event)
					{
					int result = detectCallMovement(event);
					switch (result)
						{
						case ACTION_ANSWER:
						acceptCall();
						break;

						case ACTION_REJECT:
						rejectCall();
						break;
						}
					return true;
					}
				});		
			wm.addView(myView, params);
			myView.setVisibility(View.GONE);
			}
			catch(NullPointerException e)
			{
			}
			catch(OutOfMemoryError e)
			{
			}
			catch(Exception e)
			{
			}
		}
		
	@Override public void onDestroy()
		{
		started=false;
		try
            {
			unregisterReceiver(mBroadcastReceiver);
            }
            catch(NullPointerException e)
            {
            }
            catch(OutOfMemoryError e)
            {
            }
            catch(Exception e)
            {
            }
		try
			{
			if (tts!=null)
				{
				tts.shutdown();
				}	
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		super.onDestroy();
		}
		
	@Override public int onStartCommand(Intent intent, int flags, int startId)
		{
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
		}
	
	public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
		{
		@Override public void onReceive(Context context, Intent myIntent)
			{
			try
				{
				if (started==true)
					{
					if (myIntent.getAction().equals(Intent.ACTION_TIME_TICK) |
						myIntent.getAction().equals(Intent.ACTION_TIME_CHANGED) |
						myIntent.getAction().equals("android.intent.action.TIME_SET"))
						{
						if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)==0 && Calendar.getInstance().get(Calendar.MINUTE)==0 && Calendar.getInstance().get(Calendar.SECOND)==0)
							{
							//	UPDATE ALARM COUNTER
							try
								{
								GlobalVars.setText(Main.alarms,false, getResources().getString(R.string.mainAlarms) + " (" + GlobalVars.getPendingAlarmsForTodayCount() + ")");
								}
								catch(NullPointerException e)
								{
								}
								catch(Exception e)
								{
								}
							GlobalVars.openAndLoadAlarmFile();
							}
						}
					else if (myIntent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED))
						{
						if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
							{
							if (GlobalVars.deviceIsUsingAccesibilty()==false)
								{
								GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.screenLandscape));
								}
							}
							else
							{
							if (GlobalVars.deviceIsUsingAccesibilty()==false)
								{
								GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.screenPortrait));
								}
							}
						}
					else if(myIntent.getAction().equals(Intent.ACTION_SCREEN_OFF))
						{
						if (callInProgress==false)
							{
							if (GlobalVars.deviceIsUsingAccesibilty()==false)
								{
								GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.screenOff));
								}
							}
						}
					else if(myIntent.getAction().equals(Intent.ACTION_SCREEN_ON))
						{
						if (GlobalVars.deviceIsUsingAccesibilty()==false)
							{
							GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.screenOn));
							}
						}
					else if(myIntent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
						{
						int status = myIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);			
						if (status == BatteryManager.BATTERY_STATUS_CHARGING)
							{
							if (GlobalVars.batteryIsCharging==false)
								{
								if (GlobalVars.deviceIsUsingAccesibilty()==false)
									{
									GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.deviceCharging));
									}
								GlobalVars.batteryIsCharging=true;
								}
							}
						else if (status == BatteryManager.BATTERY_STATUS_FULL)
							{
							if(GlobalVars.batteryAt100==false)
								{
								if (GlobalVars.deviceIsUsingAccesibilty()==false)
									{
									GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.deviceCharged));
									}
								GlobalVars.batteryAt100=true;
								}
							}
						}
					else if(myIntent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
						{
						if (GlobalVars.deviceIsUsingAccesibilty()==false)
							{
							GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.deviceUnplugged));
							}
						GlobalVars.batteryIsCharging=false;
						GlobalVars.batteryAt100=false;
						}
					else if(myIntent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
						{
						DetailedState state=((NetworkInfo)myIntent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
						if (state == DetailedState.CONNECTED)
							{
							if (GlobalVars.isWifiEnabled())
								{
								String name = GlobalVars.getWifiSSID();
								if (name!="")
									{
									GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.wifiConnectedToNetwork) + name);
									}
								}
							}
						else if (state == DetailedState.DISCONNECTED)
							{
							if (GlobalVars.isWifiEnabled())
								{
								GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.wifiDisconnectedFromNetwork));
								}
							}
						}
					else if(myIntent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
						{
						int stateWifi = myIntent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
						if (stateWifi==WifiManager.WIFI_STATE_ENABLED)
							{
							try
								{
								Settings.wifiicon.setImageResource(R.drawable.settingswifion);
								if (GlobalVars.activityItemLocation==7)
									{
									GlobalVars.setText(Settings.wifi,true,getResources().getString(R.string.layoutSettingsWifiOn));
									}
									else
									{
									GlobalVars.setText(Settings.wifi,false,getResources().getString(R.string.layoutSettingsWifiOn));
									}
								}
								catch(NullPointerException e)
								{
								}
								catch(Exception e)
								{
								}
							GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutSettingsWifiOn2));
							}
							else if (stateWifi==WifiManager.WIFI_STATE_DISABLED)
							{
							try
								{
								Settings.wifiicon.setImageResource(R.drawable.settingswifioff);
								if (GlobalVars.activityItemLocation==7)
									{
									GlobalVars.setText(Settings.wifi,true,getResources().getString(R.string.layoutSettingsWifiOff));
									}
									else
									{
									GlobalVars.setText(Settings.wifi,false,getResources().getString(R.string.layoutSettingsWifiOff));
									}
								}
								catch(NullPointerException e)
								{
								}
								catch(Exception e)
								{
								}
							GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutSettingsWifiOff3));
							}
						}
					else if(myIntent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE"))
						{
						if (myIntent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING))
							{
							//THE PHONE IS RINGING
							GlobalVars.tts.stop();
							GlobalVars.startTTS(tts);
							tts.setPitch((float) 1.0);
							callInProgress=true;
							repeatWho=true;
							String incomingNumber = myIntent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
							String result = GlobalVars.getContactNameFromPhoneNumber(incomingNumber);
							if (result==null)
								{
								who = GlobalVars.context.getResources().getString(R.string.callFromUnknown);
								}
								else
								{
								result = result.replaceAll("-","");
								result = result.replaceAll("  "," ");
								who = GlobalVars.context.getResources().getString(R.string.callFrom) + result;
								}
							sayWhoIsCalling();
							if (deviceHasSensorProximity())
								{
								myView.setVisibility(View.VISIBLE);
								}
							}
						else if (myIntent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
							{
							//PICK UP THE CALL
							try
								{
								tts.stop();
								}
								catch(Exception e)
								{
								}
							callInProgress=true;
							repeatWho=false;
							if (deviceHasSensorProximity())
								{
								myView.setVisibility(View.VISIBLE);
								}
							}
						else if (myIntent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE))
							{
							//THE CALL HAS ENDED
							repeatWho=false;
							GlobalVars.callLogsReady = false;
							GlobalVars.callLogsDataBase.clear();
							new CallsLogsThread().execute();
							try
								{
								CallsLogs.selectedLog = -1;
								if (GlobalVars.activityItemLocation==1)
									{
									GlobalVars.setText(CallsLogs.phonenumber,true,getResources().getString(R.string.layoutCallsLogsLogs));
									}
									else
									{
									GlobalVars.setText(CallsLogs.phonenumber,false,getResources().getString(R.string.layoutCallsLogsLogs));
									}
								}
								catch(NullPointerException e)
								{
								}
								catch(Exception e)
								{
								}

							if (callInProgress==true)
								{
								myView.setVisibility(View.GONE);
								callInProgress=false;
								GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.callEnded));
								GlobalVars.startActivity(GlobalVars.lastActivity); //IN ANDROID 2.2 AND 2.3, AFTER CALL ENDS, THE SYSTEM SHOWS THE CALL LOG ACTIVITY, SO THE LAST ACTIVITY OF THIS APP MUST BE STARTED
								Handler handler = new Handler();
								handler.postDelayed(new Runnable()
									{
									@Override public void run()
										{
										GlobalVars.startActivity(GlobalVars.lastActivity); //SAME AS ABOVE, BUT 500 MS LATER FOR SLOWER DEVICES THAT MAY SHOW THE CALL LOG AFTER THE CALL OF THE LAST ACTIVITY
										try
											{
											if (GlobalVars.activityItemLocation==2)
												{
												GlobalVars.setText(Main.calls,true,GlobalVars.context.getResources().getString(R.string.mainCalls) + " (" + String.valueOf(GlobalVars.getCallsMissedCount()) + ")");
												}
												else
												{
												GlobalVars.setText(Main.calls,false,GlobalVars.context.getResources().getString(R.string.mainCalls) + " (" + String.valueOf(GlobalVars.getCallsMissedCount()) + ")");
												}
											}
											catch(NullPointerException e)
											{
											}
											catch(Exception e)
											{
											}
										}
									}, 500);
								}
							}
						}
					else if(myIntent.getAction().equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL"))
						{
						String outgoingNumber2 = myIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
						String result = GlobalVars.getContactNameFromPhoneNumber(outgoingNumber2);
						if (result==null)
							{
							GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.callingToUnknown));
							}
							else
							{
							result = result.replaceAll("-","");
							result = result.replaceAll("  "," ");
							GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.callingTo) + result);
							}
						}
					else if(myIntent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION))
						{
						try
							{
							if (GlobalVars.activityItemLocation==6)
								{
								AudioManager audioManager = (AudioManager) GlobalVars.context.getSystemService(AUDIO_SERVICE);
								switch(audioManager.getRingerMode())
									{
									case AudioManager.RINGER_MODE_NORMAL:
									GlobalVars.setText(Settings.profile,true,getResources().getString(R.string.layoutSettingsProfileNormal));
									break;

									case AudioManager.RINGER_MODE_SILENT:
									GlobalVars.setText(Settings.profile,true,getResources().getString(R.string.layoutSettingsProfileSilent));
									break;

									case AudioManager.RINGER_MODE_VIBRATE:
									GlobalVars.setText(Settings.profile,true,getResources().getString(R.string.layoutSettingsProfileVibrate));
									break;
									}
								}
								else
								{
								AudioManager audioManager = (AudioManager) GlobalVars.context.getSystemService(AUDIO_SERVICE);
								switch(audioManager.getRingerMode())
									{
									case AudioManager.RINGER_MODE_NORMAL:
									GlobalVars.setText(Settings.profile,false,getResources().getString(R.string.layoutSettingsProfileNormal));
									break;

									case AudioManager.RINGER_MODE_SILENT:
									GlobalVars.setText(Settings.profile,false,getResources().getString(R.string.layoutSettingsProfileSilent));
									break;

									case AudioManager.RINGER_MODE_VIBRATE:
									GlobalVars.setText(Settings.profile,false,getResources().getString(R.string.layoutSettingsProfileVibrate));
									break;
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
					else if(myIntent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED"))
						{
						final Bundle bundle = myIntent.getExtras();
						try
							{
							if (bundle != null)
								{
								final Object[] pdusObj = (Object[]) bundle.get("pdus");
								for (int i = 0; i < pdusObj.length; i++)
									{
									SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
									String phoneNumber = currentMessage.getDisplayOriginatingAddress();
									String senderNum = phoneNumber;
									String result = GlobalVars.getContactNameFromPhoneNumber(senderNum);
									if (result==null)
										{
										GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.mainMessagesNewFromUnknown));
										}
										else
										{
										result = result.replace("-","");
										result = result.replace("  "," ");
										GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.mainMessagesNewFromContact) + result);
										}
									new MessagesCheckThread(GlobalVars.TYPE_INBOX).execute();
									try
										{
										MessagesInbox.selectedMessage = -1;
										if (GlobalVars.activityItemLocation==1)
											{
											GlobalVars.setText(MessagesInbox.inbox, true, GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxInbox));
											}
											else
											{
											GlobalVars.setText(MessagesInbox.inbox, false, GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxInbox));
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
								Handler handler = new Handler();
								handler.postDelayed(new Runnable()
									{
									@Override public void run()
										{
										if (GlobalVars.activityItemLocation==1)
											{
											GlobalVars.setText(Main.messages,true,GlobalVars.context.getResources().getString(R.string.mainMessages) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
											GlobalVars.setText(Messages.inbox,true,GlobalVars.context.getResources().getString(R.string.layoutMessagesInbox) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
											}
											else
											{
											GlobalVars.setText(Main.messages,false,GlobalVars.context.getResources().getString(R.string.mainMessages) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
											GlobalVars.setText(Messages.inbox,false,GlobalVars.context.getResources().getString(R.string.layoutMessagesInbox) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
											}
										}
									}, 500);
							}
							catch(NullPointerException e)
							{
							}
							catch (Exception e)
							{
							}
						}
					else if(myIntent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED))
						{
						boolean bluetoothValue = GlobalVars.isBluetoothEnabled();
						if (bluetoothValue!=GlobalVars.bluetoothEnabled)
							{
							try
								{
								GlobalVars.bluetoothEnabled = bluetoothValue;
								if (bluetoothValue==true)
									{
									if (GlobalVars.activityItemLocation==8)
										{
										GlobalVars.setText(Settings.bluetooth,true,GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOn));
										GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOn2));
										}
										else
										{
										GlobalVars.setText(Settings.bluetooth,false,GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOn));
										GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOn2));
										}
									}
									else
									{
									if (GlobalVars.activityItemLocation==8)
										{
										GlobalVars.setText(Settings.bluetooth,true,GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOff));
										GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOff2));
										}
										else
										{
										GlobalVars.setText(Settings.bluetooth,false,GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOff));
										GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutSettingsBluetoothOff2));
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
					}
				}
				catch(NullPointerException e)
				{
				}
				catch(OutOfMemoryError e)
				{
				}
				catch(Exception e)
				{
				}
			}
		};
		
	private void acceptCall()
		{
    	Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
    	buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
    	try
			{
        	GlobalVars.context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
			}
			catch(Exception e)
			{
			}

        Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
        headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        headSetUnPluggedintent.putExtra("state", 0);
        headSetUnPluggedintent.putExtra("name", "Headset");
        try
			{
            GlobalVars.context.sendOrderedBroadcast(headSetUnPluggedintent, null);
			}
			catch (Exception e)
			{
			}
		}

    private void rejectCall()
		{
        TelephonyManager tm = (TelephonyManager) GlobalVars.context.getSystemService(Context.TELEPHONY_SERVICE);
    	TelephonyManager telephony = (TelephonyManager) GlobalVars.context.getSystemService(Context.TELEPHONY_SERVICE);
    	ITelephony telephonyService;
    	try
			{
    		Class c = Class.forName(telephony.getClass().getName());
    		Method m = c.getDeclaredMethod("getITelephony");
    		m.setAccessible(true);
    		telephonyService = (ITelephony) m.invoke(telephony);
    		//telephonyService.silenceRinger();
    		telephonyService.endCall();
			}
			catch (Exception e)
			{
			}
		}
		
	private int detectCallMovement(MotionEvent event)
		{
		switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
			x1 = event.getX();
			y1 = event.getY();
			break;

			case MotionEvent.ACTION_UP:
			x2 = event.getX();
			y2 = event.getY();
			float deltaY = y2 - y1;

			if (Math.abs(deltaY) > GlobalVars.MIN_DISTANCE)
				{
				if (y2 > y1)
					{
					return ACTION_REJECT;
					}
					else 
					{
					return ACTION_ANSWER;
					}
				}
				break;
			}
		return -1;
		}
		

		
	private boolean deviceHasSensorProximity()
		{
		try
			{
	    	PackageManager manager = GlobalVars.context.getPackageManager();
	    	return manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);
			}
			catch(Exception e)
			{
			}
		return false;
		}
		
	
	
	public void onInit(int status)
		{
		}
	
	public static void sayWhoIsCallingWithDelay()
		{
		if (repeatWho==true)
			{
			new Thread()
				{  
				public void run()
					{ 
					try
						{
						sleep(2000);
						}
						catch (InterruptedException e)
						{
						}
					sayWhoIsCalling();
					}  
				}.start();  
			}
		}
	
	public static void sayWhoIsCalling()
		{
		if (repeatWho==true)
			{
			try
				{
				AudioManager mAudioManager = (AudioManager) GlobalVars.context.getSystemService(GlobalVars.context.AUDIO_SERVICE);
				if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
					{
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
					tts.speak(who,TextToSpeech.QUEUE_FLUSH, params);
					}
				}
				catch(Exception e)
				{
				}
			}
		}
	}

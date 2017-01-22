package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.database.*;
import android.graphics.*;
import android.media.*;
import android.media.MediaPlayer.*;
import android.net.*;
import android.net.wifi.*;
import android.os.Vibrator;
import android.provider.*;
import android.speech.tts.*;
import android.telephony.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.*;
import java.util.*;

import android.bluetooth.*;

public class GlobalVars extends Application
	{
	//SYSTEM VARIABLES FOR GENERAL PURPOSES
	public static TextToSpeech 				tts;
	public static int 						TTS_MAX_INPUT_LENGTH = 4000;
	public static Context 					context;
	public static Context					contextApp;
	public static final int 				MIN_DISTANCE = 150;
	public static final int 				ACTION_SELECT = 1;
	public static final int 				ACTION_SELECT_PREVIOUS = 2;
	public static final int 				ACTION_EXECUTE = 3;
	public static final int 				INPUT_KEYBOARD = 1;
	public static final int 				INPUT_VOICE = 2;
	public static final int 				INPUT_KEYBOARDTALKBACK = 3;
	public static int 						activityItemLocation = 0;
	public static int 						activityItemLimit = 0;
	public static float 					x1,x2,y1,y2;
	public static Toast 					mToast1 = null;
	public static Toast 					mToast2 = null;
	public static boolean 					firstToast = true;
	public static boolean 					toastMode = false; //FOR TESTING
	public static Class 					lastActivity; //IN ANDROID 2.X, AFTER A CALL ENDS, THE SYSTEM POPUPS THE CALL LOG ACTIVITY. BECAUSE OF THAT, THE APP SETS THE LAST ACTIVITY IN ORDER TO POPUP IT AFTER THE CALL. 
	public static boolean 					batteryIsCharging = false;
	public static boolean 					batteryAt100 = false;
	public static int 						inputMode = -1;
	public static String 					inputModeResult = null;
	public static boolean 					inputModeKeyboardOnlyNumbers = false;
	public static Activity                  inputModeKeyboardTalkbackActivity = null;
	
	//VARIABLES FOR CONTACTS
	public static boolean 					contactListReady = false;
	public static List<String>				contactDataBase = new ArrayList<String>();
	public static String 					contactToDeleteName;
	public static String 					contactToDeletePhone;
	public static boolean 					contactWasDeleted = false;
	public static boolean					contactWasCreated = false;
	
	//VARIABLES FOR CALL LOGS
	public static boolean 					callLogsReady = false;
	public static List<String>				callLogsDataBase = new ArrayList<String>();
	public static boolean 					callLogsDeleted = false;
	
	//VARIABLES FOR MEDIA PLAYER
	public static MediaPlayer 				musicPlayer = null;
	public static boolean 					musicPlayerDatabaseReady = false;
	public static boolean 					musicPlayerAlbumsReady = false;
	public static int 						musicPlayerPlayingSongIndex = -1;
	public static String 					musicPlayerCurrentArtist = "";
	public static String 					musicPlayerCurrentSong = "";
	public static List<String>				musicPlayerDatabaseFull = new ArrayList<String>();
	public static List<String>				musicPlayerDatabaseArtists = new ArrayList<String>();
	public static List<String>				musicPlayerDatabaseAlbums = new ArrayList<String>();
	public static List<String> 				musicPlayerDatabaseAlbumsByArtist = new ArrayList<String>();
	public static List<String> 				musicPlayerDatabasePlayList = new ArrayList<String>();
	
	//VARIABLES FOR VOICE RECORDER
	public static boolean 					voiceRecorderAudioWasSaved = false;
	public static boolean 					voiceRecorderAudioWasDeleted = false;
	public static int 						voiceRecorderToDelete = -1;
	public static boolean 					voiceRecorderListReady = false;
	public static List<String> 				voiceRecorderListFiles = new ArrayList<String>();
	
	//VARIABLES FOR SETTINGS
	public static List<String>				settingsToneAlarmTitle = new ArrayList<String>();
	public static List<String> 				settingsToneAlarmUri = new ArrayList<String>();
	public static List<String>				settingsToneAlarmID = new ArrayList<String>();
	public static List<String> 				settingsToneNotificationTitle = new ArrayList<String>();
	public static List<String> 				settingsToneNotificationUri = new ArrayList<String>();
	public static List<String> 				settingsToneNotificationID = new ArrayList<String>();
	public static List<String> 				settingsToneCallTitle = new ArrayList<String>();
	public static List<String> 				settingsToneCallUri = new ArrayList<String>();
	public static List<String> 				settingsToneCallID = new ArrayList<String>();
	public static MediaPlayer 				settingsRingtonePlayerTester;
	public static int 						settingsTTSReadingSpeed = 1;
	public static int 						settingsTTSReadingSpeedLimit = 5;
	public static int 						settingsScreenTimeOut = -1;
	public static List<String> 				settingsScreenTimeOutValues = new ArrayList<String>();
	
	//VARIABLES FOR ALARMS
	public static List<String> 				alarmTimeHoursValues = new ArrayList<String>();
	public static List<String> 				alarmTimeMinutesValues = new ArrayList<String>();
	public static boolean 					alarmWasCreated = false;
	public static boolean 					alarmWasDeleted = false;
	public static boolean 					alarmWereDeleted = false;
	public static String 					alarmMessage = "";
	public static String 					alarmDay = "";
	public static String 					alarmHours = "";
	public static String 					alarmMinutes = "";
	public static int 						alarmToDeleteIndex = -1;
	public static List<String> 				alarmList = new ArrayList<String>();
	public static AlarmManager 				alarmAlarmManager;
	public static ArrayList<PendingIntent> 	alarmPendingIntentArray = new ArrayList<PendingIntent>();
	public static Vibrator 					alarmVibrator;
	
	//VARIABLES FOR WEB BROWSER
	public static boolean 					bookmarkWasDeleted = false;
	public static int 						bookmarkToDeleteIndex = -1;
	public static boolean 					browserRequestInProgress = false;
	public static String 					browserWebTitle = null;
	public static String 					browserWebURL = null;
	public static String 					browserWebText = null;
	public static List<String> 				browserWebLinks = new ArrayList<String>();
	public static List<String> 				browserBookmarks = new ArrayList<String>();
	
	//VARIABLES FOR MESSAGES
	public static final int					TYPE_INBOX = 1;
	public static final int					TYPE_SENT = 2;
	public static boolean 					messagesWasSent = false;
	public static boolean					messagesWasDeleted = false;
	public static boolean					messagesInboxWereDeleted = false;
	public static boolean 					messagesInboxDatabaseReady = false;
	public static boolean					messagesSentWereDeleted = false;
	public static boolean 					messagesSentDatabaseReady = false;
	public static List<String> 				messagesInboxDataBase = new ArrayList<String>();
	public static List<String> 				messagesSentDataBase = new ArrayList<String>();
	
	//VARIABLES FOR APPLICATIONS
	public static List<String> 				applicationsList = new ArrayList<String>();
	public static boolean 					applicationsListReady = false;
	
	//VARIABLE FOR BLUETOOTH
	public static boolean					bluetoothEnabled = false;
	
	//GLOBAL FUNCTIONS REQUIRED BY ACTIVITIES
	public static String getLanguage()
		{
		if (Locale.getDefault().getLanguage().startsWith("es"))
			{
			return "es"; //TO WORK IN SPANISH
			}
		else if (Locale.getDefault().getLanguage().startsWith("pt"))
			{
			return "pt"; //TO WORK IN PORTUGUESE
			}
		else if (Locale.getDefault().getLanguage().startsWith("it"))
			{
			return "it"; //TO WORK IN ITALIAN
			}
		else if (Locale.getDefault().getLanguage().startsWith("fr"))
			{
			return "fr"; //TO WORK IN FRENCH
			}
		else if (Locale.getDefault().getLanguage().startsWith("de"))
			{
			return "de"; //TO WORK IN DEUTSCH
			}
		else if (Locale.getDefault().getLanguage().startsWith("ru"))
			{
			return "ru"; //TO WORK IN RUSSIAN
			}
		else if (Locale.getDefault().getLanguage().startsWith("id"))
			{
			return "id"; //TO WORK IN INDONESIAN
			}
		else if (Locale.getDefault().getLanguage().startsWith("in"))
			{
			return "in"; //TO WORK IN INDONESIAN
			}
		else
			{
			return "en"; //TO WORK IN ENGLISH AS DEFAULT
			}
		}
	
    public static void startTTS(TextToSpeech myTTS)
		{
    	try
			{
			Locale loc = new Locale(getLanguage(), "","");
			
    		if(myTTS.isLanguageAvailable(loc)>=TextToSpeech.LANG_AVAILABLE)
				{
    			myTTS.setLanguage(loc);
				}
			}
			catch(Exception e)
			{
			}
		}
	
    public static void talk(String a)
    	{
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
    	try
    		{
    		if (toastMode==true)
    			{
    			if (firstToast==true)
					{
    				mToast1 = Toast.makeText(context,a,Toast.LENGTH_SHORT);
    				mToast1.show();
    				try
						{
    					mToast2.cancel();
    					mToast2 = null;
						}
						catch(Exception e)
						{
						}
    				firstToast=false;
					}
					else
					{
					mToast2 = Toast.makeText(context,a,Toast.LENGTH_SHORT);
					mToast2.show();
					try
						{
						mToast1.cancel();
						mToast1 = null;
						}
						catch(Exception e)
						{
						}
					firstToast=true;
					}
    			}
				else
				{
				GlobalVars.tts.stop();
				try
					{
					musicPlayer.setVolume(0.1f, 0.1f);
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				try
					{
					AudioManager mAudioManager = (AudioManager) GlobalVars.context.getSystemService(GlobalVars.context.AUDIO_SERVICE);
					if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
						{
						if (a.length()>TTS_MAX_INPUT_LENGTH)
							{
							a = a.substring(0, TTS_MAX_INPUT_LENGTH - 1);
							}
						HashMap<String, String> params = new HashMap<String, String>();
						params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
						tts.speak(a, TextToSpeech.QUEUE_FLUSH, params);
						}
					}
					catch(Exception e)
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
    
	public static int detectMovement(MotionEvent event)
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
			float deltaX = x2 - x1;
			float deltaY = y2 - y1;

			if (-Math.abs(deltaX) < -GlobalVars.MIN_DISTANCE)
				{
				if (x2 < x1)
					{
					return ACTION_SELECT_PREVIOUS;
					}
				}
			if (Math.abs(deltaX) > GlobalVars.MIN_DISTANCE)
				{
				if (x2 > x1)
					{
					return ACTION_EXECUTE;
					}
				}
			else if (Math.abs(deltaY) > GlobalVars.MIN_DISTANCE)
				{
				if (y2 > y1)
					{
					if (activityItemLocation+1>activityItemLimit)
						{
						activityItemLocation=1;
						}
						else
						{
						activityItemLocation=activityItemLocation+1;
						}
					}
					else 
					{
					if (activityItemLocation-1<1)
						{
						activityItemLocation=activityItemLimit;
						}
						else
						{
						activityItemLocation=activityItemLocation-1;
						}
					}
				return ACTION_SELECT;
				}
			break;
			}
		return -1;
		}
		
	public static int detectKeyUp(int keyCode)
		{
		try
			{
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
				{
				if (activityItemLocation-1<1)
					{
					activityItemLocation=activityItemLimit;
					}
					else
					{
					activityItemLocation=activityItemLocation-1;
					}
				return ACTION_SELECT;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
				{
				if (activityItemLocation+1>activityItemLimit)
					{
					activityItemLocation=1;
					}
					else
					{
					activityItemLocation=activityItemLocation+1;
					}
				return ACTION_SELECT;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
				{
				return ACTION_SELECT_PREVIOUS;
				}
			else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
				return ACTION_EXECUTE;
				}
			else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				{
				volumeUp();
				}
			else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				{
				volumeDown();
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return -1;
		}
	
	public static boolean detectKeyDown(int keyCode)
		{
		if (keyCode == KeyEvent.KEYCODE_BACK)
			{
			return true;
			}
		return false;
		}
	
	public static void volumeUp()
		{
		AudioManager mAudioManager = (AudioManager) GlobalVars.context.getSystemService(Context.AUDIO_SERVICE);
		int currentvolume = mAudioManager.getStreamVolume (AudioManager.STREAM_RING);
		int maxvolume = mAudioManager.getStreamMaxVolume (AudioManager.STREAM_RING);
		String toSay = "";
		
		if (currentvolume==maxvolume)
			{
			toSay = toSay + context.getResources().getString(R.string.volumeMaximum);
			}
			else
			{
			toSay = toSay + context.getResources().getString(R.string.volumeHigher);
			}
		
		switch(mAudioManager.getRingerMode())
			{
			case AudioManager.RINGER_MODE_NORMAL:
			toSay = toSay + context.getResources().getString(R.string.mainProfileIsNormal);
			break;

			case AudioManager.RINGER_MODE_SILENT:
			toSay = toSay + context.getResources().getString(R.string.mainProfileIsSilent);
			break;

			case AudioManager.RINGER_MODE_VIBRATE:
			toSay = toSay + context.getResources().getString(R.string.mainProfileIsVibrate);
			break;
			}
		
		GlobalVars.talk(toSay);
		}
		
	public static void volumeDown()
		{
		String toSay = context.getResources().getString(R.string.volumeLower);
		
		AudioManager mAudioManager = (AudioManager) GlobalVars.context.getSystemService(Context.AUDIO_SERVICE);
		switch(mAudioManager.getRingerMode())
			{
			case AudioManager.RINGER_MODE_NORMAL:
			toSay = toSay + context.getResources().getString(R.string.mainProfileIsNormal);
			break;

			case AudioManager.RINGER_MODE_SILENT:
			toSay = toSay + context.getResources().getString(R.string.mainProfileIsSilent);
			break;

			case AudioManager.RINGER_MODE_VIBRATE:
			toSay = toSay + context.getResources().getString(R.string.mainProfileIsVibrate);
			break;
			}
		GlobalVars.talk(toSay);
		}

    public static void callTo(String a)
    	{
    	try
    		{
    		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(a));
    		context.startActivity(intent);
    		}
    		catch(NullPointerException e)
    		{
    		}
    	catch(Exception e)
    		{
    		}
    	}
    
    public static void startActivity(Class<?> a)
		{
    	try
			{
    		Intent intent = new Intent(context, a);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		context.startActivity(intent);
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
    
    public static void startService(Class<?> a)
    	{
    	try
   			{
    		Intent serviceIntent = new Intent(context, a);
    		context.startService(serviceIntent);
   			}
			catch(NullPointerException e)
			{
			}
   			catch(Exception e)
   			{
   			}
    	}
    
    public static void stopService(Class<?> a)
    	{
    	try
			{
    		Intent serviceIntent = new Intent(context, a);
    		context.stopService(serviceIntent);
			}
    		catch(NullPointerException e)
    		{
    		}
			catch(Exception e)
			{
			}
    	}
	
    public static void selectTextView(TextView myTextView, boolean selection)
		{
		try
			{
			if (selection==true)
				{
				myTextView.setTextColor(Color.CYAN);
				SpannableString content = new SpannableString(myTextView.getText().toString());
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
				myTextView.setText(content);
				}
				else
				{
				myTextView.setTextColor(Color.WHITE);
				SpannableString content1 = new SpannableString(myTextView.getText().toString());
				myTextView.setText(content1);
				}
			}
			catch(Exception e)
			{
			}
		}
    
	public static void setText(TextView myTextView, boolean style, String text)
		{
		try
			{
			myTextView.setText(text);
			if (style==true)
				{
				myTextView.setTextColor(Color.CYAN);
				SpannableString content = new SpannableString(myTextView.getText().toString());
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
				myTextView.setText(content);
				}
			}
			catch(Exception e)
			{
			}
		}

	public static String divideNumbersWithBlanks(String value)
		{
		String response = "";
		try
			{
			response = value;
			response = response.replaceAll("[^0-9]","");
			response = response.replaceAll(".(?=.)", "$0 ");
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return response;
		}
		
	public static boolean deviceIsAPhone()
		{
		try
			{
			if (((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType()== TelephonyManager.PHONE_TYPE_NONE)
				{
				return false;
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return true;
		}

	public static void deviceSetBluetooth(boolean enable)
		{
		try
			{
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			boolean isEnabled = bluetoothAdapter.isEnabled();
			if (enable && !isEnabled)
				{
				bluetoothAdapter.enable();
				}
            	else if(!enable && isEnabled)
            	{
				bluetoothAdapter.disable();
            	}
        	}
        	catch(NullPointerException e)
        	{
			}
        	catch(Exception e)
        	{
        	}
		}
		
	public static boolean deviceIsUsingAccesibilty()
		{
		try
			{
		    String SCREENREADER_INTENT_ACTION = "android.accessibilityservice.AccessibilityService";
		    String SCREENREADER_INTENT_CATEGORY = "android.accessibilityservice.category.FEEDBACK_SPOKEN";
	        Intent screenReaderIntent = new Intent(SCREENREADER_INTENT_ACTION);
	        screenReaderIntent.addCategory(SCREENREADER_INTENT_CATEGORY);

	        List<ResolveInfo> screenReaders = context.getPackageManager().queryIntentServices(screenReaderIntent, 0);
	        ContentResolver cr = context.getContentResolver();
	        Cursor cursor = null;
	        int status = 0;

	        List<String> runningServices = new ArrayList<String>();

	        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        for (android.app.ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
	        	{
	            runningServices.add(service.service.getPackageName());
	        	}

	        for (ResolveInfo screenReader : screenReaders)
	        	{
	            cursor = cr.query(Uri.parse("content://" + screenReader.serviceInfo.packageName + ".providers.StatusProvider"), null, null, null, null);

	            if (cursor!=null)
	            	{
		            if (cursor.moveToFirst())
	            		{
		            	//this part works for Android <4.1
		            	status = cursor.getInt(0);
		            	if (status == 1)
	                		{
		            		//screen reader active!
		            		return true;
	                		}
	                		else
	                		{
	                		//screen reader inactive
	                		return false;
	                		}
	            		}
	            		else
	            		{
	            		//this part works for Android 4.1+
	            		if (runningServices.contains(screenReader.serviceInfo.packageName))
	                		{
	            			//screen reader active!
	            			return true;
	                		}
	                		else
	                		{
	                		//screen reader inactive
	                		return false;
	                		}
	            		}
	            	}
	        	}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
        return false;
		}
	
	public static boolean deviceIsConnectedToMobileNetwork()
		{
		TelephonyManager tm = (TelephonyManager) GlobalVars.context.getSystemService(Context.TELEPHONY_SERVICE);
	    switch (tm.getNetworkType())
	    	{
	    	case TelephonyManager.NETWORK_TYPE_UNKNOWN:
	        return false;
	    	}
	    return true;
		}
	
	public static boolean isBCTheDefaultLauncher()
		{
		try
			{
			IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
			filter.addCategory(Intent.CATEGORY_HOME);

			List<IntentFilter> filters = new ArrayList<IntentFilter>();
			filters.add(filter);

			String myPackageName = GlobalVars.context.getPackageName();
			List<ComponentName> activities = new ArrayList<ComponentName>();
			PackageManager packageManager = (PackageManager) GlobalVars.context.getPackageManager();
			
			packageManager.getPreferredActivities(filters, activities, null);

			for (ComponentName activity : activities)
				{
				if (myPackageName.equals(activity.getPackageName()))
					{
					return true;
					}
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return false;
		}

	public static String getDayName(int dayname)
		{
		String result = "";
		switch (dayname)
			{
			case 1:
			result = context.getResources().getString(R.string.sunday);
			break;
			
			case 2:
			result = context.getResources().getString(R.string.monday);
			break;
			
			case 3:
			result = context.getResources().getString(R.string.tuesday);
			break;
			
			case 4:
			result = context.getResources().getString(R.string.wednesday);
			break;
			
			case 5:
			result = context.getResources().getString(R.string.thursday);
			break;
			
			case 6:
			result = context.getResources().getString(R.string.friday);
			break;
			
			case 7:
			result = context.getResources().getString(R.string.saturday);
			break;
			}
		return result;
		}
	
	public static boolean isWifiEnabled()
		{
		try
			{
			WifiManager wifi = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
			if (wifi.isWifiEnabled())
				{
				return true;
				}
			}
			catch(Exception e)
			{
			}
		return false;
		}
	
	public static boolean isBluetoothEnabled()
		{
		try
			{
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null)
				{
			    // Device does not support Bluetooth
				return false;
				}
				else
				{
			    if (mBluetoothAdapter.isEnabled())
			    	{
			    	return true;
			    	}
			    	else
			    	{
			    	return false;
			    	}
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return false;
		}
	
	public static String getWifiSSID()
		{
		try
			{
			WifiManager wifi = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifi.getConnectionInfo();
			String name = wifiInfo.getSSID();
			name = name.replace("\"","");
			if (name!="")
				{
				return name;
				}
			}
			catch(Exception e)
			{
			}
		return "";
		}
	
	public static String readFile(String file)
		{
        String result = "";
        DataInputStream in = null;
        try
			{
            in = new DataInputStream(context.openFileInput(file));
            for (;;)
				{
                result = result + in.readUTF();
				}
			}
			catch (Exception e)
			{
			}
        try
			{
            in.close();
			}
			catch(Exception e)
			{
			}
        return result;
		}

    public static void writeFile(String file, String text)
		{
        try
			{
            DataOutputStream out = new DataOutputStream(context.openFileOutput(file, Context.MODE_PRIVATE));
            out.writeUTF(text);
            out.close();
			}
			catch(Exception e)
			{
			}
		}

	public static void startInputActivity()
		{
		if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARD | GlobalVars.inputModeKeyboardOnlyNumbers==true)
			{
			GlobalVars.startActivity(InputKeyboard.class);
			}
		else if(GlobalVars.inputMode==GlobalVars.INPUT_VOICE)
			{
			GlobalVars.startActivity(InputVoice.class);
			}
		else if(GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARDTALKBACK)
			{
			GlobalVars.startActivity(InputKeyboardTalkback.class);
			}
		}
		
	public static int getCallsMissedCount()
		{
    	final String[] projection = null;
    	final String selection = null;
    	final String[] selectionArgs = null;
    	final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
    	Cursor cursor = null;
    	int count = 0;
    	try
			{
    	    cursor = context.getContentResolver().query(Uri.parse("content://call_log/calls"),projection,selection,selectionArgs,sortOrder);
			if (cursor!=null)
				{
				if(cursor.moveToFirst())
					{
					while(!cursor.isAfterLast())
		        		{
						String callType = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
						String isCallNew = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NEW));
						if(Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE && Integer.parseInt(isCallNew) > 0)
							{
							count = count + 1;
							}
						cursor.moveToNext();
		        		}
			        //cursor.close();
		    		}
				}
			}
			catch(Exception ex)
			{
			}
    	return count;
		}

    public static int getMessagesUnreadCount()
		{
    	int SMSNews = 0;
    	try
			{
    		final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    		Cursor cursor = context.getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
    		SMSNews = cursor.getCount();
			}
    		catch(NullPointerException e)
    		{
    		}
			catch(Exception e)
			{
			}
    	return SMSNews;
		}
    
	public static int getPendingAlarmsForTodayCount()
		{
		int total = 0;
		Calendar calendar = Calendar.getInstance();
		String today = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
		for (int i=0;i<GlobalVars.alarmList.size();i++)
			{
			if (GlobalVars.alarmList.get(i).startsWith(today + "|"))
				{
				total = total + 1;
				}
			}
		return total;
		}
		
	public static String getPendingAlarmsForTodayCountText()
		{
		int total = 0;
		Calendar calendar = Calendar.getInstance();
		String today = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
		for (int i=0;i<GlobalVars.alarmList.size();i++)
			{
			if (GlobalVars.alarmList.get(i).startsWith(today + "|"))
				{
				total = total + 1;
				}
			}
		if (total==0)
			{
			return GlobalVars.context.getResources().getString(R.string.mainAlarmsNoAlarms);			
			}
		else if (total==1)
			{
			return GlobalVars.context.getResources().getString(R.string.mainAlarmsOneAlarm);			
			}
		else
			{
			return GlobalVars.context.getResources().getString(R.string.mainAlarms) + ". " + total +
				   GlobalVars.context.getResources().getString(R.string.mainAlarmsForToday);			
			}
		}

	public static void scheduleAlarm(int day, int hours, int minutes, String message, int i)
		{
		try
			{
			Calendar calSet = Calendar.getInstance();
			calSet.setTimeInMillis(System.currentTimeMillis());
			calSet.set(Calendar.HOUR_OF_DAY, hours);
			calSet.set(Calendar.MINUTE, minutes);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);
			Intent intent = new Intent("AR.COM.LRUSSO.BLINDCOMMUNICATOR.ALARM");
			intent.putExtra("AR.COM.LRUSSO.BLINDCOMMUNICATOR.ALARM",
							String.valueOf(day) + "|" +
							String.valueOf(hours) + ":" +
							String.valueOf(minutes) + "|" +
							message);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(GlobalVars.context, i, intent, 0);
			GlobalVars.alarmAlarmManager.set(AlarmManager.RTC_WAKEUP,calSet.getTimeInMillis(), pendingIntent);
			GlobalVars.alarmPendingIntentArray.add(pendingIntent);
			}
			catch(Exception e)
			{
			}
		}
		
	public static void clearAlarms()
		{
		if(GlobalVars.alarmPendingIntentArray.size()>0)
			{
			for(int i=0; i<GlobalVars.alarmPendingIntentArray.size(); i++)
				{
				GlobalVars.alarmAlarmManager.cancel(GlobalVars.alarmPendingIntentArray.get(i));
				}
			GlobalVars.alarmPendingIntentArray.clear();
			}
		GlobalVars.alarmList.clear();
		}
		
	public static void createAlarm(String value)
		{
		GlobalVars.alarmList.add(value);
		refreshAlarmFile();
		openAndLoadAlarmFile();
		}
		
	public static void deleteAlarm(int value)
		{
		try
			{
			GlobalVars.alarmList.remove(value);
			}
			catch(Exception e)
			{
			}
		refreshAlarmFile();
		openAndLoadAlarmFile();
		}
	
	public static void deleteAllAlarms()
		{
		try
			{
			for (int i=0; i<GlobalVars.alarmList.size(); i++)
				{
				GlobalVars.alarmList.remove(i);
				}
			}
			catch(Exception e)
			{
			}
		refreshAlarmFile();
		openAndLoadAlarmFile();
		}

		
	public static void refreshAlarmFile()
		{
		String toSave = "";
		for (int i=0;i<GlobalVars.alarmList.size();i++)
			{
			String savedValue = GlobalVars.alarmList.get(i) + "\n";
			if (toSave=="")
				{
				toSave = savedValue;
				}
				else
				{
				toSave = toSave + savedValue;
				}
			}
		GlobalVars.writeFile("alarms.dat",toSave);
		}
		
	public static void openAndLoadAlarmFile()
		{
		//CLEAR ANY PREVIOUS ALARM
		GlobalVars.clearAlarms();
		
		//OPEN ALARMS.DAT AND GETS DATA
		try
			{
			String result = readFile("alarms.dat");
			Scanner scanner = new Scanner(result);
			while (scanner.hasNextLine())
				{
				String line = scanner.nextLine();
				if (line.contains("|"))
					{
					GlobalVars.alarmList.add(line);
					}
				}
			scanner.close();
			}
			catch (Exception e)
			{
			}
		
		Collections.sort(GlobalVars.alarmList, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		
		//LOAD DATA AND SCHEDULES ALARMS
		for (int i=0;i<GlobalVars.alarmList.size();i++)
			{
			try
				{
				String value = GlobalVars.alarmList.get(i);
				
				int day = Integer.valueOf(GlobalVars.getAlarmDayNumber(value));
				int hours = Integer.valueOf(GlobalVars.getAlarmHours(value));
				int minutes = Integer.valueOf(GlobalVars.getAlarmMinutes(value));
				String message = GlobalVars.getAlarmMessage(value);
				
				scheduleAlarm(day,hours,minutes,message,i);
				}
				catch(Exception e)
				{
				}
			}
		}
		
	public static String getAlarmDayNumber(String value)
		{
		try
			{
			return value.substring(0, value.indexOf("|"));
			}
			catch(Exception e)
			{
			}
		return "999";
		}
		
	public static String getAlarmDayName(String value)
		{
		try
			{
			int dayValue = Integer.valueOf(value.substring(0, value.indexOf("|")));
			return getDayName(dayValue);
			}
			catch(Exception e)
			{
			}
		return context.getResources().getString(R.string.monday);
		}
		
	public static String getAlarmHours(String value)
		{
		try
			{
			String storedTime = value.substring(value.indexOf("|") + 1, value.lastIndexOf("|"));
			return storedTime.substring(0,storedTime.indexOf(":"));
			}
			catch(Exception e)
			{
			}
		return "ERR";
		}
		
	public static String getAlarmMinutes(String value)
		{
		try
			{
			String storedTime = value.substring(value.indexOf("|") + 1, value.lastIndexOf("|"));
			return storedTime.substring(storedTime.indexOf(":")+1,storedTime.length());
			}
			catch(Exception e)
			{
			}
		return "ERR";
		}
		
	public static String getAlarmMessage(String value)
		{
		try
			{
			return value.substring(value.lastIndexOf("|") + 1, value.length());
			}
			catch(Exception e)
			{
			}
		return "ERROR";
		}
		
	public static void readBookmarksDatabase()
		{
		GlobalVars.browserBookmarks.clear();
		String result = readFile("bookmarks.dat");
		BufferedReader rdr = null;
		try
			{
			rdr = new BufferedReader(new StringReader(result));
			for (String line = rdr.readLine(); line != null; line = rdr.readLine())
				{
				if (line.length()>3)
					{
					GlobalVars.browserBookmarks.add(line);
					}
				}
			}
			catch(Exception e)
			{
			}
		try
			{
			rdr.close();
			}
			catch (Exception e)
			{
			}
		Collections.sort(GlobalVars.browserBookmarks, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		}

	public static void saveBookmarksDatabase()
		{
		String toSave = "";
		for (int i=0;i<GlobalVars.browserBookmarks.size();i++)
			{
			String savedValue = GlobalVars.browserBookmarks.get(i) + "\n";
				if (toSave=="")
				{
				toSave = savedValue;
				}
				else
				{
				toSave = toSave + savedValue;
				}
			}
		GlobalVars.writeFile("bookmarks.dat",toSave);
		}
	
	public static void sortBookmarksDatabase()
		{
		Collections.sort(GlobalVars.browserBookmarks, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		}
		
	public static void musicPlayerPreviousTrack()
		{
		if (GlobalVars.musicPlayerDatabasePlayList.size()>0)
			{
			if (GlobalVars.musicPlayerPlayingSongIndex-1==-1)
				{
				GlobalVars.musicPlayerPlayingSongIndex = GlobalVars.musicPlayerDatabasePlayList.size();
				}
				GlobalVars.musicPlayerPlayingSongIndex = GlobalVars.musicPlayerPlayingSongIndex - 1;

			try
				{
				if (GlobalVars.musicPlayer!=null)
					{
					GlobalVars.musicPlayer.stop();
					GlobalVars.musicPlayer.reset();
					GlobalVars.musicPlayer.release();
					GlobalVars.musicPlayer = null;
					}
				}
				catch(Exception e)
				{
				}

			musicPlayerPlayFile();
			}
			else
			{
			GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerNowPlayingError1));
			}
		}
		
	public static void musicPlayerNextTrack()
		{
		if (GlobalVars.musicPlayerDatabasePlayList.size()>0)
			{
			if (GlobalVars.musicPlayerPlayingSongIndex+1==GlobalVars.musicPlayerDatabasePlayList.size())
				{
				GlobalVars.musicPlayerPlayingSongIndex = -1;
				}
			GlobalVars.musicPlayerPlayingSongIndex = GlobalVars.musicPlayerPlayingSongIndex + 1;

			try
				{
				if (GlobalVars.musicPlayer!=null)
					{
					GlobalVars.musicPlayer.stop();
					GlobalVars.musicPlayer.reset();
					GlobalVars.musicPlayer.release();
					GlobalVars.musicPlayer = null;
					}
				}
				catch(Exception e)
				{
				}
			
			musicPlayerPlayFile();
			}
			else
			{
			GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerNowPlayingError1));
			}
		}

	public static void musicPlayerPlayStop()
		{
		try
			{
			if (GlobalVars.musicPlayer!=null)
				{
				try
					{
					GlobalVars.musicPlayer.stop();
					GlobalVars.musicPlayer.reset();
					GlobalVars.musicPlayer.release();
					GlobalVars.musicPlayer = null;
					}
					catch(Exception e)
					{
					}

				GlobalVars.musicPlayerPlayingSongIndex = -1;

				try
					{
					MusicPlayer.playstopicon.setImageResource(R.drawable.playerplay);
					if (GlobalVars.activityItemLocation==2)
						{
						GlobalVars.setText(MusicPlayer.playstop,true,GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerPlay));
						}
						else
						{
						GlobalVars.setText(MusicPlayer.playstop,false,GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerPlay));
						}
					}
					catch(Exception e)
					{
					}
				}
				else
				{
				if (GlobalVars.musicPlayerDatabasePlayList.size()>0)
					{
					if (GlobalVars.musicPlayerPlayingSongIndex == -1)
						{
						GlobalVars.musicPlayerPlayingSongIndex = 0;
						}
					musicPlayerPlayFile();
					}
					else
					{
					GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerNowPlayingError1));
					}
				}
			}
			catch(NullPointerException e)
			{
			GlobalVars.musicPlayerNextTrack();
			}
			catch(Exception e)
			{
			GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerNowPlayingError1));
			}
		}
		
	public static void musicPlayerPlayFile()
		{
		try
			{
			GlobalVars.musicPlayerCurrentArtist = GlobalVars.getAudioArtist(GlobalVars.musicPlayerDatabasePlayList.get(GlobalVars.musicPlayerPlayingSongIndex));
			GlobalVars.musicPlayerCurrentSong = GlobalVars.getAudioSongName(GlobalVars.musicPlayerDatabasePlayList.get(GlobalVars.musicPlayerPlayingSongIndex));
			GlobalVars.musicPlayer = new MediaPlayer();
			GlobalVars.musicPlayer.setDataSource(GlobalVars.getAudioPath(GlobalVars.musicPlayerDatabasePlayList.get(GlobalVars.musicPlayerPlayingSongIndex)));
			GlobalVars.musicPlayer.prepare();
			GlobalVars.musicPlayer.start();
			GlobalVars.musicPlayer.setOnCompletionListener(new OnCompletionListener()
				{
				public void onCompletion(MediaPlayer mp)
					{
					GlobalVars.musicPlayerNextTrack();
					}
				});

			try
				{
				MusicPlayer.playstopicon.setImageResource(R.drawable.playerstop);
				if (GlobalVars.activityItemLocation==2)
					{
					GlobalVars.setText(MusicPlayer.playstop,true,GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerStop));
					}
					else
					{
					GlobalVars.setText(MusicPlayer.playstop,false,GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerStop));
					}
				}
				catch(Exception e)
				{
				}
			}
			catch(Exception e)
			{
			GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerArtistsPlayError2));
			}
		}
	
	public static String getAudioArtist(String value)
		{
		try
			{
			return value.substring(0,value.indexOf("|"));
			}
			catch(Exception e)
			{
			return "";
			}
		}
		
	public static String getAudioAlbum(String value)
		{
		try
			{
			String subvalue = value.substring(value.indexOf("|") + 1, value.length());
			return subvalue.substring(0,subvalue.indexOf("|"));
			}
			catch(Exception e)
			{
			return "";
			}
		}
		
	public static String getAudioTrack(String value)
		{
		try
			{
			String subvalue = value.substring(value.indexOf("|") + 1, value.length());
			String subvalue2 = subvalue.substring(subvalue.indexOf("|") + 1, subvalue.length());
			return subvalue2.substring(0,subvalue2.indexOf("|"));
			}
			catch(Exception e)
			{
			return "";
			}
		}
		
	public static String getAudioSongName(String value)
		{
		try
			{
			String subvalue = value.substring(value.indexOf("|") + 1, value.length());
			String subvalue2 = subvalue.substring(subvalue.indexOf("|") + 1, subvalue.length());
			String subvalue3 = subvalue2.substring(subvalue2.indexOf("|") + 1, subvalue2.length());
			return subvalue3.substring(0,subvalue3.indexOf("|"));
			}
			catch(Exception e)
			{
			return "";
			}
		}
		
	public static String getAudioPath(String value)
		{
		try
			{
			return value.substring(value.lastIndexOf("|") + 1,value.length());
			}
			catch(Exception e)
			{
			return "";
			}
		}
		
	public static String getContactNameFromPhoneNumber(String phone)
		{
		String finalName = "";
		try
			{
			Cursor cursor = GlobalVars.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
			if (cursor!=null)
				{
			    if(cursor.moveToFirst())
			    	{
			        while(!cursor.isAfterLast())
			        	{
						String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						if (phoneNumber.replace("-", "").equals(phone.replace("-", "")) | phoneNumber.replace("-", "").contains(phone.replace("-", "")))
							{
							finalName = name;
							}
			            cursor.moveToNext();
			        	}
			        //cursor.close();
			    	}				
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return finalName;
		}
		
	public static String getMonthName(int month)
		{
		String result = "";
		switch (month)
			{
			case 1:
			result = GlobalVars.context.getResources().getString(R.string.january);
			break;

			case 2:
			result = GlobalVars.context.getResources().getString(R.string.february);
			break;

			case 3:
			result = GlobalVars.context.getResources().getString(R.string.march);
			break;

			case 4:
			result = GlobalVars.context.getResources().getString(R.string.april);
			break;

			case 5:
			result = GlobalVars.context.getResources().getString(R.string.may);
			break;

			case 6:
			result = GlobalVars.context.getResources().getString(R.string.june);
			break;

			case 7:
			result = GlobalVars.context.getResources().getString(R.string.july);
			break;

			case 8:
			result = GlobalVars.context.getResources().getString(R.string.august);
			break;

			case 9:
			result = GlobalVars.context.getResources().getString(R.string.september);
			break;

			case 10:
			result = GlobalVars.context.getResources().getString(R.string.october);
			break;

			case 11:
			result = GlobalVars.context.getResources().getString(R.string.november);
			break;

			case 12:
			result = GlobalVars.context.getResources().getString(R.string.december);
			break;
			}
		return result;
		}
	
	public static String detectNumberOrContact(String phoneNumber)
		{
		String contactName = GlobalVars.getContactNameFromPhoneNumber(phoneNumber);
		String finalName = "";
		if (contactName==null)
			{
			finalName = phoneNumber;
			finalName = finalName.replace("-","");
			finalName = finalName.replace("+","");
			finalName = finalName.replace("  "," ");
			finalName = GlobalVars.divideNumbersWithBlanks(finalName);
			}
			else
			{
			finalName = contactName;
			finalName = finalName.replace("-","");
			finalName = finalName.replace("  "," ");
			}
		return finalName;
		}
	
	public static String getMessagePhoneNumber(String value)
		{
		String val1 = value.substring(value.indexOf("|") + 1, value.length());
		return val1.substring(0,val1.indexOf("|"));
		}

	public static String getMessageContactName(String value)
		{
		String val1 = value.substring(value.indexOf("|") + 1, value.length());
		String val2 = val1.substring(0,val1.indexOf("|"));
		return GlobalVars.detectNumberOrContact(val2);
		}

	public static String getMessageBody(String value)
		{
		return value.substring(0,value.indexOf("|"));
		}
	
	public static String getMessageDateTime(String value)
		{
		String val1 = value.substring(value.indexOf("|") + 1, value.length());
		String val2 = val1.substring(val1.indexOf("|") + 1, val1.length());
		return val2.substring(0,val2.indexOf("|"));
		}

	public static String getMessageID(String value)
		{
		return value.substring(value.lastIndexOf("|") + 1, value.length());
		}
		
	public static String contactsGetPhoneNumberFromListValue(String value)
		{
		String response = "";
		try
			{
			response = value.substring(value.lastIndexOf("|") + 1, value.length());
			response = response.replaceAll("[^0-9]","");
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return response;
		}

	public static String contactsGetNameFromListValue(String value)
		{
		String response = "";
		try
			{
			response = value.substring(0, value.lastIndexOf("|"));
			response = response.replaceAll("-","");
			response = response.replaceAll("  "," ");
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return response;
		}

	public static String getMonth()
		{
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(Calendar.getInstance().getTime());
		}

	public static String getYear()
		{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(Calendar.getInstance().getTime());
		}
	
	public static void startAlarmActivity(Class<?> myClass)
		{
		try
			{
	        Intent intent = new Intent();
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setAction("android.intent.action.VIEW");
	        intent.setComponent(new ComponentName(context.getPackageName(), myClass.getName()));
	        contextApp.startActivity(intent);
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
	
	public static boolean isValidURL(String url)
		{  
	    URL u = null;

	    try
	    	{  
	        u = new URL(url);  
	    	}
	    	catch (MalformedURLException e)
	    	{  
	        return false;  
	    	}

	    try
	    	{  
	        u.toURI();  
	    	}
	    	catch (URISyntaxException e)
	    	{  
	        return false;  
	    	}  
	    return true;  
		}
	}

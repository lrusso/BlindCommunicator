package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.net.wifi.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Settings extends Activity
	{
	private TextView readingspeed;
	private TextView screentimeout;
	private TextView alarmtone;
	private TextView notifytone;
	private TextView calltone;
	public static TextView profile;
	public static TextView wifi;
	public static ImageView wifiicon;
	public static TextView bluetooth;
	private AudioManager audioManager;
	private TextView inputmode;
	private TextView systemsettings;
	private TextView privacypolicy;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.settings);
		GlobalVars.lastActivity = Settings.class;
    	readingspeed = (TextView) findViewById(R.id.readingspeed);
    	screentimeout = (TextView) findViewById(R.id.screentimeout);
    	alarmtone = (TextView) findViewById(R.id.alarmtone);
		notifytone = (TextView) findViewById(R.id.notifytone);
    	calltone = (TextView) findViewById(R.id.calltone);
    	profile = (TextView) findViewById(R.id.profile);
    	wifi = (TextView) findViewById(R.id.settingswifi);
		wifiicon = (ImageView) findViewById(R.id.settingswifiicon);
		bluetooth = (TextView) findViewById(R.id.settingsbluetooth);
    	inputmode = (TextView) findViewById(R.id.settingsinputmode);
    	systemsettings = (TextView) findViewById(R.id.systemsettings);
    	privacypolicy = (TextView) findViewById(R.id.privacypolicy);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=12;
		
		//SETS READING SPEED VALUE IN TEXTVIEW
		GlobalVars.setText(readingspeed,false,getResources().getString(R.string.layoutSettingsReadingSpeed) + String.valueOf(GlobalVars.settingsTTSReadingSpeed));
		
		//SETS SCREEN TIMEOUT VALUE IN TEXTVIEW
		String ending = "";
		if (GlobalVars.settingsScreenTimeOut<60)
			{
			ending = GlobalVars.settingsScreenTimeOut + getResources().getString(R.string.layoutSettingsSeconds);
			}
		else if(GlobalVars.settingsScreenTimeOut==60)
			{
			ending = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinute);
			}
		else
			{
			ending = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinutes);
			}	
		GlobalVars.setText(screentimeout,false,getResources().getString(R.string.layoutSettingsScreenTimeout) + ending);
	
		//SETS ACTUAL ALARM TONE VALUE IN TEXTVIEW
		GlobalVars.setText(alarmtone,false,getResources().getString(R.string.layoutSettingsAlarmTone) + getActualAlarmTone());
		
		//SETS ACTUAL NOTIFICATION TONE VALUE IN TEXTVIEW
		GlobalVars.setText(notifytone,false,getResources().getString(R.string.layoutSettingsNotifyTone) + getActualNotificationTone());

		//SETS ACTUAL CALL TONE VALUE IN TEXTVIEW
		if (GlobalVars.deviceIsAPhone()==true)
			{
			GlobalVars.setText(calltone,false,getResources().getString(R.string.layoutSettingsCallTone) + getActualCallTone());
			}
			else
			{
			GlobalVars.setText(calltone,false,getResources().getString(R.string.layoutSettingsCallToneNotAvailable));
			}
		
		//SETS DEVICE PROFILE VALUE IN TEXTVIEW
		audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
		switch(audioManager.getRingerMode())
			{
			case AudioManager.RINGER_MODE_NORMAL:
			GlobalVars.setText(profile,false,getResources().getString(R.string.layoutSettingsProfileNormal));
			break;

			case AudioManager.RINGER_MODE_SILENT:
			GlobalVars.setText(profile,false,getResources().getString(R.string.layoutSettingsProfileSilent));
			break;

			case AudioManager.RINGER_MODE_VIBRATE:
			GlobalVars.setText(profile,false,getResources().getString(R.string.layoutSettingsProfileVibrate));
			break;
			}
		
		//SETS WIFI STATE VALUE IN TEXTVIEW
		if (GlobalVars.isWifiEnabled())
			{
			wifiicon.setImageResource(R.drawable.settingswifion);
			GlobalVars.setText(wifi,false,getResources().getString(R.string.layoutSettingsWifiOn));
			}
			else
			{	
			wifiicon.setImageResource(R.drawable.settingswifioff);
			GlobalVars.setText(wifi,false,getResources().getString(R.string.layoutSettingsWifiOff));
			}
		
		//SETS BLUETOOTH STATE VALUE IN TEXTVIEW
		if (GlobalVars.isBluetoothEnabled())
			{
			GlobalVars.setText(bluetooth, false, getResources().getString(R.string.layoutSettingsBluetoothOn));
			}
			else
			{
			GlobalVars.setText(bluetooth, false, getResources().getString(R.string.layoutSettingsBluetoothOff));
			}
		
		//SETS INPUT MODE VALUE IN TEXTVIEW
		if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARD)
			{
			GlobalVars.setText(inputmode,false,getResources().getString(R.string.layoutSettingsInputMode) + getResources().getString(R.string.layoutSettingsInputModeKeyboard));
			}
		else if (GlobalVars.inputMode==GlobalVars.INPUT_VOICE)
			{
			GlobalVars.setText(inputmode,false,getResources().getString(R.string.layoutSettingsInputMode) + getResources().getString(R.string.layoutSettingsInputModeVoice));
			}
		else if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARDTALKBACK)
			{
			GlobalVars.setText(inputmode,false,getResources().getString(R.string.layoutSettingsInputMode) + getResources().getString(R.string.layoutSettingsInputModeKeyboardTalkback));
			}
		}
		
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = Settings.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=12;
		GlobalVars.selectTextView(readingspeed,false);
		GlobalVars.selectTextView(screentimeout,false);
		GlobalVars.selectTextView(alarmtone,false);
		GlobalVars.selectTextView(notifytone,false);
		GlobalVars.selectTextView(calltone,false);
		GlobalVars.selectTextView(profile,false);
		GlobalVars.selectTextView(wifi,false);
		GlobalVars.selectTextView(bluetooth,false);
		GlobalVars.selectTextView(inputmode,false);
		GlobalVars.selectTextView(systemsettings,false);
		GlobalVars.selectTextView(privacypolicy,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutSettingsOnResume));
		}
		
	public void select()
		{
		try
			{
			GlobalVars.settingsRingtonePlayerTester.stop();
			GlobalVars.settingsRingtonePlayerTester = null;
			}
			catch(Exception e)
			{
			}
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READING SPEED
			GlobalVars.selectTextView(readingspeed,true);
			GlobalVars.selectTextView(screentimeout,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsReadingSpeed2) + String.valueOf(GlobalVars.settingsTTSReadingSpeed));
			break;

			case 2: //SCREEN TIMEOUT
			GlobalVars.selectTextView(screentimeout, true);
			GlobalVars.selectTextView(readingspeed,false);
			GlobalVars.selectTextView(alarmtone,false);
			String ending = "";
			if (GlobalVars.settingsScreenTimeOut<60)
				{
				ending = GlobalVars.settingsScreenTimeOut + getResources().getString(R.string.layoutSettingsSeconds2);
				}
			else if(GlobalVars.settingsScreenTimeOut==60)
				{
				ending = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinute2);
				}
				else
				{
				ending = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinutes2);
				}
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsScreenTimeout2) + ending);
			break;

			case 3: //ALARM TONE
			GlobalVars.selectTextView(alarmtone,true);
			GlobalVars.selectTextView(screentimeout,false);
			GlobalVars.selectTextView(notifytone,false);
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsAlarmTone2) + getActualAlarmTone());
			break;
			
			case 4: //NOTIFY TONE
			GlobalVars.selectTextView(notifytone,true);
			GlobalVars.selectTextView(alarmtone,false);
			GlobalVars.selectTextView(calltone,false);
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsNotifyTone2) + getActualNotificationTone());
			break;
			
			case 5: //CALL TONE
			GlobalVars.selectTextView(calltone,true);
			GlobalVars.selectTextView(notifytone,false);
			GlobalVars.selectTextView(profile,false);
			if (GlobalVars.deviceIsAPhone()==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallTone2) + getActualCallTone());
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallToneNotAvailable2));
				}
			break;
			
			case 6: //PROFILE
			GlobalVars.selectTextView(profile,true);
			GlobalVars.selectTextView(calltone,false);
			GlobalVars.selectTextView(wifi,false);
			switch(audioManager.getRingerMode())
				{
				case AudioManager.RINGER_MODE_NORMAL:
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsProfileNormal2));
				break;

				case AudioManager.RINGER_MODE_SILENT:
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsProfileSilent2));
				break;

				case AudioManager.RINGER_MODE_VIBRATE:
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsProfileVibrate2));
				break;
				}
			break;
			
			case 7: //WIFI
			GlobalVars.selectTextView(wifi,true);
			GlobalVars.selectTextView(profile,false);
			GlobalVars.selectTextView(bluetooth,false);
			if (GlobalVars.isWifiEnabled())
				{
				String name = GlobalVars.getWifiSSID();
				if (name=="")
					{
					GlobalVars.talk(getResources().getString(R.string.layoutSettingsWifiOn2WithoutNetwork));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutSettingsWifiOn2WithNetwork) + name);
					}
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsWifiOff2));
				}
			break;
			
			case 8: //BLUETOOTH
			GlobalVars.selectTextView(bluetooth,true);
			GlobalVars.selectTextView(wifi,false);
			GlobalVars.selectTextView(inputmode,false);
			if (GlobalVars.isBluetoothEnabled()==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsBluetoothOn2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsBluetoothOff2));
				}
			break;
			
			case 9: //INPUT MODE
			GlobalVars.selectTextView(inputmode,true);
			GlobalVars.selectTextView(bluetooth,false);
			GlobalVars.selectTextView(systemsettings,false);
			if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARD)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsInputMode2) + getResources().getString(R.string.layoutSettingsInputModeKeyboard2));
				}
			else if (GlobalVars.inputMode==GlobalVars.INPUT_VOICE)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsInputMode2) + getResources().getString(R.string.layoutSettingsInputModeVoice2));
				}
			else if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARDTALKBACK)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsInputMode2) + getResources().getString(R.string.layoutSettingsInputModeKeyboardTalkback2));
				}
			break;

			case 10: //SYSTEM SETTINGS
			GlobalVars.selectTextView(systemsettings,true);
			GlobalVars.selectTextView(inputmode,false);
			GlobalVars.selectTextView(privacypolicy,false);
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsSystemSettings));
			break;

			case 11: //PRIVACY POLICY
			GlobalVars.selectTextView(privacypolicy,true);
			GlobalVars.selectTextView(systemsettings,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsPrivacyPolicy));
			break;
			
			case 12: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(privacypolicy,false);
			GlobalVars.selectTextView(readingspeed,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READING SPEED
			try
				{
				if (GlobalVars.settingsTTSReadingSpeed+1>GlobalVars.settingsTTSReadingSpeedLimit)
					{
					GlobalVars.settingsTTSReadingSpeed = 1;
					GlobalVars.tts.setSpeechRate(1);
					}
					else
					{
					GlobalVars.settingsTTSReadingSpeed = GlobalVars.settingsTTSReadingSpeed + 1;
					GlobalVars.tts.setSpeechRate(GlobalVars.settingsTTSReadingSpeed);
					}
				}
				catch(Exception e)
				{
				}
			GlobalVars.writeFile("readingspeed.cfg",String.valueOf(GlobalVars.settingsTTSReadingSpeed));
			GlobalVars.setText(readingspeed,true,getResources().getString(R.string.layoutSettingsReadingSpeed) + String.valueOf(GlobalVars.settingsTTSReadingSpeed));
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsReadingSpeed2) + String.valueOf(GlobalVars.settingsTTSReadingSpeed));
			break;

			case 2: //SCREEN TIMEOUT
			int result = -1;
			for (int i=0;i<GlobalVars.settingsScreenTimeOutValues.size();i++)
				{
				if (GlobalVars.settingsScreenTimeOut==Integer.valueOf(GlobalVars.settingsScreenTimeOutValues.get(i)))
					{
					result = i;
					}
				}
			if (result+1<=GlobalVars.settingsScreenTimeOutValues.size()-1)
				{
				result = result + 1;
				}
			else if (result+1==GlobalVars.settingsScreenTimeOutValues.size())
				{
				result=0;
				}
			else if (result==-1)
				{
				result = GlobalVars.settingsScreenTimeOutValues.size()-1;
				}
			
			try
				{
				GlobalVars.settingsScreenTimeOut = Integer.valueOf(GlobalVars.settingsScreenTimeOutValues.get(result));
				setScreenTimeOut(GlobalVars.settingsScreenTimeOut);
				GlobalVars.writeFile("screentimeout.cfg",String.valueOf(GlobalVars.settingsScreenTimeOut));
				}
				catch(Exception e)
				{
				GlobalVars.settingsScreenTimeOut = Integer.valueOf(GlobalVars.settingsScreenTimeOutValues.get(GlobalVars.settingsScreenTimeOutValues.size() -1));
				setScreenTimeOut(GlobalVars.settingsScreenTimeOut);
				GlobalVars.writeFile("screentimeout.cfg",String.valueOf(GlobalVars.settingsScreenTimeOut));
				}
				
			String ending = "";
			String ending2 = "";
			if (GlobalVars.settingsScreenTimeOut<60)
				{
				ending = GlobalVars.settingsScreenTimeOut + getResources().getString(R.string.layoutSettingsSeconds2);
				ending2 = GlobalVars.settingsScreenTimeOut + getResources().getString(R.string.layoutSettingsSeconds);
				}
				else if(GlobalVars.settingsScreenTimeOut==60)
				{
				ending = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinute2);
				ending2 = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinute);
				}
				else
				{
				ending = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinutes2);
				ending2 = (GlobalVars.settingsScreenTimeOut/60) + getResources().getString(R.string.layoutSettingsMinutes);
				}
			
			GlobalVars.setText(screentimeout,true,getResources().getString(R.string.layoutSettingsScreenTimeout) + ending2);
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsScreenTimeout2) + ending);
			break;

			case 3: //ALARM TONE
			try
				{
				int actualAlarmTone = getAlarmToneIndex(getActualAlarmTone());
				if (actualAlarmTone+1==GlobalVars.settingsToneAlarmTitle.size())
					{
					setAlarmTone(GlobalVars.settingsToneAlarmUri.get(0),GlobalVars.settingsToneAlarmID.get(0));
					}
					else
					{
					setAlarmTone(GlobalVars.settingsToneAlarmUri.get(actualAlarmTone+1),GlobalVars.settingsToneAlarmID.get(actualAlarmTone+1));
					}
				playAlarmTone();
				GlobalVars.setText(alarmtone,true,getResources().getString(R.string.layoutSettingsAlarmTone) + getActualAlarmTone());
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsAlarmTone2) + getActualAlarmTone());
				}
				catch(IndexOutOfBoundsException e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsAlarmToneNotAvailable));
				}
				catch(NullPointerException e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsAlarmToneNotAvailable));
				}
				catch(Exception e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsAlarmToneNotAvailable));
				}
			break;
			
			case 4: //NOTIFY TONE
			try
				{
				int actualNotifyTone = getNotificationToneIndex(getActualNotificationTone());
				if (actualNotifyTone+1==GlobalVars.settingsToneNotificationTitle.size())
					{
					setNotificationTone(GlobalVars.settingsToneNotificationUri.get(0),GlobalVars.settingsToneNotificationID.get(0));
					}
					else
					{
					setNotificationTone(GlobalVars.settingsToneNotificationUri.get(actualNotifyTone+1),GlobalVars.settingsToneNotificationID.get(actualNotifyTone+1));
					}
				playNotificationTone();
				GlobalVars.setText(notifytone,true,getResources().getString(R.string.layoutSettingsNotifyTone) + getActualNotificationTone());
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsNotifyTone2) + getActualNotificationTone());
				}
				catch(IndexOutOfBoundsException e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsNotifyToneNotAvailable));
				}
				catch(NullPointerException e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsNotifyToneNotAvailable));
				}
				catch(Exception e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsNotifyToneNotAvailable));
				}
			break;
			
			case 5: //CALL TONE
			if (GlobalVars.deviceIsAPhone()==true)
				{
				try
					{
					int actualCallTone = getCallToneIndex(getActualCallTone());
					if (actualCallTone+1==GlobalVars.settingsToneCallTitle.size())
						{
						setCallTone(GlobalVars.settingsToneCallUri.get(0),GlobalVars.settingsToneCallID.get(0));
						}
						else
						{
						setCallTone(GlobalVars.settingsToneCallUri.get(actualCallTone+1),GlobalVars.settingsToneCallID.get(actualCallTone+1));
						}
					playCallTone();
					GlobalVars.setText(calltone,true,getResources().getString(R.string.layoutSettingsCallTone) + getActualCallTone());
					GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallTone2) + getActualCallTone());
					}
					catch(IndexOutOfBoundsException e)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallToneNotAvailable2));
					}
					catch(NullPointerException e)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallToneNotAvailable2));
					}
					catch(Exception e)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallToneNotAvailable2));
					}
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsCallToneNotAvailable2));
				}
			break;
			
			case 6: //PROFILE
			switch(audioManager.getRingerMode())
				{
				case AudioManager.RINGER_MODE_NORMAL:
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsProfileSilent2));
				break;

				case AudioManager.RINGER_MODE_SILENT:
				audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsProfileVibrate2));
				break;

				case AudioManager.RINGER_MODE_VIBRATE:
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsProfileNormal2));
				break;
				}
			break;
			
			case 7: //WIFI
			WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			if (wifiManager.isWifiEnabled()==true)
				{
				wifiManager.setWifiEnabled(false);
				}
				else
				{
				wifiManager.setWifiEnabled(true);
				}
			break;
			
			case 8: //BLUETOOTH
			if (GlobalVars.isBluetoothEnabled()==true)
				{
				GlobalVars.deviceSetBluetooth(false);
				}
				else
				{
				GlobalVars.deviceSetBluetooth(true);
				}
			break;
			
			case 9: //INPUT MODE
			if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARD)
				{
				GlobalVars.inputMode=GlobalVars.INPUT_VOICE;
				GlobalVars.writeFile("inputmode.cfg",String.valueOf(GlobalVars.INPUT_VOICE));
				GlobalVars.setText(inputmode,true,getResources().getString(R.string.layoutSettingsInputMode) + getResources().getString(R.string.layoutSettingsInputModeVoice));
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsInputMode2) + getResources().getString(R.string.layoutSettingsInputModeVoice2));
				}
			else if (GlobalVars.inputMode==GlobalVars.INPUT_VOICE)
				{
				GlobalVars.inputMode=GlobalVars.INPUT_KEYBOARDTALKBACK;
				GlobalVars.writeFile("inputmode.cfg",String.valueOf(GlobalVars.INPUT_KEYBOARDTALKBACK));
				GlobalVars.setText(inputmode,true,getResources().getString(R.string.layoutSettingsInputMode) + getResources().getString(R.string.layoutSettingsInputModeKeyboardTalkback));
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsInputMode2) + getResources().getString(R.string.layoutSettingsInputModeKeyboardTalkback2));
				}
			else if (GlobalVars.inputMode==GlobalVars.INPUT_KEYBOARDTALKBACK)
				{
				GlobalVars.inputMode=GlobalVars.INPUT_KEYBOARD;
				GlobalVars.writeFile("inputmode.cfg",String.valueOf(GlobalVars.INPUT_KEYBOARD));
				GlobalVars.setText(inputmode,true,getResources().getString(R.string.layoutSettingsInputMode) + getResources().getString(R.string.layoutSettingsInputModeKeyboard));
				GlobalVars.talk(getResources().getString(R.string.layoutSettingsInputMode2) + getResources().getString(R.string.layoutSettingsInputModeKeyboard2));
				}
			break;

			case 10: //SYSTEM SETTINGS
			try
				{
				startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			break;
			
			case 11: //PRIVACY POLICY
			GlobalVars.talk(getResources().getString(R.string.layoutSettingsPrivacyPolicy2));
			break;

			case 12: //GO BACK TO THE MAIN MENU
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
		
	private void setNotificationTone(String uri, String id)
		{
		try
			{
			Uri newUri = ContentUris.withAppendedId(Uri.parse(uri), Long.valueOf(id));
			RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, newUri);
			}
			catch(Exception e)
			{
			}
		}
		
	private String getActualNotificationTone()
		{
		try
			{
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone ringtone = RingtoneManager.getRingtone(this, alert);
			String titleDefault = ringtone.getTitle(this);
			return titleDefault.substring(titleDefault.indexOf("(") + 1, titleDefault.length()-1);
			}
			catch(Exception e)
			{
			}
			return getResources().getString(R.string.layoutSettingsToneNotAvailable);
		}
	
	private void setCallTone(String uri, String id)
		{
		try
			{
			Uri newUri = ContentUris.withAppendedId(Uri.parse(uri), Long.valueOf(id));
			RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri);
			}
			catch(Exception e)
			{
			}
		}
		
	private String getActualCallTone()
		{
		try
			{
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			Ringtone ringtone = RingtoneManager.getRingtone(this, alert);
			String titleDefault = ringtone.getTitle(this);
			return titleDefault.substring(titleDefault.indexOf("(") + 1, titleDefault.length()-1);
			}
			catch(Exception e)
			{
			}
		return getResources().getString(R.string.layoutSettingsToneNotAvailable);
		}

	private void setAlarmTone(String uri, String id)
		{
		try
			{
			Uri newUri = ContentUris.withAppendedId(Uri.parse(uri), Long.valueOf(id));
			RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, newUri);
			}
			catch(Exception e)
			{
			}
		}

	private String getActualAlarmTone()
		{
		try
			{
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			Ringtone ringtone = RingtoneManager.getRingtone(this, alert);
			String titleDefault = ringtone.getTitle(this);
			return titleDefault.substring(titleDefault.indexOf("(") + 1, titleDefault.length()-1);
			}
			catch(Exception e)
			{
			}
		return getResources().getString(R.string.layoutSettingsToneNotAvailable);
		}

	private void playAlarmTone()
		{
		try
			{
			GlobalVars.settingsRingtonePlayerTester.stop();
			GlobalVars.settingsRingtonePlayerTester = null;
			}
		catch(Exception e)
		{
		}
		try
			{
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			GlobalVars.settingsRingtonePlayerTester = MediaPlayer.create(this, notification);
			GlobalVars.settingsRingtonePlayerTester.start();
			GlobalVars.settingsRingtonePlayerTester.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()
				{
					@Override public void onSeekComplete(MediaPlayer mediaPlayer)
					{
						mediaPlayer.stop();
					}
				});
			}
			catch (Exception e)
			{
			}
		}

	private int getAlarmToneIndex(String toneTitle)
		{
		for (int i=0;i<GlobalVars.settingsToneAlarmTitle.size();i++)
			{
			if (GlobalVars.settingsToneAlarmTitle.get(i).toLowerCase().contains(toneTitle.toLowerCase()))
				{
				return i;
				}
			}
		return -1;
		}

	private int getNotificationToneIndex(String toneTitle)
		{
		for (int i=0;i<GlobalVars.settingsToneNotificationTitle.size();i++)
			{
			if (GlobalVars.settingsToneNotificationTitle.get(i).toLowerCase().contains(toneTitle.toLowerCase()))
				{
				return i;
				}
			}
		return -1;
		}

	private void playNotificationTone()
		{
		try
			{
			GlobalVars.settingsRingtonePlayerTester.stop();
			GlobalVars.settingsRingtonePlayerTester = null;
			}
			catch(Exception e)
			{
			}
		try
			{
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			GlobalVars.settingsRingtonePlayerTester = MediaPlayer.create(this, notification);
			GlobalVars.settingsRingtonePlayerTester.start();
			GlobalVars.settingsRingtonePlayerTester.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()
				{
					@Override public void onSeekComplete(MediaPlayer mediaPlayer)
					{
						mediaPlayer.stop();
					}
				});
			}
			catch (Exception e)
			{
			}
		}

	private int getCallToneIndex(String toneTitle)
		{
		for (int i=0;i<GlobalVars.settingsToneCallTitle.size();i++)
			{
			if (GlobalVars.settingsToneCallTitle.get(i).toLowerCase().contains(toneTitle.toLowerCase()))
				{
				return i;
				}
			}
		return -1;
		}

	private void playCallTone()
		{
		try
			{
			GlobalVars.settingsRingtonePlayerTester.stop();
			GlobalVars.settingsRingtonePlayerTester = null;
			}
		catch(Exception e)
		{
		}
		try
			{
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			GlobalVars.settingsRingtonePlayerTester = MediaPlayer.create(this, notification);
			GlobalVars.settingsRingtonePlayerTester.start();
			GlobalVars.settingsRingtonePlayerTester.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()
				{
				@Override public void onSeekComplete(MediaPlayer mediaPlayer)
					{
					mediaPlayer.stop();
					}
				});
			}
			catch (Exception e)
			{
			}
		}
		
	private void setScreenTimeOut(int a)
		{
		try
			{
			android.provider.Settings.System.putInt(GlobalVars.context.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, a * 1000);
			}
			catch(Exception e)
			{
			}
		}
	}

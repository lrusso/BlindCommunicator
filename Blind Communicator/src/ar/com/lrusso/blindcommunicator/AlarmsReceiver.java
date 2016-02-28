package ar.com.lrusso.blindcommunicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.*;

public class AlarmsReceiver extends BroadcastReceiver
	{
	@Override public void onReceive(Context context, Intent intent)
		{
		try
			{
			String value = intent.getStringExtra("AR.COM.LRUSSO.BLINDCOMMUNICATOR.ALARM");
			
			String actualDayName = GlobalVars.getDayName(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
			String actualHour = Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
			String actualMinutes = Integer.toString(Calendar.getInstance().get(Calendar.MINUTE));

			//TO PREVENT ANY PAST ALARM TO POPUP
			if (GlobalVars.getAlarmDayName(value).equals(actualDayName))
				{
				if (GlobalVars.getAlarmHours(value).equals(actualHour))
					{
					if (GlobalVars.getAlarmMinutes(value).equals(actualMinutes))
						{
						GlobalVars.alarmMessage = GlobalVars.getAlarmMessage(value);
						GlobalVars.alarmDay = GlobalVars.getAlarmDayName(value);
						GlobalVars.alarmHours = GlobalVars.getAlarmHours(value);
						GlobalVars.alarmMinutes = GlobalVars.getAlarmMinutes(value);
						GlobalVars.startAlarmActivity(AlarmsNotifier.class);
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
		}
	}
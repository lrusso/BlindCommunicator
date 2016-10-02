package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.database.*;
import android.os.*;
import android.provider.*;

import java.util.*;

public class CallsLogsThread extends AsyncTask<Activity, String, Boolean>
	{

	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.callLogsReady = false;
		GlobalVars.callLogsDataBase.clear();
		}

	@Override protected Boolean doInBackground(Activity... act)
		{
		try
			{
			Cursor managedCursor = GlobalVars.context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null, null);
			
			int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
			int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
			int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
			int id = managedCursor.getColumnIndex(CallLog.Calls._ID);

			while (managedCursor.moveToNext())
				{
				String phoneNumber = managedCursor.getString(number);
				String callType = managedCursor.getString(type);
				String callDate = managedCursor.getString(date);
				String idCall = managedCursor.getString(id);
				
				Date callDayTime = new Date(Long.valueOf(callDate));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(callDayTime);

				String dayname = GlobalVars.getDayName(calendar.get(Calendar.DAY_OF_WEEK));
				String day = String.valueOf(callDayTime.getDay());
				String month = GlobalVars.getMonthName(callDayTime.getMonth() + 1);
				String year = String.valueOf(calendar.get(Calendar.YEAR));
				String hours = String.valueOf(callDayTime.getHours());
				String minutes = String.valueOf(callDayTime.getMinutes());
				String seconds = String.valueOf(callDayTime.getSeconds());
				
				GlobalVars.callLogsDataBase.add(callType + "|" +
												phoneNumber + "|" +
												GlobalVars.detectNumberOrContact(phoneNumber) + "| " +
												dayname + " " + day + GlobalVars.context.getResources().getString(R.string.mainOf) +
												month + GlobalVars.context.getResources().getString(R.string.mainOf) + year +
												GlobalVars.context.getResources().getString(R.string.layoutCallsLogsAt) +
												hours + GlobalVars.context.getResources().getString(R.string.layoutCallsLogsHours) +
												minutes + GlobalVars.context.getResources().getString(R.string.layoutCallsLogsMinutes) +
												seconds + GlobalVars.context.getResources().getString(R.string.layoutCallsLogsSeconds) +
												"|" + idCall);
				}
			}
			catch(Exception e)
			{
			}
		return false;
		}

	@Override protected void onPostExecute(Boolean pageloaded)
		{
		GlobalVars.callLogsReady = true;
		}
		
	}
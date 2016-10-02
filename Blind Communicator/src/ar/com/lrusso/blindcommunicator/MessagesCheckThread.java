package ar.com.lrusso.blindcommunicator;

import java.util.Calendar;

import android.database.Cursor;
import android.net.Uri;
import android.os.*;

public class MessagesCheckThread extends AsyncTask<Integer, String, Boolean>
	{
	int SMSType = -1;
	
    public MessagesCheckThread(int a)
    	{
    	SMSType = a;
    	}

	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		switch (SMSType)
			{
			case GlobalVars.TYPE_INBOX:
			GlobalVars.messagesInboxDataBase.clear();
			GlobalVars.messagesInboxDatabaseReady = false;
			break;
			
			case GlobalVars.TYPE_SENT:
			GlobalVars.messagesSentDataBase.clear();
			GlobalVars.messagesSentDatabaseReady = false;
			break;
			}
		}

	@Override protected Boolean doInBackground(Integer... act)
		{
    	Cursor cursor = null;
		try
			{
	    	String typeSMSValue = "";
			switch (SMSType)
				{
				case GlobalVars.TYPE_INBOX:
				typeSMSValue = "content://sms/inbox";
				break;
		
				case GlobalVars.TYPE_SENT:
				typeSMSValue = "content://sms/sent";
				break;
				}
	    	cursor = GlobalVars.context.getContentResolver().query(Uri.parse(typeSMSValue), null, null, null, "date DESC");
	    	
	       	cursor.moveToFirst();

			int index_Address = cursor.getColumnIndex("address");  
            int index_Body = cursor.getColumnIndex("body");  
            int index_Date = cursor.getColumnIndex("date");
            int index_ID = cursor.getColumnIndex("_id");

			for(int i=0;i<cursor.getCount();i++)
				{
                String messageAddress = cursor.getString(index_Address);  
                String messageBody = cursor.getString(index_Body);  
                long messageDate = cursor.getLong(index_Date);
                String messageID = cursor.getString(index_ID);

                switch (SMSType)
					{
					case GlobalVars.TYPE_INBOX:
					GlobalVars.messagesInboxDataBase.add(messageBody + "|" + messageAddress + "|" + getMessageDateTime(messageDate) + "|" + String.valueOf(messageID));
					break;

					case GlobalVars.TYPE_SENT:
					GlobalVars.messagesSentDataBase.add(messageBody + "|" + messageAddress + "|" + getMessageDateTime(messageDate) + "|" + String.valueOf(messageID));
					break;
					}
				cursor.moveToNext();
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

	@Override protected void onPostExecute(Boolean pageloaded)
		{
		switch (SMSType)
			{
			case GlobalVars.TYPE_INBOX:
			GlobalVars.messagesInboxDatabaseReady = true;
			break;

			case GlobalVars.TYPE_SENT:
			GlobalVars.messagesSentDatabaseReady = true;
			break;
			}
		}
	
	private String getMessageDateTime(long timestamp)
		{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		String mYear = String.valueOf(calendar.get(Calendar.YEAR));
		String mMonth = GlobalVars.getMonthName(calendar.get(Calendar.MONTH) + 1);
		String mDayName = GlobalVars.getDayName(calendar.get(Calendar.DAY_OF_WEEK));
		String mDayNumber = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String mHours = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		String mMinutes = String.valueOf(calendar.get(Calendar.MINUTE));
		String mSeconds = String.valueOf(calendar.get(Calendar.SECOND));
		return GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageOn) + 
			   mDayName + " " + mDayNumber +
			   GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageOf) +   
			   mMonth + GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageOf) +
			   mYear + GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageAt) +
			   mHours + GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageHours) +
			   mMinutes + GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageMinutes) +
			   mSeconds + GlobalVars.context.getResources().getString(R.string.layoutMessagesInboxMessageSeconds);
		}
	}

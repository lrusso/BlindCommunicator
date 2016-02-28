package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.provider.CallLog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.app.Activity;
import android.content.ContentValues;
import ar.com.lrusso.blindcommunicator.R;

public class CallsLogs extends Activity
	{
	public static TextView phonenumber;
	private TextView makecall;
	private TextView writeto;
	private TextView addtonewcontact;
	private TextView goback;
	public static int selectedLog = -1;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.callslogs);
		GlobalVars.lastActivity = CallsLogs.class;
		phonenumber = (TextView) findViewById(R.id.phonenumber);
		makecall = (TextView) findViewById(R.id.calltonumber);
		writeto = (TextView) findViewById(R.id.writeto);
		addtonewcontact = (TextView) findViewById(R.id.addtonewcontact);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=5;
		selectedLog = -1;
		
		GlobalVars.callLogsReady = false;
		GlobalVars.callLogsDataBase.clear();
		new CallsLogsThread().execute();
		}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = CallsLogs.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=5;
		GlobalVars.selectTextView(phonenumber,false);
		GlobalVars.selectTextView(makecall,false);
		GlobalVars.selectTextView(writeto, false);
		GlobalVars.selectTextView(addtonewcontact,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.messagesWasSent == true)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsOnResume2));
			GlobalVars.messagesWasSent = false;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsOnResume));
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CALL LOGS
			GlobalVars.selectTextView(phonenumber,true);
			GlobalVars.selectTextView(makecall,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedLog==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogs2));
				}
				else
				{
				if (GlobalVars.callLogsReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsPleaseTryAgain));
					}
					else
					{
					String callType = identifyCallType(getCallType(GlobalVars.callLogsDataBase.get(selectedLog)));
					String finalName = getContactNameIfAvailable(GlobalVars.callLogsDataBase.get(selectedLog));

					if (getCallType(GlobalVars.callLogsDataBase.get(selectedLog))==CallLog.Calls.OUTGOING_TYPE)
						{
						GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogItem) + (selectedLog + 1) + getResources().getString(R.string.layoutCallsLogsOfNumber) +
										GlobalVars.callLogsDataBase.size() + ". " + callType +
										getResources().getString(R.string.layoutCallsLogsCallTo) +
										finalName + "." + getDateTimeCall(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogItem) + (selectedLog + 1) + getResources().getString(R.string.layoutCallsLogsOfNumber) +
										GlobalVars.callLogsDataBase.size() + ". " + callType +
										getResources().getString(R.string.layoutCallsLogsCallFrom) +
										finalName + "." + getDateTimeCall(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
					}
				}
			break;

			case 2: //CALL TO NUMBER
			GlobalVars.selectTextView(makecall, true);
			GlobalVars.selectTextView(phonenumber,false);
			GlobalVars.selectTextView(writeto,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsCall2));
			break;
			
			case 3: //SEND A MESSAGE
			GlobalVars.selectTextView(writeto, true);
			GlobalVars.selectTextView(makecall,false);
			GlobalVars.selectTextView(addtonewcontact,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsSendMessage2));
			break;

			case 4: //ADD TO NEW CONTACT
			GlobalVars.selectTextView(addtonewcontact, true);
			GlobalVars.selectTextView(writeto,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsAddToNewContact));
			break;
			
			case 5: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(addtonewcontact,false);
			GlobalVars.selectTextView(phonenumber,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CALL LOGS
			if (GlobalVars.callLogsReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsPleaseTryAgain));
				}
				else
				{
				if (GlobalVars.callLogsDataBase.size()>0)
					{
					if (selectedLog+1==GlobalVars.callLogsDataBase.size())
						{
						selectedLog = -1;
						}
					selectedLog = selectedLog + 1;

					String callType = identifyCallType(getCallType(GlobalVars.callLogsDataBase.get(selectedLog)));
					String finalName = getContactNameIfAvailable(GlobalVars.callLogsDataBase.get(selectedLog));

					GlobalVars.setText(phonenumber, true, getResources().getString(R.string.layoutCallsLogsLogItem) + " (" + (selectedLog + 1) + "/" + GlobalVars.callLogsDataBase.size() + ")\n" +
									   callType + "\n" + finalName);
					
					if (getCallType(GlobalVars.callLogsDataBase.get(selectedLog))==CallLog.Calls.OUTGOING_TYPE)
						{
						GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogItem) + (selectedLog + 1) + getResources().getString(R.string.layoutCallsLogsOfNumber) +
										GlobalVars.callLogsDataBase.size() + ". " + callType +
										getResources().getString(R.string.layoutCallsLogsCallTo) +
										finalName + "." + getDateTimeCall(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogItem) + (selectedLog + 1) + getResources().getString(R.string.layoutCallsLogsOfNumber) +
										GlobalVars.callLogsDataBase.size() + ". " + callType +
										getResources().getString(R.string.layoutCallsLogsCallFrom) +
										finalName + "." + getDateTimeCall(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
					markCallLogRead(getCallID(GlobalVars.callLogsDataBase.get(selectedLog)));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsNoLogs));
					}
				}
			break;

			case 2: //CALL TO NUMBER
			if (GlobalVars.callLogsReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsPleaseTryAgain));
				}
				else
				{
				if (selectedLog==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsError));
					}
					else
					{
					try
						{
						GlobalVars.callTo("tel:" + getPhoneNumber(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
						catch(Exception e)
						{
						}
					}
				}
			break;
			
			case 3: //SEND A MESSAGE
			if (GlobalVars.callLogsReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsPleaseTryAgain));
				}
				else
				{
				if (selectedLog==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsError));
					}
					else
					{
					if (GlobalVars.deviceIsAPhone()==true)
						{
						GlobalVars.startActivity(MessagesCompose.class);
						try
							{
							MessagesCompose.messageToContactNameValue = getContactNameIfAvailable(GlobalVars.callLogsDataBase.get(selectedLog));
							MessagesCompose.messageToPhoneNumberValue = getPhoneNumber(GlobalVars.callLogsDataBase.get(selectedLog)); 
							}
							catch(Exception e)
							{
							}
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.mainNotAvailable));
						}
					}
				}
			break;

			case 4: //ADD TO NEW CONTACT
			if (GlobalVars.callLogsReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsPleaseTryAgain));
				}
				else
				{
				if (selectedLog==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsError));
					}
					else
					{
					GlobalVars.startActivity(ContactsCreate.class);
					try
						{
						ContactsCreate.phonenumberValue = getPhoneNumber(GlobalVars.callLogsDataBase.get(selectedLog));
						}
						catch(Exception e)
						{
						}
					}
				}
			break;
			
			case 5: //GO BACK TO THE PREVIOUS MENU
			selectedLog = -1;
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CALL LOGS
			if (GlobalVars.callLogsReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsPleaseTryAgain));
				}
				else
				{
				if (GlobalVars.callLogsDataBase.size()>0)
					{
					if (selectedLog-1<0)
						{
						selectedLog = GlobalVars.callLogsDataBase.size();
						}
					selectedLog = selectedLog - 1;

					String callType = identifyCallType(getCallType(GlobalVars.callLogsDataBase.get(selectedLog)));
					String finalName = getContactNameIfAvailable(GlobalVars.callLogsDataBase.get(selectedLog));

					GlobalVars.setText(phonenumber, true, getResources().getString(R.string.layoutCallsLogsLogItem) + " (" + (selectedLog + 1) + "/" + GlobalVars.callLogsDataBase.size() + ")\n" +
															callType + "\n" + finalName);
				
					if (getCallType(GlobalVars.callLogsDataBase.get(selectedLog))==CallLog.Calls.OUTGOING_TYPE)
						{
						GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogItem) + (selectedLog + 1) + getResources().getString(R.string.layoutCallsLogsOfNumber) +
										GlobalVars.callLogsDataBase.size() + ". " + callType +
										getResources().getString(R.string.layoutCallsLogsCallTo) +
										finalName + "." + getDateTimeCall(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsLogItem) + (selectedLog + 1) + getResources().getString(R.string.layoutCallsLogsOfNumber) +
										GlobalVars.callLogsDataBase.size() + ". " + callType +
										getResources().getString(R.string.layoutCallsLogsCallFrom) +
										finalName + "." + getDateTimeCall(GlobalVars.callLogsDataBase.get(selectedLog)));
						}
					markCallLogRead(getCallID(GlobalVars.callLogsDataBase.get(selectedLog)));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsNoLogs));
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

	public int getCallType(String value)
		{
		try
			{
			return Integer.valueOf(value.substring(0,value.indexOf("|")));
			}
			catch(Exception e)
			{
			return CallLog.Calls.MISSED_TYPE;
			}
		}

	public String getPhoneNumber(String value)
		{
		try
			{
			String val1 = value.substring(value.indexOf("|") + 1, value.length());
			return val1.substring(0,val1.indexOf("|"));
			}
			catch(Exception e)
			{
			return "0";
			}
		}
		
	public String getContactNameIfAvailable(String value)
		{
		try
			{
			String val1 = value.substring(value.indexOf("|") + 1, value.length());
			String val2 = val1.substring(val1.indexOf("|") + 1, val1.length());
			return val2.substring(0,val2.indexOf("|"));
			}
			catch(Exception e)
			{
			return "0";
			}
		}
		
	public String getDateTimeCall(String value)
		{
		try
			{
			String val1 = value.substring(value.indexOf("|") + 1, value.length());
			String val2 = val1.substring(val1.indexOf("|") + 1, val1.length());
			String val3 = val2.substring(val2.indexOf("|") + 1, val2.length());
			return val3.substring(0,val3.indexOf("|"));
			}
			catch(Exception e)
			{
			return "";
			}
		}
	
	public String getCallID(String value)
		{
		try
			{
			return value.substring(value.lastIndexOf("|") + 1, value.length());
			}
			catch(Exception e)
			{
			return "";
			}
		}
		
	public String identifyCallType(int value)
		{
		String callType = getResources().getString(R.string.layoutCallsLogsMissedCall);
		switch (value)
			{
			case CallLog.Calls.MISSED_TYPE:
			callType = getResources().getString(R.string.layoutCallsLogsMissedCall);
			break;

			case CallLog.Calls.INCOMING_TYPE:
			callType = getResources().getString(R.string.layoutCallsLogsIncomingCall);
			break;

			case CallLog.Calls.OUTGOING_TYPE:
			callType = getResources().getString(R.string.layoutCallsLogsOutgoingCall);
			break;
			}
		return callType;
		}
	
	public void markCallLogRead(String callID)
		{
		try
			{
		    ContentValues values = new ContentValues();
		    values.put(CallLog.Calls.NEW, Integer.valueOf(0));
		    String[] fv = new String[] {callID};
		    getContentResolver().update(CallLog.Calls.CONTENT_URI, values, CallLog.Calls._ID + "= ?", fv);			
			}
			catch(Exception e)
			{
			}
		}
	}
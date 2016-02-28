package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.widget.*;

public class MessagesCompose extends Activity
	{
	public static TextView addresse;
	private TextView body;
	private TextView send;
	private TextView goback;
	public static String messageToPhoneNumberValue = "";
	public static String messageToContactNameValue = "";
	public static String messageBody = "";
	private int selectedContact = -1;
	private boolean sending = false;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.messagescompose);
		GlobalVars.lastActivity = MessagesCompose.class;
		addresse = (TextView) findViewById(R.id.messagesaddresse);
		body = (TextView) findViewById(R.id.messagesbody);
		send = (TextView) findViewById(R.id.messagessent);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.messagesWasSent = false;
		if (messageToContactNameValue!="")
			{
			GlobalVars.setText(addresse, false, messageToContactNameValue);
			}
		new ContactsListThread().execute("");
    	}
	
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		if (GlobalVars.inputModeResult!=null)
			{
			messageBody = GlobalVars.inputModeResult;
			GlobalVars.setText(body, false, messageBody);
			GlobalVars.inputModeResult = null;
			}
		GlobalVars.lastActivity = MessagesCompose.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(addresse,false);
		GlobalVars.selectTextView(body,false);
		GlobalVars.selectTextView(send,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ADDRESSE
			GlobalVars.selectTextView(addresse,true);
			GlobalVars.selectTextView(body,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedContact==-1 && messageToContactNameValue=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeAddressee2));
				}
				else
				{
				if (messageToContactNameValue!="")
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeAddressee3) + messageToContactNameValue);
					}
				else if (selectedContact>-1)
					{
					if (GlobalVars.contactListReady==false)
						{
						GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
						}
						else
						{
						GlobalVars.talk(GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact)) +
									    getResources().getString(R.string.layoutContactsListWithThePhoneNumber) +
									    GlobalVars.divideNumbersWithBlanks(GlobalVars.contactsGetPhoneNumberFromListValue(GlobalVars.contactDataBase.get(selectedContact))));
						}
					}
				}
			break;

			case 2: //BODY
			GlobalVars.selectTextView(body, true);
			GlobalVars.selectTextView(addresse,false);
			GlobalVars.selectTextView(send,false);
			if (messageBody=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeMessage2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeMessage3) + messageBody);
				}
			break;

			case 3: //SEND
			GlobalVars.selectTextView(send, true);
			GlobalVars.selectTextView(body,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeSend));
			break;
			
			case 4: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(send,false);
			GlobalVars.selectTextView(addresse,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ADDRESSE
			if (GlobalVars.contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (GlobalVars.contactDataBase.size()>0)
					{
					if (selectedContact+1==GlobalVars.contactDataBase.size())
						{
						selectedContact=-1;
						}
					selectedContact=selectedContact+1;
					GlobalVars.setText(addresse, true, GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact)) + "\n" +
											   		   GlobalVars.contactsGetPhoneNumberFromListValue(GlobalVars.contactDataBase.get(selectedContact)));
					GlobalVars.talk(GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact)) +
									getResources().getString(R.string.layoutContactsListWithThePhoneNumber) +
									GlobalVars.divideNumbersWithBlanks(GlobalVars.contactsGetPhoneNumberFromListValue(GlobalVars.contactDataBase.get(selectedContact))));
					messageToPhoneNumberValue = GlobalVars.contactDataBase.get(selectedContact).substring(GlobalVars.contactDataBase.get(selectedContact).lastIndexOf("|") + 1,GlobalVars.contactDataBase.get(selectedContact).length());
					messageToContactNameValue = GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListNoContacts));
					}
				}
			break;

			case 2: //BODY
			GlobalVars.inputModeKeyboardOnlyNumbers=false;
			GlobalVars.startInputActivity();
			break;

			case 3: //SEND
			if (messageToPhoneNumberValue=="" | messageBody=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesComposeErrorComplete));
				}
				else
				{
				if (sending==true)
					{
					GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSendingError));
					}
					else
					{
					sendSMS(messageToPhoneNumberValue,messageBody);
					}
				}
			break;
			
			case 4: //GO BACK TO THE PREVIOUS MENU
			messageToPhoneNumberValue = "";
			messageToContactNameValue = "";
			messageBody = "";
			selectedContact = -1;
			this.finish();
			break;
			}
		}
		
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ADDRESSE
			if (GlobalVars.contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (GlobalVars.contactDataBase.size()>0)
					{
					if (selectedContact-1<0)
						{
						selectedContact = GlobalVars.contactDataBase.size();
						}
					selectedContact = selectedContact - 1;
					GlobalVars.setText(addresse, true, GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact)) + "\n" +
													   GlobalVars.contactsGetPhoneNumberFromListValue(GlobalVars.contactDataBase.get(selectedContact)));
					GlobalVars.talk(GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact)) +
									getResources().getString(R.string.layoutContactsListWithThePhoneNumber) +
									GlobalVars.divideNumbersWithBlanks(GlobalVars.contactsGetPhoneNumberFromListValue(GlobalVars.contactDataBase.get(selectedContact))));
					messageToPhoneNumberValue = GlobalVars.contactDataBase.get(selectedContact).substring(GlobalVars.contactDataBase.get(selectedContact).lastIndexOf("|") + 1,GlobalVars.contactDataBase.get(selectedContact).length());
					messageToContactNameValue = GlobalVars.contactsGetNameFromListValue(GlobalVars.contactDataBase.get(selectedContact));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListNoContacts));
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
		
	public void sendSMS(final String phone, final String message)
		{
		sending = true;
		GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSending));
		
		try
			{
			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";
			PendingIntent sentPI = PendingIntent.getBroadcast(GlobalVars.context, 0, new Intent(SENT), 0);
			PendingIntent deliveredPI = PendingIntent.getBroadcast(GlobalVars.context, 0, new Intent(DELIVERED), 0);

			// ---when the SMS has been sent---
			GlobalVars.context.registerReceiver(new BroadcastReceiver()
				{
				@Override public void onReceive(Context arg0, Intent arg1)
					{
					switch (getResultCode())
						{
						case Activity.RESULT_OK:
						//THE MESSAGE WAS SENT
						break;

						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSendingError2));
						sending = false;
						break;

						case SmsManager.RESULT_ERROR_NO_SERVICE:
						GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSendingError2));
						sending = false;
						break;

						case SmsManager.RESULT_ERROR_NULL_PDU:
						GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSendingError2));
						sending = false;
						break;

						case SmsManager.RESULT_ERROR_RADIO_OFF:
						GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSendingError2));
						sending = false;
						break;
						}
					}
				}, new IntentFilter(SENT));

			// ---when the SMS has been delivered---
			GlobalVars.context.registerReceiver(new BroadcastReceiver()
				{
				@Override public void onReceive(Context arg0, Intent arg1)
					{
					switch (getResultCode())
						{
						case Activity.RESULT_OK:
						//THE MESSAGE WAS DELIVERED
						GlobalVars.messagesWasSent = true;
						//CREATES THE MESSAGE INTO THE SYSTEM DATABASE
						try
							{
							ContentValues values = new ContentValues();
							values.put("address", phone);
							values.put("body", message);
							GlobalVars.context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
							}
							catch(Exception e)
							{
							}
						try
							{
							messageToPhoneNumberValue = "";
							messageToContactNameValue = "";
							messageBody = "";
							}
							catch(Exception e)
							{
							}
						sending = false;
						finish();
						break;

						case Activity.RESULT_CANCELED:
						GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutMessagesComposeSendingError2));
						sending = false;
						break;
						}
					}
				}, new IntentFilter(DELIVERED));

			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phone, null, message, sentPI, deliveredPI);
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
	}

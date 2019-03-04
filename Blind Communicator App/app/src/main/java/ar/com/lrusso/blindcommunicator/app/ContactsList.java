package ar.com.lrusso.blindcommunicator.app;

import java.util.ArrayList;
import java.util.List;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ar.com.lrusso.blindcommunicator.app.R;

public class ContactsList extends Activity
	{
	private TextView contacts;
	private TextView callto;
	private TextView writeto;
	private TextView delete;
	private TextView goback;
	public int selectedContact = -1;

	public static boolean contactListReady = false;
	public static List<String> contactDataBase = new ArrayList<String>();
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.contactslist);
		GlobalVars.lastActivity = ContactsList.class;
		contacts = (TextView) findViewById(R.id.contactslist);
		callto = (TextView) findViewById(R.id.contactscall);
		writeto = (TextView) findViewById(R.id.contactssms);
		delete = (TextView) findViewById(R.id.contactsdelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=5;
		selectedContact = -1;
		new ContactsListThread().execute("");

		//HIDES THE NAVIGATION BAR
		if (Build.VERSION.SDK_INT>11){try{GlobalVars.hideNavigationBar(this);}catch(Exception e){}}
    	}
		
    @Override protected void onResume()
    	{
    	super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = ContactsList.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=5;
		GlobalVars.selectTextView(contacts,false);
		GlobalVars.selectTextView(callto,false);
		GlobalVars.selectTextView(writeto,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.contactToDeleteName="";
    	GlobalVars.contactToDeletePhone="";
    	if (GlobalVars.contactWasDeleted==true)
    		{
    		GlobalVars.contactWasDeleted=false;
			GlobalVars.setText(contacts, false, getResources().getString(R.string.layoutContactsListContactsList));
			GlobalVars.talk(getResources().getString(R.string.layoutContactsListOnResumeContactDeleted));
			selectedContact = -1;
			GlobalVars.context = this;
			new ContactsListThread().execute("");
    		}
			else
			{
			if (GlobalVars.messagesWasSent == true)
				{
				GlobalVars.messagesWasSent = false;
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListOnResume2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListOnResume));
				}
			}

    	//HIDES THE NAVIGATION BAR
		if (Build.VERSION.SDK_INT>11){try{GlobalVars.hideNavigationBar(this);}catch(Exception e){}}
    	}    
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ CONTACT NAME
			GlobalVars.selectTextView(contacts,true);
			GlobalVars.selectTextView(callto,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedContact==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListContactsList2));
				}
				else
				{
				if (contactListReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
					}
					else
					{
					GlobalVars.talk(GlobalVars.contactsGetNameFromListValue(contactDataBase.get(selectedContact)) +
									getResources().getString(R.string.layoutContactsListWithThePhoneNumber) +
									GlobalVars.divideNumbersWithBlanks(GlobalVars.contactsGetPhoneNumberFromListValue(contactDataBase.get(selectedContact))));
					}
				}
			break;

			case 2: //CALL TO CONTACT
			GlobalVars.selectTextView(callto, true);
			GlobalVars.selectTextView(contacts,false);
			GlobalVars.selectTextView(writeto,false);
			GlobalVars.talk(getResources().getString(R.string.layoutContactsListCall2));
			break;

			case 3: //SEND A MESSAGE
			GlobalVars.selectTextView(writeto, true);
			GlobalVars.selectTextView(callto,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.layoutContactsListSendMessage2));
			break;
			
			case 4: //DELETE CONTACT
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(writeto,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutContactsListDelete2));
			break;
			
			case 5: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(contacts,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ CONTACT NAME
			if (contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (contactDataBase.size()>0)
					{
					if (selectedContact+1==contactDataBase.size())
						{
						selectedContact = -1;
						}
					selectedContact = selectedContact + 1;
					GlobalVars.setText(contacts, true, GlobalVars.contactsGetNameFromListValue(contactDataBase.get(selectedContact)) + "\n" +
													   GlobalVars.contactsGetPhoneNumberFromListValue(contactDataBase.get(selectedContact)));
					GlobalVars.talk(GlobalVars.contactsGetNameFromListValue(contactDataBase.get(selectedContact)) +
									getResources().getString(R.string.layoutContactsListWithThePhoneNumber) +
									GlobalVars.divideNumbersWithBlanks(GlobalVars.contactsGetPhoneNumberFromListValue(contactDataBase.get(selectedContact))));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListNoContacts));
					}
				}
			break;

			case 2: //CALL TO CONTACT
			if (contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (selectedContact==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListError));
					}
					else
					{
					if (GlobalVars.deviceIsAPhone()==true)
						{
						String number = "tel:" + contactDataBase.get(selectedContact).substring(contactDataBase.get(selectedContact).lastIndexOf("|") + 1,contactDataBase.get(selectedContact).length());
						GlobalVars.callTo(number);
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.mainNotAvailable));
						}
					}
				}
			break;

			case 3: //SEND A MESSAGE
			if (contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (selectedContact==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListError));
					}
					else
					{
					if (GlobalVars.deviceIsAPhone()==true)
						{
						GlobalVars.startActivity(MessagesCompose.class);
						try
							{
							MessagesCompose.messageToContactNameValue = GlobalVars.contactsGetNameFromListValue(contactDataBase.get(selectedContact));
							MessagesCompose.messageToPhoneNumberValue = contactDataBase.get(selectedContact).substring(contactDataBase.get(selectedContact).lastIndexOf("|") + 1,contactDataBase.get(selectedContact).length()); 
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
			
			case 4: //DELETE CONTACT
			if (contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (selectedContact==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutContactsListError));
					}
					else
					{
					GlobalVars.contactToDeleteName = contactDataBase.get(selectedContact).substring(0, contactDataBase.get(selectedContact).lastIndexOf("|"));
					GlobalVars.contactToDeletePhone = contactDataBase.get(selectedContact).substring(contactDataBase.get(selectedContact).lastIndexOf("|") + 1,contactDataBase.get(selectedContact).length());
					GlobalVars.startActivity(ContactsDelete.class);
					}
				}
			break;
			
			case 5: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ CONTACT NAME
			if (contactListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsListPleaseWait));
				}
				else
				{
				if (contactDataBase.size()>0)
					{
					if (selectedContact-1<0)
						{
						selectedContact=contactDataBase.size();
						}
					selectedContact = selectedContact - 1;
					GlobalVars.setText(contacts, true, GlobalVars.contactsGetNameFromListValue(contactDataBase.get(selectedContact)) + "\n" +
													   GlobalVars.contactsGetPhoneNumberFromListValue(contactDataBase.get(selectedContact)));
					GlobalVars.talk(GlobalVars.contactsGetNameFromListValue(contactDataBase.get(selectedContact)) +
									getResources().getString(R.string.layoutContactsListWithThePhoneNumber) +
									GlobalVars.divideNumbersWithBlanks(GlobalVars.contactsGetPhoneNumberFromListValue(contactDataBase.get(selectedContact))));
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
	}

package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class MessagesSent extends Activity
	{
	private TextView sent;
	private TextView delete;
	private TextView deleteall;
	private TextView goback;
	private int selectedMessage = -1;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
 	  	setContentView(R.layout.messagessent);
		GlobalVars.lastActivity = MessagesSent.class;
		sent = (TextView) findViewById(R.id.messagessent);
		delete = (TextView) findViewById(R.id.messagesdelete);
		deleteall = (TextView) findViewById(R.id.messagesdeleteall);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		selectedMessage = -1;
		new MessagesCheckThread(GlobalVars.TYPE_SENT).execute();
    	}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = MessagesSent.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(sent,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(deleteall,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.messagesWasDeleted==true)
			{
			new MessagesCheckThread(GlobalVars.TYPE_SENT).execute();
			try
				{
				selectedMessage = -1;
				if (GlobalVars.activityItemLocation==1)
					{
					GlobalVars.setText(sent, true, GlobalVars.context.getResources().getString(R.string.layoutMessagesSentSent));
					}
					else
					{
					GlobalVars.setText(sent, false, GlobalVars.context.getResources().getString(R.string.layoutMessagesSentSent));
					}
				}
				catch(Exception e)
				{
				}
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentOnResume2));
			GlobalVars.messagesWasDeleted=false;
			}
		else if (GlobalVars.messagesSentWereDeleted==true)
			{
			new MessagesCheckThread(GlobalVars.TYPE_SENT).execute();
			try
				{
				selectedMessage = -1;
				if (GlobalVars.activityItemLocation==1)
					{
					GlobalVars.setText(sent, true, GlobalVars.context.getResources().getString(R.string.layoutMessagesSentSent));
					}
					else
					{
					GlobalVars.setText(sent, false, GlobalVars.context.getResources().getString(R.string.layoutMessagesSentSent));
					}
				}
				catch(Exception e)
				{
				}
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentOnResume3));
			GlobalVars.messagesSentWereDeleted=false;
			}
		else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentOnResume));
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SENT MESSAGES
			GlobalVars.selectTextView(sent,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedMessage==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentSent2));
				}
				else
				{
				if (GlobalVars.messagesSentDatabaseReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentTryAgain));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentMessageItem) +
									String.valueOf(selectedMessage + 1) + 
									getResources().getString(R.string.layoutMessagesSentMessageOf) +
									String.valueOf(GlobalVars.messagesSentDataBase.size()) + ". " +
									getResources().getString(R.string.layoutMessagesSentMessageSent) +
									GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
									GlobalVars.getMessageDateTime(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
									getResources().getString(R.string.layoutMessagesSentMessageMessageBody) +
									GlobalVars.getMessageBody(GlobalVars.messagesSentDataBase.get(selectedMessage)));
					}
				}
			break;

			case 2: //DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(sent,false);
			GlobalVars.selectTextView(deleteall,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentDelete));
			break;

			case 3: //DELETE ALL SENT MESSAGES
			GlobalVars.selectTextView(deleteall, true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentDeleteAllDeleteDelete));
			break;

			case 4: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(sent,false);
			GlobalVars.selectTextView(deleteall,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SENT MESSAGES
			if (GlobalVars.messagesSentDatabaseReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentTryAgain));
				}
				else
				{
				if (GlobalVars.messagesSentDataBase.size()>0)
					{
					if (selectedMessage+1==GlobalVars.messagesSentDataBase.size())
						{
						selectedMessage = -1;
						}
					selectedMessage = selectedMessage + 1;
					GlobalVars.setText(sent, true, getResources().getString(R.string.layoutMessagesSentMessageItem) +
											   "(" + String.valueOf(selectedMessage + 1) + "/" + 
											   String.valueOf(GlobalVars.messagesSentDataBase.size()) + ")\n" +
											   GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage)));
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentMessageItem) +
									String.valueOf(selectedMessage + 1) + 
									getResources().getString(R.string.layoutMessagesSentMessageOf) +
									String.valueOf(GlobalVars.messagesSentDataBase.size()) + ". " +
									getResources().getString(R.string.layoutMessagesSentMessageSent) +
									GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
									GlobalVars.getMessageDateTime(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
									getResources().getString(R.string.layoutMessagesSentMessageMessageBody) +
									GlobalVars.getMessageBody(GlobalVars.messagesSentDataBase.get(selectedMessage)));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentNoMessages));
					}
				}
			break;

			case 2: //DELETE
			if (selectedMessage==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentError));
				}
				else
				{
				if (GlobalVars.messagesSentDatabaseReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentTryAgain));
					}
					else
					{
					GlobalVars.startActivity(MessagesDelete.class);
					MessagesDelete.messageType = GlobalVars.TYPE_SENT;
					MessagesDelete.messageIDToDelete = GlobalVars.getMessageID(GlobalVars.messagesSentDataBase.get(selectedMessage));
					MessagesDelete.messageFrom = GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage));
					MessagesDelete.messageToDelete = GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
					GlobalVars.getMessageDateTime(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
					getResources().getString(R.string.layoutMessagesSentMessageMessageBody) +
					GlobalVars.getMessageBody(GlobalVars.messagesSentDataBase.get(selectedMessage));
					}
				}
			break;

			case 3: //DELETE ALL SENT MESSAGES
			GlobalVars.startActivity(MessagesSentDeleteAll.class);
			break;

			case 4: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SENT MESSAGES
			if (GlobalVars.messagesSentDatabaseReady==false)
				{	
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentTryAgain));
				}
				else
				{
				if (GlobalVars.messagesSentDataBase.size()>0)
					{
					if (selectedMessage-1<0)
						{
						selectedMessage = GlobalVars.messagesSentDataBase.size();
						}
					selectedMessage = selectedMessage - 1;
					GlobalVars.setText(sent, true, getResources().getString(R.string.layoutMessagesSentMessageItem) +
													"(" + String.valueOf(selectedMessage + 1) + "/" + 
													String.valueOf(GlobalVars.messagesSentDataBase.size()) + ")\n" +
													GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage)));
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentMessageItem) +
									String.valueOf(selectedMessage + 1) + 
									getResources().getString(R.string.layoutMessagesSentMessageOf) +
									String.valueOf(GlobalVars.messagesSentDataBase.size()) + ". " +
									getResources().getString(R.string.layoutMessagesSentMessageSent) +
									GlobalVars.getMessageContactName(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
									GlobalVars.getMessageDateTime(GlobalVars.messagesSentDataBase.get(selectedMessage)) +
									getResources().getString(R.string.layoutMessagesSentMessageMessageBody) +
									GlobalVars.getMessageBody(GlobalVars.messagesSentDataBase.get(selectedMessage)));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesSentNoMessages));
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

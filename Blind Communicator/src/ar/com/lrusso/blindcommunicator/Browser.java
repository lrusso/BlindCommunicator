package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Browser extends Activity
	{
	private TextView browsergoogle;
	private TextView bookmarks;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.browser);
		GlobalVars.lastActivity = Browser.class;
		browsergoogle = (TextView) findViewById(R.id.browsergoogle);
		bookmarks = (TextView) findViewById(R.id.bookmarks);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
    	}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = Browser.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(browsergoogle,false);
		GlobalVars.selectTextView(bookmarks,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.inputModeResult!=null)
			{
			if (GlobalVars.browserRequestInProgress==false)
				{
				GlobalVars.browserRequestInProgress=true;
				new BrowserThreadGoTo().execute("http://www.google.com/custom?q=" + GlobalVars.inputModeResult);
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserErrorPendingRequest));
				}
			GlobalVars.inputModeResult = null;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserOnResume));
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SEARCH IN GOOGLE
			GlobalVars.selectTextView(browsergoogle,true);
			GlobalVars.selectTextView(bookmarks,false);
			GlobalVars.selectTextView(goback,false);
			if (GlobalVars.browserRequestInProgress==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserSearchInGoogle) + 
								getResources().getString(R.string.layoutBrowserAWebPageItsBeenLoading));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserSearchInGoogle));
				}
			break;

			case 2: //LIST BOOKMARKS
			GlobalVars.selectTextView(bookmarks, true);
			GlobalVars.selectTextView(browsergoogle,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserListBookmarks));
			break;

			case 3: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(bookmarks,false);
			GlobalVars.selectTextView(browsergoogle,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SEARCH IN GOOGLE
			if (GlobalVars.browserRequestInProgress==false)
				{
				GlobalVars.startInputActivity();
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserErrorPendingRequest));
				}
			break;

			case 2: //LIST BOOKMARKS
			GlobalVars.startActivity(BookmarksList.class);
			break;

			case 3: //GO BACK TO THE MAIN MENU
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
	}
package ar.com.lrusso.blindcommunicator;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.app.Activity;
import ar.com.lrusso.blindcommunicator.R;

public class BookmarksDelete extends Activity
	{
	private TextView todelete;
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.bookmarksdelete);
		GlobalVars.lastActivity = BookmarksDelete.class;
		todelete = (TextView) findViewById(R.id.bookmarks);
		delete = (TextView) findViewById(R.id.bookmarksdelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.setText(todelete,false, GlobalVars.browserBookmarks.get(GlobalVars.bookmarkToDeleteIndex).substring(0, GlobalVars.browserBookmarks.get(GlobalVars.bookmarkToDeleteIndex).indexOf("|")));
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
    	}
	
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = BookmarksDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(todelete,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutBookmarksDeleteOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //TO DELETE
			GlobalVars.selectTextView(todelete,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBookmarksDeleteToDelete) + GlobalVars.browserBookmarks.get(GlobalVars.bookmarkToDeleteIndex).substring(0, GlobalVars.browserBookmarks.get(GlobalVars.bookmarkToDeleteIndex).indexOf("|")));
			break;

			case 2: //DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBookmarksDeleteDelete));
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //TO DELETE
			GlobalVars.talk(getResources().getString(R.string.layoutBookmarksDeleteToDelete) + GlobalVars.browserBookmarks.get(GlobalVars.bookmarkToDeleteIndex).substring(0, GlobalVars.browserBookmarks.get(GlobalVars.bookmarkToDeleteIndex).indexOf("|")));
			break;

			case 2: //DELETE
			GlobalVars.browserBookmarks.remove(GlobalVars.bookmarkToDeleteIndex);
			GlobalVars.saveBookmarksDatabase();
			GlobalVars.bookmarkToDeleteIndex = -1;
			GlobalVars.bookmarkWasDeleted=true;
			this.finish();
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.bookmarkToDeleteIndex = -1;
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

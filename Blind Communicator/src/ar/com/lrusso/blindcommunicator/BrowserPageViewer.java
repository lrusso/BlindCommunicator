package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class BrowserPageViewer extends Activity
	{
	private TextView pagetitle;
	private TextView pagetext;
	private TextView pagelinks;
	private TextView linksgoto;
	private TextView addtobookmarks;
	private TextView goback;
	public static int linkLocation = -1;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.browserpageviewer);
		GlobalVars.lastActivity = BrowserPageViewer.class;
		pagetitle = (TextView) findViewById(R.id.readpagetitle);
		pagetext = (TextView) findViewById(R.id.readpagetext);
		pagelinks = (TextView) findViewById(R.id.pagelinks);
		linksgoto = (TextView) findViewById(R.id.linksgoto);
		addtobookmarks = (TextView) findViewById(R.id.addtobookmarks);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=6;
    	}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = BrowserPageViewer.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=6;
		GlobalVars.selectTextView(pagetitle,false);
		GlobalVars.selectTextView(pagetext,false);
		GlobalVars.selectTextView(pagelinks,false);
		GlobalVars.selectTextView(linksgoto,false);
		GlobalVars.selectTextView(addtobookmarks,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ PAGE TITLE
			GlobalVars.selectTextView(pagetitle,true);
			GlobalVars.selectTextView(pagetext,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerReadTitle));
			break;
			
			case 2: //READ PAGE TEXT
			GlobalVars.selectTextView(pagetext,true);
			GlobalVars.selectTextView(pagetitle,false);
			GlobalVars.selectTextView(pagelinks,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerReadText));
			break;

			case 3: //PAGE LINKS
			GlobalVars.selectTextView(pagelinks, true);
			GlobalVars.selectTextView(pagetext,false);
			GlobalVars.selectTextView(linksgoto,false);
			if (linkLocation==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks3) + String.valueOf(linkLocation + 1) + getResources().getString(R.string.layoutBrowserPageViewerPageLinks3Of) + String.valueOf(GlobalVars.browserWebLinks.size()) + ". " + GlobalVars.browserWebLinks.get(linkLocation).substring(0,GlobalVars.browserWebLinks.get(linkLocation).indexOf("|")));
				}
			break;

			case 4: //GO TO LINK
			GlobalVars.selectTextView(linksgoto, true);
			GlobalVars.selectTextView(pagelinks,false);
			GlobalVars.selectTextView(addtobookmarks,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerGoToLink));
			break;
			
			case 5: //ADD TO BOOKMARKS
			GlobalVars.selectTextView(addtobookmarks, true);
			GlobalVars.selectTextView(linksgoto,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerAddToBookmarks));
			break;
			
			case 6: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(addtobookmarks,false);
			GlobalVars.selectTextView(pagetitle,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ PAGE TITLE
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerReadTitle2) + GlobalVars.browserWebTitle);
			break;
			
			case 2: //READ PAGE TEXT
			GlobalVars.talk(GlobalVars.browserWebText);
			break;

			case 3: //PAGE LINKS
			if (GlobalVars.browserWebLinks.size()==0)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks4));
				}
				else
				{
				if (linkLocation+1==GlobalVars.browserWebLinks.size())
					{
					linkLocation=-1;
					}
				linkLocation = linkLocation + 1;
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks3) + String.valueOf(linkLocation + 1) + getResources().getString(R.string.layoutBrowserPageViewerPageLinks3Of) + String.valueOf(GlobalVars.browserWebLinks.size()) + ". " + GlobalVars.browserWebLinks.get(linkLocation).substring(0,GlobalVars.browserWebLinks.get(linkLocation).indexOf("|")));
				}
			break;

			case 4: //GO TO LINK
			if (linkLocation==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinkSelectError));
				}
				else
				{
				new BrowserThreadGoTo().execute(GlobalVars.browserWebLinks.get(linkLocation).substring(GlobalVars.browserWebLinks.get(linkLocation).indexOf("|") + 1, GlobalVars.browserWebLinks.get(linkLocation).length()));
				}
			break;
			
			case 5: //ADD TO BOOKMARKS
			String newBookmark = GlobalVars.browserWebTitle + "|" + GlobalVars.browserWebURL;
			boolean repeatedBookmark = false;
			for (int i=0;i<GlobalVars.browserBookmarks.size();i++)
				{
				if (GlobalVars.browserBookmarks.get(i).toLowerCase().equals(newBookmark.toLowerCase()))
					{
					repeatedBookmark = true;
					i = GlobalVars.browserBookmarks.size();
					}
				}
			if (repeatedBookmark==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerAddToBookmarksRepeated));
				}
				else
				{
				GlobalVars.browserBookmarks.add(newBookmark);
				GlobalVars.sortBookmarksDatabase();
				GlobalVars.saveBookmarksDatabase();
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerAddToBookmarksAdded));
				}
			break;
			
			case 6: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}

	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 3: //PAGE LINKS
			if (GlobalVars.browserWebLinks.size()==0)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks4));
				}
				else
				{
				if (linkLocation-1<0)
					{
					linkLocation = GlobalVars.browserWebLinks.size();
					}
				linkLocation = linkLocation - 1;
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks3) + String.valueOf(linkLocation + 1) + getResources().getString(R.string.layoutBrowserPageViewerPageLinks3Of) + String.valueOf(GlobalVars.browserWebLinks.size()) + ". " + GlobalVars.browserWebLinks.get(linkLocation).substring(0,GlobalVars.browserWebLinks.get(linkLocation).indexOf("|")));
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
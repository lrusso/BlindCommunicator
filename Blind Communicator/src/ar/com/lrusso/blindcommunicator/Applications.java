package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Applications extends Activity
	{
	private TextView applications;
	private TextView launchapplication;
	private TextView goback;
	private int selectedApplication = -1;

	@Override protected void onCreate(Bundle savedInstanceState)
    	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applications);
		GlobalVars.lastActivity = Applications.class;
		applications = (TextView) findViewById(R.id.applications);
		launchapplication = (TextView) findViewById(R.id.launchapplication);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		new ApplicationsThread().execute();
    	}

	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = Applications.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(applications,false);
		GlobalVars.selectTextView(launchapplication,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.setText(applications,false, getResources().getString(R.string.layoutApplicationsList));
		GlobalVars.talk(getResources().getString(R.string.layoutApplicationsOnResume));
		selectedApplication = -1;
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ APP NAME
			GlobalVars.selectTextView(applications,true);
			GlobalVars.selectTextView(launchapplication,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedApplication==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutApplicationsList2));
				}
				else
				{
				if (GlobalVars.applicationsListReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutApplicationsListPleaseWait));
					}
					else
					{
					GlobalVars.talk(GlobalVars.applicationsList.get(selectedApplication).substring(0,GlobalVars.applicationsList.get(selectedApplication).indexOf("|")));
					}
				}
			break;

			case 2: //LAUNCH APP
			GlobalVars.selectTextView(launchapplication, true);
			GlobalVars.selectTextView(applications,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutApplicationsLaunch));
			break;

			case 3: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(applications,false);
			GlobalVars.selectTextView(launchapplication,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ APP NAME
			if (GlobalVars.applicationsListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutApplicationsListPleaseWait));
				}
				else
				{
				if (GlobalVars.applicationsList.size()>0)
					{
					if (selectedApplication+1==GlobalVars.applicationsList.size())
						{
						selectedApplication = -1;
						}
					selectedApplication = selectedApplication + 1;
					GlobalVars.setText(applications, true, GlobalVars.applicationsList.get(selectedApplication).substring(0,GlobalVars.applicationsList.get(selectedApplication).indexOf("|")));
					GlobalVars.talk(GlobalVars.applicationsList.get(selectedApplication).substring(0,GlobalVars.applicationsList.get(selectedApplication).indexOf("|")));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutApplicationsNoApps));
					}
				}
			break;

			case 2: //LAUNCH APP
			if (selectedApplication==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutApplicationsLaunchError));
				}
				else
				{
				try
					{
					Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(GlobalVars.applicationsList.get(selectedApplication).substring(GlobalVars.applicationsList.get(selectedApplication).indexOf("|")+1, GlobalVars.applicationsList.get(selectedApplication).length()));
					startActivity(LaunchIntent);
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				}
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ APP NAME
			if (GlobalVars.applicationsList.size()>0)
				{
				if (selectedApplication-1<0)
					{
					selectedApplication = GlobalVars.applicationsList.size();
					}
				selectedApplication = selectedApplication - 1;
				GlobalVars.setText(applications, true, GlobalVars.applicationsList.get(selectedApplication).substring(0,GlobalVars.applicationsList.get(selectedApplication).indexOf("|")));
				GlobalVars.talk(GlobalVars.applicationsList.get(selectedApplication).substring(0,GlobalVars.applicationsList.get(selectedApplication).indexOf("|")));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutApplicationsNoApps));
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

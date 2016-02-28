package ar.com.lrusso.blindcommunicator;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.*;
import java.util.*;

public class ApplicationsThread extends AsyncTask<String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.applicationsListReady = false;
		GlobalVars.applicationsList.clear();
		}

	@Override protected Boolean doInBackground(String... url)
		{
		GlobalVars.applicationsList.clear();
		try
			{
			PackageManager pm = GlobalVars.context.getPackageManager();
			List<ApplicationInfo> apps = pm.getInstalledApplications(0);
			for(ApplicationInfo app : apps)
				{
				if (pm.getLaunchIntentForPackage(app.packageName)!=null)
					{
					try
						{
						String applicationName = (String)pm.getApplicationLabel(app);
						String packageName = app.packageName;
						GlobalVars.applicationsList.add(applicationName + "|" + packageName);
						}
						catch(NullPointerException e)
						{
						}
						catch(Exception e)
						{
						}
					}
				}
			}
			catch(NullPointerException e)
			{	
			}
			catch(Exception e)
			{
			}
		Collections.sort(GlobalVars.applicationsList, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		return false;
		}

	@Override protected void onPostExecute(Boolean pageloaded)
		{
		GlobalVars.applicationsListReady = true;
		}
	}
package ar.com.lrusso.blindcommunicator;

import android.os.*;
import java.util.*;
import java.io.*;

public class VoiceRecorderListThread extends AsyncTask<String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.voiceRecorderListReady = false;
		GlobalVars.voiceRecorderListFiles.clear();
		}

	@Override protected Boolean doInBackground(String... url)
		{
		try
			{
			try
				{
				File externalStoragePath = Environment.getExternalStorageDirectory();
				String finalPath = "";
				if (externalStoragePath.toString().endsWith("/"))
					{
					finalPath = externalStoragePath + "BlindCommunicator/Audio";
					}
					else
					{
					finalPath = externalStoragePath + "/BlindCommunicator/Audio";
					}
				File pathChecker = new File(finalPath);
				pathChecker.mkdirs();
				File f = new File(finalPath);        
				File file[] = f.listFiles();
				for (int i=0; i < file.length; i++)
					{
					if (file[i].isFile()==true && file[i].getName().toLowerCase().endsWith(".mp3"))
						{
						GlobalVars.voiceRecorderListFiles.add(finalPath + "/" + file[i].getName());
						}
					}
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			Collections.sort(GlobalVars.voiceRecorderListFiles,new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
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
		GlobalVars.voiceRecorderListReady = true;
		}
	}

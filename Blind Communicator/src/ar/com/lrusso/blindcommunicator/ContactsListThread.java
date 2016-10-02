package ar.com.lrusso.blindcommunicator;

import android.database.*;
import android.os.*;
import android.provider.*;
import java.util.*;

public class ContactsListThread extends AsyncTask<String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.contactListReady = false;
		GlobalVars.contactDataBase.clear();
		}

	@Override protected Boolean doInBackground(String... url)
		{
		try
			{
			Cursor cursor = GlobalVars.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
			while (cursor.moveToNext())
				{
				try
					{
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					GlobalVars.contactDataBase.add(name.concat("|").concat(phoneNumber));
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				}
			Collections.sort(GlobalVars.contactDataBase,new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
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
		GlobalVars.contactListReady = true;
		}
	}
package ar.com.lrusso.blindcommunicator.app;

import android.database.*;
import android.os.*;
import android.provider.*;
import java.util.*;

public class ContactsListThread extends AsyncTask<String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		ContactsList.contactListReady = false;
		ContactsList.contactDataBase.clear();
		}

	@Override protected Boolean doInBackground(String... url)
		{
		try
			{
			Cursor cursor = GlobalVars.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
            HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
			while (cursor.moveToNext())
				{
				try
					{
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                String normalizedNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
	                if (normalizedNumbersAlreadyFound.add(normalizedNumber))
	                	{
						ContactsList.contactDataBase.add(name.concat("|").concat(phoneNumber));
	                	}
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				}
			cursor.close();

			Collections.sort(ContactsList.contactDataBase,new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
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
		ContactsList.contactListReady = true;
		}
	}
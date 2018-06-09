package ar.com.lrusso.blindcommunicator;

import android.content.Context;
import android.database.*;
import android.os.*;
import android.provider.*;

import java.io.DataOutputStream;
import java.util.*;

public class ContactsListThread extends AsyncTask<Context, String, Boolean>
	{
	private Context context;

	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.contactListReady = false;
		}

	@Override protected Boolean doInBackground(Context... myContext)
		{
		context = myContext[0];

		writeFile("sizeofcontacts.cfg","0");

		GlobalVars.contactDataBase.clear();

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

		writeFile("sizeofcontacts.cfg",String.valueOf(GlobalVars.contactDataBase.size()));
		}

	private void writeFile(String file, String text)
		{
        try
			{
            DataOutputStream out = new DataOutputStream(context.openFileOutput(file, Context.MODE_PRIVATE));
            out.writeUTF(text);
            out.close();
			}
			catch(Exception e)
			{
			}
		}
	}
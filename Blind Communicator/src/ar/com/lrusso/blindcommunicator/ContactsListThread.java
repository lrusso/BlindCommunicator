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

			// SORTING CONTACTS
			Collections.sort(GlobalVars.contactDataBase,new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
			
		    // DELETING REPEATED VALUES
			try
				{
				for (int i=0;i<=GlobalVars.contactDataBase.size();i++)
					{
					try
						{
						String contact1 = GlobalVars.contactDataBase.get(i);
						String contact1Name = GlobalVars.getContactNameFromPhoneNumber(contact1);
						String contact1Phone = GlobalVars.contactsGetPhoneNumberFromListValue(contact1);
						
						for (int a=0;a<=GlobalVars.contactDataBase.size();a++)
							{
							try
								{
								String contact2 = GlobalVars.contactDataBase.get(a);
								String contact2Name = GlobalVars.getContactNameFromPhoneNumber(contact2);
								String contact2Phone = GlobalVars.contactsGetPhoneNumberFromListValue(contact2);
								
								if (contact1Name.equals(contact2Name) && contact1Phone.equals(contact2Phone) && i!=a)
									{
						        	GlobalVars.contactDataBase.remove(a);
									}
								}
								catch(NullPointerException e)
								{
								}
								catch(Exception e)
								{
								}
							}
						}
						catch(NullPointerException e)
						{
						}
						catch(Exception e)
						{
						}
					}
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}			
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
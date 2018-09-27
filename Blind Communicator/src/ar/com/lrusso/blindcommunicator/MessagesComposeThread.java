package ar.com.lrusso.blindcommunicator;

import android.database.*;
import android.os.*;
import android.provider.*;
import java.util.*;

public class MessagesComposeThread extends AsyncTask<String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		try
			{
			MessagesCompose.contactListReady = false;
			MessagesCompose.contactDataBase.clear();
			}
			catch(Exception e)
			{
			}
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
					MessagesCompose.contactDataBase.add(name.concat("|").concat(phoneNumber));
					}
					catch(NullPointerException e)
					{
					}
					catch(Exception e)
					{
					}
				}

			try
				{
				cursor.close();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}

			// SORTING CONTACTS
			Collections.sort(MessagesCompose.contactDataBase,new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
			
		    // DELETING REPEATED VALUES
			try
				{
				for (int i=0;i<=MessagesCompose.contactDataBase.size();i++)
					{
					try
						{
						String contact1 = MessagesCompose.contactDataBase.get(i);
						String contact1Name = GlobalVars.getContactNameFromPhoneNumber(contact1);
						String contact1Phone = GlobalVars.contactsGetPhoneNumberFromListValue(contact1);
						
						for (int a=0;a<=MessagesCompose.contactDataBase.size();a++)
							{
							try
								{
								String contact2 = MessagesCompose.contactDataBase.get(a);
								String contact2Name = GlobalVars.getContactNameFromPhoneNumber(contact2);
								String contact2Phone = GlobalVars.contactsGetPhoneNumberFromListValue(contact2);
								
								if (contact1Name.equals(contact2Name) && contact1Phone.equals(contact2Phone) && i!=a)
									{
									MessagesCompose.contactDataBase.remove(a);
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
		try
			{
			MessagesCompose.contactListReady = true;
			}
			catch(Exception e)
			{
			}
		}
	}
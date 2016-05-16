package ar.com.lrusso.blindcommunicator;

import android.os.*;

public class MusicPlayerThreadLoadAlbums extends AsyncTask <String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.musicPlayerAlbumsReady=false;
		}

	@Override protected Boolean doInBackground(String... artist)
		{
		GlobalVars.musicPlayerDatabaseAlbumsByArtist.clear();

		try
			{
			for (int i=0;i<GlobalVars.musicPlayerDatabaseAlbums.size();i++)
				{
				try
					{
					if (GlobalVars.musicPlayerDatabaseAlbums.get(i).startsWith(artist[0]))
						{
						GlobalVars.musicPlayerDatabaseAlbumsByArtist.add(
								GlobalVars.musicPlayerDatabaseAlbums.get(i).substring(
										GlobalVars.musicPlayerDatabaseAlbums.get(i).indexOf("|")+1,
										GlobalVars.musicPlayerDatabaseAlbums.get(i).length()));
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
		return true;
		}

	@Override protected void onPostExecute(Boolean pageloaded)
		{
		GlobalVars.musicPlayerAlbumsReady=true;
		}
	}

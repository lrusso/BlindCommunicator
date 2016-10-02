package ar.com.lrusso.blindcommunicator;

import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import java.util.*;

public class MusicPlayerThreadRefreshDatabase extends AsyncTask <String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.musicPlayerDatabaseReady=false;
		}
		
	@Override protected Boolean doInBackground(String... nothing)
		{
		GlobalVars.musicPlayerDatabaseFull.clear();
		GlobalVars.musicPlayerDatabaseArtists.clear();
		GlobalVars.musicPlayerDatabaseAlbums.clear();
		GlobalVars.musicPlayerDatabaseAlbumsByArtist.clear();
		GlobalVars.musicPlayerDatabasePlayList.clear();
		
		Cursor cur = null;
		
		try
			{
			ContentResolver cr = GlobalVars.context.getContentResolver();
			Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
			String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
			cur = cr.query(uri, null, selection, null, sortOrder);
			int count = 0;
			if(cur != null)
				{
				count = cur.getCount();
				if(count>0)
					{
					while(cur.moveToNext())
						{
						try
							{
							String data = "";
							if (cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST))!="" && !cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)).equals("<unknown>"))
								{
								data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
								}
								else
								{
								data = GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerUnknown);
								}
							
							GlobalVars.musicPlayerDatabaseArtists.add(data);
							data = data + "|";
							if (cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM))!="" && !cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)).equals("0"))
								{
								data = data + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM));
								}
								else
								{
								data = data + GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerUnknown);
								}
							GlobalVars.musicPlayerDatabaseAlbums.add(data);
							data = data + "|";
							if (cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TRACK))!="")
								{
								String track = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TRACK));
								if (track.length()<2)
									{
									track = "0" + track;
									}
								data = data + track;
								}
								else
								{
								data = data + "0";
								}
							data = data + "|";
							if (cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE))!="" && !cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)).equals("0"))
								{
								data = data + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
								}
								else
								{
								data = data + GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerUnknown);
								}
							data = data + "|";
							data = data + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
							GlobalVars.musicPlayerDatabaseFull.add(data);
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
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}

		//DELETE ANY REPETEAD ARTISTS|ALBUM|TRACK|TITLE
		HashSet<String> hashSet = new HashSet<String>(GlobalVars.musicPlayerDatabaseFull);
		GlobalVars.musicPlayerDatabaseFull.clear();
		GlobalVars.musicPlayerDatabaseFull.addAll(hashSet);

		//DELETE ANY REPETEAD ARTISTS
		HashSet<String> hashSet2 = new HashSet<String>(GlobalVars.musicPlayerDatabaseArtists);
		GlobalVars.musicPlayerDatabaseArtists.clear();
		GlobalVars.musicPlayerDatabaseArtists.addAll(hashSet2);

		//DELETE ANY REPETEAD ALBUMS
		HashSet<String> hashSet3 = new HashSet<String>(GlobalVars.musicPlayerDatabaseAlbums);
		GlobalVars.musicPlayerDatabaseAlbums.clear();
		GlobalVars.musicPlayerDatabaseAlbums.addAll(hashSet3);

		Collections.sort(GlobalVars.musicPlayerDatabaseFull, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		Collections.sort(GlobalVars.musicPlayerDatabaseArtists, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		Collections.sort(GlobalVars.musicPlayerDatabaseAlbums, new Comparator<String>(){public int compare(String s1, String s2){return s1.compareToIgnoreCase(s2);}});
		return true;
		}

	@Override protected void onPostExecute(Boolean pageloaded)
		{
		GlobalVars.musicPlayerDatabaseReady=true;
		}
	}

package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MusicPlayerArtists extends Activity
	{
	private TextView artistslist;
	private TextView albumslist;
	private TextView play;
	private TextView goback;
	private int selectedArtist = - 1;
	private int selectedAlbum = -1;
	private AsyncTask albumLoader = null;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.musicplayerartist);
		GlobalVars.lastActivity = MusicPlayerArtists.class;
		artistslist = (TextView) findViewById(R.id.artist);
		albumslist = (TextView) findViewById(R.id.album);
		play = (TextView) findViewById(R.id.play);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		selectedArtist = -1;
		selectedAlbum = -1;
    	}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = MusicPlayerArtists.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(artistslist,false);
		GlobalVars.selectTextView(albumslist,false);
		GlobalVars.selectTextView(play,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ARTIST NAME
			GlobalVars.selectTextView(artistslist,true);
			GlobalVars.selectTextView(albumslist,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedArtist==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsList2));
				}
				else
				{
				GlobalVars.talk(GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
				}
			break;

			case 2: //ALBUM NAME
			GlobalVars.selectTextView(albumslist, true);
			GlobalVars.selectTextView(artistslist,false);
			GlobalVars.selectTextView(play,false);
			if (selectedAlbum==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsAlbumsList2));
				}
				else
				{
				GlobalVars.talk(GlobalVars.musicPlayerDatabaseAlbumsByArtist.get(selectedAlbum));
				}
			break;

			case 3: //PLAY
			GlobalVars.selectTextView(play,true);
			GlobalVars.selectTextView(albumslist,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsPlayAlbum));
			break;
			
			case 4: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(play,false);
			GlobalVars.selectTextView(artistslist,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ARTIST NAME LIST
			try
				{
				if (GlobalVars.musicPlayerDatabaseArtists.size()>0)
					{
					if (selectedArtist+1==GlobalVars.musicPlayerDatabaseArtists.size())
						{
						selectedArtist = -1;
						}
					selectedArtist = selectedArtist + 1;
					GlobalVars.setText(artistslist, true, GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
					GlobalVars.talk(GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
					selectedAlbum = -1;
					GlobalVars.setText(albumslist, false, getResources().getString(R.string.layoutMusicPlayerArtistsAlbumsList));
					try
						{
						if (albumLoader!=null)
							{
							albumLoader.cancel(true);
							}
						}
						catch(NullPointerException e)
						{
						}
						catch(Exception e)
						{
						}
					try
						{
						albumLoader = new MusicPlayerThreadLoadAlbums().execute(GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
						}
						catch(Exception e)
						{
						}
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsNoArtists));
					}
				}
				catch(Exception e)
				{
				}
			break;

			case 2: //ALBUM NAME LIST
			try
				{
				if (selectedArtist==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsAlbumsList3));
					}
					else
					{
					if (GlobalVars.musicPlayerAlbumsReady==true)
						{
						if (GlobalVars.musicPlayerDatabaseAlbumsByArtist.size()>0)
							{
							if (selectedAlbum+1==GlobalVars.musicPlayerDatabaseAlbumsByArtist.size())
								{
								selectedAlbum = -1;
								}
							selectedAlbum = selectedAlbum + 1;
							GlobalVars.setText(albumslist, true, GlobalVars.musicPlayerDatabaseAlbumsByArtist.get(selectedAlbum));
							GlobalVars.talk(GlobalVars.musicPlayerDatabaseAlbumsByArtist.get(selectedAlbum));
							}
							else
							{
							GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtisrsAlbumsNoAlbums));
							}
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsPleaseWait));
						}
					}
				}
				catch(Exception e)
				{
				}
			break;
			
			case 3: //PLAY
			if (selectedAlbum==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsPlayError));
				}
				else
				{
				GlobalVars.musicPlayerDatabasePlayList.clear();
				GlobalVars.musicPlayerPlayingSongIndex = -1;
				GlobalVars.musicPlayerCurrentArtist = "";
				GlobalVars.musicPlayerCurrentSong = "";
				String filter = GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist) + "|" +
								GlobalVars.musicPlayerDatabaseAlbumsByArtist.get(selectedAlbum) +"|";
				for (int i=0;i<GlobalVars.musicPlayerDatabaseFull.size();i++)
					{
					if (GlobalVars.musicPlayerDatabaseFull.get(i).startsWith(filter))
						{
						GlobalVars.musicPlayerDatabasePlayList.add(GlobalVars.musicPlayerDatabaseFull.get(i));
						}
					}
				GlobalVars.musicPlayerNextTrack();
				this.finish();
				}
			break;

			case 4: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ARTIST NAME LIST
			try
				{
				if (GlobalVars.musicPlayerDatabaseArtists.size()>0)
					{
					if (selectedArtist-1<0)
						{
						selectedArtist = GlobalVars.musicPlayerDatabaseArtists.size();
						}
					selectedArtist = selectedArtist - 1;
					GlobalVars.setText(artistslist, true, GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
					GlobalVars.talk(GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
					selectedAlbum = -1;
					GlobalVars.setText(albumslist, false, getResources().getString(R.string.layoutMusicPlayerArtistsAlbumsList));
					try
						{
						if (albumLoader!=null)
							{
							albumLoader.cancel(true);
							}
						}
						catch(NullPointerException e)
						{
						}
						catch(Exception e)
						{
						}
					try
						{
						albumLoader = new MusicPlayerThreadLoadAlbums().execute(GlobalVars.musicPlayerDatabaseArtists.get(selectedArtist));
						}
						catch(Exception e)
						{
						}
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsNoArtists));
					}
				}
				catch(Exception e)
				{
				}
			break;

			case 2: //ALBUM NAME LIST
			try
				{
				if (selectedArtist==-1)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsAlbumsList3));
					}
					else
					{
					if (GlobalVars.musicPlayerAlbumsReady==true)
						{
						if (GlobalVars.musicPlayerDatabaseAlbumsByArtist.size()>0)
							{
							if (selectedAlbum-1<0)
								{
								selectedAlbum = GlobalVars.musicPlayerDatabaseAlbumsByArtist.size();
								}
							selectedAlbum = selectedAlbum - 1;
							GlobalVars.setText(albumslist, true, GlobalVars.musicPlayerDatabaseAlbumsByArtist.get(selectedAlbum));
							GlobalVars.talk(GlobalVars.musicPlayerDatabaseAlbumsByArtist.get(selectedAlbum));
							}
							else
							{
							GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtisrsAlbumsNoAlbums));
							}
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtistsPleaseWait));
						}
					}
				}
				catch(Exception e)
				{
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

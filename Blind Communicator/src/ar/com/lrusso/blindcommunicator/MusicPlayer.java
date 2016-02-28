package ar.com.lrusso.blindcommunicator;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MusicPlayer extends Activity
	{
	private TextView playingnow;
	public static TextView playstop;
	public static ImageView playstopicon;
	private TextView previoustrack;
	private TextView nexttrack;
	private TextView lowervolume;
	private TextView highervolume;
	private TextView artists;
	private TextView goback;
	private AudioManager audio;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.musicplayer);
		GlobalVars.lastActivity = MusicPlayer.class;
		playingnow = (TextView) findViewById(R.id.playingnow);
		playstop = (TextView) findViewById(R.id.playstop);
		playstopicon = (ImageView) findViewById(R.id.playstopicon);
		previoustrack = (TextView) findViewById(R.id.previoustrack);
		nexttrack = (TextView) findViewById(R.id.nexttrack);
		lowervolume = (TextView) findViewById(R.id.lowervolume);
		highervolume = (TextView) findViewById(R.id.highervolume);
		artists = (TextView) findViewById(R.id.artists);
		goback = (TextView) findViewById(R.id.goback);
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=8;
		
		if (GlobalVars.musicPlayer!=null)
			{
			MusicPlayer.playstopicon.setImageResource(R.drawable.playerstop);
			GlobalVars.setText(MusicPlayer.playstop,false,GlobalVars.context.getResources().getString(R.string.layoutMusicPlayerStop));
			}
    	}
    
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = MusicPlayer.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=8;
		GlobalVars.selectTextView(playingnow,false);
		GlobalVars.selectTextView(playstop,false);
		GlobalVars.selectTextView(previoustrack,false);
		GlobalVars.selectTextView(nexttrack,false);
		GlobalVars.selectTextView(lowervolume,false);
		GlobalVars.selectTextView(highervolume,false);
		GlobalVars.selectTextView(artists,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //PLAYING NOW
			GlobalVars.selectTextView(playingnow,true);
			GlobalVars.selectTextView(playstop,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerNowPlaying2));
			break;

			case 2: //PLAY-STOP
			GlobalVars.selectTextView(playstop, true);
			GlobalVars.selectTextView(playingnow,false);
			GlobalVars.selectTextView(previoustrack,false);
			try
				{
				if (GlobalVars.musicPlayer!=null)
					{
					if (GlobalVars.musicPlayer.isPlaying()==true)
						{
						GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerStop));
						}
						else
						{
						GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerPlay));
						}
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerPlay));
					}
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			break;

			case 3: //PREVIOUS TRACK
			GlobalVars.selectTextView(previoustrack,true);
			GlobalVars.selectTextView(playstop,false);
			GlobalVars.selectTextView(nexttrack,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerPreviousTrack2));
			break;
			
			case 4: //NEXT TRACK
			GlobalVars.selectTextView(nexttrack,true);
			GlobalVars.selectTextView(previoustrack,false);
			GlobalVars.selectTextView(lowervolume,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerNextTrack2));
			break;
			
			case 5: //LOWER VOLUME
			GlobalVars.selectTextView(lowervolume,true);
			GlobalVars.selectTextView(nexttrack,false);
			GlobalVars.selectTextView(highervolume,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerLowerVolume2));
			break;
			
			case 6: //HIGHER VOLUME
			GlobalVars.selectTextView(highervolume,true);
			GlobalVars.selectTextView(lowervolume,false);
			GlobalVars.selectTextView(artists,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerHigherVolume2));
			break;
			
			case 7: //ARTISTS
			GlobalVars.selectTextView(artists,true);
			GlobalVars.selectTextView(highervolume,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerArtists));
			break;
			
			case 8: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(artists,false);
			GlobalVars.selectTextView(playingnow,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //PLAYING NOW
			playingNow();
			break;
			
			case 2: //PLAY-STOP
			GlobalVars.musicPlayerPlayStop();
			break;
			
			case 3: //PREVIOUS TRACK
			GlobalVars.musicPlayerPreviousTrack();
			break;
			
			case 4: //NEXT TRACK
			try
				{
				if (GlobalVars.musicPlayerDatabasePlayList.size()>0)
					{
					int nextSong;
					if (GlobalVars.musicPlayerPlayingSongIndex + 1== GlobalVars.musicPlayerDatabasePlayList.size())
						{
						nextSong = 0;
						}
						else
						{
						nextSong = GlobalVars.musicPlayerPlayingSongIndex + 1;
						}
					GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerNowPlaying2) +
									getResources().getString(R.string.layoutMusicPlayerNowPlaying3) +
									String.valueOf(nextSong + 1) +
									getResources().getString(R.string.layoutMusicPlayerNowPlaying4) +
									String.valueOf(GlobalVars.musicPlayerDatabasePlayList.size()) + ". " +
									GlobalVars.getAudioSongName(GlobalVars.musicPlayerDatabasePlayList.get(nextSong)) + " " +
									getResources().getString(R.string.layoutMusicPlayerNowPlaying5) +
									GlobalVars.getAudioArtist(GlobalVars.musicPlayerDatabasePlayList.get(nextSong)));
					}
				GlobalVars.musicPlayerNextTrack();
				}
				catch(NullPointerException e)
				{
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
				}
				catch(Exception e)
				{
				}
			break;
			
			case 5: //LOWER VOLUME
			try
				{
				audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
				}
				catch(Exception e)
				{
				}
			GlobalVars.volumeDown();
			break;
			
			case 6: //HIGHER VOLUME
			try
				{
				audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
				}
				catch(Exception e)
				{
				}
			GlobalVars.volumeUp();
			break;

			case 7: //ARTISTS
			GlobalVars.startActivity(MusicPlayerArtists.class);
			break;

			case 8: //GO BACK TO THE MAIN MENU
			if (GlobalVars.musicPlayer==null)
				{
				GlobalVars.musicPlayerDatabasePlayList.clear();
				}
			this.finish();
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
	
	private void playingNow()
		{
		if (GlobalVars.musicPlayerDatabasePlayList.size()==0)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerNowPlayingError1));
			}
			else
			{
			if (GlobalVars.musicPlayer==null)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerNowPlayingError2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutMusicPlayerNowPlaying2) +
								getResources().getString(R.string.layoutMusicPlayerNowPlaying3) +
								String.valueOf(GlobalVars.musicPlayerPlayingSongIndex + 1) +
								getResources().getString(R.string.layoutMusicPlayerNowPlaying4) +
								String.valueOf(GlobalVars.musicPlayerDatabasePlayList.size()) + ". " +
								GlobalVars.musicPlayerCurrentSong + " " +
								getResources().getString(R.string.layoutMusicPlayerNowPlaying5) +
								GlobalVars.musicPlayerCurrentArtist);
				}
			}
		}
	}

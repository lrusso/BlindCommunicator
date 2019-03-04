package ar.com.lrusso.blindcommunicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Main extends Activity
	{
	private Activity activity;
	
	@Override protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		activity = this;
		
		Button buttonApp = (Button) findViewById(R.id.buttonApp);
		buttonApp.setOnClickListener(new View.OnClickListener()
			{
		    @Override
		    public void onClick(View view)
		    	{
		    	String url = getResources().getString(R.string.app_download_link);
		    	Intent i = new Intent(Intent.ACTION_VIEW);
		    	i.setData(Uri.parse(url));
		    	startActivity(i);
		    	}
			});
		
		Button buttonSource = (Button) findViewById(R.id.buttonSource);
		buttonSource.setOnClickListener(new View.OnClickListener()
			{
		    @Override
		    public void onClick(View view)
		    	{
		    	String url = getResources().getString(R.string.app_source_link);
		    	Intent i = new Intent(Intent.ACTION_VIEW);
		    	i.setData(Uri.parse(url));
		    	startActivity(i);
		    	}
			});
		
		Button buttonPrivacy = (Button) findViewById(R.id.buttonPrivacy);
		buttonPrivacy.setOnClickListener(new View.OnClickListener()
			{
		    @Override
		    public void onClick(View view)
		    	{
				LayoutInflater inflater = LayoutInflater.from(activity);
				View view2 = inflater.inflate(R.layout.privacy, null);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);  
				alertDialog.setTitle(getResources().getString(R.string.app_privacy));  
				alertDialog.setView(view2);
				alertDialog.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener()
					{
					public void onClick(DialogInterface dialog, int whichButton)
						{
						}
					});
				alertDialog.show();
		    	}
			});
		}
	}
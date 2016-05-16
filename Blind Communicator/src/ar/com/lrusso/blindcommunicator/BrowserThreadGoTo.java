package ar.com.lrusso.blindcommunicator;

import android.os.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class BrowserThreadGoTo extends AsyncTask<String, String, Boolean>
	{
	@Override protected void onPreExecute()
		{
		super.onPreExecute();
		GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutBrowserLoadingPagePleaseWait));
		}

	@Override protected Boolean doInBackground(String... url)
		{
		try
			{
			if (!url[0].toLowerCase().startsWith("http://"))
				{
				url[0] = "http://" + url[0];
				}
			return browserGoTo(url[0]);
			}
			catch(Exception e)
			{
			return false;
			}
		}
		
	@Override protected void onPostExecute(Boolean pageloaded)
		{
		GlobalVars.browserRequestInProgress=false;
		if (pageloaded==true)
			{
			BrowserPageViewer.linkLocation=-1;
			GlobalVars.startActivity(BrowserPageViewer.class);
			}
			else
			{
			GlobalVars.talk(GlobalVars.context.getResources().getString(R.string.layoutBrowserPageNotFound));
			}
		}
		
	public static boolean browserGoTo(String url)
		{
		try
			{
			Document doc = Jsoup.connect(url).header("Accept-Language", GlobalVars.getLanguage()).get();

			GlobalVars.browserWebTitle = doc.title();
			GlobalVars.browserWebText = doc.text();
			GlobalVars.browserWebURL = url;

			//GETS EVERY PAGE LINK
			GlobalVars.browserWebLinks.clear();
			Elements links = doc.select("a[href]");
			for (Element link : links)
				{
				if (!link.attr("abs:href").contains("javascript:void()"))
					{
					if (link.attr("abs:href")!=null &&
						link.attr("abs:href")!="" &&
					    link.attr("abs:href").replaceAll(" ","")!="" &&
						link.attr("abs:href").length()>0 &&
						link.text()!=null &&
						link.text()!="" &&
						link.text().replaceAll(" ","")!="" &&
						link.text().length()>0 &&
						//DELETES GOOGLE CACHE LINKS TO ALLOW THE USER TO PERFORM A BETTER AND CLEANER SEARCHING
						!link.attr("abs:href").toLowerCase().startsWith("http://webcache.googleusercontent.com/") &&
						!link.attr("abs:href").toLowerCase().startsWith("https://webcache.googleusercontent.com/") &&
						//DELETES GOOGLE SIMILAR LINKS TO ALLOW THE USER TO PERFORM A BETTER AND CLEANER SEARCHING
						!(link.attr("abs:href").toLowerCase().startsWith("http://www.google.com/custom?") && link.attr("abs:href").toLowerCase().contains("q=related:")) &&
						!(link.attr("abs:href").toLowerCase().startsWith("https://www.google.com/custom?") && link.attr("abs:href").toLowerCase().contains("q=related:"))
						)
						{
						if (GlobalVars.isValidURL(link.attr("abs:href"))==true)
							{
							GlobalVars.browserWebLinks.add(link.text() + "|" + link.attr("abs:href"));
							}
						}
					}
				}
			}
			catch(Exception e)
			{
			return false;
			}
		return true;
		}
	}

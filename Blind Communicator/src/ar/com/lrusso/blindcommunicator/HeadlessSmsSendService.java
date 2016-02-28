package ar.com.lrusso.blindcommunicator;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class HeadlessSmsSendService extends IntentService
	{
    public HeadlessSmsSendService()
    	{
        super(HeadlessSmsSendService.class.getName());
        setIntentRedelivery(true);
    	}

    @Override protected void onHandleIntent(Intent intent)
    	{
        String action = intent.getAction();
        if (!TelephonyManager.ACTION_RESPOND_VIA_MESSAGE.equals(action))
        	{
            return;
        	}

        Bundle extras = intent.getExtras();

        if (extras == null)
        	{
            return;
        	}

        String message = extras.getString(Intent.EXTRA_TEXT);
        Uri intentUri = intent.getData();
        String recipients = getRecipients(intentUri);

        if (TextUtils.isEmpty(recipients))
        	{
            return;
        	}

        if (TextUtils.isEmpty(message))
        	{
            return;
        	}

        String[] destinations = TextUtils.split(recipients, ";");

        sendAndStoreTextMessage(getContentResolver(), destinations, message);
    	}

    private String getRecipients(Uri uri)
    	{
        String base = uri.getSchemeSpecificPart();
        int pos = base.indexOf('?');
        return (pos == -1) ? base : base.substring(0, pos);
    	}

    private void sendAndStoreTextMessage(ContentResolver contentResolver, String[] destinations, String message)
    	{
        SmsManager smsManager = SmsManager.getDefault();

        Uri smsSentUri = Uri.parse(SmsConstant.SMS_SENT_URI);

        for (String destination : destinations)
        	{
            smsManager.sendTextMessage(destination, null, message, null, null);

            ContentValues values = new ContentValues();
            values.put(SmsConstant.COLUMN_ADDRESS, destination);
            values.put(SmsConstant.COLUMN_BODY, message);

            Uri uri = contentResolver.insert(smsSentUri, values);
        	}
    	}
	}
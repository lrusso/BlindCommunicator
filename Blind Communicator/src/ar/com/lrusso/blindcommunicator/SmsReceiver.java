package ar.com.lrusso.blindcommunicator;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver
	{

    @Override public void onReceive(Context context, Intent intent)
    	{
        Bundle extras = intent.getExtras();

        if (extras == null)
        	{
            return;
        	}

        Object[] smsExtras = (Object[]) extras.get(SmsConstant.PDUS);

        ContentResolver contentResolver = context.getContentResolver();
        Uri smsUri = Uri.parse(SmsConstant.SMS_URI);

        for (Object smsExtra : smsExtras)
        	{
            byte[] smsBytes = (byte[]) smsExtra;

            SmsMessage smsMessage = SmsMessage.createFromPdu(smsBytes);

            String body = smsMessage.getMessageBody();
            String address = smsMessage.getOriginatingAddress();

            ContentValues values = new ContentValues();
            values.put(SmsConstant.COLUMN_ADDRESS, address);
            values.put(SmsConstant.COLUMN_BODY, body);

            Uri uri = contentResolver.insert(smsUri, values);
        	}
    	}
	}
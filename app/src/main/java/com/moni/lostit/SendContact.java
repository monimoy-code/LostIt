package com.moni.lostit;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;


import androidx.annotation.NonNull;
import androidx.work.Worker;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

public class SendContact extends Worker{
    private Context context;

    public SendContact(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String msg = getInputData().getString("message");
        String destination = getInputData().getString("destination");
        getContactList(msg,destination);
        return Result.success();
    }
    private void getContactList(String msg,String destination)
    {
        List<String> names = new ArrayList<String>();
        List<String> nums = new ArrayList<String>();
        ContentResolver resolver = context.getContentResolver();
        String[] projection={
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        };
        String selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" LIKE ?";
        String selQuery = "%"+msg+"%";
        String[] args = {selQuery};
        Cursor cr = resolver.query(ContactsContract.Contacts.CONTENT_URI,projection,selection,args,null);
        String selection2 = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?";
        String[] projection2 = {
                    ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        if(cr==null)
        {
            Log.d("Contacts","not found.....");
        }
        else
        {
            while (cr.moveToNext())
            {
                String lookUp = cr.getString(cr.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cr.getString(cr.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                String[] args2 = {lookUp};
                Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,projection2,selection2,args2,null);
                if(phone==null)
                {
                    Log.d(name+":","no number....");
                }
                else
                {
                    while (phone.moveToNext())
                    {
                        String pno = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d(name+":",pno+"");
                        names.add(name);
                        nums.add(pno);
                    }
                    phone.close();
                }

            }
            cr.close();

        }
        StringBuilder msg_body = new StringBuilder();
        if(!names.isEmpty() && !nums.isEmpty())
        {
            for(int i=0;i<names.size();i++)
            {
                msg_body.append(names.get(i)+" "+nums.get(i)+"\n");
            }
            String scMessage=null;
            ArrayList<PendingIntent> sentIntent = null,deliveryIntent=null;
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> part_sms=smsManager.divideMessage(msg_body.toString());
            smsManager.sendMultipartTextMessage(destination,scMessage,part_sms,sentIntent,deliveryIntent);
        }
    }

}

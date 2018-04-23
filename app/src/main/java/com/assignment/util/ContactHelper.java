package com.assignment.util;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.widget.Toast;

import com.assignment.model.Contact;

import java.util.ArrayList;

public class ContactHelper {
    private String TAG = ContactHelper.class.toString();

    private static long getContactID(ContentResolver contactHelper,
                                     String number) {
        Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        String[] projection = {PhoneLookup._ID};
        Cursor cursor = null;

        try {
            cursor = contactHelper.query(contactUri, projection, null, null,
                    null);

            if (cursor.moveToFirst()) {
                int personID = cursor.getColumnIndex(PhoneLookup._ID);
                return cursor.getLong(personID);
            }

            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return -1;
    }

    public static int deleteContact(ContentResolver contactHelper,
                                    String number) {
        ArrayList <ContentProviderOperation> ops = new ArrayList <ContentProviderOperation>();
        String[] args = new String[]{String.valueOf(getContactID(contactHelper, number))};

        ops.add(ContentProviderOperation.newDelete(RawContacts.CONTENT_URI)
                .withSelection(RawContacts.CONTACT_ID + "=?", args).build());
        try {
            contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d("TAG", "deleteContact: ");
            return 1;
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

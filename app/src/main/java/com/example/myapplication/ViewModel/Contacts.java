package com.example.myapplication.ViewModel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Model.Contact;

import java.util.ArrayList;

public class Contacts extends AndroidViewModel {

    private MutableLiveData<ArrayList<Contact>> mutableLiveData;

    public Contacts(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Contact>> getMutableLiveData(){

        if(mutableLiveData==null){
            mutableLiveData=new MutableLiveData<>();
            setMutableLiveData();
        }
        return mutableLiveData;
    }

    public void setMutableLiveData() {

        ArrayList<Contact> contactList = new ArrayList<>();
        ContentResolver contentResolver =getApplication().getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (cursor != null && cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = "";

            //now retrieve the phone number
            if (cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                Cursor newCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                        new String[]{id}, null);

                while (newCursor != null && newCursor.moveToNext())
                    phoneNumber = newCursor.getString(newCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if (newCursor != null)
                    newCursor.close();

                contactList.add(new Contact(name, phoneNumber));
            }
        }
        if (cursor != null)
            cursor.close();

        Log.d("contactList", "" + contactList.size());

        mutableLiveData.setValue(contactList);
    }

}

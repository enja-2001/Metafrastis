package com.example.myapplication.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.myapplication.Model.Contact;
import com.example.myapplication.R;
import com.example.myapplication.ViewModel.Contacts;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    private static final String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO};

    private ArrayList<Contact> contactList;
    private RecyclerView recyclerView;
    private com.example.myapplication.Adapters.Contacts adapter;

    private Contacts viewModel;     //Contacts ViewModel extends AndroidViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        requestPermission();
    }

    private void initViews(){
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getContactsList();
    }

    private void getContactsList() {
        viewModel = ViewModelProviders.of(this).get(Contacts.class);
        MutableLiveData<ArrayList<Contact>> mutableLiveData = viewModel.getMutableLiveData();
        mutableLiveData.observe(this,getObserver());
    }

    private void requestPermission() {

        int countPermissionGranted = 0;

        for (String perm : permissions)
            if (ActivityCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED)
                countPermissionGranted++;

        if (countPermissionGranted == permissions.length)   //when all the permissions have been granted
            initViews();

        else
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            int countPermissionGranted = 0;

            for (int result : grantResults)
                if (result == PackageManager.PERMISSION_GRANTED)
                    countPermissionGranted++;

            if (countPermissionGranted == grantResults.length)   //when all the permissions have been granted
                initViews();

            else
                finish();
        }
    }

    private Observer<ArrayList<Contact>> getObserver(){

        return updatedContactList -> {      //replaces the inner class with lambda expression
            if(contactList == null){
                contactList = updatedContactList;
                adapter=new com.example.myapplication.Adapters.Contacts(contactList,ContactsActivity.this);
                recyclerView.setAdapter(adapter);
            }
            else{
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return contactList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return updatedContactList.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        Contact ob1 = contactList.get(oldItemPosition);
                        Contact ob2 = updatedContactList.get(newItemPosition);

                        return (ob1.getPhoneNumber().equals(ob2.getPhoneNumber()) && ob1.getName().equals(ob2.getName()));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return contactList.get(oldItemPosition).equals(updatedContactList.get(newItemPosition));
                    }
                });
                result.dispatchUpdatesTo(adapter);
                contactList = updatedContactList;
            }
        };
    }
}

package com.example.contactapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import MyAdapters.Myadapter;
import model_list.ListItem_contacts;

public class MainActivity extends AppCompatActivity {

    private List<ListItem_contacts> listing;
    private Myadapter adapter;
    private RecyclerView recyclerView;
    int count = 0;
    Parcelable state;
    EditText search;
    ImageView clear;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private static ProgressDialog pd;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.search_contact);
        clear = findViewById(R.id.clear_text);
        recyclerView = findViewById(R.id.contact_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listing = new ArrayList<>();
        fetch_contact();


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search.setText("");
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


    }





    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }




    @Override
    protected void onResume() {
        super.onResume();

        listing = getContacts();
        adapter = new Myadapter(listing, MainActivity.this);
        recyclerView.setAdapter(adapter);
        if (state != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(state);
        }

        search.setText("");
    }




    @Override
    protected void onPause() {

        state = ((LinearLayoutManager) recyclerView.getLayoutManager()).onSaveInstanceState();
        super.onPause();

    }





    public void fetch_contact() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {

            new LoadContacts().execute();

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new LoadContacts().execute();
            } else {
                Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
            }
        }
    }





    public class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            listing = getContacts();
            count = listing.size();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (listing != null && listing.size() > 0) {

                adapter = null;
                if (adapter == null) {
                    adapter = new Myadapter(listing, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            } else {

                Toast.makeText(MainActivity.this, "There are no contacts.",
                        Toast.LENGTH_LONG).show();
            }



        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

    }




    public ArrayList<ListItem_contacts> getContacts() {
        ArrayList<ListItem_contacts> contactList = new ArrayList<ListItem_contacts>();
        ContentResolver cResolver = this.getContentResolver();
        ContentProviderClient mCProviderClient = cResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI);

        try {
            Cursor mCursor = mCProviderClient.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                while (!mCursor.isLast()) {
                    Uri img_uri;
                    String displayName = mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    String id = mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));


                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Integer.parseInt(id));
                    img_uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    contactList.add(new ListItem_contacts(displayName, id, img_uri));
                    mCursor.moveToNext();
                }
                if (mCursor.isLast()) {
                    Uri img_uri;
                    String displayName = mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    String id = mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));


                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Integer.parseInt(id));
                    img_uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                    contactList.add(new ListItem_contacts(displayName, id, img_uri));
                }
            }

            mCursor.close();
        } catch (RemoteException e) {
            e.printStackTrace();
            contactList = null;
        } catch (Exception e) {
            e.printStackTrace();
            contactList = null;
        }

        return contactList;
    }





    void filter(String text){
        List<ListItem_contacts> temp = new ArrayList();
        for(ListItem_contacts i: listing){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(i.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(i);
            }
        }

        adapter = new Myadapter(temp, MainActivity.this);
        recyclerView.setAdapter(adapter);

    }





}


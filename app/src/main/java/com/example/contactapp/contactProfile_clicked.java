package com.example.contactapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class contactProfile_clicked extends AppCompatActivity {

    TextView name,mobile,email;
    ImageView call,message,mail;
    CircleImageView profile_image;
    ImageView back;
    String number="";
    String email_profile ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_profile_clicked);
        name = findViewById(R.id.profile_contact_name);
        mobile= findViewById(R.id.profile_contact_mobile);
        email = findViewById(R.id.profile_contact_email);
        profile_image = findViewById(R.id.profile_image);
        back = findViewById(R.id.back_to_main);
        call = findViewById(R.id.get_call);
        message = findViewById(R.id.get_message);
        mail = findViewById(R.id.get_mail);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + number));
                startActivity(dialIntent);

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(email_profile));
                startActivity(Intent.createChooser(emailIntent,"send by..?"));

            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/CONSOLAB.TTF");
        Typeface typeface_gothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");
        Typeface typeface_roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        final Intent intent = getIntent();
        String clicked_intent_name = intent.getStringExtra("name");
        String clicked_intent_id = intent.getStringExtra("id");
        Uri myUri = Uri.parse(intent.getStringExtra("photo"));






        profile_image.setImageURI(myUri);

        if(profile_image.getDrawable()==null)
        {
            profile_image.setImageResource(R.drawable.man);
        }

        name.setTypeface(typeface_roboto);
        email.setTypeface(typeface_gothic);
        mobile.setTypeface(typeface);

        name.setText(clicked_intent_name);

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor =  contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                         " = ?", new String[]{clicked_intent_id}, null);

        while (cursor.moveToNext()) {
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i("ph", number);
        }
        cursor.close();

        mobile.setText(number);

        if(mobile.getText().toString().matches(""))
        {

            mobile.setText("No Number found");
            call.setEnabled(false);
            message.setEnabled(false);
        }

        Cursor cursor_email =  contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                " = ?", new String[]{clicked_intent_id}, null);

        while (cursor_email.moveToNext()) {
            email_profile = cursor_email.getString(cursor_email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

        }
        cursor.close();

        email.setText(email_profile);

        if(email.getText().toString().matches(""))
        {

            email.setText("No email found");
            mail.setEnabled(false);
        }


    }

    @Override
    public void onBackPressed() {
       finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}



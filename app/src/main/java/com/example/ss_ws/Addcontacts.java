package com.example.ss_ws;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.ss_ws.Login.MY_PREFERENCES;

public class Addcontacts extends AppCompatActivity {

    Button button,save ;
    Intent intent;


    String primary1,primary2;
    public  static final int RequestPermissionCode  = 1 ;
    LinkedHashMap<String,String> hmap = new LinkedHashMap<>();
    HashSet<String> name_set = new HashSet<>();
    HashSet<String> phone_set = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);
        button = findViewById(R.id.button);

        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        primary1 = preferences.getString("contact1", "");
        primary2 = preferences.getString("contact2", "");
        hmap.put("Primary_contact", primary1);
        hmap.put("Secondary_contact", primary2);
        save = findViewById(R.id.butAddSave);
        EnableRuntimePermission();
        boolean set = preferences.getBoolean("set", false);
        if (set) {
            name_set = (HashSet<String>) preferences.getStringSet("name_set", Collections.singleton(""));
            phone_set = (HashSet<String>) preferences.getStringSet("phone_set", Collections.singleton(""));
            if (name_set.size() > 0) {
               display(name_set, phone_set);
            }
        }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(hmap.size() <= 8) {
                        intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, 7);
                    }
                    else
                    {
                        Toast.makeText(Addcontacts.this, "You can add only 10 Contacts", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hmap.size() > 0) {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();
                        HashSet<String> pSet = new HashSet<>(hmap.values());
                        HashSet<String> nSet = new HashSet<>(hmap.keySet());
                        editor.putStringSet("phone_set", pSet);
                        editor.putStringSet("name_set", nSet);
                        editor.putBoolean("set", true);
                        editor.apply();
                        Toast.makeText(Addcontacts.this, R.string.saved, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    private void display(final HashSet<String> name_set, final HashSet<String> phone_set) {
        Iterator it = name_set.iterator();
        Iterator it1 = phone_set.iterator();
        while (it.hasNext() && it1.hasNext()) {

            final String key1 = (String) it.next();
            final String value = (String) it1.next();
            hmap.put(key1, value);
        }
        display();
    }

    private void display() {

            TableLayout tb = findViewById(R.id.displayTable);
            tb.removeAllViews();
            tb.removeAllViewsInLayout();
            String value;
                for (final Map.Entry<String, String> entry : hmap.entrySet()) {
                    value = entry.getKey() + " : " + entry.getValue();
                    TableRow tr = new TableRow(getApplicationContext());
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText(value);
                    tv.setTextAppearance(getApplicationContext(), R.style.text);
                    tr.addView(tv);
                    if(!entry.getKey().equals(primary1) || !entry.getKey().equals(primary2)) {
                        TextView tv1 = new TextView(getApplicationContext());
                        tv1.setTextAppearance(getApplicationContext(), R.style.text1);
                        tv1.setText(R.string.delete);
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hmap.remove(entry.getKey());
                                display();
                            }
                        });
                        tr.addView(tv1);
                    }
                        tb.addView(tr);

                }
            }



    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Addcontacts.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(Addcontacts.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Addcontacts.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(Addcontacts.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    showDialog("", " This app needs location permission to work with out problems ", "Yes,Grant permissions",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                   EnableRuntimePermission();
                                }
                            },
                            "No exit app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }, false);

                }
                break;
        }
    }
    public AlertDialog showDialog(String title, String message, String positiveText, DialogInterface.OnClickListener positiveonClickListener, String negativeText, DialogInterface.OnClickListener negativeonClickListener, boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText,positiveonClickListener);
        builder.setNegativeButton(negativeText,negativeonClickListener);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }
    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;
                    uri = ResultIntent.getData();
                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                hmap.put(TempNameHolder,TempNumberHolder);
                            }
                        }
                    }
                }
                break;
        }
        if(hmap.size() <= 8) {
            display();
        }

    }

}



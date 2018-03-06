package com.example.yur.rw_checker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Parser extends Activity {
    StringBuilder mStringBuilder;
    ArrayList<Integer> inbox, myShops, result;
    ListView lsmyShops, lsInbox, lsResult;
    File sourceFile;

    String tmpTextMail = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
//        lsmyShops = findViewById(R.id.lsMShRedy);
        lsInbox = findViewById(R.id.lsInboxRedy);
        lsResult = findViewById(R.id.lsResult);
        result = new ArrayList<>();
        Intent intent = getIntent();
        myShops = intent.getIntegerArrayListExtra("myList");
        inbox = intent.getIntegerArrayListExtra("inboxList");

        toParse(myShops, inbox);
        if ((result.isEmpty()) | (result == null)) {

            Toast.makeText(this, "Совпадениий не найдено", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, result);
//        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,  android.R.layout.simple_list_item_1, tmpI );

        lsResult.setAdapter(adapter);
//        lsInbox.setAdapter(adapter2);
//        finish();
    }

    void toParse (ArrayList sh, ArrayList inbox){

        for(Object s: sh) {
            if (inbox.contains(s)) {
                result.add((Integer)s);

            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putIntegerArrayListExtra("lResult", result);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}

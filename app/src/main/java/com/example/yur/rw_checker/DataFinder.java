package com.example.yur.rw_checker;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataFinder extends Activity implements View.OnClickListener{

    EditText etMyShoplistInbox, etMailList;
    Button btnProcessMyShopInbox, btnProcessMailList;
    TextView tvShops, tvMaillist;
    StringBuilder sb;
    HashSet<Integer> rowMyShop;
    HashSet<Integer> rowMailList;
    HashSet<Integer> tmp;
    ArrayList<Integer> myShopList;
    ArrayList<Integer> mailList;
    String reGex = "(\"(\\\\b)(\\\\d{1,4})((\\\\p{Punct})|(\\\\s))\")";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datafinder);
        etMyShoplistInbox = findViewById(R.id.myShopText);
        etMailList = findViewById(R.id.mailText);
        btnProcessMyShopInbox = findViewById(R.id.btnLoadMyShops);
        btnProcessMailList = findViewById(R.id.btnLoadMailList);
        tvShops = findViewById(R.id.tvShops);
        tvMaillist = findViewById(R.id.tvMailList);
        rowMyShop = new HashSet<>();
        rowMailList = new HashSet<>();
        tmp = new HashSet<>();
        btnProcessMyShopInbox.setOnClickListener(this);
        btnProcessMailList.setOnClickListener(this);

    }

    ArrayList<Integer> loadList(EditText et, HashSet<Integer> ls) {
        String s = (et.getText().toString());
////////tmp
        if (s == null) {
            s = tmpDataReader();
        }
//////////tmp
        s = s + ",";
        Pattern pattern = Pattern.compile("(\\b)(\\d{1,4})((\\p{Punct})|(\\s))");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            try {
                String tmp = matcher.group();
                tmp = tmp.replaceAll("\\D", "");
                ls.add(Integer.parseInt(tmp));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
         ArrayList<Integer> list = new ArrayList<>(ls);
        return list;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLoadMyShops:
                myShopList.clear();
                myShopList = loadList(etMyShoplistInbox, rowMyShop);
                MessageToUser("Your Shops is loaded");
                Collections.sort(myShopList);
                tvShops.setText(myShopList.toString());
                int tmp = myShopList.size();
                btnProcessMyShopInbox.setText(String.valueOf(tmp));
                etMyShoplistInbox.setText(null);
                break;
            case R.id.btnLoadMailList:
                mailList.clear();
                mailList = loadList(etMailList,  rowMailList);
                MessageToUser("List from mail is loaded");
                Collections.sort(mailList);
                btnProcessMailList.setText(String.valueOf(mailList.size()));
                tvMaillist.setText(String.valueOf(mailList.toString()));
                etMailList.setText(null);
                break;
        }
    }

    void MessageToUser(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putIntegerArrayListExtra("mailList", mailList);
        intent.putIntegerArrayListExtra("myShopList", myShopList);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    String tmpDataReader() {
        ArrayList<String> s = new ArrayList<>();
        String sd = android.os.Environment.getExternalStorageState();
        File sdDir = android.os.Environment.getExternalStorageDirectory();
        File filePath = new File(sdDir.getAbsolutePath());
        File sdFile = new File(filePath, "1111.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sdFile));
            String t ;
                while ((t = bufferedReader.readLine()) != null) {
                    t = t + ",";
                    s.add(t);
                }
                bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return s.toString();
    }
}


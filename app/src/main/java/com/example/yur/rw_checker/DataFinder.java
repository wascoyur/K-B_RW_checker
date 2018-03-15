package com.example.yur.rw_checker;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    HashSet<Integer> rowMyShop = new HashSet<>();
    HashSet<Integer> rowMailList = new HashSet<>();
    HashSet<Integer> tmp;
    ArrayList<Integer> myShopList = new ArrayList<>();
    ArrayList<Integer> mailList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 123;
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
        ls.clear();
////////tmp
        if (s == null| s.isEmpty()) {
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
                rowMailList.clear();
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
                rowMailList.clear();
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

        if (!haspermissions()) {
            requestPermissionWithRationale();
        }

        ArrayList<String> s = new ArrayList<>();
        File sdDir = null;// = android.os.Environment.getExternalStorageDirectory();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            sdDir = android.os.Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
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

    public void requestMultiplePermissions(String permission, int requestCode){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    private boolean haspermissions() {
        int res = PackageManager.PERMISSION_GRANTED;
        String[] permissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE };
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {

        boolean allowed = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void requestPermissionWithRationale() {
        boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (b) {
            final String msg = "Storage permission is needed to show files count";

            Snackbar.make(DataFinder.this.findViewById(R.id.llMyShops), msg, Snackbar
                .LENGTH_LONG).setAction("Grant", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    requestPerms();
                }
            }).show();
        } else {
            requestPerms();
        }
    }

    private void requestPerms() {
        String[] permissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }
}


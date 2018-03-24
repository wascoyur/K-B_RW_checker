package com.example.yur.rw_checker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    ListView lsMyShopList, lsMailList, lsResult;
    Button btnEnterData, btnProc, btnLoad, btnSave;
    ArrayList processedMyShopList= new ArrayList<>();
    ArrayList processedMailList = new ArrayList<>();
    ArrayList lResult = new ArrayList<>();
    SharedPreferences sPref;
    private final String TAG_shopList = "myShops";
    private final String TAG_mailList = "mailList";
    private final String TAG_result = "result";

    ArrayAdapter<Integer> adapterMyShoplist;
    ArrayAdapter<Integer> adapterMailList;
    ArrayAdapter<Integer> adapterResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lsMyShopList = findViewById(R.id.lsMyShops);
        lsMailList = findViewById(R.id.lsMailShops);
        lsResult = findViewById(R.id.lsCoicidence);
        btnEnterData = findViewById(R.id.btnEnterData);
        btnProc = findViewById(R.id.btnProcessData);
        btnLoad = findViewById(R.id.btnLoad);
        btnSave = findViewById(R.id.btnSave);

        adapterMyShoplist = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMyShopList);
        adapterMailList = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMailList);
        adapterResult = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, lResult);
        lsMyShopList.setAdapter(adapterMyShoplist);
        lsMailList.setAdapter(adapterMailList);
        lsResult.setAdapter(adapterResult);
        btnEnterData.setOnClickListener(this);
        btnProc.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        registerForContextMenu(lsMyShopList);
        registerForContextMenu(lsResult);
//        Intent dataFinder = new Intent(MainActivity.this, DataFinder.class);
//            startActivity(dataFinder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnterData:
                Intent dataFinder = new Intent(this, DataFinder.class);
                startActivityForResult(dataFinder, 1);
                refreshLists();
                break;
            case R.id.btnProcessData:
                Intent parser = new Intent(this, Parser.class);
                parser.putIntegerArrayListExtra("myList",processedMyShopList);//todo: заменить строковые значения на финал стринг переменные TAG
                parser.putIntegerArrayListExtra("inboxList",processedMailList);
                startActivityForResult(parser, 1);
                refreshLists();
                break;
            case R.id.btnSave:
                saveOnClose();
                break;
            case R.id.btnLoad:
                recoveryData(loadOnStart());
                refreshLists();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getIntegerArrayListExtra("lResult") != null) {
                lResult = data.getIntegerArrayListExtra("lResult");
                Collections.sort(lResult);
                ArrayAdapter<Integer> adapterResult = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, lResult);
                lsResult.setAdapter(adapterResult);
            }

            if ((data.getIntegerArrayListExtra("mailList") != null) && !(data.getIntegerArrayListExtra("mailList").isEmpty())) {
                processedMailList = data.getIntegerArrayListExtra("mailList");
                Collections.sort(processedMailList);
                ArrayAdapter<Integer> adapterMailList = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMailList);
                lsMailList.setAdapter(adapterMailList);
            }
            if ((data.getIntegerArrayListExtra("myShopList") != null) && !(data.getIntegerArrayListExtra("myShopList").isEmpty())) {
                processedMyShopList = data.getIntegerArrayListExtra("myShopList");
                Collections.sort(processedMyShopList);
                ArrayAdapter<Integer> adapterMyShops = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMyShopList);
                lsMyShopList.setAdapter(adapterMyShops);
            }

        }
    }




  private void saveOnClose() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(TAG_shopList, processedMyShopList.toString());
        editor.putString(TAG_mailList, processedMailList.toString());
        editor.putString(TAG_result, lResult.toString());
        editor.apply();
    }

    private Map<String, ?> loadOnStart() {
        sPref = getPreferences(MODE_PRIVATE);
        Map<String, ?> saved = sPref.getAll();
        return saved;
    }

    private void recoveryData(Map<String, ?> map) {
        if (map != null && (!map.isEmpty())) {

            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String key = entry.getKey();
                String tmp;
                switch (key) {
                    case TAG_shopList:
                        tmp = (String) entry.getValue();
                        processedMyShopList = removeBrecket(tmp);
                        break;
                    case TAG_mailList:
                        tmp = (String) entry.getValue();
                        processedMailList = removeBrecket(tmp);
                        break;
                    case TAG_result:
                        tmp = (String) entry.getValue();
                        lResult = removeBrecket(tmp);
                        break;

                }
            }
        }


    }

    private ArrayList<Integer> removeBrecket(String str) {
        ArrayList<Integer> mass = new ArrayList<>();
        String[] tmp = str.split(",");
        for (String entry : tmp) {
            entry = entry.replaceAll("\\D", "");
            try {
                Integer integer = Integer.parseInt(entry);
                mass.add(integer);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return mass;
    }

     void refreshLists() {
        adapterMyShoplist.clear();
        adapterMyShoplist.addAll(processedMyShopList);
        adapterMyShoplist.notifyDataSetChanged();

        adapterMailList.clear();
        adapterMailList.addAll(processedMailList);
        adapterMailList.notifyDataSetChanged();

        adapterResult.clear();
        adapterResult.addAll(lResult);
        adapterResult.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 101, 0, "Удалить позицию");
        menu.add(0, 102, 0, "Сохранить список");

    }

    @Override public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
            (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 102:
            Log.d("mylog", String.valueOf(item.getItemId()));
        }
        int tmp = adapterContextMenuInfo.position;
        processedMyShopList.remove(tmp);
        refreshLists();
        return super.onContextItemSelected(item);
    }
}

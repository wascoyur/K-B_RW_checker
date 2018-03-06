package com.example.yur.rw_checker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener {

    ListView lsMyShopList, lsMailList, lsResult;
    Button btnEnterData, btnProc, btnClear, btnSave;
    ArrayList<Integer> processedMyShopList= new ArrayList<>();
    ArrayList<Integer> processedMailList = new ArrayList<>();
    ArrayList<Integer> lResult = new ArrayList<>();
    SharedPreferences sPref;
    private final String TAG_shopList = "myShops";
    private final String TAG_mailList = "mailList";
    private final String TAG_result = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lsMyShopList = findViewById(R.id.lsMyShops);
        lsMailList = findViewById(R.id.lsMailShops);
        lsResult = findViewById(R.id.lsCoicidence);
        btnEnterData = findViewById(R.id.btnEnterData);
        btnProc = findViewById(R.id.btnProcessData);
        btnClear = findViewById(R.id.btnClearAll);
        btnSave = findViewById(R.id.btnSave);
//        if (loadOnStart() != null && !loadOnStart().isEmpty()) {
//            checkSavedData(loadOnStart());
//        }

////////////////TMP Debug
        List<Integer> tmpMyShops = new ArrayList<Integer>( Arrays.asList(575,824,875,1144,1193,1362,1363,1622,1753,1837,2058,2302,2365,2417,2541,2542,4281,1838,4826));
//        List<Integer> tmpMailList = new ArrayList<Integer>(Arrays.asList(575,8, 19, 20, 23, 24, 26, 37, 39, 41, 49, 51, 58, 61, 65, 79, 80, 81, 82, 88, 89, 95, 101, 107, 111, 117, 118, 131, 133, 143, 148, 151, 157, 159, 174, 175, 187, 190, 191, 198, 203, 204, 205, 222, 224, 227, 241, 242, 250, 258, 262, 272, 276, 277, 282, 284, 287, 293, 296, 297, 302, 304, 312, 313, 318, 321, 323, 325, 332, 335, 349, 352, 353, 354, 355, 357, 359, 363, 364, 370, 373, 377, 378, 381, 383, 387, 410, 412, 418, 419, 439, 443, 449, 451, 452, 464, 465, 484, 487, 495, 502, 504, 505, 506, 511, 523, 538, 547, 548, 553, 555, 563, 576, 577, 578, 590, 592, 593, 597, 599, 600, 606, 617, 620, 621, 629, 638, 639, 640, 641, 646, 651, 652, 653, 655, 658, 662, 668, 671, 683, 684, 688, 689, 690, 692, 695, 696, 698, 702,824));
//        processedMyShopList = (ArrayList<Integer>) tmpMyShops;
//        processedMailList = (ArrayList<Integer>) tmpMailList;
////////////////TMP Debug
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMyShopList);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMailList);
        ArrayAdapter<Integer> adapter3 = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, lResult);
        lsMyShopList.setAdapter(adapter);
        lsMailList.setAdapter(adapter2);
        lsResult.setAdapter(adapter3);
        btnEnterData.setOnClickListener(this);
        btnProc.setOnClickListener(this);
//        Intent dataFinder = new Intent(MainActivity.this, DataFinder.class);
//            startActivity(dataFinder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnterData:
                Intent dataFinder = new Intent(this, DataFinder.class);
                startActivityForResult(dataFinder, 1);
                break;
            case R.id.btnProcessData:
                Intent parser = new Intent(this, Parser.class);
                parser.putIntegerArrayListExtra("myList",processedMyShopList);//todo: заменить строковые значения на финал стринг переменные TAG
                parser.putIntegerArrayListExtra("inboxList",processedMailList);
//                parser.putIntegerArrayListExtra("lResult",lResult);
                startActivityForResult(parser, 1);
                break;
//            case R.id.btnEnterData:
//                Intent dataFinder = new Intent(this, DataFinder.class);
//                startActivityForResult(dataFinder, 1);
//                break;
//            case R.id.btnEnterData:
//                Intent dataFinder = new Intent(this, DataFinder.class);
//                startActivityForResult(dataFinder, 1);
//                break;
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

            if (data.getIntegerArrayListExtra("mailList") != null) {
                processedMailList = data.getIntegerArrayListExtra("mailList");
                Collections.sort(processedMailList);
                ArrayAdapter<Integer> adapterMailList = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMailList);
                lsMailList.setAdapter(adapterMailList);
            }
            if (data.getIntegerArrayListExtra("myShopList") != null) {
                processedMyShopList = data.getIntegerArrayListExtra("myShopList");
                Collections.sort(processedMyShopList);
                ArrayAdapter<Integer> adapterMyShops = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, processedMyShopList);
                lsMyShopList.setAdapter(adapterMyShops);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveOnClose();
    }

    private void saveOnClose() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("myShops", processedMyShopList.toString());//todo: заменить строковые значения на финал стринг TAG
        editor.putString("mailList", processedMailList.toString());
        editor.putString("result", lResult.toString());
    }

    private Map<String, ?> loadOnStart() {
        sPref = getPreferences(MODE_PRIVATE);
        Map<String, ?> saved = sPref.getAll();
        return saved;
    }

    private void checkSavedData(Map<String, ?> map) {
        if (map != null && (!map.isEmpty())) {

            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String key = entry.getKey();
                switch (key) {
                    case "myShops":
                        ArrayList arrayList = new ArrayList(Arrays.asList(entry.getValue()));
                        processedMyShopList = new ArrayList<>(arrayList);
                        //TODO: impplement восстановление данных
                }
            }
        }


    }
}

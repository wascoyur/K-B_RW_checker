package com.example.yur.rw_checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yur on 09.03.2018.
 */

class IOclass {



    String tmpDataReader() {
        ArrayList<String> s = new ArrayList<>();
        String sd = android.os.Environment.getExternalStorageState();
        File sdDir = android.os.Environment.getExternalStorageDirectory();
        File filePath = new File(sdDir.getAbsolutePath() + "/download/");
        File sdFile = new File(filePath, "InboxMail.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sdFile));
            String t ;
            while ((t = bufferedReader.readLine()) != null) {
                t = t + ",";
                s.add(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s.toString();
    }
}

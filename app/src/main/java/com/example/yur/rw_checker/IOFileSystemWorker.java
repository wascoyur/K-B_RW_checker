package com.example.yur.rw_checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yur on 12.03.2018.
 */

public  class IOWorker  {
  private String t;
  private String inFileName;
  private File directoryOfSd;
  private File pathToInputFile;
  private File otputFile;
  ArrayList<String> s = new ArrayList<>();

  public IOWorker(String inputFileName) {
    this.directoryOfSd = android.os.Environment.getExternalStorageDirectory();
    this.pathToInputFile = new File(directoryOfSd.getAbsolutePath(), inputFileName);

  }

  //File filePath = new File(sdDir.getAbsolutePath());
  //File sdFile = new File(filePath, "1111.txt");
  private String readStringsFromFile() {
    BufferedReader bufferedReader = null;
    ArrayList list = new ArrayList();
    try{
      bufferedReader = new BufferedReader(new FileReader(new File(directoryOfSd,
          inFileName)));
      String tmp;
      while ((tmp = bufferedReader.readLine()) != null){
        if (!tmp.endsWith(",")) {
          tmp = tmp + ",";
        }
        if (!tmp.startsWith(",")) {
          tmp = tmp + ",";
        }
        list.add(tmp);
      }
    }catch (FileNotFoundException e) {
      e.printStackTrace();
      try {
        bufferedReader.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list.toString();
  }


}

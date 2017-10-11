package com.zhangyn.androidtd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AndroidTDOpenHelper extends SQLiteOpenHelper {

  private static final String DB_NAME = "android_td.db";
  private static final int DB_VERSION = 1;

  private static AndroidTDOpenHelper mInstance;

  public static AndroidTDOpenHelper getInstance(Context context){
    if (null == mInstance){
      mInstance = new AndroidTDOpenHelper(context);
    }
    return mInstance;
  }

  public AndroidTDOpenHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(Player.CREATE_TABLE);

    Player.InsertRow insertRow = new Player.InsertRow(db);
    insertRow.bind("Joker",1L);
    insertRow.program.executeInsert();
    insertRow.bind("Alice",2L);
    insertRow.program.executeInsert();
    insertRow.bind("Bob",3L);
    insertRow.bind("Dell",4L);
    insertRow.program.executeInsert();
  }

  @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }
}

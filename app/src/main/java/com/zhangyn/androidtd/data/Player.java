package com.zhangyn.androidtd.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;
import com.squareup.sqldelight.SqlDelightStatement;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Player implements PlayerModel {

  public static final Factory<Player> FACTORY = new Factory<>(new Creator<Player>() {
    @Override public Player create(long _id, @NonNull String name, long number) {
      return new AutoValue_Player(_id, name, number);
    }
  });

  public static final RowMapper<Player> SELECT_ALL_MAPPER = FACTORY.selectAllMapper();

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public List<Player> getAllPlayers(SQLiteDatabase db){
    List<Player> players = new ArrayList<>();
    SqlDelightStatement query = FACTORY.selectAll();
    try (Cursor cursor = db.rawQuery(query.statement,query.args)){
      while (cursor.moveToNext()){
        players.add(SELECT_ALL_MAPPER.map(cursor));
      }
    }
    return players;
  }

  public static final RowMapper<String> PLAYER_NAME_MAPPER = FACTORY.playerNamesMapper();

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public List<String> playerNames(SQLiteDatabase db){
    List<String> names = new ArrayList<>();
    SqlDelightStatement query = FACTORY.playerNames();
    try (Cursor cursor = db.rawQuery(query.statement,query.args)){
      while (cursor.moveToNext()){
        names.add(PLAYER_NAME_MAPPER.map(cursor));
      }
    }
    return names;
  }

  public int updatePlayer(SQLiteDatabase db,long number,String name){
    Player.UpdateNumber updateNumber = new Player.UpdateNumber(db);
    updateNumber.bind(number, name);
    return updateNumber.program.executeUpdateDelete();
  }

  public void insertPlayer(SQLiteDatabase db,long number,String name){
    Player.InsertRow insertRow = new Player.InsertRow(db);
    insertRow.bind(name, number);
    insertRow.program.executeInsert();
  }

  public static TypeAdapter<Player> typeAdapter(Gson gson){
    return new AutoValue_Player.GsonTypeAdapter(gson);
  }
}

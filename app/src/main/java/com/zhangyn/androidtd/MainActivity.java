package com.zhangyn.androidtd;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import com.zhangyn.androidtd.data.AndroidTDOpenHelper;
import com.zhangyn.androidtd.data.Player;
import com.zhangyn.androidtd.data.PlayerModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import org.reactivestreams.Subscription;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SqlBrite sqlBrite = new SqlBrite.Builder().build();
    final BriteDatabase db = sqlBrite.wrapDatabaseHelper(AndroidTDOpenHelper.getInstance(this.getApplication()),Schedulers.io());
    SqlDelightStatement queryAll = Player.FACTORY.selectAll();

    db.createQuery(queryAll.tables,queryAll.statement,queryAll.args)
        .mapToList(new Function<Cursor, Player>() {
          @Override public Player apply(@NonNull Cursor cursor) throws Exception {
            return Player.SELECT_ALL_MAPPER.map(cursor);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Player>>() {
          @Override public void accept(List<Player> players) throws Exception {
            Log.e(TAG, "accept: " + players.toString());
          }
        });

    Observable.just(1).subscribeOn(Schedulers.io()).doOnNext(new Consumer<Integer>() {
      @Override public void accept(Integer integer) throws Exception {
        Player.InsertRow insertRow = new PlayerModel.InsertRow(db.getWritableDatabase());
        insertRow.bind("Dell",3);
        db.executeInsert(insertRow.table,insertRow.program);
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }
}

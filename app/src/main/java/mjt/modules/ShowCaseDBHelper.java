package mjt.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mike on 24/05/2017.
 */

public class ShowCaseDBHelper extends SQLiteOpenHelper {

    ShowCaseDBHelper(Context context, String databasename, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databasename, factory, version);

    }

    public void onCreate(SQLiteDatabase db) {
        expandDB(db, false);
    }

    public void onUpgrade(SQLiteDatabase db, int oldeversion, int newversion) {

    }

    void expandDB(SQLiteDatabase db, boolean buildandexpand) {

    }

}

package mjt.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Minimal DBHelper i.e. doesn't create tables
 */

@SuppressWarnings({"WeakerAccess", "UnusedParameters", "SameParametervalue"})
public class ShowCaseDBHelper extends SQLiteOpenHelper {

    ShowCaseDBHelper(Context context,
                     String databasename,
                     SQLiteDatabase.CursorFactory factory,
                     int version) {
        super(context, databasename, factory, version);
    }
    public void onCreate(SQLiteDatabase db) {
    }
    public void onUpgrade(SQLiteDatabase db,
                          int oldeversion,
                          int newversion) {
    }
}

package mjt.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Minimal DBHelper i.e. doesn't create tables
 */

@SuppressWarnings({"WeakerAccess", "UnusedParameters"})
public class ShowCaseDBHelper extends SQLiteOpenHelper {

    ShowCaseDBHelper(Context context, @SuppressWarnings("SameParameterValue") String databasename, @SuppressWarnings("SameParameterValue") SQLiteDatabase.CursorFactory factory, @SuppressWarnings("SameParameterValue") int version) {
        super(context, databasename, factory, version);

    }

    public void onCreate(SQLiteDatabase db) {
        expandDB(db, false);
    }

    public void onUpgrade(SQLiteDatabase db, int oldeversion, int newversion) {

    }

    @SuppressWarnings("EmptyMethod")
    /*
      This could be where the table creation modifcation takes place
     */
    void expandDB(SQLiteDatabase db, @SuppressWarnings("SameParameterValue") boolean buildandexpand) {

    }

}

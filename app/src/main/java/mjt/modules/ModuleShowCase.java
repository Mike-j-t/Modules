package mjt.modules;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbdatabase.DBDatabase;
import mjt.dbindex.DBIndex;
import mjt.dbtable.DBTable;
import mjt.emsg.Emsg;
import mjt.displayhelp.DisplayHelp;
import mjt.pickcolour.PickColour;

import static mjt.sqlwords.SQLKWORD.*;


/******************************************************************************
 * ModuleShowCase Activity to show use of the modules
 */
public class ModuleShowCase extends AppCompatActivity {

    LinearLayout mLinearLayout;
    TextView mTextView;
    Button mDoItButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_show_case);
        mTextView = (TextView) findViewById(R.id.textview);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        mDoItButton = (Button) findViewById(R.id.doit_button);

        // Example Use of Emsg an Extended message handling class
        // see ShowCase method below
        //ShowCaseEmsg();
        // Example use of DBColumn, DBTable and DBDatabase for
        // defining SQlite databases and subsequently altering
        // them. see ShowCaseDBCLasses method below
        //ShowCaseDBClasses();
        //ExampleDB();
        //ShowCaseDisplayHelp();
        //ShowCasePickColour();

        mDoItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCasePickColour();
            }
        });
    }
    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void ShowCaseDisplayHelp() {

        ArrayList<String> altlist = DisplayHelp.convertResourceToArrayList(this,R.array.xxhelp_shoporder_field);
        DisplayHelp dh = new DisplayHelp(this,"my Title",altlist,80,false,0xffff0000, 0xbbffffff, 20f, 16f, 12);

        //new DisplayHelp(this,"ALt Title",R.array.help_main_activity,80,true,0xffff0000, 0xbbffffff,20f,16f,12);

    }

    private void ShowCasePickColour() {
        Intent i = new Intent(this, PickColour.class);
        i.putExtra(PickColour.INTENTKEY_STARTCOLOUR,0xFF_70_FD_87);
        startActivityForResult(i,10);

    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent rv) {
        if (requestcode == 10) {
            if (resultcode == RESULT_OK) {
                long returnedcolour = rv.getLongExtra(
                        PickColour.INTENTKEY_RETURNCOLOUR,0x01_FF_FF_FF_FFL
                );
                mLinearLayout.setBackgroundColor((int) returnedcolour);
            }
        }

    }

    /**************************************************************************
     * ShowCase the Emsg Class - Extended error messaging class
     */
    private void ShowCaseEmsg() {

        // Instantiate an Emsg object using the default constructor
        Emsg errormsg1 = new Emsg();        // error on
                                            // 0 error number,
                                            // empty msg
        logEmsg(errormsg1);                 // write error info to log

        // Instantiate another using alternative constructor
        Emsg errormsg2 = new Emsg(true,     // Set as in error
                100,                        // the error number
                "This did not work." +      // The error message
                        "");
        logEmsg(errormsg2);                 // write error info to log

        // Set values via methods (4)
        errormsg1.setErrorIndicator(true);  // Set to in error
        errormsg1.setErrorNumber(99);       // Set err number
        errormsg1.setErrorMessage("This went wrong"); // set error message
        logEmsg(errormsg1);
        errormsg1.setAll(true,98,"Ouch that hurt!");
        logEmsg(errormsg1);

        // See logEmsg method below for data retrieval methods (getters)

    }

    /**************************************************************************
     * Write error message data to the log
     * @param emsg  The Emsg object
     */
    private void logEmsg(Emsg emsg) {
        Log.i("EMSG",
                " Error State=" +
                        Boolean.toString(emsg.getErrorIndicator()) +
                        " Error Number=" +
                        Integer.toString(emsg.getErrorNumber()) +
                        " Error Message=" +
                        emsg.getErrorMessage()
        );

        /* EXAMPLE LOG OUTPUT
         I/EMSG:  Error State=true Error Number=0 Error Message=
         I/EMSG:  Error State=true Error Number=100 Error Message=This did not work.
         I/EMSG:  Error State=true Error Number=99 Error Message=This went wrong
         I/EMSG:  Error State=true Error Number=98 Error Message=Ouch that hurt!
         */
    }

    /**************************************************************************
     * ShowCase DB Classes
     *
     * DB Classes are DBDatabase, DBTable and DBCOlumn and are used to build a
     * pseudo schema from which SQL can be retrieved to create an SQLite
     * Database.
     * A number of all three objects are required for a functional/useful
     * database.
     *
     * There would only be 1 DBDatabase instance (per database)
     * There would be 1 or more instances of DBTable
     * There would be at least 1 DBColumn instance per table.
     *
     * Generally you would define the columns per table, then the table
     * for each table and then the database.
     *
     * Note SQLKWORD is imported, this provides some of the more common SQL
     * keywords e.g. SQLSELECT resovles to SELECT (with surrounding spaces).
     *
     */
    private void ShowCaseDBClasses() {

        final String OKLOGTAG = "ShowCaseOK";
        final String ERRLOGTAG = "ShowCaseERR";
        final String DB2LOGTAG = "ShowCaseCPYDB";

        // Create or get the Database as per usual via a DBHelper
        ShowCaseDBHelper schelper = new ShowCaseDBHelper(this,
                ShowCaseDBInfo.DBNAME,null,
                ShowCaseDBInfo.DBVERSION
        );
        // Open the database
        SQLiteDatabase db = schelper.getWritableDatabase();

        if (ShowCaseDBInfo.pseudodbschema.isDBDatabaseUsable()) {

            // Likewise look at the SQL that is generated for the build
            // of the database.
            // However, an ArrayList is returned with n SQL statements
            // Again wouldn't normally do this.
            ArrayList<String> buildSQL =
                    ShowCaseDBInfo.pseudodbschema.generateDBBuildSQL(db);
            for (String sql : buildSQL) {
                Log.d(OKLOGTAG, sql);
            }

            // THIS IS IT BUILD the actual database
            // Note runs all statements in a transaction
            ShowCaseDBInfo.pseudodbschema.actionDBBuildSQL(db);

            // DBdatabase pseudoschema is OK so look at the extracted
            // pseudo Schema
            // (wouldn't normally do this)
            Log.d(OKLOGTAG,
                    ShowCaseDBInfo.pseudodbschema.generateExportSchemaSQL());

            // Now we have a database have a look at the Alter SQL (should be nothing to do)
            Log.d(OKLOGTAG,"Pre AlterSQL");
            ArrayList<String> altersql =
                    ShowCaseDBInfo.pseudodbschema.generateDBAlterSQL(db);
            for (String sql : altersql) {
                Log.d(OKLOGTAG,
                        sql
                );
            }
            Log.d(OKLOGTAG,
                    "Post AlterSQL"
            );
        } else {
            Log.e(ERRLOGTAG,
                    ShowCaseDBInfo.pseudodbschema.
                            getAllDBDatabaseProblemMsgs());
        }

        // As an example of not using an SQLiteOpenHelper
        // not really something that would normally be done
        SQLiteDatabase db2 = openOrCreateDatabase("myotherdatabase",
                Context.MODE_PRIVATE,
                null
        );
        // Build the 2nd Database using the same pseudo schema
        ShowCaseDBInfo.pseudodbschema.actionDBBuildSQL(db2);

        // Show the tables and indexes of the first Database
        Cursor dbcsr = db.query(SQLITEMASTERTABLE,null,null,null,null,null,null);
        while (dbcsr.moveToNext()) {
            Log.d(OKLOGTAG,
                    "Type of Entry=" +
                            dbcsr.getString(
                                    dbcsr.getColumnIndex(
                                            SQLITEMASTERCOLUMN_TYPE
                                    )
                            ) +
                            "\tName of Entry=" +
                            dbcsr.getString(
                                    dbcsr.getColumnIndex(
                                            SQLITEMASTERCOLUMN_NAME
                                    )
                            )
            );
        }
        dbcsr.close();

        // Show the tables and indexes of the 2nd Database
        Cursor db2csr = db2.query(SQLITEMASTERTABLE,null,null,null,null,null,null);
        while (db2csr.moveToNext()) {
            Log.d(DB2LOGTAG,
                    "Type of Entry=" +
                            db2csr.getString(
                                    db2csr.getColumnIndex(
                                            SQLITEMASTERCOLUMN_TYPE
                                    )
                            ) +
                            "\tName of Entry=" +
                            db2csr.getString(
                                    db2csr.getColumnIndex(
                                            SQLITEMASTERCOLUMN_NAME
                                    )
                            )
            );
        }
        if (!SCAnyData(db, ShowCaseDBInfo.pseudodbschema)) {
            SCInsertAdminUser(db);
            Log.d(OKLOGTAG,"Admin User Added");
        }

        db2csr.close();
    }

    /**************************************************************************
     * Insert a default user named ADMIN but only if there are no other rows
     * @param db    The SQLite database in which to insert the user
     */
    private void SCInsertAdminUser(SQLiteDatabase db) {
        Cursor csr = db.query(ShowCaseDBInfo.USERS_TABLENAME,
                null,null,null,null,null,null
        );
        if (csr.getCount() > 0) {
            csr.close();
            return;
        }
        csr.close();
        ContentValues cv = new ContentValues();
        cv.put(ShowCaseDBInfo.USERS_NAME_COLNAME,"ADMIN");
        cv.put(ShowCaseDBInfo.USERS_INFO_COLNAME,"The admin user");
        db.insert(ShowCaseDBInfo.USERS_TABLENAME,null,cv);
    }

    /**************************************************************************
     * Find if there are any rows, thus data, in the database
     * @param db        SQLite Database
     * @param dbschema  DBDatabase schema to find all tables
     * @return          true if there are any rows found, else false
     */
    private boolean SCAnyData(SQLiteDatabase db, @SuppressWarnings("SameParameterValue") DBDatabase dbschema) {
        boolean rv = false;
        int rowcount = 0;
        Cursor csr;
        db.beginTransaction();
        for (String table: dbschema.getTables()) {
            csr = db.query(table,null,null,null,null,null,null);
            rowcount = rowcount + csr.getCount();
            csr.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        if (rowcount > 0) {
            rv = true;
        }
        return rv;
    }

    /**
     * Example Usage of the DB family of objects
     */
    private void ExampleDB() {
        String DBNAME = "myexampledatabase"; // Database name
        int DBVERSION = 1;            // Database version

        // Table and column names for the user table.
        String DBUSERTABLE = "users";
        String DBUSERNAMECOL = "name";
        String DBUSERDESCCOL = "descr";

        // The DBColumn objects for the user table.
        // First column is how you can easily create a standard _id column
        // i.e. _id INTEGER PRIMARY INDEX (note true or false will work, just has to be boolean)
        DBColumn userid = new DBColumn(true);             // Construct standard _id column

        // 2nd and 3rd columns are columnname TEXT
        //    3rd paramter false = not a primary index)
        //    4th paramter is for the default value, empty string = no default
        DBColumn username = new DBColumn(DBUSERNAMECOL,"TEXT",false,"");
        DBColumn userdesc = new DBColumn(DBUSERDESCCOL,"TEXT",false,"");

        // Prepare an ArrayList of the columns for use by the DBTable
        ArrayList<DBColumn> userscolumns = new ArrayList<>(
                Arrays.asList(userid,username,userdesc)
                );

        // Table and column names for the property table.
        String DBPROPTABLE = "proprties";
        String DBPROPINFOCOL = "info";

        DBColumn propertyid = new DBColumn(false);
        DBColumn propertyinfo = new DBColumn(DBPROPINFOCOL,"TEXT",false,"");

        // Prepare an ArrayList of the columns for use by the DBTable
        ArrayList<DBColumn> propertiescolumns = new ArrayList<>();
        propertiescolumns.add(propertyid);
        propertiescolumns.add(propertyinfo);

        // The DBTable objects
        DBTable usertable = new DBTable(DBUSERTABLE,userscolumns);
        // Note without an ArrayList of DBColumns
        DBTable propertiestable = new DBTable(
                DBPROPTABLE,
                new ArrayList<>(
                        Arrays.asList(
                                propertyid,
                                propertyinfo
                        )
                )
        );
        // Prepare an ArrayList of the tables
        ArrayList<DBTable> tables = new ArrayList<>(
                Arrays.asList(
                        usertable,
                        propertiestable
                )
        );

        // Define some Indexes
        // Index for the properties table on the info column sorted in
        // dsecending order.
        DBIndex propertiesinfo = new DBIndex("propertiesinfoindex",
                propertiestable,
                propertyinfo,
                false,
                false
        );
        // Index for the users table with name and desc columns sorted in
        // ascending and descending order respectively.
        ArrayList<DBColumn> usernamedesccolumns = new ArrayList<>(Arrays.asList(
                username,userdesc
        ));
        ArrayList<Boolean> usernamedescsort = new ArrayList<>(Arrays.asList(
                true,false
        ));
        DBIndex usernamedescindex = new DBIndex("usernamedescindex",
                usertable,
                usernamedesccolumns,
                usernamedescsort,
                false
                );

        // 3rd Index for the users table with dsec and name columns sorted
        // in ascending and descending order respectively and where the
        // index is to be unique.
        DBIndex usersdescnameindex = new DBIndex("userdescnameindex",
                usertable,
                new ArrayList<>(
                        Arrays.asList(
                                userdesc,
                                username
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                true,
                                false
                        )
                ),
                true
        );

        // Prepare a list of the indexes for inclusion in the DBDatabase
        ArrayList<DBIndex> indexes = new ArrayList<>(
                Arrays.asList(
                        propertiesinfo,
                        usernamedescindex,
                        usersdescnameindex
                ));

        //Finally construct the DBDatabase Object
        DBDatabase baseschema = new DBDatabase(DBNAME,tables,indexes);

        //To use the baseschema to create the tables, an SQLite database
        // will be required.
        ShowCaseDBHelper dbhelper = new ShowCaseDBHelper(this,DBNAME,null,DBVERSION);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Alternative method of getting an SQLite Database not using a helper
        SQLiteDatabase db2 =  openOrCreateDatabase(
                "anotherexampledatabase",
                Context.MODE_PRIVATE,
                null
        );

        // Check for usability of baseschema
        if (baseschema.isDBDatabaseUsable()) {
            // Build the Database Structures for the two databases
            baseschema.actionDBBuildSQL(db);
            baseschema.actionDBBuildSQL(db2);

            // Now Alter the database to apply any changes, if any
            baseschema.actionDBAlterSQL(db);
            baseschema.actionDBAlterSQL(db2);
            // Typically Build followed by Alter would be run at the
            // start of the application, thus creating the database if
            // it doesn't exist and then altering it if any changes
            // have been made (e.g. adding a new DBColumn to a table).
        }
    }
}

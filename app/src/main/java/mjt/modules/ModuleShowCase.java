package mjt.modules;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbdatabase.DBDatabase;
import mjt.dbtable.DBTable;
import mjt.emsg.Emsg;

import static mjt.dbcolumn.DBColumn.DB_NUMERIC;
import static mjt.dbcolumn.DBColumn.DB_STD_ID;

import static mjt.dbcolumn.DBColumn.DB_IDTYPE;
import static mjt.dbcolumn.DBColumn.DB_TXT;

import static mjt.modules.DBInfo.*;

/******************************************************************************
 * ModuleShowCase Activity to show use of the modules
 */
public class ModuleShowCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_show_case);

        // Example Use of Emsg an Extended message handling class
        // see ShowCase method below
        ShowCaseEmsg();
        // Example use of DBColumn, DBTable and DBDatabase
        ShowCaseDBClasses();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        // See logEmsg method for data retrieval methods (getters)

    }

    /**************************************************************************
     * Write error message data to the log
     * @param emsg  The Emsg object
     */
    @SuppressWarnings("DanglingJavadoc")
    private void logEmsg(Emsg emsg) {
        Log.i("EMSG",
                " Error State=" +
                        Boolean.toString(emsg.getErrorIndicator()) +
                        " Error Number=" +
                        Integer.toString(emsg.getErrorNumber()) +
                        " Error Message=" +
                        emsg.getErrorMessage()
        );

        /** EXAMPLE LOG OUTPUT
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

        // Define the instances to create a Database with two simple
        // tables (users and properties)
        // Note names are defined in the DBINFO class

        // Construct a DBCOlumn object using the most comprehensive
        // and recommended constructor.
        //
        // Note as this column is a primary index (id column) DB_STD_ID
        // is used for the column name and DB_IDTYPE for the column type.
        //
        // Defined column types are DB_INT, DB_TXT, DB_IDTYPE, DB_REAL,
        // DB_NUMERIC and DB_BLOB.
        // DB_ID_TYPE is and alias for DB_INT
        DBColumn userid = new DBColumn(
                DB_STD_ID,      // Column Name as String
                DB_IDTYPE,      // Column type
                true,           // Is it a Primary Index column, false if not
                "",             // Default value, empty = no default applied
                0               // Order of the column within the table
        );
        // Note DBInfo.DBUSER_ID_COLUMNNAME should really have been used.
        // This though, refers to DB_STD_ID.

        // Username is Text Column not part of the primary index
        DBColumn username = new DBColumn(
                DBInfo.DBUSER_NAME_COLUMNNAME,
                DB_TXT,
                false,
                "",
                1
        );
        // User info is a Text Column not part of the primary index
        DBColumn userinfo = new DBColumn(
                DBInfo.DBUSER_INFO_COLUMNNAME,
                DB_TXT,
                false,
                "",
                2
        );
        // Create an Array List of the columns for use when defining the table
        ArrayList<DBColumn> usercolumns = new ArrayList<>(
                Arrays.asList(
                        userid,
                        username,
                        userinfo
                )
        );
        // define the User Table applying the columns above
        DBTable usertable = new DBTable(
                DBUSER_TABELNAME,   // Table name as a string
                usercolumns         // ArrayList of DBCOlumns
        );

        // Define th columns for the Property table
        DBColumn propertyid = new DBColumn(
                DBPROPERTY_ID_COLUMNNAME,   // Note refers to DB_STD_ID
                DB_IDTYPE,
                true,
                "",
                0
        );
        DBColumn propertydescription = new DBColumn(
                DBPROPERTY_DESC_COLUMNNAME,
                DB_TXT,
                false,
                "",
                1
                );

        // Define the Property table using the Property Columns
        DBTable propertytable = new DBTable(
                DBPROPERTY_TABLENAME,
                new ArrayList<>(Arrays.asList(
                        propertyid,
                        propertydescription
                ))
        );

        // Define the Database psuedo schema
        DBDatabase psuedoscehma = new DBDatabase(
                DBNAME,
                new ArrayList<DBTable>(Arrays.asList(
                        usertable,
                        propertytable
                ))
        );

        // Check to see if the psuedoschema is useable if not Log the messages
        // otherwise extract the SQL to build the tables.

        if (!psuedoscehma.isDBDatabaseUsable()) {
            Log.e("DBTEST",psuedoscehma.getAllDBDatabaseProblemMsgs());

        } else {
            // To build the SQL for Create we need to have the Database so
            // create the empty (or get the existing one if it exists)
            ShowCaseDBHelper scdbhelper = new ShowCaseDBHelper(this,DBNAME,null,DBVERSION);

            // We need the database itself so get a readable
            SQLiteDatabase scdb = scdbhelper.getWritableDatabase();

            // Get the pre-create alterSQL (wouldn't normally do this)
            ArrayList<String> precrtaltSQL = psuedoscehma.generateDBAlterSQL(scdb);

            // Get the SQL to create the Tables
            ArrayList<String> createSQL = psuedoscehma.generateDBBuildSQL(scdb);

            // Get the SQL to alter any tables after comparing against the
            // actual database and  the psuedo schema
            ArrayList<String> alterSQL = psuedoscehma.generateDBAlterSQL(scdb);

            // Write the table create SQL to the log
            for (String sql : createSQL) {
                Log.i("DBTEST-CRTSQL",sql);
            }

            // Write the Alter SQL to the log
            // Note! There will be no elements as there is nothing to alter
            //  more specififcally alter only looks at adding columns
            // that don't exist in a table, so if a table doesn't exist
            // then it cannot be altered
            for (String sql : alterSQL) {
                Log.i("DBTEST-ALTSQL", sql);
            }

            // Actually build the database
            // i.e. create the tables and columns
            psuedoscehma.actionDBBuildSQL(scdb);
            // Like wise apply any alterations (none)
            psuedoscehma.actionDBAlterSQL(scdb);


            // NOTE! The following would not normally be done in this way
            // This is bacuse you would normally alter the definitions
            // prior to this.

            // Define a new column
            DBColumn newcolumn = new DBColumn("MYnewColumn",DB_NUMERIC,false,"21",4);
            // Add the column to the property table (uses singluar add method)
            propertytable.AddDBColumnToDBTable(newcolumn);

            // Generate an exportable schema (works with MySQL)
            // Introduces a new method that should work with MYSQL
            String newschema =  psuedoscehma.generateExportSchemaSQL();
            Log.i("DBTEST-NEWSCHM", newschema);

            // Report Any problems with the updated schema
            // yet another method not previously used
            Log.i("DBTEST-USE",psuedoscehma.getDBDatabaseProblemMsg());

            // Build SQL would be this (would not be applied as tables exist)
            // Nothing to Log as nothing changed as far as build rather
            // a new ccolumn needs to be applied via Alter SQL
            ArrayList<String> newcolbldSQL = psuedoscehma.generateDBBuildSQL(scdb);
            for (String sql : newcolbldSQL) {
                Log.i("DBTEST-NEWCRT",sql);
            }

            // Alter SQL which will now exist
            ArrayList<String> newcolalterSQL = psuedoscehma.generateDBAlterSQL(scdb);
            for (String sql : newcolalterSQL) {
                Log.i("DBTEST-NCALT",sql);
            }

            // Actually apply the alter Sql to add the new column
            psuedoscehma.actionDBAlterSQL(scdb);

            // The database should now be modified by the addition of the
            // new column.

            scdb.close();

        }
    }
}

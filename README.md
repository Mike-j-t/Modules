# Modules
Modules for Android
Modules that have been written for Android development

## DisplayHelp ##
A dialog for displaying help (needs more info etc)

## Emsg ##
An extended error message class.
An Emsg instance has 3 members 

- errorindicator (boolean true indicates and error, false if not)
- errornumber (Integer intended so that error's can be uniquely identified)
- errormessage (String the error message)

## CommonSQLiteUtilities
Some utilities for inspecting SQLite Databases :-
- LogDatabaseInfo writes database information Pragmas, tables, indexes, foreign Keys to the Log.
- LogCursorColumns write the columns in a Cursor to the Log.
- LogCursorData write all data from the Cursor to the log using the main Cursor get???? methods.
- getAllRowsFromTable returns a Cursor with all columns for all rows.

## DB???? Family ##
Modules that create a psuedo/base SQLite database scehma, ultimately creating
the database structure or altering the database structure.

There are 4 core Classes **DBColumn**, **DBTAble**, **DBIndex**  and **DBDatabase**. 
Additionally Class **SQLKWORD**has some more commonly used SQL KeyWords defined as constants e.g.
**`SQLSELECT`** resolves to ` SELECT `(with surrounding spaces)
**`SQLSELECTALLFROM`** resolves to ` SELECT * FROM `
The intentional use is to reduce typing errors.

Typically you would use **DBColumn** to create the column onjects (once per column), 
then you would use **DBTable** to create the table objects, passing the relevant columns, 
you could then use **DBIndex** to create any additional indexes.

You would then use **DBDatabase** to create a base/pseudo schema, this being passed the **DBTable** list
and the **DBIndex** list.

You would then typically check that the schema is usable using the **`isDatabaseUsable`** method.
If the schema is usable you would then use the **`actionDBBuildSQL`** method to build the database and to
cater for potential changes, follow this with **`actionDBAlterSQL`** which would apply alterations.
Both of these actions require an SQLilte Database to be passed.

The following is example code that utlisies a very basic **SQLiteOpenhelper** :-

First the minimal **SQLiteOpenHelper** noting that this does nothing in either the `onCreate` or the `onUpgrade` methods.

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

Now the code within an Activity or a Method within an Activity that is called:-

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
                SQLiteDatabase.CREATE_IF_NECESSARY,
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

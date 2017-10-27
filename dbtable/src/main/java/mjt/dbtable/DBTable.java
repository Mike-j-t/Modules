package mjt.dbtable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mjt.dbcolumn.DBColumn;
import static mjt.sqlwords.SQLKWORD.*;

/**
 * DBTable a component of the DB???? classes that represents a Table
 * of a SQLite Database.
 *
 * <p>
 *     A DBTable is one of a number of DB???? classes (DBDatabase, DBTable,
 *     DBCColumn and DBIndex) designed for assisting with the creation
 *     and alteration of SQLite Databases.
 * </p>
 *
 * <p>
 *     <b>DBTable dependencies on other DB???? classes/objects.</b>
 * </p>
 * <p>
 *     A DBTable requires at least one DBColumn, a DBDatabase requires at
 *     least one DBTable, A DBIndex requires one DBTable.
 * </p>
 *
 *      <b>Usability.</b>
 * </p>
 * <p>
 *     The construction of a DBTable instance includes checking that the
 *     name is at least 1 character in length and that a usable  DBColumn
 *     has been specified. If not then the DBtable will have it's usability
 *     flag set to false. The usability flag is propogated to the
 *     DBDatabase object.
 * </p>
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class DBTable {
    /**
     * A boolean that indicates if the DBColumn is usable (true)
     * or not (false).
     */
    private boolean usable;
    /**
     * A String that will be the name of the table in the actual database.
     *
     * <p>The name will be converted to lowercase, it should not include any
     * whitespace.
     * </p>
     */
    private String table_name;
    /**
     * A String ArrayList of DBColumns that the table will be comprised of.
     */
    private ArrayList<DBColumn> table_columns;
    /**
     * An internally created string that will contain issues found
     * when instantiating or modifying the DBTable.&nbsp;It would only be set
     * if the usable flag is set to false.
     */
    private String problem_msg;

    /**************************************************************************
     * Constructs an unusable DBTable with problem message set.
     *
     * <p>To make the DBTable usable the DBtable will have to have to be
     * provided with the table name via <code>setDBTableName</code> and
     * additionally at least one DBColumn will have to be provided.
     * </p>
     * <p>
     *     Methods <code>addDBColumntoDBTable</code>,
     *     <code>addDBColumnsToDBTable</code>,
     *     <code>addMultipleColumnsToDBTable</code> facilitate adding
     *     DBColumns.
     * </p>
     */
    public DBTable() {
        this.usable = false;
        this.table_name = "";
        this.table_columns = new ArrayList<>();
        this.problem_msg = "\nWDBT0004 - Uninstantiated - " +
                "Use addDBColumnToDBTable to add at least 1 usable DBColumn or " +
                "Use addDBColumnsToDBTable to add at least 1 usable DBColumn or " +
                "Use AddMultipleColumnsToDBTable to add at least 1 usable DBColumn. " +
                "Note any unusable DBcolumn will render table unusable. " +
                "Also use setDBTableTableName to set the Table Name. " +
                "Caller=DBTable (Default Constructor)";
    }

    /**
     * * Constructs a DBTable with only the table name, the resultant
     * DBTable will be flagged as unusable as no DBCOlumns have been
     * assigned.
     *
     * <p>
     *     Methods <code>addDBColumntoDBTable</code>,
     *     <code>addDBColumnsToDBTable</code>,
     *     <code>addMultipleColumnsToDBTable</code> facilitate adding
     *     DBColumns.
     * </p>
     *
     * @param table_name    The name, as a String, adhering to SQLIte
     *                      naming conventions.
     */
    public DBTable(String table_name) {
        this.usable = false;
        this.table_name = table_name.toLowerCase();
        this.table_columns = new ArrayList<>();
        this.problem_msg = "\nWDBT0005 - Partially Instantiated - " +
                "Use addDBColumnToDBTable to add at least 1" +
                " usable DBColumns or " +
                "Use addDBColumnsToDBTable to add at least 1" +
                " usable DBColumn or " +
                "Use AddMultipleColumnsToDBTable to add at least 1" +
                " usable DBColumn. " +
                "Note any unusable DBColumn will render table unusable. " +
                "Caller=DBTable (Table Name only Constructor)";
        if(table_name.length() < 1) {
            this.problem_msg = this.problem_msg + "\nWDTB0006 - " +
                    "Invalid Table Name - Must be at least 1 " +
                    "character in length. " +
                    "Caller=(DBTable (table_name))";
        }
    }

    /**************************************************************************
     * Constructs a DBTable with a single DBColumn
     *
     * <p>
     *     Methods <code>addDBColumntoDBTable</code>,
     *     <code>addDBColumnsToDBTable</code>,
     *     <code>addMultipleColumnsToDBTable</code> facilitate adding
     *     DBColumns.
     * </p>
     *
     * @param table_name    The name, as a String, adhering to SQLIte
     *                      naming conventions.
     * @param table_column  The DBColumn to be assigned to the table and thus
     *                      to be the only column of the table; unless
     *                      more DBColumns are subsequently added.
     *
     */
    public DBTable(String table_name, DBColumn table_column) {
        this();
        this.table_name = table_name;
        this.table_columns.add(table_column);
        this.problem_msg = "";
        this.checkDBTableIsUsable("DBTable (table_name, table_column (singular))");
    }

    /**************************************************************************
     * Constructs a DBTable with one or more DBCOLumns.
     *
     * @param table_name    The name, as a String, adhering to SQLIte
     *                      naming conventions.
     * @param table_columns A DBColumn ArrayList for the columns to be
     *                      assigned to the table.
     */
    public DBTable(String table_name, ArrayList<DBColumn> table_columns) {
        this();
        this.problem_msg = "";
        this.table_name = table_name;
        this.table_columns = table_columns;
        this.checkDBTableIsUsable("DBTable Full Constructor");
    }

    /**
     * Look for duplicate column names, if found add to problem message
     * and flag DBTable as unusable.
     */
    private void findDuplicateColumns() {
        // no or 1 column cannot have duplicates so done
        if (this.table_columns.size() < 2) {
            return;
        }
        ArrayList<Integer> needlematches = new ArrayList<>();
        ArrayList<Integer> haystackmatches = new ArrayList<>();
        int i =0;
        for (DBColumn needlecolumn : this.table_columns) {
            int j = 0;
            for (DBColumn haystackcolumn : this.table_columns) {
                //Skip checking the column against itself
                if (j == i) {
                    j++;
                    continue;
                }

                //Skip checking already matched columns
                boolean alreadymatched = false;
                for (int k = 0; k < needlematches.size(); k++) {
                    if (i == haystackmatches.get(k) && j == needlematches.get(k) ) {
                        alreadymatched = true;
                    }
                }
                if (alreadymatched) {
                    j++;
                    continue;
                }

                if (needlecolumn.getDBColumnName().equals(
                        haystackcolumn.getDBColumnName()
                )) {
                    needlematches.add(i);
                    haystackmatches.add(j);
                    this.problem_msg = this.problem_msg +
                            "\nEDBT0015 - Duplicated Column - " +
                            "Column=" + needlecolumn.getDBColumnName() +
                            " col(" +
                            Integer.toString(i) +
                            ") is the same as " +
                            "Column=" + haystackcolumn.getDBColumnName() +
                            " col(" +
                            Integer.toString(j) + ")";
                    this.usable = false;
                }
                j++;
            }
            i++;
        }
    }

    /**************************************************************************
     * Adds a DBColumn to the DBTable.
     *
     * @param dbcolumn  The DBColumn to add to the DBTable.
     */
    public void AddDBColumnToDBTable(DBColumn dbcolumn) {
        this.table_columns.add(dbcolumn);
        this.problem_msg = "";
        this.checkDBTableIsUsable("AddDBColumnToDBTable");
    }

    /**************************************************************************
     * Adds  multiple DBColumns to the DBTable.
     *
     * @param dbcolumns The DBColumn ArrayList containing the DBCOlumns to be
     *                  added to the DBTable.
     */
    public void AddDBColumnsToDBTable(ArrayList<DBColumn> dbcolumns) {
        this.table_columns.addAll(dbcolumns);
        this.problem_msg = "";
        this.checkDBTableIsUsable("AddDBColumnsToDBtable");
    }

    /**************************************************************************
     * Add multiple DBColumns to the DBTable
     * (psuedonym for AddDBColumnsToDBTable).
     *
     * @param dbcolumns The DBColumn ArrayList containing the DBCOlumns to be
     *                  added to the DBTable.
     */
    public void AddMultipleColumnsToDBTable(ArrayList<DBColumn> dbcolumns) {
        this.AddDBColumnsToDBTable(dbcolumns);
    }

    /**************************************************************************
     * Sets the DBTable's table name.
     *
     * @param table_name The name, as a String, adhering to SQLIte
     *                      naming conventions.
     */
    public void setDBTableName(String table_name) {
        this.table_name = table_name.toLowerCase();
        this.problem_msg = "";
        this.checkDBTableIsUsable("setDBTableName");
    }

    /**************************************************************************
     * Gets the DBTable's table name.
     *
     * @return The table name as a String.
     */
    public String getDBTableName() { return this.table_name; }

    /**************************************************************************
     * Gets the usability status of the DBTable.
     *
     * @return The usability status, as a boolean, true indicates that
     * the DBTable is usable, false that it is not.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDBTableUsable() { return this.usable; }

    /**************************************************************************
     * Gets the number of DBColumns currently assigned to the DBTable.
     *
     * @return The number of DBColumns in the DBTables table_columns
     * DBColumns ArrayList.
     */
    public int numberOfColumnsInTable() { return this.table_columns.size(); }

    /**************************************************************************
     * Gets the DBColumns assigned to the DBTable.
     *
     * @return The DBTable's DBColumn ArrayList.
     */
    public ArrayList<DBColumn> getTableDBColumns() {
        return this.table_columns;
    }

    /**************************************************************************
     * Gets the DBTables problem msg.
     *
     * @return The DBTable's problem msg as a String
     */
    public String getDBTableProblemMsg() { return this.problem_msg; }

    /**************************************************************************
     * Gets the DBTable's problem message along with the underlying
     * problem messages from the DBColumns assigned to the DBTable.
     *
     * @return A string with all of the problem messages fron the DBTable and
     * the DBColumns assigned to the DBTable.
     */
    public String getAllDBTableProblemMsgs() {
        String problem_messages = this.getDBTableProblemMsg();
        for(DBColumn tc : this.table_columns) {
            problem_messages = problem_messages + tc.getDBColumnProblemMsg();
        }
        return  problem_messages;
    }

    /**************************************************************************
     * Check the DBTable's usability status of the DBTable including  calling
     * <code>anyEmptyDBColumnsInDBTable</code>, which checks for no DBColumns
     * and also for the usability status of existing DBColumns.
     *
     * @param caller The label of the invoking routine, to assist in locating
     *               any issues.
     * @return  the usability status (expected to often not be used).
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean checkDBTableIsUsable(String caller) {
        this.usable = false;
        if(this.anyEmptyDBColumnsInDBTable(caller)) {
            this.usable = true;
            if (this.table_name.length() < 1) {
                this.problem_msg = this.problem_msg +
                        "\nEDBT0009 - Inavlid Table Name - " +
                        "Must be at least 1 character in length. " +
                        "Caller=(" + caller + ")";
                this.usable = false;
            }
        }
        this.findDuplicateColumns();
        return this.usable;
    }

    /**************************************************************************
     * Checks for no DBColumns being assigned to the DBTable and also for
     * any DBColumns that sre unusable.
     *
     * @param caller The label of the invoking routine, to assist in locating
     *               any issues.
     * @return the usability status of the DBTable.
     */
    public boolean anyEmptyDBColumnsInDBTable(String caller) {
        boolean rv = true;
        if(this.table_columns.isEmpty()) {
            this.problem_msg = this.problem_msg + "\nEDBT0007 - No Columns - " +
                    "Must have at least 1 Column in the Table. " +
                    "Caller=(" + caller + ")";
            this.usable = false;
            return false;
        }
        for(DBColumn tc : this.table_columns) {
            // Set the DBColumn's Fully Qualified Name i.e. table.column
            tc.setFullyQualifiedColumnName(this.table_name);
            if(!tc.isDBColumnUsable()) {
                this.problem_msg = this.problem_msg + "\nEDBT0008 - Column " + tc.getDBColumnName() +
                        " is unusable. Must be usable. " +
                        "Caller=(" + caller + ")";
                rv = false;
            }
        }
        return rv;
    }

    /**************************************************************************
     * Gets the DBTable's SQL creation statement but only if the SQLite
     * database table doesn't exist (not overly important as SQL statements
     * are in the form CREATE TABLE IF NOT EXISTS).
     *
     * @param db The SQLite database to inspect.
     * @return the sql create string, empty String if the SQLite database
     * contains the table.
     */
    public String getSQLCreateString(SQLiteDatabase db) {

        // Check to see if this table exists, if not then skip CREATE
        String where = SQLITEMASTERCOLUMN_TYPE +
                "=? " +
                SQLAND +
                SQLITEMASTERCOLUMN_NAME +
                "!=?";
        String[] whereargs = {"table",SQLITEMASTERNAME_METADATA};
        Cursor csr_mstr = db.query(SQLITEMASTERTABLE,
                new String[]{SQLITEMASTERCOLUMN_NAME},
                where,
                whereargs,
                null,
                null,
                SQLITEMASTERCOLUMN_NAME
        );
        boolean table_exists = false;

        while(csr_mstr.moveToNext()) {
            if(this.table_name.equals(csr_mstr.getString(0))) {
                table_exists = true;
            }
        }
        // Finished with master
        csr_mstr.close();
        // if the table exists then provide no SQL (changes should be done via Alter)
        if(table_exists) {
            return "";
        }

        // Extract Columns that are flagged as PRIMARY INDEXES so we have a count
        // More than one has to be handled differently
        ArrayList<String> indexes = new ArrayList<>();
        for(DBColumn dc : this.table_columns) {
            if(dc.getDBColumnIsPrimaryIndex()) {
                indexes.add(dc.getDBColumnName());
            }
        }
        // Build the CREATE SQL
        String part1 = SQLCREATE +
                SQLTABLE +
                SQLIFNOTEXISTS +
                this.table_name +
                " (";
        int dccount = 0;
        // Main Loop through the columns
        for(DBColumn dc : this.table_columns) {
            part1 = part1 + dc.getDBColumnName() + " " + dc.getDBColumnType() + " ";
            // Apply the default value if required
            if(dc.getDBColumnDefaultValue() != null && dc.getDBColumnDefaultValue().length() > 0) {
                part1 = part1 +
                        SQLDEFAULT +
                        dc.getDBColumnDefaultValue() + " ";
            }
            // if only 1 PRIMARY INDEX and this is it then add it
            if(dc.getDBColumnIsPrimaryIndex() & indexes.size() == 1) {
                part1 = part1 + SQLPRIMARYKEY;
            }
            // If more to do then include comma separator
            dccount++;
            if (dccount < this.table_columns.size()) {
                part1 = part1 + ", ";
            }
        }
        // Handle multiple PRIMARY INDEXES ie add PRIMARY KEY (<col>, <col> .....)
        int ixcount = 1;
        if(indexes.size() > 1 ) {
            part1 = part1 + SQLPRIMARYKEYSTART;
            for(String ix : indexes) {
                part1 = part1 + ix;
                if(ixcount < (indexes.size() ) ) {
                    part1 = part1 + ", ";
                }
                ixcount++;
            }
            part1 = part1 + SQLPRIMARYKEYEND;
        }
        part1 = part1 + ") ;";
        return part1;
    }

    /**************************************************************************
     * Gets SQL table create as string, this used for export rather than
     * internal use; hence no usability checking.
     *
     * @param doasmysql flag, if true indicates that the generated SQL is for
     *                  use by MYSQL (and perhaps other DBM's)
     * @return the sql table create as string
     */
    public String getSQLTableCreateAsString(@SuppressWarnings("SameParameterValue") Boolean doasmysql) {

        // Extract Columns that are flagged as PRIMARY INDEXES so we have a count
        // More than one has to be handled differently
        ArrayList<String> indexes = new ArrayList<>();
        for(DBColumn dc : this.table_columns) {
            if(dc.getDBColumnIsPrimaryIndex()) {
                indexes.add(dc.getDBColumnName());
            }
        }
        // Build the CREATE SQL
        String part1 = SQLCREATE +
                SQLTABLE +
                SQLIFNOTEXISTS +
                "'" +
                this.table_name +
                "' (";
        int dccount = 0;
        // Main Loop through the columns
        for(DBColumn dc : this.table_columns) {
            // FOR mysql export need to use BIGINT(20) instead of INTEGER
            if(doasmysql && dc.getDBColumnType().equals(SQLINTEGER)) {
                part1 = part1 + "`" + dc.getDBColumnName() + "` BIGINT(20) NOT NULL ";
            } else {
                part1 = part1 + "`" + dc.getDBColumnName() + "` " + dc.getDBColumnType() + " ";
            }

            // Apply the default value if required
            if(dc.getDBColumnDefaultValue().length() > 0 ) {
                part1 = part1 + SQLDEFAULT + dc.getDBColumnDefaultValue() + " ";
            }
            // if only 1 PRIMARY INDEX and this is it then add it
            if(dc.getDBColumnIsPrimaryIndex() & indexes.size() == 1) {
                part1 = part1 + SQLPRIMARYKEY;
            }
            // If more to do then include comma separator
            dccount++;
            if (dccount < this.table_columns.size()) {
                part1 = part1 + ", ";
            }
        }
        // Handle multiple PRIMARY INDEXES ie add PRIMARY KEY (<col>, <col> .....)
        int ixcount = 1;
        if(indexes.size() > 1 ) {
            part1 = part1 + SQLPRIMARYKEYSTART;
            for(String ix : indexes) {
                part1 = part1 + ix;
                if(ixcount < (indexes.size() ) ) {
                    part1 = part1 + ", ";
                }
                ixcount++;
            }
            part1 = part1 + SQLPRIMARYKEYEND;
        }
        part1 = part1 + ") ;";
        return part1;
    }

    /**************************************************************************
     * Gets SQL alter statements to add new columns to the SQLite table.
     *
     * <p>
     *     Note that this uses the SQLITE PRAGMA to get the table's column
     *     information and then it
     * </p>
     *
     * @param db The SQLite database to inspect.
     * @return the SQL statements to add new columns as a String ArrayList,
     * there could be no elements.
     */
    public ArrayList<String> getSQLAlterToAddNewColumns(SQLiteDatabase db) {
        // Have to return an array (arraylist) as ALTER statements can only Add 1 column at a time.
        ArrayList<String> result = new ArrayList<>();

        // Prepare to get the current database table information
        // i.e columns in the table via PRAGMA.
        String sqlstr = " PRAGMA table_info (" + this.table_name + ")";
        Cursor csr = db.rawQuery(sqlstr, null);

        // Check to see if this table exists, if not then cannot ALTER anything
        // Should never happen if the method actionDBAlterSQL (method that
        // invokes this method) is preceeded by actionDBBuildSQL, as that
        // should create any tables that don't exist.
        String where = SQLITEMASTERCOLUMN_TYPE +
                "=?" +
                SQLAND +
                SQLITEMASTERCOLUMN_NAME +
                "!=?";
        String[] whereargs = {SQLITEMASTERTYPE_TABLE,
                SQLITEMASTERNAME_METADATA};

        //String sqlstr_mstr = "SELECT name FROM sqlite_master WHERE type = 'table' AND name!='android_metadata' ORDER by name;";
        //Cursor csr_mstr = db.rawQuery(sqlstr_mstr,null);
        Cursor csr_mstr = db.query(SQLITEMASTERTABLE,
                new String[]{SQLITEMASTERCOLUMN_NAME},
                where,
                whereargs,null,null,SQLITEMASTERCOLUMN_NAME);
        boolean table_exists = false;

        while(csr_mstr.moveToNext()) {
            if(this.table_name.equals(csr_mstr.getString(0))) { table_exists = true; }
        }
        csr_mstr.close();
        // If the table doesn't exist then return the empty String ArrayList.
        if(!table_exists) {
            csr.close();
            return result;
        }

        // Tables does exist, so loop through all the DBColumns (the poitentiall
        // new ccolumns)
        for(DBColumn dc : this.table_columns) {
            String columntofind = dc.getDBColumnName();
            boolean columnmatch = false;

            // ensure that the cursor is positioned before the first row
            csr.moveToPosition(-1);
            // Check to see if the current column exists, if it doesn then
            // nothing will be done for this column.
            while(csr.moveToNext()) {
                if(csr.getString(1).equals(columntofind)) {
                    columnmatch = true;
                }
            }

            // Column not found so create SQL alter statement to add the
            // column to the table.
            if(!columnmatch) {
                String altersql = SQLALTERTABLE +
                        this.table_name +
                        SQLADDCOLUMN +
                        dc.getDBColumnName() + " " +
                        dc.getDBColumnType() + " ";
                if(dc.isDBColumnPrimaryIndex()) {
                    altersql = altersql + SQLPRIMARYINDEX;
                }
                if(dc.getDBColumnDefaultValue().length() > 0 ) {
                    altersql = altersql + SQLDEFAULT + dc.getDBColumnDefaultValue() + " ";
                }
                altersql = altersql + " ; ";
                result.add(altersql);
            }
        }
        csr.close();
        return result;
    }
}

package mjt.dbdatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import mjt.dbcolumn.DBColumn;
import mjt.dbindex.DBIndex;
import mjt.dbtable.DBTable;

/**
 * * DBTDatabase a component of the DB???? classes that represents an SQLite
 * Database.
 *
 * <p>
 *     A DBDatabase is one of a number of DB???? classes (DBDatabase, DBTable,
 *     DBCColumn and DBIndex) designed for assisting with the creation
 *     and alteration of SQLite Databases.
 * </p>
 *
 * <p>
 *     A DBDatabase can be considered as the base schema for the SQLite
 *     database.
 *     Methods of the DBDatabase will provide the SQL to create, modify
 *     or export the Database.
 * </p>
 * <p>
 *     <code>generateDBBuildSQL</code> will return a String ArrayList of
 *     SQL statements to build the SQLite Database.
 * </p>
 * <p>
 *     <code>actionBuildSQL</code> calls <code>generateBuildSQL</code>
 *     and actions the SQL statements building the SQLite database.
 * </p>
 * <p>
 *     <code>generateAlterSQL</code> will build the SQL statements to
 *     alter the SQLite database, based upon changes made to the
 *     DBDatabase. New columns and/or new or changed Indexes are catered
 *     for. Note! removal of columns is not directly supported so old
 *     or redundant columns are not removed. tables are not catered for
 *     as these are catered for by the Build, which should precede an
 *     Alter.
 * </p>
 * <p>
 *     <code>actionAlterSQL</code> calls <code>generateAlterSQL</code>
 *     and then actions the SQL statements, altering the SQLite database.
 * </p>
 *
 * <p>
 *     <b>DBDatabase dependencies on other DB???? classes/objects.</b>
 * </p>
 * <p>
 *     A DBDatabase requires at least one DBTable, and thus as a DBTable
 *     requires at least one DBColumn their is a hierarchical requirement
 *     of at least one DBColumn. A DBDatabase can have DBIndex's each of
 *     which requires a DBTable and at least one DBColumn.
 * </p>
 *
 *      <b>Usability.</b>
 * </p>
 * <p>
 *     The construction of a DBDatabase instance includes checking that the
 *     name is at least 1 character in length and that it is usable. As
 *     each DBTable can only be usable if it's DBColumns are all usable,
 *     then DBColumns that are not usable will also result in the
 *     DBDatabase being unusable. The same goes for DBIndexes, that is,
 *     an unusable DBIndex will also make the DBDatabase unusable.
 * </p>
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused", "UnusedReturnValue"})
public class DBDatabase {
    /**
     * A boolean that indicates if the DBDatabase is usable (true)
     * or not (false).
     */
    private boolean usable;
    /**
     * A String that will be the name of the actual database.
     *
     * <p>
     *     The name should not include any whitespace and adhere to SQLite
     *     naming rules.
     * </p>
     */
    private String database_name;
    /**
     * A String ArrayList of DBTable that the database will be comprised
     * of.
     * <p>At least on DBTable is required. DBColumns are determined from
     * the DBTables. For the DBDatabase to be usable all DBTables and
     * DBColumns must themselves be usable.</p>
     *
     */
    private ArrayList<DBTable> database_tables;
    /**
     * A String ArrayList of 0 or more DBIndexes.
     *
     * <p>
     *     DBIndexes are optional. However, if any DBIndexes are specified then
     *     they and any underlying DBColumns must be usable for the DBDatabase
     *     to be usable.
     * </p>
     */
    private ArrayList<DBIndex> database_indicies;
    /**
     * An internally created string that will contain issues found
     * when instantiating or modifying the DBDatabase.&nbsp;It would only be
     * set if the usable flag is set to false.
     */
    private String problem_msg;

    /**************************************************************************
     * Constructs an unusable DBDatabase with problem message set.
     */
    public DBDatabase() {
        this.usable = false;
        this.database_name = "";
        this.database_tables = new ArrayList<>();
        this.database_indicies = new ArrayList<>();
        this.problem_msg = "WDBD0100 - Uninstantiated - " +
                "Use setDBDatabaseName to set the Database Name. " +
                "Use addDBTableToDBDatabase to add at least 1 Table or " +
                "Use addDBTablesToDBDatabase to add at least 1 Table or " +
                "Use addMultipleTablesToDBDatabase to add at least 1 Table";
    }

    /**************************************************************************
     * Constructs an unusable DBDatabase (call to default constructor)
     * with the name for the SQLite Database.
     *
     * @param dbname    the database name
     */
    public DBDatabase(String dbname) {
        this();
        this.database_name = dbname;
        this.problem_msg = "WDBD0101 - Partially Instantiated -  " +
                "Use addDBTableToDBDatabase to add at least 1 Table or " +
                "Use addDBTablesToDBDatabase to add at least 1 Table or " +
                "Use addMultipleTablesToDBDatabase to add at least 1 Table";
    }

    /**************************************************************************
     * Constructs a DBDatabase with a single DBTable and thus would create
     * an SQLite Database with a single table, unless subsequently modified.
     *
     * @param dbname    The name to be given to the SQLite database.
     * @param table     The DBTable to be applied to the DBDatabase.
     */
    public DBDatabase(String dbname, DBTable table) {
        this();
        this.database_name = dbname;
        this.problem_msg = "";
        this.database_tables.add(table);
        this.checkDBDatabaseIsUsable("DBDatabase (dbname, table)");
    }

    /**************************************************************************
     * Constructs a DBDatabase with multiple DBTables (but no Indexes).
     *
     * @param dbname    The name to be given to the SQLite database.
     * @param tables    A DBTable ArrayList of the DBTables to be applied to
     *                  the DBDatabase.
     */
    public DBDatabase(String dbname, ArrayList<DBTable> tables) {
        this();
        this.database_name = dbname;
        this.problem_msg = "";
        this.database_tables.addAll(tables);
        this.checkDBDatabaseIsUsable("DBDatabase Full Constructor");
    }

    /**************************************************************************
     * Constructs a DBDatabase with Database name a single table and
     * 1 or more indexes from a DBIndex ArrayList
     * @param dbname    The name to be given to the SQLite database.
     * @param table     The DBTable to be applied to the DBDatabase..
     * @param indicies  A DBIndex ArrayList providing the DBIndexes that will
     *                  be applied to the DBDatabase.
     */
    public DBDatabase(String dbname,
                      DBTable table,
                      ArrayList<DBIndex> indicies) {
        this(dbname,
                new ArrayList<>(Collections.singletonList(table)),
                indicies
        );
    }

    /**************************************************************************
     * Constructs a DBDatabase with Database name, 1 or more DBTables and
     * with 1 DBIndex
     * @param dbname    The name to be given to the SQLite database.
     * @param tables    A DBTable ArrayList of the DBTables to be applied to
     *                  the DBDatabase.
     * @param index     A DBIndex to be applied to the DBDatabase.
     */
    public DBDatabase(String dbname, ArrayList<DBTable> tables, DBIndex index) {
        this(dbname,
                tables,
                new ArrayList<>(Collections.singletonList(index))
        );
    }

    /**************************************************************************
     * Constructs a DBDatabase with 1 DBTable and 1 DBIndex
     * @param dbname    The name to be given to the SQLite database.
     * @param table     The DBTable to be applied to the DBDatabase.
     * @param index     The DBIndex to applied to the DBDatabase.
     */
    public DBDatabase(String dbname, DBTable table, DBIndex index) {
        this(dbname,
                new ArrayList<>(Collections.singletonList(table)),
                new ArrayList<>(Collections.singletonList(index))
        );
    }

    /**************************************************************************
     * Constructs a DBDatabase with multiple DBTables and multiple DBIndexes.
     * @param dbname    The name to be given to the SQLite database.
     * @param tables    A DBTable ArrayList of the DBTables to be applied to
     *                  the DBDatabase.
     * @param indicies  A DBIndex ArrayList providing the DBIndexes that will
     *                  be applied to the DBDatabase.
     */
    public DBDatabase(String dbname,
                      ArrayList<DBTable> tables,
                      ArrayList<DBIndex> indicies) {
        this();
        this.database_name = dbname;
        this.problem_msg = "";
        this.database_tables.addAll(tables);
        this.database_indicies.addAll(indicies);
        this.checkDBDatabaseIsUsable("DBDatabase (dbname, tables, indicies)");
    }

    /**************************************************************************
     * Gets the usability status of the DBDatabase.
     *
     * @return A boolean, true if the DBDatabase is usable, else false if not.
     */
    public boolean isDBDatabaseUsable() { return this.usable; }

    /**************************************************************************
     * Gets the number of Tables in the DBDatabase.
     *
     * @return A long containing the number of DBTable objects defined to the
     * DBDatabase.
     */
    public long numberOfTablesInDBDatabase() {
        return this.database_tables.size();
    }

    /**************************************************************************
     * Sets DBDatabase's name.
     *
     * @param database_name The name, as a String, that will be given to the
     *                      SQLite database.
     */
    public void setDBDatabaseName(String database_name) {
        this.database_name = database_name;
    }

    /**************************************************************************
     * Gets the name that will be given to the SQLite database.
     *
     * @return A String containing the name that will be given to the SQLite
     * database.
     */
    public String getDBDatabaseName() { return this.database_name; }

    /**************************************************************************
     * Gets the DBDatabase's problem message (not underlying problem messages)
     *
     * @return  A string with the DBDatabase's problem message.
     */
    public String getDBDatabaseProblemMsg() { return this.problem_msg; }

    /**************************************************************************
     * Gets all of the DBDatabase problem messages.
     *
     * @return A String containing all problem messages for the DBDatabase and
     * it's children.
     */
    public String getAllDBDatabaseProblemMsgs() {
        String problem_messages = this.getDBDatabaseProblemMsg();
        for(DBTable dt : this.database_tables) {
            problem_messages = problem_messages +
                    dt.getAllDBTableProblemMsgs();
        }
        return  problem_messages;
    }

    /**************************************************************************
     * Gets the table names from the list of DBTables assigned to the
     * DBDatabase.
     *
     * @return  String ArrayList of all the names from the DBTables assigned to
     * the DBDatabase.
     */
    public ArrayList<String> getTables() {
        ArrayList<String> rv = new ArrayList<>();

        for (DBTable table : database_tables) {
            rv.add(table.getDBTableName());
        }
        return rv;
    }

    /**************************************************************************
     * Adds a DBTable to DBDatabase.
     *
     * @param database_table The DBTable to add to the DBDatabase.
     */
    public void addDBTableToDBDatabase(DBTable database_table) {
        this.database_tables.add(database_table);
        this.problem_msg = "";
        this.checkDBDatabaseIsUsable("AddDBTableToDBDatabase");
    }

    /**************************************************************************
     * Adds 1 or more DBTables to the DBDatabase.
     *
     * @param database_tables The DBTable ArrayList containing the DBTables
     *                        to be added to the DBDatabase.
     */
    public void addDBTablesToDBDatabase(ArrayList<DBTable> database_tables) {
        this.database_tables.addAll(database_tables);
        this.problem_msg = "";
        this.checkDBDatabaseIsUsable("AddDBTablesToDBDatabase");
    }

    /**************************************************************************
     * Sets the usable status of the DBDatabase.
     *
     * @param caller A label, as a String, to assist in the location of the
     *               method's caller.
     * @return A boolean reflecting the determined status of the DBDatabase,
     * true if it is usable, false if not.
     */
    public boolean checkDBDatabaseIsUsable(String caller) {
        this.usable = false;
        if(this.anyEmptyDBTablesInDBDatabase(caller)) {
            this.usable = true;
            if (this.database_name.length() < 1) {
                this.problem_msg = this.problem_msg + "\nEDBD0105 - Inavlid Table Name - " +
                        "Must be at least 1 character in length. " +
                        "Caller=(" + caller + ")";
                this.usable = false;
            }
        }
        if(this.anyUnusableIndiciesInDBDatabase(caller)) {
            this.usable = false;
        }
        return this.usable;
    }

    /**************************************************************************
     * Checks for any DBTables that have an empty name and that the DBTables
     * are usable.
     *
     * @param caller A label, as a String, to assist in the location of the
     *               method's caller.
     * @return A boolean, true if the DBTables have a name and are usable,
     * otherwise false.
     */
    public boolean anyEmptyDBTablesInDBDatabase(String caller) {
        boolean rc = true;
        if(this.database_tables.isEmpty()) {
            this.problem_msg = this.problem_msg +
                    "\nEDBD0103 - No Tables - " +
                    "Must have at least 1 Table in the Database. " +
                    "Caller=(" + caller + ")";
            this.usable = false;
            return false;
        }
        for(DBTable dt : this.database_tables) {
            if(!dt.isDBTableUsable()) {
                this.problem_msg = this.problem_msg +
                        "\nEDBT0104 - Table " +
                        dt.getDBTableName() +
                        " is unusable. Must be usable. " +
                        "Caller=(" + caller + ")";
                rc = false;
            }

        }
        return rc;
    }

    /**************************************************************************
     * Check for any unusable Indexes.
     *
     * @param caller A label, as a String, to assist in the location of the
     *               method's caller.
     * @return  A boolean, true if any unusable indicies, else false.
     */
    public boolean anyUnusableIndiciesInDBDatabase(String caller) {
        boolean rv = false;
        for (DBIndex ix : this.database_indicies) {
            if (!ix.isUsable()) {
                this.problem_msg = this.problem_msg +
                        "\nEDBD106 - Unusable Index - " +
                        "Index " +
                        ix.getIndexName() +
                        " is marked as unusable. Issue(s) " +
                        ix.getDBIndexProblemMsg() +
                        " Caller(" + caller + ")";
                rv = true;
            }
        }
        return rv;
    }

    /**************************************************************************
     * Generate SQLite database build sql array list.
     *
     * @param db The SQLite database to inspect
     * @return A String ArrayList of the SQL statements needed to build the
     * SQLite database.
     */
    public ArrayList<String> generateDBBuildSQL(SQLiteDatabase db) {
        ArrayList<String> generatedSQLStatements = new ArrayList<>();
        for(DBTable dbt : this.database_tables) {
            String current_create_string = dbt.getSQLCreateString(db);
            if(current_create_string.length() > 0) {
                generatedSQLStatements.add(current_create_string);
            }
        }
        for(DBIndex dbi : this.database_indicies) {
            String ixcreate = dbi.getIndexBuildSQL(db);
            if (ixcreate.length() > 0) {
                generatedSQLStatements.add(ixcreate);
            }
        }
        return generatedSQLStatements;
    }

    /**************************************************************************
     * Generate export schema sql string.
     *
     *  <p>
     *     Note this is not really or core/important feature, rather it may be
     *     potentially useful on occasions.
     * </p>
     *
     * @return A string containing commented SQL statements with USE statements
     * that could be used to build the database elsewhere (e.g. by MYSQL).
     *
     */
    public String generateExportSchemaSQL() {
        String sql = "";
        String tablesql;
        if(this.usable) {
            //sql = "--CRTDB_START\n";
            //sql = sql + " CREATE DATABASE IF NOT EXISTS `" + this.database_name + "` ;\n" +
            //        "--CRTDB_USESTART\n" +
            //        " USE `" + this.database_name + "`;" + "\n" ;
            //sql = sql + "--CRTDB_USEFINISH\n";
            for(DBTable dbt : this.database_tables) {
                tablesql = dbt.getSQLTableCreateAsString(true);
                if(tablesql.length() > 0) {
                    sql = sql + "--CRTTB_START\n" + tablesql + "\n--CRTTB_FINISH\n";
                }
            }
            //sql = sql + "--CRTDB_FINISH\n";
        }
        return sql;
    }

    /**************************************************************************
     * Generate export data sql string.
     *
     *  <p>
     *     Note this is not really or core/important feature, rather it may be
     *     potentially useful on occasions.
     * </p>
     *
     * @param db The SQLite database to be inspected and from which data is to
     *           be extracted.
     * @return A String containing SQL statements that may be used elsewhere to
     * create a copy of the SQLite database.&nbsp;Note! that it only has
     * rudimentary escaping in that apostrophes are changed to #@APOST@# and
     * quotes are changed to #@QUOTE@#.
     */
    // Export All Table Data (not expect to work as no escaping as yet)
    //TODO 1 Need to do equiv to MYSQL_REAL_ESCAPE otherwise OK load
    public String generateExportDataSQL(SQLiteDatabase db) {
        String sql = "";
        String sqlcols;
        Cursor csr;
        String coldata;
        if(!this.usable) { return sql; }
        //sql =
        //sql = "--TDL_USESTART\n USE `" + this.database_name + "`;\n--TDL_USEFINISH\n";
        for(DBTable dbt : this.database_tables) {
            // Select all rows from the respective table into Cursor csr.
            // TODO if run b4 build tables will not actually exist so need to skip if they don't
            csr = db.query(dbt.getDBTableName(),null,null,null,null,null,null);
            // Skip if no rows
            if(csr.getCount() > 0 ){
                int coli = 0;
                // Build Column list
                ArrayList<Integer> coltype = new ArrayList<>();
                sqlcols = "";
                for(DBColumn dbtc : dbt.getTableDBColumns()) {
                    if(coli++ > 0) {
                        sqlcols = sqlcols + ", ";
                    }
                    if(dbtc.getDBColumnType().equals("TEXT")) {
                        coltype.add(1);
                    } else {
                        coltype.add(0);
                    }
                    sqlcols = sqlcols + "`" + dbtc.getDBColumnName() + "` ";
                }
                // Process each row from the table
                csr.moveToPosition(-1);
                while(csr.moveToNext()) {
                    sql = sql + "--TBL_INSERTSTART\nINSERT INTO " + dbt.getDBTableName() + " ( " + sqlcols + ") VALUES(";
                    for(int i=0; i < coli;i++) {
                        coldata = csr.getString(i);
                        if(coldata == null) {coldata = ""; }
                        if(coltype.get(i) == 1) {
                            if(coldata.length() > 0) {
                                try {
                                    coldata = coldata.replaceAll("\'", "#@APOST@#")
                                            .replaceAll("\"", "#@QUOTE@#");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            sql = sql + "'" + coldata + "'";
                        } else {
                            sql = sql + csr.getString(i);
                        }
                        if(i < (coltype.size()-1)) {
                            sql = sql + ", ";
                        }
                    }
                    sql = sql + " );\n--TBL_INSERTFINISH\n";
                }
            } else {
                sql = sql + "-- ERROR - TABLE " + dbt.getDBTableName() + " IS EMPTY SKIPPED \n";
            }
            csr.close();
        }
        return sql;
    }

    /**************************************************************************
     * Actions the build of the SQLite Database using the SQL statements from
     * a call to <code>generateDBBuildSQL</code>.
     *
     * @param db The SQLite database to be built, noting that an already built
     *           database will either remain as it is or that any new tables or
     *           indexes will be added (i.e. it is safe to run and an
     *           expectation that it is run on application startup.)
     */
    // Obtain and the run the SQL statements to build the database
    public void actionDBBuildSQL(SQLiteDatabase db) {
        // Gets the SQL statements
        ArrayList<String> actionsql = new ArrayList<>(generateDBBuildSQL(db));
        // Begin a Transaction
        db.beginTransaction();
        for(String currentsql : actionsql) {
            // Run the current SQL statement
            db.execSQL(currentsql);
        }
        // Set and End the Transaction
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**************************************************************************
     * Generates the SQL to apply additional columns or changed
     * indexes to the SQLite database.
     * <p>
     *     This DOES NOT apply changes.
     * </p>
     *
     * @param db The SQLite database to be inspected and potentially changed.
     * @return A String ArrayList of SQL statements, if any, of the changes
     * that would be applied to the SQLite database after comparison between the
     * DBDatabase and the actual SQLite database.
     */
    // Generate the SQL Alter statements for the tables and Indexes.
    public ArrayList<String> generateDBAlterSQL(SQLiteDatabase db) {
        ArrayList<String> generatedSQLStatements = new ArrayList<>();
        // Tables
        for(DBTable dbt: this.database_tables) {
            generatedSQLStatements.addAll(dbt.getSQLAlterToAddNewColumns(db));
        }
        // Indexes
        for (DBIndex dbi: this.database_indicies) {
            generatedSQLStatements.addAll(dbi.generateAlterSQL(db));
        }
        return generatedSQLStatements;
    }

    /**************************************************************************
     * Actions the alteration, if any, of the SQLite database by applying the
     * SQL statements generated by <code>generateAlterSQL</code>
     *
     * @param db the db
     */
    public void actionDBAlterSQL(SQLiteDatabase db) {
        ArrayList<String> actionsql = new ArrayList<>(generateDBAlterSQL(db));
        db.beginTransaction();
        for(String currentsql : actionsql) {
            db.execSQL(currentsql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}

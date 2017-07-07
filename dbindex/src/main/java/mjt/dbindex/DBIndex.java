package mjt.dbindex;

import static mjt.sqlwords.SQLKWORD.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import mjt.dbcolumn.DBColumn;
import mjt.dbtable.DBTable;

/**
 * Component of the DB???? classes representing a Database Index.
 *
 * <p>
 *     A DBIndex is one of a number of DB???? classes (DBDatabase, DBTable,
 *     DBColumn and DBIndex) designed for assisting with the creation
 *     and alteration of SQLite Databases.
 *
 * </p>
 * <p>
 *     <b>DBIndex dependencies on other DB???? classes/objects.</b>
 * </p>
 * <p>
 *     A DBTable instance is required. This defines the table that the index
 *     will be for. One or more DBColumns are required. These define the
 *     columns of the index. The columns MUST be columns of the table.
 *
 * </p>
 * <p>
 *     <b>Usability.</b>
 * </p>
 * <p>
 *     Construction of a DBIndex instance includes some checking which will
 *     result in a flag being set to reflect whether or not the DBIndex
 *     is <b>usable</b>. The usability will be propagated to the owning
 *     DBDatabase, thus marking it as unusable.
 * </p>
 * <p>
 *     The reason(s) for a DBIndex being flagged as unusable are held and can
 * be obtained via the <code>getProblemMessage</code> method. The usability
 * flag can also be obtained, this via the <code>isUsable</code> method.
 * </p>
 * <p>
 *     <b>Class Members.</b>
 * </p>
 * <p>
 *     <b>usable</b>
 *     Boolean - Indicates whether nor not the DBIndex can be used.
 * </p>
 * <p>
 *     <b>index_name</b> String - The name of the index.
 * </p>
 * <p>
 *     <b>full_index_name</b> String - The name of the index, prefixed with
 * the table name and a period.
 * </p>
 * <p>
 *     <b>owningtable</b> DBTable - The table the index is for.
 * </p>
 * <p>
 *     <b>columns</b> ArrayList<DBColumn> - The column or columns
 * the index is comprised off <i>(must be columns in the
 * <b>owningtable</b>)</i>
 * </p>
 * <p>
 *     <b>columnorders</b> ArrayList<Boolean> - A flag indicating a column's
 *     sort order, true is ascending, false descending. There must be as many
 *     elements in this ArrayList as there are elements in the <b>columns</b>
 *     ArrayList.
 * </p>
 * <p>
 *     <b>indexcreateSQL</b> String - The SQL that will create the Index.
 * </p>
 * <p>
 *     <b>problem_msg</b> String - The issues determined by the checking.
 * </p>
 * <p>
 *     <b>unique</b> Boolean - Indicator as to whether or not this index
 *     is a unique index, true if unique, false if not.
 * </p>
 *
 *  @author MJT
 **/
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DBIndex {

    private boolean usable;
    private String index_name;
    private String full_index_name;
    private DBTable owningtable;
    private ArrayList<DBColumn> columns;
    private ArrayList<Boolean> columnorders;
    private String indexcreateSQL;
    private String problem_msg;
    private Boolean unique;
    private static final String callmodeprefix = "Constructed using";

    /**************************************************************************
     * Constructs a DBIndex from Multiple Columns and Multiple Indexes.
     *
     * @param indexname         Names the Index
     * @param table             A DBTable that specifies the table the index
     *                          is for.
     * @param columns           An ArrayList of DBColumns, these specifying
     *                          the columns in the index.
     * @param ascendinglist     An ArrayList of Booleans that indicate the
     *                          sort order for the respective column. True
     *                          for ascending, false for descending. The number
     *                          of elements in the ArrayList MUST match the
     *                          number of elements in the columns ArrayList.
     * @param uniqueindex       true if the index is to have unique rows.
     */
    public DBIndex(String indexname,
                   DBTable table,
                   ArrayList<DBColumn> columns,
                   ArrayList<Boolean> ascendinglist,
                   boolean uniqueindex) {
        String callcode = callmodeprefix + " Multiple Columns";
        // Initialise the object
        initDBIndex(indexname,
                table,
                columns,
                ascendinglist,
                uniqueindex,
                callcode
        );
    }

    /**************************************************************************
     * Constructs a DBIndex from a Single Column and a Single Index.
     * @param indexname     Names the Index
     * @param table         A DBTable that specifies the table the index
     *                      is for.
     * @param column        A single DBColumn that specifies the column
     *                      used for the index.
     * @param ascending     The sort order of the column. True for ascending
     *                      false for descending.
     * @param unqiueindex   true if the index is to have unique rows.
     */
    public DBIndex(String indexname,
                   DBTable table,
                   DBColumn column,
                   boolean ascending,
                   boolean unqiueindex) {
        String callcode = callmodeprefix + " Single Column";
        initDBIndex(indexname,
                table,
                new ArrayList<>(Collections.singletonList(column)),
                new ArrayList<>(Collections.singletonList(ascending)),
                unqiueindex,
                callcode);
    }

    /**************************************************************************
     * Initialises the DBIndex object (called by the constructors)
     * @param indexname             The name of the Index
     * @param table                 The DBTable object for the index's table
     * @param columns               The list of DBColumn Objects
     * @param ascendingorderlist    The sort orders for the columns
     * @param uniqueindex           True if to make index unique, false if not
     * @param caller                The invoking/calling constructor
     */
    private void initDBIndex(String indexname,
                             DBTable table,
                             ArrayList<DBColumn> columns,
                             ArrayList<Boolean> ascendingorderlist,
                             Boolean uniqueindex,
                             String caller) {
        this.index_name = indexname;
        this.owningtable = table;
        this.full_index_name = table + "." + indexname;
        this.columns = columns;
        this.columnorders = ascendingorderlist;
        this.unique = uniqueindex;
        this.problem_msg = "";
        this.usable = false;
        // Undertake the checks
        checkDBIndexIsUsable(caller);
        this.indexcreateSQL = "";
        if (this.usable) {
            this.indexcreateSQL = generateCreateSQL();
        }
    }

    /**************************************************************************
     * Adds a column to the DBIndex.
     * @param column    The DbColumn to add to the DBIndex.
     * @param ascending The column order. True for Ascending, false for
     *                  descending.
     */
    public void addIndexColumn(DBColumn column, Boolean ascending) {
        this.columns.add(column);
        this.columnorders.add(ascending);
        checkDBIndexIsUsable("Add Column");
        if (this.usable) {
            this.indexcreateSQL = generateCreateSQL();
        } else {
            this.indexcreateSQL = "";
        }
    }

    /**************************************************************************
     * Generates the SQL to create the actual database index.
     * @return  The generated SQL as a string
     */
    private String generateCreateSQL() {

        String sql = SQLCREATE;
        if (this.unique) {
            sql = sql + SQLUNIQUE;
        }
        sql = sql + SQLINDEX +
                SQLIFNOTEXISTS +
                this.index_name + SQLON +
                this.owningtable.getDBTableName() + "(";
        int lenb4cols = sql.length();

        for (int i = 0; i < columns.size(); i++) {
            if (sql.length() > lenb4cols) {
                sql = sql + ", ";
            }
            sql = sql + columns.get(i).getDBColumnName();
            if (columnorders.get(i)) {
                sql = sql + SQLORDERASCENDING;
            } else {
                sql = sql + SQLORDERDESCENDING;
            }
        }
        sql = sql + ")";
        return sql;
    }

    /**************************************************************************
     * Gets the SQL to build the actual index, but only if the index doesn't
     * already exist in the actual database.
     *
     * @param db    The actual database as an SQLiteDatabase.
     * @return      The SQL string, empty if the actual index exists.
     */
    public String getIndexBuildSQL(SQLiteDatabase db) {
        // Check to see if the index exists (i.e. check the master table)
        // If it does exist then skip create build SQL for the index
        String wherestr = SQLITEMASTERCOLUMN_NAME +
                "=?" +
                SQLAND +
                SQLITEMASTERCOLUMN_TYPE +
                "=?";
        String[] whereargs = {this.getIndexName(), "index"};
        Cursor mstcsr = db.query(SQLITEMASTERTABLE,
                null,
                wherestr,
                whereargs,
                null,null,null);

        int indexcount = mstcsr.getCount();
        mstcsr.close();
        if (indexcount > 0) {
            return "";
        }
        return this.indexcreateSQL;
    }

    /**************************************************************************
     * Generates a String ArrayList of SQL statements that will align the
     * actual database index according to this DBIndex.
     * <P>If an index doesn't exist it will be added(normally done when
     * Building the Database).</P>
     * <p>If an index is different then it will be added, but after the actual
     * index is deleted (dropped).</p>
     * <p>Comparison is weak in that the SQL stored in the master table
     * is compared against the DBIndex's SQL as opposed to actually
     * checking the columns fron a PRAGMA index_list.</p>
     *
     * <p><b>Stages</b></p>
     * <p>Gets the real database index (named as per this DBIndex), if one,
     * from the master table.</p>
     * <p>If there isn't one then return the index create SQL (i.e.
     * member indexcreateSQL) so that the real index will be created.</p>
     * <p>If no</p>
     * <p>If there is a real index that matches the name of the DBIndex then
     * extract the SQL from the master table entry.</p>
     * <p>Tailor the DBIndex's SQL by removing IF NOT EXISTS and whitespaces.
     * Tailor the real index's SQL by removing whitespaces. Then compare the
     * two.</p>
     * <p>If the two tailored SQL statements match then return the empty
     * ArrayList. Otherwise, add an SQL statement to DROP the real database
     * followed by the DBINdex's SQL to create the real index.</p>
     *
     * @param db    The SQLite database, aka the real database, which is
     *              accessed to determine the actual current indexes for
     *              comparison against the pseudo schema of which DBIndexes
     *              are part. i.e. to determine what, if anything, needs
     *              altering to align the real database with the pseudo
     *              schema.
     * @return      A String ArrayList of 0 or more SQL statements; i.e
     *              the alterations to be made.
     */
    public ArrayList<String> generateAlterSQL(SQLiteDatabase db) {

        ArrayList<String> rv = new ArrayList<>();

        // Get the index's record from the sqlite master table to obtain the
        // actual index's CREATE SQL as recorded by sqlite
        String columns[] = {SQLITEMASTERCOLUMN_NAME,
                SQLITEMASTERCOLUMN_SQL};
        String where = SQLITEMASTERCOLUMN_TYPE +
                "=?" +
                SQLAND +
                SQLITEMASTERCOLUMN_NAME
                + "=?";
        String[] whereargs = {SQLITEMASTERTYPE_INDEX, this.index_name};
        Cursor mstcsr = db.query(SQLITEMASTERTABLE,
                columns,
                where,
                whereargs,
                null,null,null
        );
        // If the index entry doesn't exist then the index needs to be created
        if (mstcsr.getCount() < 1) {
            mstcsr.close();
            return new ArrayList<>(Collections.singletonList(this.indexcreateSQL));
        }
        mstcsr.moveToFirst();
        String actualcreatesql = mstcsr.getString(
                mstcsr.getColumnIndex(
                        SQLITEMASTERCOLUMN_SQL
                )
        );
        mstcsr.close();

        // Does the actual SQL match this instances SQL, if so,
        // then actual does not need altering
        // Note before the comparison
        //      IF NOT EXISTS is removed from the instances CREATE INDEX SQL
        //      White Spaces are removed from both the instances and the
        //      actual CREATE INDEX SQL
        String icomparesql = this.indexcreateSQL;
        icomparesql = icomparesql.replace(SQLIFNOTEXISTS,"");
        icomparesql = icomparesql.replaceAll("\\p{Z}","");
        String acomparesql = actualcreatesql.replaceAll("\\p{Z}","");
        if (acomparesql.equals(icomparesql)) {
            return rv;
        }
        // As the index exists and there doesn't appear to be a match between
        // the actual and instances versions of the Index a DROP INDEX, followed by
        // a CREATE INDEX, as per the psuedo schema, is a returned.
        rv.add(SQLDROP + SQLINDEX + SQLIFEXISTS + this.index_name);
        rv.add(this.indexcreateSQL);
        return rv;
    }


    /**************************************************************************
     * Returns a boolean indicating whether or not the DBIndex is usable.
     * @return  The usability status of the DBIndex,
     *          true if usable, else false.
     */
    public boolean isUsable() { return this.usable; }

    /**************************************************************************
     * Gets the name to be used for the underlying index.
     * @return  The Index name as a String
     */
    public String getIndexName() {
        return this.index_name;
    }

    /**************************************************************************
     * Gets the Fully Qualified name to be used for the underlying index.
     * i.e. table.indexname
     * @return  The name of the index, prefixed with the name of the table and
     *          a period.
     */
    public String getFullyQualifiedIndexName() { return this.full_index_name; }

    /**************************************************************************
     * Gets the SQL, as a string, that is used to create the actual index.
     * @return  SQL string containing SQL to create the index
     */
    public String generateCreateIndexSQL() {
        return this.indexcreateSQL;
    }

    /**************************************************************************
     * Gets the problem messsage as a string
     *      Note the message may contain a number of issues it msy also
     *      be an empty string if there are no issues.
     *      i.e. if the DBIndex is marked as usable, there will be no issues
     * @return the issues as a string
     */
    public String getDBIndexProblemMsg() { return this.problem_msg; }

    /**************************************************************************
     * Checks that the DBIndex is usable
     *      Index name must be at least 1 character
     *      The Table, a DBTable object, must be usable.
     *      The Table name, extracted from the DBTable, must be at least
     *      1 character.
     *      Column names, as extracted from the DBColumn object, must be
     *      at least 1 character.
     *      The Column must also be defined in the table.
     *      The number of column sort order elements must match the number
     *      of column elements.
     * @param caller    A string to identify the method that called this
     *                  method. i.e. a name for the constructor which is
     *                  passed via initDBIndex
     * @return  true if the Index is usable, else false
     *          not really needed as the usable member will be set accordingly
     */
    private boolean checkDBIndexIsUsable(String caller) {
        this.usable = true;
        String pmsg = "";
        // Table must be usable
        if (!this.owningtable.isDBTableUsable()) {
            this.usable = false;
            this.problem_msg = this.problem_msg +
                    "\nEDBI0014 - Table is Unusable - " +
                    " Table " +
                    this.owningtable.getDBTableName() +
                    " is marked as unusable as per (" +
                    this.owningtable.getDBTableProblemMsg() +
                    ") the issue or issues with the Table need to be resolved. " +
                    "Caller = (" + caller + ")"
            ;
        }
        // Table must be at least one characater in length
        // Note! very unlikely to happen
        if (this.owningtable.getDBTableName().length() < 1) {
            this.usable = false;
            this.problem_msg = this.problem_msg +
                    "\nEDBI0010 - Invalid Table Name - " +
                    "Must be at least 1 character in length. " +
                    "Caller = (" + caller + ")"
            ;
        }
        // Column Names must be at least 1 character and
        // be a column in the provided table
        for (int i = 0; i < this.columns.size(); i++) {
            if (this.columns.get(i).getDBColumnName().length() < 1) {
                this.usable = false;
                pmsg =  pmsg + "\nEDBI0011 - Invalid Column Name for Column(" +
                        Integer.toString(i) +
                        " of " +
                        Integer.toString(columns.size()) +
                        ")- " +
                            "Must be at least 1 character in length. ";
            }
            // Check that the column is in the index's table
            boolean columnintable = false;
            for (int j = 0; j < this.owningtable.getTableDBColumns().size(); j++) {
                if (this.owningtable.getTableDBColumns().get(j).equals(
                        this.columns.get(i))) {
                    columnintable = true;
                    break;
                }
            }
            if (!columnintable) {
                this.usable = false;
                pmsg = pmsg + "\nEDBI0015 - Column is not in Table - " +
                        "Column " +
                        this.columns.get(i).getDBColumnName() +
                        " must be a column from table " +
                        this.owningtable.getDBTableName() +
                        ". "
                ;
            }
        }
        if (pmsg.length() > 0) {
            this.problem_msg = this.problem_msg + pmsg +
                    "Caller = (" + caller + ")";
        }
        if (this.index_name.length() < 1) {
            this.usable = false;
            this.problem_msg = this.problem_msg +
                    "\nEDBI012 - Invalid Index Name - " +
                    "Must be at least 1 character in length. " +
                    "Caller = (" + caller + ")";
        }
        if (this.columns.size() != this.columnorders.size()) {
            this.usable = false;
            this.problem_msg = this.problem_msg +
                    "\nEDBI013 - Mismatched Columns against Column Orders - " +
                    "The number of Columns(" +
                    Integer.toString(this.columns.size()) +
                    ") must be the same as the number of Column Orders(" +
                    Integer.toString(this.columnorders.size()) +
                    ")" +
                    "Caller = (" + caller + ")";
        }
        return this.usable;
    }
}

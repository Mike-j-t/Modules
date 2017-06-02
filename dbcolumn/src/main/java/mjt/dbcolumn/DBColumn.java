package mjt.dbcolumn;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * Component of the DB???? classes that represents a Column of a Table of a Database.
 *
 * <p>
 *     A DBColumn is one of a number of DB???? classes (DBDatabase, DBTable,
 *     DBCColumn and DBIndex) designed for assisting with the creation
 *     and alteration of SQLite Databases.
 * </p>
 *
 * <p>
 *     <b>DBColumn dependencies on other DB???? classes/objects.</b>
 * </p>
 * <p>
 *     A DBColumn has no dependencies, rather DBTable, DBIndex and indirectly
 *     DBDatabase objects require a DBColumn object.
 * </p>
 * <p>
 *     <b>Usability.</b>
 * </p>
 * <p>
 *     The construction of a DBColumn instance includes checking that the
 *     name is at least 1 character in length and that a column type has
 *     been specified. If not then the DBColumn will have it's usability
 *     flag set to false. This flag will be propagated to any DBTable or
 *     DBIndex objects that include the DBColumn and then to the
 *     DBDatabase object when usability checking is undertaken by those
 *     objects.
 * </p>
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal","unused"})
public class DBColumn {
    /**
     * Usability flag indicates if the DBColumn is usable (true)
     * or not (false).
     */
    private boolean usable;
    /**
     * A String that will be the name of the column in the actual database.
     *
     * <p>The name will be converted to lowercase, it should not include any
     * whitespace.
     * </p>
     */
    private String column_name;
    /**
     * The fully qualified column name, that is column_name prefixed by the
     * table name and a period.
     * This set by the DBTable that uses the DBColumn by calling the
     * <code>setFullyQualifiedColumnName</code>
     *
     * <p>
     *     This field is determined when the DBColumn is assigned to a table.
     *     It is the DBTable that sets this values
     * </p>
     */
    private String full_column_name;
    /**
     * The type of the column, any value can be used.&nbsp;However, the
     * <code>simplifyColumnType</code> method will convert the
     * string into one of the 5 core SQLite Data Types.
     * @see #simplifyColumnType(String)
     */
    private String column_type;
    /**
     * Whether or not this column is to be part of the table's Primary Index.
     * &nbsp;True if it is to be part of the Primary Index, false if not.
     */
    private boolean primary_index;
    /**
     * The default value of a column, as a string, used if no value is
     * given when  inserting a column into the database.&nbsp;If
     * default_value is an empty String then no default value will be applied upon
     * insertion.
     */
    private String default_value;
    /**
     * An internally created string that will contain issues found
     * when instantiating or modifying the DBColumn.&nbsp;It would only be set
     * if the usable flag is set to false.
     */
    private String problem_msg;

    /**************************************************************************
     * Constructs DBColumn flagged as unusable.
     *
     * <p>
     *     To make the DBColumn usable <code>setDBColumn</code> and
     *     <code>setDBColumnType</code> should be used. Additionally
     *     <code>setDBDefaultValue</code> and
     *     <code>setPrimaryIndex</code> may be called.
     * </p>
     */
    public DBColumn() {
        this.usable = false;
        this.column_name = "";
        this.column_type = "";
        this.primary_index = false;
        this.default_value = "";
        this.problem_msg = "\nWDBC0003 - Uninstantiated - " +
                "Use at least setDBColumnType" +
                " AND setDBColumnName methods. " +
                "Caller=(DBColumn (Default))";
    }

    /**************************************************************************
     * Constructs a DBColumn with only the column name being given.
     *
     * <p>
     *     Using this constructor will result in a TEXT column, with no
     *     default value and not as part of the primary index.
     * </p>
     *
     * @param column_name the column name
     */
    public DBColumn(String column_name) {
        this.usable = false;
        this.column_name = column_name.toLowerCase();
        this.column_type = "TEXT";
        this.default_value = "";
        this.primary_index = false;
        this.problem_msg = "";
        this.checkDBColumnIsUsable("DBColumn (Quick Constructor)");
    }


    /**************************************************************************
     * Constructs a DBColumn from column name, type, primary index flag and
     * default value.
     *
     * @param column_name   A String, the name to be given to the column.
     * @param column_type   A String, the type of column. Type can be any
     *                      string, even an empty string. The SQLite rules
     *                      are then applied, in short any string is converted
     *                      to 1 of 5 types (INTEGER, TEXT, BLOB, REAL or
     *                      NUMERIC).
     *                      @see #simplifyColumnType(String)
     *
     * @param primary_index A boolean, true if this column is to be included
     *                      in the primary index, false if not.
     * @param default_value A String, if it's length is greater then 1, then
     *                      when a row is inserted into a table and no value
     *                      has been specified, the column will be set to
     *                      this value.
     *
     */
    public DBColumn(String column_name,
                    String column_type,
                    boolean primary_index,
                    String default_value) {
        column_type = column_type.toUpperCase();
        column_name = column_name.toLowerCase();

        // Lots of potential values for the column type; so validate
        this.problem_msg = "";
        this.column_type = simplifyColumnType(column_type);
        this.column_name = column_name;
        this.primary_index = primary_index;
        this.default_value = default_value;
        this.checkDBColumnIsUsable("DBColumn (Full)");
    }

    /**************************************************************************
     * Constructs a DBColumn as a standard (commonly used) id Column. i.e.
     * the column name will be _id the type will be INTEGER PRIMARY KEY.
     *
     * @param asSTDPRIMARYINDEX boolean, irrelevant as this is a special
     *                          constructor for the common scenario of
     *                          creating a standard _id column.
     */
    public DBColumn(boolean asSTDPRIMARYINDEX) {
        this.problem_msg = "";
        this.column_name = SQLSTD_ID;
        this.column_type = SQLINTEGER;
        this.primary_index = true;
        this.default_value = "";
        this.checkDBColumnIsUsable("DBColumn (asSTDPRIMARYINDEX)");
    }

    /**************************************************************************
     * Sets a DBColumn's column name.
     *
     * @param column_name the column name
     */
    public void setDBColumnName(String column_name) {
        this.column_name = column_name.toLowerCase();
        this.checkDBColumnIsUsable("setDBColumnName");
    }

    /**************************************************************************
     * Sets a DBColumn's fully qualified name; this is intended to only be
     * used from within DBTable.
     *
     * @param owning_table  the table that owns this column
     */
    public void setFullyQualifiedColumnName(String owning_table) {
        this.full_column_name = owning_table + "." + this.column_name;
    }

    /**************************************************************************
     * Sets a DBColumns's column type, which may be altered according to
     * <code>simplifyColumnType</code>, which applies one of the 5 core
     * SQLite column types.
     * @see #simplifyColumnType(String)
     *
     * @param column_type the column type as a String.
     */
    public void setDBColumnType(String column_type) {
        this.column_type = simplifyColumnType(column_type);
        this.checkDBColumnIsUsable("setDBColumnType");
    }

    /**************************************************************************
     * Sets a DBColumn's default value.
     *
     * @param default_value the default value to be used when a column is
     *                      inserted, into the database, with no value provided.
     */
    public void setDefaultValue(String default_value) {
        this.default_value = default_value;
    }

    /**
     * Sets the primary_index flag.
     *
     * @param primaryindex  boolean flag, true indicating inclusion of the
     *                      column in the table's primary index.
     */
    public void setPrimaryIndex(boolean primaryindex) {
        this.primary_index = primaryindex;
        this.checkDBColumnIsUsable("serPrimaryIndex");
    }

    /**************************************************************************
     * Gets the DBColumn's column name.
     *
     * @return the DBColumn's name as a String.
     */
    public String getDBColumnName() {
        return this.column_name;
    }

    /***************************************************************************
     * Gets the fully qualified column name.
     * @return the fully qualified column name as a String; the fully
     * qualified name is the column name prefixed by the DBTable's name
     * with a period separating the two names. &nbsp;Note! if the DBColumn
     * has not been assigned to a DBTable it will be blank.
     */
    public String getFullyQualifiedDBColumnName() {
        return this.full_column_name;
    }

    /**************************************************************************
     * Gets the DBColumn's column type.
     *
     * @return the DBColumn's type as a String.
     */
    public String getDBColumnType() {
        return this.column_type;
    }

    /**************************************************************************
     * Gets the DBColumn's default value.
     *
     * @return The DBColumn's default value as a String.
     */
    public String getDBColumnDefaultValue() { return this.default_value; }

    /**************************************************************************
     * Gets the DBColumns usability status.
     *
     * @return the DBColumn's usability status as a boolean; true if the
     * DBColumn is usable, false if not.
     */
    public boolean getDBColumnIsUsable() {
        return this.usable;
    }

    /**************************************************************************
     * Gets the DBColumn's usability status
     * (same as <code>getDBColumnIsUsable</code>).
     *
     * @return The DBColumn's usability status as a boolean; true if the
     * DBColumn is usable, false if not.
     */
    public boolean isDBColumnUsable() {
        return this.usable;
    }

    /**************************************************************************
     * Gets the DBColumn's primary index status.
     *
     * @return The DBColumn's primary index status; true if the column will
     * be part of the primary index, false if not.
     */
    public boolean getDBColumnIsPrimaryIndex() {
        return this.primary_index;
    }


    /**************************************************************************
     * Gets the DBColumn's primary index status.
     *
     * @return The DBColumn's primary index status; true if the column will
     * be part of the primary index, false if not.
     */
    public boolean isDBColumnPrimaryIndex() { return this.primary_index; }

    /**************************************************************************
     * Gets the DBColumns problem msg.
     *
     * @return The DBColumn's problem message as a String.
     */
    public String getDBColumnProblemMsg() { return this.problem_msg; }

    /**************************************************************************
     * Gets the DBColumns problem msg
     * (same as <code>getDBColumnProblemMsg</code>).
     *
     * @return The DBColumn's problem message as a String.
     */
    public String getUnusableMsg() { return this.problem_msg; }

    /**************************************************************************
     * Checks the DBColumn for issues, setting the problem message accordingly.
     *
     * <p>
     *     The DBColumn's name must be at least 1 character in length,
     *     as must be the DBColumn's type. This second issue should not
     *     normally arise due to the conversion undertaken by
     *     <code>simplifyColumnType</code>
     * </p>
     * @param   caller  indicator/tag of what called this for inclusion in
     *                  the resultant message.
     * @return  true if the column is usable else false
     */
    @SuppressWarnings("UnusedReturnValue")
    private boolean checkDBColumnIsUsable(String caller) {
        this.usable = false;
        if(this.column_name.length() > 0 & this.column_type.length() > 0) {
            this.usable = true;
            this.problem_msg = "";
        } else {
            if(this.column_name.length() < 1) {
                this.problem_msg=this.problem_msg +
                        "\nEDBC001 - Invalid Column Name - " +
                        "Must be at least 1 character in length. " +
                        "Caller=(" + caller + ")";
            }
            if(this.column_type.length() < 1) {
                this.problem_msg=this.problem_msg +
                        "\nEDBC002 - Invalid Column Type - " +
                        "Must be a valid SQLite DATATYPE. " +
                        "Caller=(" + caller + ")";
            }
        }
        return this.usable;
    }

    /**************************************************************************
     * Converts column allowable column type to the core column type:-
     * TEXT, NUMERIC, INTEGER, REAL or BLOB
     * As per <b>Datatypes In SQLite Version 3, Section 3 Type Affinity</b>
     * @link    https://sqlite.org/datatype3.html
     *
     * @param type  the type passed
     * @return      the converted type
     */
    private String simplifyColumnType(String type) {
        type = type.toUpperCase();

        if (type.contains("INT")) {
            return  SQLINTEGER;
        }
        if (type.contains("CHAR") ||
                type.contains("CLOB") ||
                type.contains("TEXT")) {
            return  SQLTEXT;
        }
        if (type.contains("BLOB") ||
                type.length() < 1 ) {
            return SQLBLOB;
        }
        if (type.contains("REAL") ||
                type.contains("FLOA") ||
                type.contains("DOUB")) {
            return  SQLREAL;
        }
        return SQLNUMERIC;
    }
}

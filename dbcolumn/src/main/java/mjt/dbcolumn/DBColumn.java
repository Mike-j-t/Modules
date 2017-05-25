package mjt.dbcolumn;

/**
 * The type Db column.
 */
/*==================================================================================================
//==================================================================================================
// DBTColumn class - Database Column Class has Column Name, Column Datatype, primary index flag,
// and default value "" = no default.
// Also has a usability flag and a Problem Message (both of these are managed internally)
// Note!! Column name is converted to lowercase
//
// If default constructor used then the table will be flagged as unusable. It will require:-
//        1) A table name via the setDBTable
//        2) One or more columns via methods :-
//            AddDBColumnToDBTable (adds a single column via a DBColumn object)
//            AddDBColumnsToDBTable (adds multiple columns (1 or more) via an ArrayList of DBCOlumn
//                Objects.
//            AddMultipleColumnstoDBTable just a psuedonym for AddColumnsToDBTable, that is easier
//                to differentiate between AddDBColumnToDBTAble and AddColumnsToDBTable (plural)
//
// If Intermediate Constructor used (sets the table name but not any columns). The table will be
// flagged as unusable. However, the table name would be set (unless "" given as the table name).
// Assuming the name is not "" then a column or columns will have to be added as per 2) above.
// If "" given as name then 1) above would have to be applied.
//
// If HigherIntermediate Constructor (sets the table name and 1 column) then the table would be
// usable. However 2) above would be required if further columns were needed.
//
// If Full constructor used then (sets table name and multiple columns as per and ArrayList of
// DBColumn objects) table should be usable.
// NOTE!!! In all cases the assumption is that all DBColumns applied are themselves usable. If any
//         are not then the table will be flagged as unusable. Refer to DBColumns for their
//         usability criteria.
//==================================================================================================
//================================================================================================*/
@SuppressWarnings({"WeakerAccess", "CanBeFinal","unused"})
public class DBColumn {
    private boolean usable;
    private String column_name;
    private String column_type;
    private boolean primary_index;
    private String default_value;
    private String problem_msg;
    private int order;

    public static final String DB_STD_ID = "_id";
    public static final String DB_PERIOD = ".";

    // Column Types
    public static final String DB_INT = "INTEGER";
    public static final String DB_TXT = "TEXT";
    public static final String DB_IDTYPE = DB_INT;
    public static final String DB_REAL = "REAL";
    public static final String DB_NUMERIC = "NUMERIC";
    public static final String DB_BLOB = "BLOB";


    /**************************************************************************
     * Default Constructor
     */
    public DBColumn() {
        this.usable = false;
        this.column_name = "";
        this.column_type = "";
        this.primary_index = false;
        this.default_value = "";
        this.order = 0;
        this.problem_msg = "WDBC0003 - Uninstantiated - " +
                "Use at least setDBColumnType AND setDBColumnName methods. " +
                "Caller=(DBColumn (Default))";
    }

    /**************************************************************************
     * Constructor with just the column name
     *
     * @param column_name the column name
     */
    // Intermediate Constructor - Just give the column name -
    // defaults to TEXT and not a primary index
    // Note!!! always assumed to be usable.
    public DBColumn(String column_name) {
        this.usable = false;
        this.column_name = column_name.toLowerCase();
        this.column_type = "TEXT";
        this.default_value = "";
        this.primary_index = false;
        this.problem_msg = "";
        this.order = 0;
        this.checkDBColumnIsUsable("DBCOlumn (Quick Constructor)");
    }

    /**************************************************************************
     * Constructor with name and sortorder
     *
     * @param column_name the column name
     * @param sortorder   the sortorder
     */
    @SuppressWarnings("unused")
    public DBColumn(String column_name,
                    int sortorder) {
        this(column_name);
        this.order = sortorder;
    }

    /**************************************************************************
     * Constructor with column name, type, primary index flag and default value
     *
     * @param column_name   the column name
     * @param column_type   the column type
     * @param primary_index the primary index
     * @param default_value the default value
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
        this.order = 0;
        this.checkDBColumnIsUsable("DBColumn (Full)");
    }

    /**************************************************************************
     * Constructor with column name, type, primary index flag, default value
     * and sort order
     *
     * @param column_name   the column name
     * @param column_type   the column type
     * @param primary_index the primary index
     * @param default_value the default value
     * @param sortorder     the sortorder
     */
    public DBColumn(String column_name,
                    String column_type,
                    boolean primary_index,
                    String  default_value,
                    int sortorder) {
        this(column_name, column_type, primary_index, default_value);
        this.order = sortorder;
    }

    /**************************************************************************
     * Sets db column name.
     *
     * @param column_name the column name
     */
    public void setDBColumnName(String column_name) {
        this.column_name = column_name;
        this.checkDBColumnIsUsable("setDBColumnName");
    }

    /**************************************************************************
     * Sets db column type.
     *
     * @param column_type the column type
     */
    public void setDBColumnType(String column_type) {
        this.column_type = simplifyColumnType(column_type);
        this.checkDBColumnIsUsable("setDBColumnType");
    }

    /**************************************************************************
     * Sets default value.
     *
     * @param default_value the default value
     */
    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    /**************************************************************************
     * Gets db column name.
     *
     * @return the db column name
     */
    public String getDBColumnName() {
        return this.column_name;
    }

    /**************************************************************************
     * Gets db column type.
     *
     * @return the db column type
     */
    public String getDBColumnType() {
        return this.column_type;
    }

    /**************************************************************************
     * Gets db column is usable.
     *
     * @return the db column is usable
     */
    public boolean getDBColumnIsUsable() {
        return this.usable;
    }

    /**************************************************************************
     * Is db column usable boolean.
     *
     * @return the boolean
     */
    public boolean isDBColumnUsable() {
        return this.usable;
    }

    /**************************************************************************
     * Gets db column is primary index.
     *
     * @return the db column is primary index
     */
    public boolean getDBColumnIsPrimaryIndex() {
        return this.primary_index;
    }

    /**************************************************************************
     * Gets db column default value.
     *
     * @return the db column default value
     */
    public String getDBColumnDefaultValue() { return this.default_value; }

    /**************************************************************************
     * Is db column primary index boolean.
     *
     * @return the boolean
     */
    public boolean isDBColumnPrimaryIndex() { return this.primary_index; }

    /**************************************************************************
     * Gets db column problem msg.
     *
     * @return the db column problem msg
     */
    public String getDBColumnProblemMsg() { return this.problem_msg; }

    /**************************************************************************
     * Gets unusable msg.
     *
     * @return the unusable msg
     */
    public String getUnusableMsg() { return this.problem_msg; }

    /**************************************************************************
     * Gets sortorder.
     *
     * @return the sortorder
     */
    public int getSortorder() { return this.order; }

    /**************************************************************************
     * Sets the column usable status
     * @param   caller  indicator/tag of what called this for inclusion in
     *                  the resultant message.
     * @return  true if the column is usable else false
     */
    private boolean checkDBColumnIsUsable(String caller) {
        this.usable = false;
        if(this.column_name.length() > 0 & this.column_type.length() > 0) {
            this.usable = true;
            this.problem_msg = "";
        } else {
            if(this.column_name.length() < 1) {
                this.problem_msg=this.problem_msg +
                        "EDBC001 - Invalid Column Name - Must be at least 1 character in length. " +
                        "Caller=(" + caller + ")";
            }
            if(this.column_type.length() < 1) {
                this.problem_msg=this.problem_msg +
                        "EDBC002 - Invalid Column Type - Must be a valid SQLite DATATYPE. " +
                        "Caller=(" + caller + ")";
            }
        }
        return this.usable;
    }

    /**************************************************************************
     * Converts column allowable column type to the core column type:-
     * TEXT, NUMERIC, INTEGER, REAL or BLOB
     * @param type  the type passed
     * @return      the converted type
     */
    private String simplifyColumnType(String type) {
        type = type.toUpperCase();

        if (type.contains("CHAR")) { return DB_TXT; }
        if (type.contains("DECIMAL")) { return DB_NUMERIC; }

        switch (type) {
            case "DB_INT": { return DB_INT; }
            case "TINYINT": { return DB_INT; }
            case "SMALLINT": { return DB_INT; }
            case "MEDIUMINT": { return DB_INT; }
            case "BIGINT": { return DB_INT; }
            case "UNSIGNED BIG INT": { return DB_INT; }
            case "INT2": { return DB_INT; }
            case "INT8": { return DB_INT; }
            case "INTEGER":  { return DB_INT; }
            case "LONG": { return  DB_INT; }
            case "BOOLEAN": { return DB_INT ;}

            case "CLOB": { return DB_TXT; }
            case "TEXT": { return DB_TXT; }

            case "DB_REAL": { return DB_REAL; }
            case "DOUBLE": { return DB_REAL; }
            case "DOUBLE PRECISION": { return DB_REAL; }
            case "FLOAT": { return DB_REAL; }

            case "NUMERIC": { return DB_NUMERIC ;}

            case "DATE": { return DB_NUMERIC ;}
            case "DATETIME": { return DB_NUMERIC ;}

            case "BLOB": { return DB_BLOB; }
            default: { return DB_BLOB; }
        }
    }
}

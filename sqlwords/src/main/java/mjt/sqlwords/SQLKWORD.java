package mjt.sqlwords;

/**
 * SQLite SQL Keywords/Phrases
 */
@SuppressWarnings({"unused","WeakerAccess"})
public class SQLKWORD {

    public static final String SQLTABLE = " TABLE ";
    public static final String SQLCREATE = " CREATE ";
    public static final String SQLDROP = " DROP ";
    public static final String SQLINDEX = " INDEX ";
    public static final String SQLUNIQUE = " UNIQUE ";
    public static final String SQLIFEXISTS = " IF EXISTS ";
    public static final String SQLIFNOTEXISTS = " IF NOT EXISTS ";
    public static final String SQLPRIMARYKEY = " PRIMARY KEY";
    public static final String SQLPRIMARYKEYSTART = ", PRIMARY KEY (";
    public static final String SQLPRIMARYKEYEND = ") ";
    public static final String SQLDEFAULT = " DEFAULT ";
    public static final String SQLALTERTABLE = " ALTER" + SQLTABLE;
    public static final String SQLADDCOLUMN = " ADD COLUMN ";
    public static final String SQLPRIMARYINDEX = " PRIMARY " + SQLINDEX;

    public static final String SQLPRAGMA = "PRAGMA ";
        public static final String SQLTABLEINFO_START = SQLPRAGMA + " TABLE_INFO (";
            public static final String SQLTABLEINFO_END = ") ";
            public static final String SQLTABLEINFO_IDCOL = "cid";
            public static final String SQLTABLEINFO_COLNAME_COL = "name";
            public static final String SQLTABLEINFO_COLTYPE_COL = "type";
            public static final String SQLTABLEINFO_COLNOTNULL_COL = "notnull";
            public static final String SQLTABLEINFO_COLDFLTVAL_COL = "dflt_value";
            public static final String SQLTABLEINFO_COLPRIMARYKEY_COL = "pk";


    // Core SQLite Types
    public static final String SQLINTEGER = "INTEGER";
    public static final String SQLSTDIDTYPE = SQLINTEGER + SQLPRIMARYKEY;
    public static final String SQLSTD_ID = "_id";
    public static final String SQLTEXT = "TEXT";
    public static final String SQLNUMERIC = "NUMERIC";
    public static final String SQLREAL = "REAL";
    public static final String SQLBLOB = "BLOB";

    public static final String SQLSELECT = " SELECT ";
    public static final String SQLSELECTDISTINCT = SQLSELECT + "DISTINCT ";
    public static final String SQLFROM = " FROM ";
    public static final String SQLGROUP = " GROUP BY ";
    public static final String SQLWHERE = " WHERE ";
    public static final String SQLORDERBY = " ORDER BY ";
    public static final String SQLORDERASCENDING = " ASC ";
    public static final String SQLORDERDESCENDING = " DESC ";
    public static final String SQLAND = " AND ";
    public static final String SQLOR = " OR ";
    public static final String SQLON = " ON ";
    public static final String SQLAS = " AS ";
    public static final String SQLENDSTATEMENT = " ;";
    public static final String SQLJOIN = " JOIN ";
    public static final String SQLLEFTJOIN = " LEFT JOIN ";
    public static final String SQLNULL = " NULL ";
    public static final String SQLNOT = " NOT ";
    public static final String SQLIN = " IN ";
    public static final String SQLNOTIN = SQLNOT + SQLIN;
    public static final String SQLNOTEQUALS = " <> ";
    public static final String SQLLIMIT = " LIMIT ";
    public static final String SQLLIKECHARSTART = " LIKE '%";
    public static final String SQLLIKECHAREND = "%' ";
    public static final String SQLIS = " IS ";
    public static final String SQLISNOTNULL = SQLIS + SQLNOT + SQLNULL;
    public static final String SQLISNULL = SQLIS + SQLNULL;
    public static final String SQLPERIOD = ".";

    public static final String SQLSUM = " SUM(";
    public static final String SQLSUMCLOSE = ") ";

    public static final String SQLMAX = " MAX(";
    public static final String SQLMAXCLOSE = SQLSUMCLOSE;
    public static final String SQLAVERAGE = " AVG(";
    public static final String SQLAVERAGECLOSE = SQLSUMCLOSE;

    public static final String SQLSELECTALLFROM = SQLSELECT + "*" + SQLFROM;
    public static final String SQLSELECTDISTINCTALLFROM =
            SQLSELECTDISTINCT + "*" + SQLFROM;

    // SQLite Master Table definitions
    public static final String SQLITEMASTERTABLE = "sqlite_master";
    public static final String SQLITEMASTERCOLUMN_ROWID = "rowid";
    public static final String SQLITEMASTERCOLUMN_TYPE = "type";
    public static final String SQLITEMASTERCOLUMN_NAME = "name";
    public static final String SQLITEMASTERCOLUMN_TBLNAME = "tbl_name";
    public static final String SQLITEMASTERCOLUMN_ROOTPAGE = "rootpage";
    public static final String SQLITEMASTERCOLUMN_SQL = "sql";
    public static final String SQLITEMASTERTYPE_INDEX = "index";
    public static final String SQLITEMASTERTYPE_TABLE = "table";
    public static final String SQLITEMASTERNAME_METADATA = "android_metadata";
    public static final String SQLITEMASTERNAMEPREFIX_AUTOINDEX =
            "sqlite_autoindex";
}

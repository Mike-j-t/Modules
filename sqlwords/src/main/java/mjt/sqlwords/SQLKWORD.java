package mjt.sqlwords;

/**
 * SQLite SQL Keywords/Phrases
 */
@SuppressWarnings("unused")
public class SQLKWORD {

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

    public static final String SQLSUM = " SUM(";
    public static final String SQLSUMCLOSE = ") ";

    public static final String SQLMAX = "MAX(";
    public static final String SQLMAXCLOSE = ") ";

    public static final String SQLSELECTALLFROM = SQLSELECT + "*" + SQLFROM;
    public static final String SQLSELECTDISTINCTALLFROM =
            SQLSELECTDISTINCT + "*" + SQLFROM;
}

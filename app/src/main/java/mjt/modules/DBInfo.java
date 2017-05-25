package mjt.modules;


/**
 * DBInfo Note this is part of and sepcific to the ShowCase
 */

import mjt.dbcolumn.DBColumn;
import static mjt.dbcolumn.DBColumn.*;

@SuppressWarnings({"unused","WeakerAccess"})
class DBInfo {

    public static final String DBNAME = "mydatabase";
    public static final int DBVERSION = 1;

    public static final String DBUSER_TABELNAME = "users";
    public static final String DBUSER_ID_COLUMNNAME = DBColumn.DB_STD_ID;
    public static final String DBUSER_ID_FULLCOLUMNNAME =
            DBUSER_TABELNAME + DB_PERIOD +DBUSER_ID_COLUMNNAME;
    public static final String DBUSER_NAME_COLUMNNAME = "name";
    public static final String DBUSER_NAME_FULLCOLUMNNAME =
            DBUSER_TABELNAME + DB_PERIOD;
    public static final String DBUSER_INFO_COLUMNNAME = "info";
    public static final String DBUSER_INFO_FULLCOLUMNNAME =
            DBUSER_TABELNAME + DB_PERIOD + DBUSER_INFO_COLUMNNAME;

    public static final String DBPROPERTY_TABLENAME = "property";
    public static final String DBPROPERTY_ID_COLUMNNAME = DBColumn.DB_STD_ID;
    public static final String DBPROPERTY_ID_FULLCOLUMNNAME =
            DBPROPERTY_TABLENAME + DB_PERIOD + DBPROPERTY_ID_COLUMNNAME;
    public static final String DBPROPERTY_DESC_COLUMNNAME = "desc";
    public static final String DBPROPERTY_DESC_FULLCOLUMNNAME =
            DBPROPERTY_TABLENAME + DB_PERIOD + DBPROPERTY_DESC_COLUMNNAME;

}

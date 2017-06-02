package mjt.modules;


import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbdatabase.DBDatabase;
import mjt.dbindex.DBIndex;
import mjt.dbtable.DBTable;
import static mjt.sqlwords.SQLKWORD.*;

/**
 * ShowCase Database definitions.
 *
 * <p>
 *     This class defines constants used for ShowCasing the DB???? classes,
 *     DBDatabase, DBTable, DBColumn and DBIndex. These classes being used
 *     to create a pseudo schema from which SQL statements are extracted to
 *     build and/or alter the actual SQLite database via the DBDatabase
 *     methods <code>actionDBBuildSQL</code> and
 *     <code>actionDBAlterSQL</code> respectively.
 * </p>
 *
 */
@SuppressWarnings({"unused", "WeakerAccess", "ArraysAsListWithZeroOrOneArgument"})
class ShowCaseDBInfo {

    /*
    The ShowCase is going to create an SQLite Database named mydatabase
    it will consist of 3 tables, namely users, property and userpropertylink.
    The latter being a table that facilitates a many-many relationship
    between users and properties. i.e. a user can have many properties and
    a property can have many users.
    The users table is to have columns; _id (standard unique id), name and info.
    The property table is to have columns; _id and descr
    The userpropertylink table will have two columns userref and propertyref.
    There wil be two indexes; one name username_index, the other named
    propertydesc_index
     */
    // Define the constants for the Database name and the version number
    //  note there is little, if any need, to ever alter the DB version
    public static final String DBNAME = "mydatabase";
    public static final int DBVERSION = 1;

    // Define the constants for the users table
    public static final String USERS_TABLENAME = "users";
    // NOTE uses the standard defined name for the _id column
    public static final String USERS_ID_COLNAME = SQLSTD_ID;
    public static final String USERS_NAME_COLNAME = "name";
    public static final String USERS_INFO_COLNAME = "info";

    //Define the constants for the property table
    public static final String PROPERTY_TABLENAME = "property";
    // Again use the standard defined name for the _id column
    public static final String PROPERTY_ID_COLNAME = SQLSTD_ID;
    public static final String PROPERTY_DESC_COLNAME = "descr";

    //Define the constants for the userpropertylink table
    public static final String USERPROPERTYLINK_TABLENAME = "userpropertylink";
    public static final String USERPROPERTYLINK_USERREF_COLNAME =
            "userref";
    public static final String USERPROPERTYLINK_PROPERTYREF_COLNAME =
            "propertyref";

    public static final String USERS_NAME_INDEX = "username_index";
    public static final String PROPERTY_DESCR_INDEX = "propertydescr_index";

    // Define the DB???? instances

    // Define the DBColumns for the users table
    // NOTE special constructor for standrd _id columns (can be resused)
    public static final DBColumn users_id = new DBColumn(true);
    public static final DBColumn users_name = new DBColumn(USERS_NAME_COLNAME,SQLTEXT,false,"");
    public static final DBColumn users_info = new DBColumn(USERS_INFO_COLNAME,SQLTEXT,false,"");

    // Define the DBColumns for the property table
    // Note will reuse of the stdid column for the _id
    public static final DBColumn property_id = new DBColumn(true);
    public static final DBColumn property_descr = new DBColumn(PROPERTY_DESC_COLNAME,SQLTEXT,false,"");

    // Define the DBColumns for the userpropertylink table
    public static final DBColumn userpropertylink_userref = new DBColumn(
            USERPROPERTYLINK_USERREF_COLNAME,
            SQLINTEGER,
            true,
            ""
    );
    public static final DBColumn userpropertylink_propertyref = new DBColumn(
            USERPROPERTYLINK_PROPERTYREF_COLNAME,
            SQLINTEGER,
            true,
            ""
    );

    // Define the DBTable instances
    // users table
    public static final DBTable users = new DBTable(USERS_TABLENAME,
            new ArrayList<>(
                    Arrays.asList(
                            users_id,
                            users_name,
                            users_info
                    )
            )
    );

    // property table
    public static final DBTable property = new DBTable(PROPERTY_TABLENAME,
            new ArrayList<>(
                    Arrays.asList(
                            property_id,
                            property_descr
                    )
            )
    );
    // userpropertylink table
    public static final DBTable userpropertylink = new DBTable(
            USERPROPERTYLINK_TABLENAME,
            new ArrayList<>(
                    Arrays.asList(
                            userpropertylink_userref,
                            userpropertylink_propertyref
                    )
            )
    );

    // UserName Index
    public static final DBIndex  users_name_index = new DBIndex(USERS_NAME_INDEX,
            users,
            users_name,
            true,
            false
    );

    // Property Index (note although only single column, showing multiple column method)
    // ignore the asList with 1 arg warning
    public static final  DBIndex property_desc_index = new DBIndex(PROPERTY_DESCR_INDEX,property,
            new ArrayList<>(
                    Arrays.asList(
                            property_descr)
            ),
            new ArrayList<>(
                    Arrays.asList(true)
            ),
            false
    );

    // Define the DBDatabase instance to use Tables and Indexes
    public static final DBDatabase pseudodbschema = new DBDatabase(DBNAME,
            new ArrayList<>(
                    Arrays.asList(
                            users,
                            property,
                            userpropertylink
                    )
            ),
            new ArrayList<>(Arrays.asList(
                    users_name_index,
                    property_desc_index
            ))
    );

    // Define some other potentially useful constants to overcome ambiguities

    // Fully qualified users _id column name
    // Note does not use the getFullyQualifiedDBColumnName due to the stdid
    // DBColumn being resused
    public static final String USERS_ID_FULLCOLNAME =
            users.getDBTableName() +
                    SQLPERIOD +
                    users_id.getDBColumnName();
    // Alternative users _id column name
    //  For cases where fully qualified name cannot be used
    //  e.g. Cursor adpaters
    public static final String USERS_ID_ALTCOLNAME =
            users.getDBTableName() +
            "_" +
            users_id.getDBColumnName()
            ;
    public static final String USERS_NAME_FULLCOLNAME =
            users_name.getFullyQualifiedDBColumnName();
    public static final String USERS_INFO_FULLCOLNAME =
            users_info.getFullyQualifiedDBColumnName();

    // Fully qualified property_id column name
    public static final String PROPERTY_ID_FULLCOLNAME =
            property.getDBTableName() +
                    SQLPERIOD +
                    property_id.getDBColumnName()
            ;
    // Alternative property_id column name
    //  For cases where fully qualified name cannot be used
    //  e.g. Cursor adpaters
    public static final String PROPERTY_ID_ALTCOLNAME =
            property.getDBTableName() +
                    "_" +
                    property_id.getDBColumnName();
    public static final String PROPERTY_DESCR_FULLCOLNAME =
            property_descr.getFullyQualifiedDBColumnName();

    public static final String USERPROPERTY_USERREF_FULLCOLNAME =
            userpropertylink.getDBTableName() +
                    SQLPERIOD +
                    userpropertylink_userref.getDBColumnName();
    public static final String USER_PROPERTY_PROPERTYREF_FULLCOLNAME =
            userpropertylink.getDBTableName() +
                    SQLPERIOD +
                    userpropertylink_propertyref.getDBColumnName();
}

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

    NOTE! The ShowCase writes messages to the log

    The first run log would be similar to :-

    06-04 09:06:57.319 4499-4499/mjt.modules D/ShowCaseOK:  CREATE  TABLE  IF NOT EXISTS users (_id INTEGER  PRIMARY KEY, name TEXT , info TEXT ) ;
    06-04 09:06:57.319 4499-4499/mjt.modules D/ShowCaseOK:  CREATE  TABLE  IF NOT EXISTS property (_id INTEGER  PRIMARY KEY, descr TEXT ) ;
    06-04 09:06:57.319 4499-4499/mjt.modules D/ShowCaseOK:  CREATE  TABLE  IF NOT EXISTS userpropertylink (userref INTEGER , propertyref INTEGER , PRIMARY KEY (userref, propertyref) ) ;
    06-04 09:06:57.319 4499-4499/mjt.modules D/ShowCaseOK:  CREATE  INDEX  IF NOT EXISTS username_index ON users(name ASC )
    06-04 09:06:57.319 4499-4499/mjt.modules D/ShowCaseOK:  CREATE  INDEX  IF NOT EXISTS propertydescr_index ON property(descr ASC )
    06-04 09:06:57.324 4499-4499/mjt.modules D/ShowCaseOK: --CRTTB_START
                                                            CREATE  TABLE  IF NOT EXISTS 'users' (`_id` BIGINT(20) NOT NULL  PRIMARY KEY, `name` TEXT , `info` TEXT ) ;
                                                           --CRTTB_FINISH
                                                           --CRTTB_START
                                                            CREATE  TABLE  IF NOT EXISTS 'property' (`_id` BIGINT(20) NOT NULL  PRIMARY KEY, `descr` TEXT ) ;
                                                           --CRTTB_FINISH
                                                           --CRTTB_START
                                                            CREATE  TABLE  IF NOT EXISTS 'userpropertylink' (`userref` BIGINT(20) NOT NULL , `propertyref` BIGINT(20) NOT NULL , PRIMARY KEY (userref, propertyref) ) ;
                                                           --CRTTB_FINISH
    06-04 09:06:57.324 4499-4499/mjt.modules D/ShowCaseOK: Pre AlterSQL
    06-04 09:06:57.326 4499-4499/mjt.modules D/ShowCaseOK: Post AlterSQL
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=table	Name of Entry=android_metadata
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=table	Name of Entry=users
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=table	Name of Entry=property
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=table	Name of Entry=userpropertylink
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=index	Name of Entry=sqlite_autoindex_userpropertylink_1
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=index	Name of Entry=username_index
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseOK: Type of Entry=index	Name of Entry=propertydescr_index
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=table	Name of Entry=android_metadata
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=table	Name of Entry=users
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=table	Name of Entry=property
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=table	Name of Entry=userpropertylink
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=index	Name of Entry=sqlite_autoindex_userpropertylink_1
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=index	Name of Entry=username_index
    06-04 09:06:57.341 4499-4499/mjt.modules D/ShowCaseCPYDB: Type of Entry=index	Name of Entry=propertydescr_index
    06-04 09:06:57.348 4499-4499/mjt.modules D/ShowCaseOK: Admin User Added

    The log from subsequent runs would be similar except that the very last line "Admin User Added" would not be present.
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
    public static final DBColumn ooops = new DBColumn(USERS_INFO_COLNAME,SQLTEXT,false,"");

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

    /*
        Define some other potentially useful constants to overcome
        ambiguities.

     */
    // Fully qualified users _id column
    public static final String USERS_ID_FULLCOLNAME =
            users_id.getFullyQualifiedDBColumnName();
    // Alternative users _id column name
    //  For cases where fully qualified name cannot be used but _id
    //  would be ambiguous, so incorporate the tablename by prefixing
    // the column name with the table name and an underscore.
    // e.g. users__id
    public static final String USERS_ID_ALTCOLNAME =
            users.getDBTableName() +
            "_" +
            users_id.getDBColumnName()
            ;
    // Fully qualified user name
    public static final String USERS_NAME_FULLCOLNAME =
            users_name.getFullyQualifiedDBColumnName();
    // Full qualified user info
    public static final String USERS_INFO_FULLCOLNAME =
            users_info.getFullyQualifiedDBColumnName();

    // Fully qualified property_id column name
    public static final String PROPERTY_ID_FULLCOLNAME =
            property.getDBTableName() +
                    SQLPERIOD +
                    property_id.getDBColumnName()
            ;
    // Alternative property_id column name
    public static final String PROPERTY_ID_ALTCOLNAME =
            property.getDBTableName() +
                    "_" +
                    property_id.getDBColumnName();
    // Full qualified property descr
    public static final String PROPERTY_DESCR_FULLCOLNAME =
            property_descr.getFullyQualifiedDBColumnName();
    // Full qualified userpropertyref userref
    public static final String USERPROPERTY_USERREF_FULLCOLNAME =
            userpropertylink.getDBTableName() +
                    SQLPERIOD +
                    userpropertylink_userref.getDBColumnName();
    // Fully qualified userpropertyref propertyref
    public static final String USER_PROPERTY_PROPERTYREF_FULLCOLNAME =
            userpropertylink.getDBTableName() +
                    SQLPERIOD +
                    userpropertylink_propertyref.getDBColumnName();
}

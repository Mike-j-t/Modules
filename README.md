# Modules
Modules for Android
Modules that have been written for Android development

## Emsg ##
An extended error message class.
An Emsg instance has 3 members 

- errorindicator (boolean true indicates and error, false if not)
- errornumber (Integer intended so that error's can be uniquely identified)
- errormessage (String the error message)

## DB???? Family ##
Modules that create a psuedo/base SQLite database scehma, ultimately creating
the dataabse structure or altering the database structure.

There are 4 core Classes **DBColumn**, **DBTAble**, **DBIndex**  and **DBDatabase**. 
Additionally Class **SQLKWORD**has some more commonly used SQL KeyWords defined as constants e.g.
SQLSELECT resolves to SELECT (with surrounding spaces)
SQLSELECTALLFROM resolves to SELECT * FROM
The intentional use is to reduce typing errors.

Typically you would use **DBColumn** to create the column onjects (once per column), 
then you would use **DBTable** to create the table objects, passing the relevant columns, 
you could then use DBIndex to create any additional indexes.

You would then use **DBDatabase** to create a base/pseudo schema, this being passed the **DBTable** list
and the **DBIndex** list.

You would then typically check that the schema is usable using the **isDatabaseUsable** method.
If the schema is usable you would then use the **actionDBBuildSQL** method to build the database.

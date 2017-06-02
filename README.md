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
Modules that create a psuedo SQLite database scehma from which the SQL
to build or alter the Database can be derived.

There are 3 core Classes **DBColumn**, **DBTAble** and **DBDatabase**. 
Additionally Class **SQLKWORD**
has some more commonly used SQL KeyWords defined as constants e.g.
SQLSELECT resolves to SELECT (with surrounding spaces)
SQLSELECTALLFROM resolves to SELECT * FROM
The intentional use is to reduce typing errors.

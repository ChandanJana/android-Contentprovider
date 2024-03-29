# android-Contentprovider

What is Content Provider?
Content Providers is one of the android application important component that serves the purpose of a relational database to store the data of applications. Content provider in the android system is like a “central repository” in which data of the applications are stored, and it allows other applications to securely access or supplies and even modifies that data based on the requirements.

Content Providers support the four basic operations, normally called CRUD-operations.

What is Content URI(Universal Resource Identifier)?
Content URI is the key concept of content provider. To query a content provider, you can specify the query string in the form of URI.

URI: content://authority/data_type/id

content:// →This is prefix of all the URI and it is mandatory part of URI.
authority →This part should be always unique for every content provider, it specify the name of content provider. for example contacts, browser etc.
data_type → This indicates the type of data provider by content provider.
id → it is a numeric value that is used to access particular record.
Operations of Content Provider are CRUD

CREATE : you can create a new data.
READ : you can access the available data.
UPDATE: you can modify the existing data.
DELETE: you can delete the data permanently from device as well.
How does Content Provider works internally?
Android applications UI components like Activities/Fragments use an object CursorLoader to send query requests to ContentResolver. The ContentResolver object sends requests like “create, read, update, and delete” to the ContentProvider as a client. After receiving a request, ContentProvider process it and returns the expected result.

package com.project.bookkeeper.DBManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "personal_Library.db";
    private static final int DATABASE_VERSION = 1;
    private static MyDBHelper me;
    private static Context ctxt;
    public static String booksOrderBy=" order by Favorite desc";

    public static MyDBHelper getInstance(Context context){
        if(me==null){
            me=new MyDBHelper(context);
            MyDBHelper.ctxt=context;
        }
        return me;
    }

    private MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*/String sql2= "DROP TABLE IF EXISTS book";
        //db.execSQL(sql2);
        //sql2= "DROP TABLE IF EXISTS category";
        //db.execSQL(sql2);
        //sql2= "DROP TABLE IF EXISTS book_category";
        //db.execSQL(sql2);

        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS book_category");*/
        String sql = "CREATE TABLE IF NOT EXISTS book (book_ID INTEGER PRIMARY KEY, " +
                "title TEXT, author TEXT, description TEXT, buyingDate TEXT, categories TEXT," +
                "status TEXT, favorite INTEGER, isArchived INTEGER, image mediumblob)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS person (person_ID INTEGER PRIMARY KEY, " +
                "first_Name TEXT, last_Name TEXT, phone TEXT, adress TEXT, email TEXT, isDeleted INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS lending (lending_ID INTEGER PRIMARY KEY, " +
                "book_ID INTEGER, person_ID INTEGER, starting_date TEXT, return_date TEXT, " +
                "isReturned INTEGER, isArchived INTEGER)";
        db.execSQL(sql);

        /*sql = "SELECT count(*) FROM category";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // Insert the data
                sql = "INSERT INTO category (name) VALUES ('Action'), ('Adult'), ('Adventure'), "+
                        "('Comedy'), ('Drama'), ('Fantasy'), ('Historical'), ('Horror'),"+
                        " ('Manga'), ('Martial Arts'), ('Mature'), ('Mecha'), ('Mystery'),"+
                        "('Psychological'), ('Romance'), ('School Life'), ('Sci-fi'),"+
                        " ('Slice of Life'), ('Sports'), ('Tragedy')";
                db.execSQL(sql);
            }
        }
        cursor.close();*/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Called when the database needs to be upgraded to a new version.
        // Update the schema, indices, and other database objects here.
    }
    @SuppressLint("Range")//for testing
    public void logAllBooks() {
        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all books
        String query = "SELECT * FROM book";
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the result set and log the books
        if (cursor.moveToFirst()) {
            do {
                int book_ID = cursor.getInt(cursor.getColumnIndex("book_ID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String buyingDate = cursor.getString(cursor.getColumnIndex("buyingDate"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                int favorite = cursor.getInt(cursor.getColumnIndex("favorite"));
                int isArchived = cursor.getInt(cursor.getColumnIndex("Archived"));
                Log.d("Book", "ID: " + book_ID + ", Title: " + title + ", Author: " + author + ", Description: " + description + ", Buying Date: " + buyingDate + ", Status: " + status + ", Favorite: " + favorite + ", Archived: " + isArchived);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }
    @SuppressLint("Range")
    public void logTableInfo() {
        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all tables in the database
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the result set and log the table info
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("Table", tableName);

                // Get the table info
                Cursor tableInfo = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

                // Iterate through the table info and log the column info
                if (tableInfo.moveToFirst()) {
                    do {
                        String columnName = tableInfo.getString(tableInfo.getColumnIndex("name"));
                        String columnType = tableInfo.getString(tableInfo.getColumnIndex("type"));
                        int notNull = tableInfo.getInt(tableInfo.getColumnIndex("notnull"));
                        Log.d("Column", columnName + " (" + columnType + ", Not Null: " + notNull + ")");
                    } while (tableInfo.moveToNext());
                }

                // Close the table info cursor
                tableInfo.close();
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }


    public long addBook(Book book) {
        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();


        // Create a ContentValues object
        ContentValues values = new ContentValues();
        values.put("title", book.title);
        values.put("author", book.author);
        values.put("description", book.description);
        values.put("buyingDate", book.buyingDate);
        values.put("categories", book.getCategories());
        values.put("status", book.status);
        values.put("favorite", book.favorite);
        values.put("isArchived", book.isArchived);

        // Insert the book into the books table
        int bookId =(int) db.insert("book", null, values);
        book.book_ID=bookId;

        // Close the database connection
        db.close();

        // Return the book ID
        return bookId;
    }
    public byte[] getImage(int bookId) {
        // Execute a SELECT statement to retrieve the image
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT image FROM book WHERE book_ID = "+bookId;
        Cursor cursor = db.rawQuery(sql, null);

        // Get the image data as a byte[] array
        byte[] imageBytes = null;
        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(0);
        }
        cursor.close();

        return imageBytes;
    }
    public void insertImage(int bookId, String filePath) throws IOException {
        // Read the image file into a byte[] array
        byte[] imageBytes = Files.readAllBytes(Paths.get(filePath));

        SQLiteDatabase db = getWritableDatabase();

        // Update the image column in the book table
        String sql = "UPDATE book SET image = ? WHERE book_ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindBlob(1, imageBytes);
        statement.bindLong(2, bookId);
        statement.executeUpdateDelete();
    }


    public void updateBook(Book book) {
        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object to hold the updated values
        ContentValues values = new ContentValues();
        values.put("title", book.title);
        values.put("author", book.author);
        values.put("description", book.description);
        values.put("buyingDate", book.buyingDate);
        values.put("categories", book.getCategories());
        values.put("status", book.status);
        //book.categories=Book.fromStringToList(cursor.getString(5));
        values.put("favorite", book.favorite ? 1 : 0);
        values.put("isArchived", book.isArchived ? 1 : 0);

        // Update the book in the database
        db.update("book", values, "book_ID = ?", new String[] { String.valueOf(book.book_ID) });

        // Close the database connection
        db.close();
    }
    public ArrayList<Book> loadBooks() {

        ArrayList<Book> books = new ArrayList<>();

        // Select all columns from the book table
        String sql = "SELECT * FROM book where isArchived!=1 "+booksOrderBy+", Favorite desc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Loop through the cursor and add each book to the list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.book_ID = cursor.getInt(0);
                book.title = cursor.getString(1);
                book.author = cursor.getString(2);
                book.description = cursor.getString(3);
                book.buyingDate = cursor.getString(4);
                book.categories=Book.fromStringToList(cursor.getString(5));
                book.status = cursor.getString(6);
                book.favorite = cursor.getInt(7) != 0;
                book.isArchived = cursor.getInt(8) != 0;

                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return books;
    }
    public ArrayList<Book> searchBooks(String category, String name, String status, boolean favorite, boolean isArchived) {
        ArrayList<Book> books = new ArrayList<>();

        // Join the book and book_category tables and select the book columns
        String sql = "SELECT * FROM book WHERE Archived = ";
        ArrayList<String> selectionArgs = new ArrayList<>();

        if (isArchived)
            sql += "1 ";
        else
            sql += "0 ";
        // Filter by name using the LIKE operator
        if (name != null) {
            sql += " AND book.title LIKE ?";
            selectionArgs.add("%" + name + "%");
        }
        if (status != null) {
            if (!status.equals(""))
                sql += " AND status = "+status;
        }
        // Filter by favorite status
        if (favorite)
            sql += " AND book.favorite = 1";
        sql += booksOrderBy+", order by favorite desc";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, selectionArgs.toArray(new String[0]));

        // Loop through the cursor and add each book to the list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.book_ID = cursor.getInt(0);
                book.title = cursor.getString(1);
                book.author = cursor.getString(2);
                book.description = cursor.getString(3);
                book.buyingDate = cursor.getString(4);
                book.status = cursor.getString(5);
                book.favorite = cursor.getInt(6) == 1;
                book.isArchived = cursor.getInt(7) == 1;
                books.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return books;
    }
    public boolean updateBookStatus(Book book, String status) {
        if(book.status.equals(status))
            return false;
        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object to hold the updated values
        ContentValues values = new ContentValues();
        values.put("status", status);

        // Update the book in the database
        db.update("book", values, "book_ID = ?", new String[] { String.valueOf(book.book_ID) });

        // Close the database connection
        db.close();
        return true;
    }
    @SuppressLint("Range")
    public ArrayList<Book> getLendedBooksForPerson(int person_ID) {
        ArrayList<Book> books = new ArrayList<>();

        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all books that are currently lended to the person
        String query = "SELECT book.* FROM book INNER JOIN lending ON book.book_ID = "+
                "lending.book_ID WHERE lending.person_ID = ? AND lending.isReturned = 0 AND"+
                " lending.isArchived = 0"+booksOrderBy+", Favorite desc";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(person_ID) });

        // Iterate through the result set and add the books to the list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.book_ID = cursor.getInt(0);
                book.title = cursor.getString(1);
                book.author = cursor.getString(2);
                book.description = cursor.getString(3);
                book.buyingDate = cursor.getString(4);
                book.categories=Book.fromStringToList(cursor.getString(5));
                book.status = cursor.getString(6);
                book.favorite = cursor.getInt(7) == 1;
                book.isArchived = cursor.getInt(8) == 1;
                books.add(book);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return books;
    }
    public Book getBook(int bookId) {
        // Execute a SELECT statement to retrieve the book
        String sql = "SELECT * FROM book WHERE book_ID = "+bookId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Get the values for each column of the book table
        Book book = new Book();
        if (cursor.moveToFirst()) {
            book.book_ID = cursor.getInt(0);
            book.title = cursor.getString(1);
            book.author = cursor.getString(2);
            book.description = cursor.getString(3);
            book.buyingDate = cursor.getString(4);
            book.categories=Book.fromStringToList(cursor.getString(5));
            book.status = cursor.getString(6);
            book.favorite = cursor.getInt(7) != 0;
            book.isArchived = cursor.getInt(8) != 0;
        }
        cursor.close();

        return book;
    }

    public int addLending(Lending lending) {

        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object to hold the values
        ContentValues values = new ContentValues();
        values.put("book_ID", lending.book_ID);
        values.put("person_ID", lending.person_ID);
        values.put("starting_date", lending.starting_date);
        values.put("return_date", lending.return_date);
        values.put("isReturned", lending.isReturned ? 1 : 0);
        values.put("isArchived", lending.isArchived ? 1 : 0);

        // Insert the lending into the database
        int lendingId=(int)db.insert("lending", null, values);
        lending.lending_ID=lendingId;
        // Close the database connection
        db.close();
        return lendingId;
    }
    public void updateLending(Lending lending) {
        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object to hold the updated values
        ContentValues values = new ContentValues();
        values.put("book_ID", lending.book_ID);
        values.put("person_ID", lending.person_ID);
        values.put("starting_date", lending.starting_date);
        values.put("return_date", lending.return_date);
        values.put("isReturned", lending.isReturned ? 1 : 0);
        values.put("isArchived", lending.isArchived ? 1 : 0);

        // Update the lending in the database
        db.update("lending", values, "lending_ID = ?", new String[] { String.valueOf(lending.lending_ID) });

        // Close the database connection
        db.close();
    }
    public ArrayList<Lending> getAllLendings() {
        ArrayList<Lending> lendings = new ArrayList<>();

        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all lendings where the return date is today, or the return date has passed but isReturned and isArchived are both false
        String query = "SELECT * FROM lending where isArchived!=1 order by lending_ID desc";
        Cursor cursor = db.rawQuery(query, null);


        // Iterate through the result set and add the lendings to the list
        if (cursor.moveToFirst()) {
            do {
                Lending lending = new Lending();
                lending.lending_ID = cursor.getInt(0);
                lending.book_ID = cursor.getInt(1);
                lending.person_ID = cursor.getInt(2);
                lending.starting_date = cursor.getString(3);
                lending.return_date = cursor.getString(4);
                lending.isReturned = cursor.getInt(5) == 1;
                lending.isArchived = cursor.getInt(6) == 1;
                lendings.add(lending);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lendings;
    }
    public ArrayList<Lending> getOverdueLendings() {
        ArrayList<Lending> lendings = new ArrayList<>();

        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all lendings where the return date is today, or the return date has passed but isReturned and isArchived are both false
        String query = "SELECT * FROM lending WHERE date(return_date) <= date('now') AND isReturned = 0";
        Cursor cursor = db.rawQuery(query, null);


        // Iterate through the result set and add the lendings to the list
        if (cursor.moveToFirst()) {
            do {
                Lending lending = new Lending();
                lending.lending_ID = cursor.getInt(0);
                lending.book_ID = cursor.getInt(1);
                lending.person_ID = cursor.getInt(2);
                lending.starting_date = cursor.getString(3);
                lending.return_date = cursor.getString(4);
                lending.isReturned = cursor.getInt(5) == 1;
                lending.isArchived = cursor.getInt(6) == 1;
                lendings.add(lending);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lendings;
    }
    public List<Lending> getLendingsForPerson(int person_ID) {
        List<Lending> lendings = new ArrayList<>();

        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all lendings for the given person
        String query = "SELECT * FROM lending WHERE person_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(person_ID) });

        // Iterate through the result set and add the lendings to the list
        if (cursor.moveToFirst()) {
            do {
                Lending lending = new Lending();
                lending.lending_ID = cursor.getInt(0);
                lending.book_ID = cursor.getInt(1);
                lending.person_ID = cursor.getInt(2);
                lending.starting_date = cursor.getString(3);
                lending.return_date = cursor.getString(4);
                lending.isReturned = cursor.getInt(5) == 1;
                lending.isArchived = cursor.getInt(6) == 1;
                lendings.add(lending);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Return the list of lendings
        return lendings;
    }
    public void archiveLendingsP(int personId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isArchived", 1);
        String whereClause = "person_id = ?";
        String[] whereArgs = { String.valueOf(personId) };
        int rowsUpdated = db.update("lending", values, whereClause, whereArgs);
    }
    public void archiveLendingsB(int bookId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isArchived", 1);
        String whereClause = "book_id = ?";
        String[] whereArgs = { String.valueOf(bookId) };
        int rowsUpdated = db.update("lending", values, whereClause, whereArgs);
    }

    public long addPerson(Person person) {
        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object to hold the values to be inserted
        ContentValues values = new ContentValues();
        values.put("first_Name", person.first_Name);
        values.put("last_Name", person.last_Name);
        values.put("phone", person.phone);
        values.put("adress", person.adress);
        values.put("email", person.email);
        values.put("isDeleted", person.isDeleted ? 1 : 0);

        // Insert the person into the database and return the ID
        long person_ID = db.insert("person", null, values);

        // Close the database connection
        db.close();

        return person_ID;
    }
    public int updatePerson(Person person) {
        // Get a writable database
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object to hold the values to be updated
        ContentValues values = new ContentValues();
        values.put("first_Name", person.first_Name);
        values.put("last_Name", person.last_Name);
        values.put("phone", person.phone);
        values.put("adress", person.adress);
        values.put("email", person.email);
        values.put("isDeleted", person.isDeleted ? 1 : 0);

        // Update the person in the database and return the number of rows affected
        int rows = db.update("person", values, "person_ID = ?", new String[] { String.valueOf(person.getPerson_ID()) });

        // Close the database connection
        db.close();

        return rows;
    }
    public Person getPerson(int personId) {
        // Execute a SELECT statement to retrieve the book
        String sql = "SELECT * FROM book WHERE book_ID = "+personId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Get the values for each column of the book table
        Person person = new Person();
        if (cursor.moveToFirst()) {
            person.person_ID = cursor.getInt(0);
            person.first_Name = cursor.getString(1);
            person.last_Name = cursor.getString(2);
            person.phone = cursor.getString(3);
            person.adress = cursor.getString(4);
            person.email = cursor.getString(5);
            person.isDeleted = cursor.getInt(6) == 1;
        }
        cursor.close();

        return person;
    }
    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> persons = new ArrayList<>();

        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Select all persons
        String query = "SELECT * FROM person where isDeleted!=1";
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the result set and add the persons to the list
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.person_ID = cursor.getInt(0);
                person.first_Name = cursor.getString(1);
                person.last_Name = cursor.getString(2);
                person.phone = cursor.getString(3);
                person.adress = cursor.getString(4);
                person.email = cursor.getString(5);
                person.isDeleted = cursor.getInt(6) == 1;
                persons.add(person);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Return the list of persons
        return persons;
    }
}

package com.project.bookkeeper.DBManager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Book {
    protected int book_ID;
    public String title, author, description, buyingDate, status;
    public ArrayList<String> categories;
    public boolean favorite, isArchived;
    public Book(){}
    public Book(int book_ID, String title, String author, String description, String buyingDate,
                String status, String categories, boolean favorite, boolean isArchived) {
        this.book_ID = book_ID;
        this.title = title;
        this.author = author;
        this.description = description;
        this.buyingDate = buyingDate;
        this.status = status;
        this.categories =fromStringToList(categories);
        this.favorite = favorite;
        this.isArchived = isArchived;
    }

    public int getBook_ID() {
        return book_ID;
    }

    public String getCategories(){
        String output="";
        for(String s : categories)
            output+=s+" ";
        Log.d("categories : ", output);
        return output;
    }
    public static ArrayList<String> fromStringToList(String s){
        ArrayList output = new ArrayList<>();
        String[] strarray = s.split(" ");
        for(String elm: strarray)
            output.add(elm);
        return output;
    }
    public void uploadImage(String path, Context context) throws IOException{
        File file = new File(path);
        byte[] image = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(image);
        fis.close();
        // Generate the file name using the book_ID
        String fileName = "book" + book_ID + ".jpg";

        File directory = context.getExternalFilesDir(null);

        // Create the directory if it does not already exist
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Create the file in the specified repository
        File file2 = new File(directory, fileName);
        FileOutputStream fos = new FileOutputStream(file2);

        // Save the image to the file
        fos.write(image);
        fos.close();
    }
    public byte[] getImage(Context context) throws IOException {
        // Generate the file name using the book_ID
        String fileName = "book" + book_ID + ".jpg";
        // Get the absolute file path of the external storage directory
        File directory = context.getExternalFilesDir(null);

        // Create the directory if it does not already exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file in the external storage directory
        File file = new File(directory, fileName);

        if (file.exists()) {
            // Read the image file into a byte array
            byte[] image = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(image);
            fis.close();
            return image;
        }
        return null;// or default image ?
    }

}

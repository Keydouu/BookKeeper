package com.project.bookkeeper.DBManager;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Person {
    protected int person_ID;
    public String first_Name, last_Name, phone, adress, email;
    public boolean isDeleted;

    protected  Person(){}

    public Person(int person_ID, String first_Name, String last_Name, String phone, String adress, String email) {
        this.person_ID = person_ID;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.phone = phone;
        this.adress = adress;
        this.email = email;
        this.isDeleted=false;
    }
    protected Person(int person_ID, String first_Name, String last_Name, String phone, String adress, String email, boolean isDeleted) {
        this.person_ID = person_ID;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.phone = phone;
        this.isDeleted=isDeleted;
    }
    public int getPerson_ID() {
        return person_ID;
    }
    public void uploadImage(String path, Context context) throws IOException{
        File file = new File(path);
        byte[] image = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(image);
        fis.close();
        // Generate the file name using the person_ID
        String fileName = "friend" + person_ID + ".jpg";

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
        // Generate the file name using the person_ID
        String fileName = "friend" + person_ID + ".jpg";
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
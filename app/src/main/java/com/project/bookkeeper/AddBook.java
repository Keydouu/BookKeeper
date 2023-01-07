package com.project.bookkeeper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.MyDBHelper;

import java.io.IOException;

public class AddBook extends AppCompatActivity {

    ImageButton imageButton;
    int SELECT_IMAGE_CODE=1;
    String imagePath ;
    String fullFilePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        EditText title = findViewById(R.id.add_title);
        EditText author = findViewById(R.id.add_author);
        EditText description = findViewById(R.id.add_description);
        CheckBox cat1 = findViewById(R.id.add_cat1);
        CheckBox cat2 = findViewById(R.id.add_cat2);
        CheckBox cat3 = findViewById(R.id.add_cat3);

        //Adding
        Button addButton = findViewById(R.id.btn_add_book);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categories=cat1.isChecked()+" "+cat2.isChecked()+" "+cat3.isChecked()+" ";

                Book b = new Book(0,
                        title.getText().toString(),
                        author.getText().toString(),
                        description.getText().toString(),
                        UriUtils.getTodaysDate(),
                        "Available",
                        categories,
                        false,
                        false
                        );

                MyDBHelper.getInstance(null).addBook(b);
                Intent intent = new Intent(AddBook.this, MyBooks.class);
                startActivity(intent);

            }
        });


        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_IMAGE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Uri uri = data.getData();
            imageButton.setImageURI(uri);
            imagePath = uri.getPath();
            fullFilePath = UriUtils.getPathFromUri(this,uri);

        }
        else{
            finish();
        }
    }
}
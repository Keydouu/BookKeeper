package com.project.bookkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.MyDBHelper;

public class UpdateBook extends AppCompatActivity {

    Button addButton;

    public static Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        TextView title = findViewById(R.id.update_title);
        TextView author = findViewById(R.id.update_author);
        TextView description = findViewById(R.id.update_description);
        CheckBox cat1 = findViewById(R.id.update_cat1);
        CheckBox cat2 = findViewById(R.id.update_cat2);
        CheckBox cat3 = findViewById(R.id.update_cat3);


        title.setText(book.title);
        author.setText(book.author);
        description.setText(book.description);
        String[] cats=book.getCategories().split(" ");
        cat1.setChecked(cats[0].equals("true"));
        cat2.setChecked(cats[1].equals("true"));
        cat3.setChecked(cats[2].equals("true"));

        Button updateB = findViewById(R.id.btn_update_book);
        Button deleteB = findViewById(R.id.btn_delete_book);

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.title=title.getText().toString();
                book.author=author.getText().toString();
                book.description=description.getText().toString();
                String categories=cat1.isChecked()+" "+cat2.isChecked()+" "+cat3.isChecked()+" ";
                book.categories=Book.fromStringToList(categories);
                MyDBHelper.getInstance(null).updateBook(book);

                Intent intent = new Intent(UpdateBook.this, MyBooks.class);
                startActivity(intent);
                Toast.makeText(UpdateBook.this, "Book Updated !", Toast.LENGTH_SHORT).show();
            }
        });

        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.isArchived=true;
                MyDBHelper.getInstance(null).updateBook(book);
                MyDBHelper.getInstance(null).archiveLendingsB(book.getBook_ID());

                Intent intent = new Intent(UpdateBook.this, MyBooks.class);
                startActivity(intent);
                Toast.makeText(UpdateBook.this, "Book Deleted !", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
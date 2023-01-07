package com.project.bookkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.io.IOException;

public class AddPerson extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        Button addButton = findViewById(R.id.btn_add_person);

        EditText name = findViewById(R.id.add_name);
        EditText lastName = findViewById(R.id.add_last);
        EditText phone = findViewById(R.id.add_phone);
        EditText adress = findViewById(R.id.add_adress);
        EditText email = findViewById(R.id.add_email);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = new Person(0, name.getText().toString(), lastName.getText().toString(),
                        phone.getText().toString(), adress.getText().toString(), email.getText().toString());

                MyDBHelper.getInstance(null).addPerson(p);
                Intent intent = new Intent(AddPerson.this, People.class);
                startActivity(intent);
            }
        });
    }
}
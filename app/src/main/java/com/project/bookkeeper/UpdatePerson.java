package com.project.bookkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

public class UpdatePerson extends AppCompatActivity {

    public static Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_person);

        TextView name = findViewById(R.id.update_name);
        TextView last = findViewById(R.id.update_last);
        TextView phone = findViewById(R.id.update_phone);
        TextView adress = findViewById(R.id.update_adress);
        TextView email = findViewById(R.id.update_email);

        Button update = findViewById(R.id.btn_update_person);
        Button delete = findViewById(R.id.btn_delete_person);

        name.setText(person.first_Name);
        last.setText(person.last_Name);
        phone.setText(person.phone);
        adress.setText(person.adress);
        email.setText(person.email);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.first_Name=name.getText().toString();
                person.last_Name=last.getText().toString();
                person.phone=phone.getText().toString();
                person.adress=adress.getText().toString();
                person.email=email.getText().toString();

                MyDBHelper.getInstance(null).updatePerson(person);

                Intent intent = new Intent(UpdatePerson.this, People.class);
                startActivity(intent);
                Toast.makeText(UpdatePerson.this, "Person Updated !", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.isDeleted=true;
                MyDBHelper.getInstance(null).updatePerson(person);
                MyDBHelper.getInstance(null).archiveLendingsP(person.getPerson_ID());
                Intent intent = new Intent(UpdatePerson.this, People.class);
                startActivity(intent);
                Toast.makeText(UpdatePerson.this, "Person Deleted !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
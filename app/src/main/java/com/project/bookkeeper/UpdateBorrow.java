package com.project.bookkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.Lending;
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateBorrow extends AppCompatActivity {

    static Lending l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_borrow);

        //Book bookObject = MyDBHelper.getInstance(null).getBook(l.getBook_ID());
        //Person personObject = MyDBHelper.getInstance(null).getPerson(l.getPerson_ID());



        Spinner book = findViewById(R.id.update_spinner_book);
        Spinner person = findViewById(R.id.update_spinner_person);
        Button update = findViewById(R.id.btn_update_borrow);
        Button delete = findViewById(R.id.btn_delete_borrow);


        // on below line we are initializing our variables.
        Button pickDateBtn = findViewById(R.id.update_start_date);
        TextView start = findViewById(R.id.show_start_update);

        // on below line we are adding click listener for our pick date button
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        UpdateBorrow.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        // on below line we are initializing our variables.
        Button pickDateBtnEnd = findViewById(R.id.update_end_date);
        TextView end = findViewById(R.id.show_end_update);

        // on below line we are adding click listener for our pick date button
        pickDateBtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        UpdateBorrow.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                end.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        ArrayList<Book> books=MyDBHelper.getInstance(null).loadBooks();
        int i=0;
        String[] itemsB = new String[books.size()];
        int personIndex=0, bookIndex=0;


        for(Book b : books){
            itemsB[i] =b.getBook_ID()+"-> " +b.title;
            if(b.getBook_ID()==l.book_ID)
                bookIndex=i;
            i++;
        }

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterB = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsB);
        //set the spinners adapter to the previously created one.
        book.setAdapter(adapterB);

        ArrayList<Person>persons=MyDBHelper.getInstance(null).getAllPersons();
        i=0;
        String[] itemsP = new String[persons.size()];


        for(Person p : persons){
            itemsP[i] =p.getPerson_ID()+"-> "+ p.first_Name+" "+p.last_Name;
            if(p.getPerson_ID()==l.person_ID)
                personIndex=i;
            i++;
        }
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsP);
        //set the spinners adapter to the previously created one.
        person.setAdapter(adapterP);

        book.setSelection(bookIndex);
        person.setSelection(personIndex);

        start.setText(l.starting_date);
        end.setText(l.return_date);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] str1=book.getSelectedItem().toString().split("-> ");
                String[] str2=person.getSelectedItem().toString().split("-> ");

                l.book_ID=Integer.parseInt(str1[0]);
                l.person_ID=Integer.parseInt(str2[0]);
                l.starting_date=start.getText().toString();
                l.return_date=end.getText().toString();
                MyDBHelper.getInstance(null).updateLending(l);

                Intent intent = new Intent(UpdateBorrow.this, Borrowing.class);
                startActivity(intent);
                Toast.makeText(UpdateBorrow.this, "Borrow Updated !", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.isArchived=true;
                MyDBHelper.getInstance(null).updateLending(l);

                Intent intent = new Intent(UpdateBorrow.this, Borrowing.class);
                startActivity(intent);
                Toast.makeText(UpdateBorrow.this, "Borrow deleted !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.project.bookkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.Lending;
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddBorrow extends AppCompatActivity {

    private Button pickDateBtn;
    private TextView selectedDateTV;

    private Button pickDateBtnEnd;
    private TextView selectedDateTVEnd;

    Button addBorrow ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_borrow);




        // on below line we are initializing our variables.
        pickDateBtn = findViewById(R.id.borrow_start_date);
        selectedDateTV = findViewById(R.id.show_start);

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
                        AddBorrow.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                selectedDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
        pickDateBtnEnd = findViewById(R.id.borrow_end_date);
        selectedDateTVEnd = findViewById(R.id.show_end);

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
                        AddBorrow.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                selectedDateTVEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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






        //get the spinner from the xml.
        Spinner dropdownB = findViewById(R.id.borrow_spinner_book);
        //create a list of items for the spinner.
        //ArrayList<Person> p=MyDBHelper.getInstance(null).getAllPersons();
        ArrayList<Book>books=MyDBHelper.getInstance(null).loadBooks();
        int i=0;
        String[] itemsB = new String[books.size()];

        for(Book b : books){
            itemsB[i] =b.getBook_ID()+"-> " +b.title;
            i++;
        }

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterB = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsB);
        //set the spinners adapter to the previously created one.
        dropdownB.setAdapter(adapterB);

        //get the spinner from the xml.
        Spinner dropdownP = findViewById(R.id.borrow_spinner_person);
        //create a list of items for the spinner.

        ArrayList<Person>persons=MyDBHelper.getInstance(null).getAllPersons();
        i=0;
        String[] itemsP = new String[persons.size()];

        for(Person p : persons){
            itemsP[i] =p.getPerson_ID()+"-> "+ p.first_Name+" "+p.last_Name;
            i++;
        }
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsP);
        //set the spinners adapter to the previously created one.
        dropdownP.setAdapter(adapterP);

        Spinner bookName = findViewById(R.id.borrow_spinner_book);
        Spinner personName = findViewById(R.id.borrow_spinner_person);

        addBorrow = findViewById(R.id.btn_add_borrow);
        addBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] str1=bookName.getSelectedItem().toString().split("-> ");
                String[] str2=personName.getSelectedItem().toString().split("-> ");
                if(!selectedDateTV.getText().toString().equals("Start Date")&&!selectedDateTVEnd.getText().toString().equals("End Date")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Date d1 = null;
                    try {
                        d1 = sdf.parse(selectedDateTV.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date d2 = null;
                    try {
                        d2 = sdf.parse(selectedDateTVEnd.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(d1.compareTo(d2)<=0 ){
                        Lending l = new Lending(0, Integer.parseInt(str1[0]), Integer.parseInt(str2[0]),
                                selectedDateTV.getText().toString(), selectedDateTVEnd.getText().toString());
                        MyDBHelper.getInstance(null).addLending(l);

                        Intent intent = new Intent(AddBorrow.this, Borrowing.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(AddBorrow.this, "Start Date must be before the End Date !", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(AddBorrow.this, "Please Select a date !", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
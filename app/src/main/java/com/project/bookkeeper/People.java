package com.project.bookkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.util.ArrayList;
import java.util.List;

public class People extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton addBook;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        ArrayList<Person> people= MyDBHelper.getInstance(People.this).getAllPersons();
        recyclerView = findViewById(R.id.recyclerViewPeople);
        recyclerView.setLayoutManager(new LinearLayoutManager(People.this));
        PersonListElement ple=new PersonListElement(people);
        recyclerView.setAdapter(ple);

        recyclerView = findViewById(R.id.recyclerViewPeople);
        addBook = findViewById(R.id.fab_add_person);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(People.this, AddPerson.class);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.peoplelayout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_mybooks: {
                        Intent intent = new Intent(People.this, MyBooks.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.nav_borrowing: {
                        Intent intent = new Intent(People.this, Borrowing.class);
                        startActivity(intent);
                    }
                    case R.id.nav_people: {
                        Toast.makeText(People.this, "Already In", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });

    }
}
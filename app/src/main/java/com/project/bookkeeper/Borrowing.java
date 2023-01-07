package com.project.bookkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.Lending;
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.util.ArrayList;

public class Borrowing extends AppCompatActivity {

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
        setContentView(R.layout.activity_borrowing);
        ArrayList<Lending> lendingsArray = MyDBHelper.getInstance(Borrowing.this).getAllLendings();
        recyclerView = findViewById(R.id.recyclerViewBorrow);
        recyclerView.setLayoutManager(new LinearLayoutManager(Borrowing.this));
        LendingListElement lle = new LendingListElement(lendingsArray);
        recyclerView.setAdapter(lle);

        recyclerView = findViewById(R.id.recyclerViewBorrow);
        addBook = findViewById(R.id.fab_add_borrow);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Book> books = MyDBHelper.getInstance(null).loadBooks();
                ArrayList<Person>persons=MyDBHelper.getInstance(null).getAllPersons();
                if(books.size()!=0 ) {
                    if(persons.size()!=0){
                        Intent intent = new Intent(Borrowing.this, AddBorrow.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Borrowing.this, "You need to add at least 1 Person !", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(Borrowing.this, "You need to add at least 1 Book !", Toast.LENGTH_LONG).show();
                }
            }
        });

        drawerLayout = findViewById(R.id.borrowinglayout);
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
                        Intent intent = new Intent(Borrowing.this, MyBooks.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.nav_borrowing: {
                        Toast.makeText(Borrowing.this, "Already In", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_people: {
                        Intent intent = new Intent(Borrowing.this, People.class);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
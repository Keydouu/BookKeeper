package com.project.bookkeeper;

import static java.security.AccessController.getContext;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.MyDBHelper;

import java.util.ArrayList;

public class MyBooks extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addBook;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ArrayList<Book>  books;
    BookListElement ble;


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
        setContentView(R.layout.activity_my_books);

        /*Book b = new Book(0, "Title test 1", "Youness",
                "A book guiding to knowledge", "2020-03-30",
                "Owned", "Mystery", true, false);
        MyDBHelper.getInstance(MyBooks.this).addBook(b);
        Book b2 = new Book(1, "start with when", "Einstein",
                "time management book", "2023-01-01",
                "Owned", "Adventure", false, false);
        MyDBHelper.getInstance(MyBooks.this).addBook(b2);*/
        books =MyDBHelper.getInstance(MyBooks.this).loadBooks();
        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyBooks.this));
        ble = new BookListElement(books);
        recyclerView.setAdapter(ble);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        addBook = findViewById(R.id.fab_add_book);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyBooks.this, AddBook.class);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.mybookslayout);
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
                        Toast.makeText(MyBooks.this, "Already In", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_borrowing: {
                        Intent intent = new Intent(MyBooks.this, Borrowing.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.nav_people: {
                        Intent intent = new Intent(MyBooks.this, People.class);
                        startActivity(intent);
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

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.books_bottom_sheet_dialog);

        LinearLayout sortAZ = bottomSheetDialog.findViewById(R.id.sortAZ);
        LinearLayout sortZA = bottomSheetDialog.findViewById(R.id.sortZA);
        LinearLayout dateDown = bottomSheetDialog.findViewById(R.id.dateDown);
        LinearLayout dateUp = bottomSheetDialog.findViewById(R.id.dateUp);

        sortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHelper.booksOrderBy=" Order by title asc";
                reloadBooks();
                bottomSheetDialog.dismiss();
            }
        });

        sortZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHelper.booksOrderBy=" Order by title desc";
                reloadBooks();
                bottomSheetDialog.dismiss();
            }
        });

        dateDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHelper.booksOrderBy=" Order by date(buyingDate) desc";
                reloadBooks();
                bottomSheetDialog.dismiss();
            }
        });

        dateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHelper.booksOrderBy=" Order by date(buyingDate) asc";
                reloadBooks();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
    public void reloadBooks(){
        books = MyDBHelper.getInstance(null).loadBooks();
        //searchBooks(category, name, status, favorite, isArchived) ;
        ble = new BookListElement(books);
        recyclerView.setAdapter(ble);
    }

}
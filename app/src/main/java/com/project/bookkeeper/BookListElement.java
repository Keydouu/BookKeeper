package com.project.bookkeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.MyDBHelper;

import java.util.ArrayList;


public class BookListElement extends RecyclerView.Adapter<BookListElement.MyViewHolder> {

    private ArrayList<Book> books;
    public BookListElement(ArrayList<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_book, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Book b = books.get(position);
        //holder.book_id_txt.setText(String.valueOf(b.getBook_ID()));
        //b.getCategories();
        holder.row_book_title.setText(String.valueOf(b.title));
        holder.row_book_author.setText(String.valueOf(b.author));
        //Bitmap bitmap = BitmapFactory.decodeByteArray(b.getImage(get), 0, imageBytes.length);
        //R.setImageBitmap(bitmap);
        ImageView imageButton = holder.row_book_img;
        byte[] imageBytes = MyDBHelper.getInstance(null).getImage(b.getBook_ID());
        if(imageBytes!=null){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageButton.setImageBitmap(bitmap);}
        if(b.favorite){
            holder.row_book_favorite.setImageResource(R.drawable.ic_favorite);
        }
        else{
            holder.row_book_favorite.setImageResource(R.drawable.ic_favorite_null);
        }
        //holder.row_book_img.setImageResource(String.valueOf(b.));

        //holder.book_pages_txt.setText(String.valueOf(b.favorite));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBook.book=b;

                Intent intent = new Intent(MainActivity.conteeext, UpdateBook.class);
                MainActivity.activityyy.startActivityForResult(intent, 1);
            }
        });
        holder.row_book_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.favorite=!b.favorite;
                MyDBHelper.getInstance(null).updateBook(b);
                MainActivity.activityyy.finish();
                MainActivity.activityyy.startActivity(new Intent(MainActivity.conteeext, MyBooks.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        TextView row_book_title, row_book_author;
        ImageView row_book_img;
        ImageButton row_book_favorite;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //book_id_txt = itemView.findViewById(R.id.book_id_txt);
            row_book_title = itemView.findViewById(R.id.row_book_title);
            row_book_author = itemView.findViewById(R.id.row_book_author);
            row_book_img = itemView.findViewById(R.id.row_book_img);
            row_book_favorite=itemView.findViewById(R.id.row_book_favorite);
            //book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            //Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate);
            //mainLayout.setAnimation(translate_anim);
        }

    }

}
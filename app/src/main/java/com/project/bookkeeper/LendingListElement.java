package com.project.bookkeeper;


import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.project.bookkeeper.DBManager.Book;
import com.project.bookkeeper.DBManager.Lending;
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.util.ArrayList;


public class LendingListElement extends RecyclerView.Adapter<LendingListElement.MyViewHolder> {

    private ArrayList<Lending> LendingsList;

    public LendingListElement(ArrayList<Lending> l){
        this.LendingsList = l;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_borrow, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Lending l = LendingsList.get(position);
        Book lendedBook=MyDBHelper.getInstance(null).getBook(l.book_ID);
        holder.row_borrow_title.setText(lendedBook.title);
        holder.row_date_start.setText(l.starting_date);
        holder.row_date_end.setText(l.return_date);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(b.getImage(get), 0, imageBytes.length);
        //R.setImageBitmap(bitmap);
        //holder.row_book_img.setImageResource(String.valueOf(b.));

        //holder.book_pages_txt.setText(String.valueOf(b.favorite));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBorrow.l=l;
                Intent intent = new Intent(MainActivity.conteeext, UpdateBorrow.class);
                MainActivity.activityyy.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return LendingsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        TextView row_borrow_title, row_date_start, row_date_end;
        ImageView row_book_img;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //book_id_txt = itemView.findViewById(R.id.book_id_txt);
            row_borrow_title = itemView.findViewById(R.id.row_borrow_title);
            row_date_start = itemView.findViewById(R.id.row_date_start);
            row_date_end = itemView.findViewById(R.id.row_date_end);
            //row_book_img = itemView.findViewById(R.id.row_book_img);
            //book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            //Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate);
            //mainLayout.setAnimation(translate_anim);
        }

    }

}
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
import com.project.bookkeeper.DBManager.MyDBHelper;
import com.project.bookkeeper.DBManager.Person;

import java.util.ArrayList;


public class PersonListElement extends RecyclerView.Adapter<PersonListElement.MyViewHolder> {

    private ArrayList<Person> persons;

    public PersonListElement(ArrayList<Person> persons){
        this.persons = persons;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_person, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Person p = persons.get(position);
        holder.row_person_name.setText(String.valueOf(p.first_Name +" "+p.last_Name));
        holder.row_person_phone.setText(String.valueOf(p.phone));
        //Bitmap bitmap = BitmapFactory.decodeByteArray(b.getImage(get), 0, imageBytes.length);
        //R.setImageBitmap(bitmap);
        //holder.row_book_img.setImageResource(String.valueOf(b.));

        //holder.book_pages_txt.setText(String.valueOf(b.favorite));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePerson.person=p;

                Intent intent = new Intent(MainActivity.conteeext, UpdatePerson.class);
                MainActivity.activityyy.startActivityForResult(intent, 1);

            }
        });


    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        TextView row_person_name, row_person_phone;
        ImageView row_book_img;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //book_id_txt = itemView.findViewById(R.id.book_id_txt);
            row_person_name = itemView.findViewById(R.id.row_person_name);
            row_person_phone = itemView.findViewById(R.id.row_person_phone);
            //row_book_img = itemView.findViewById(R.id.row_book_img);
            //book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            //Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate);
            //mainLayout.setAnimation(translate_anim);
        }

    }

}
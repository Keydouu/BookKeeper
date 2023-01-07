package com.project.bookkeeper.DBManager;

public class Lending {
    protected int lending_ID;
    public int book_ID, person_ID;
    public String starting_date, return_date;
    public boolean isReturned, isArchived;

    protected Lending(){}

    protected Lending(int lending_ID, int book_ID, int person_ID, String starting_date, String return_date, boolean isReturned, boolean isArchived) {
        this.lending_ID = lending_ID;
        this.book_ID = book_ID;
        this.person_ID = person_ID;
        this.starting_date = starting_date;
        this.return_date = return_date;
        this.isReturned = isReturned;
        this.isArchived=isArchived;
    }
    public Lending(int lending_ID, int book_ID, int person_ID, String starting_date, String return_date) {
        this.lending_ID = lending_ID;
        this.book_ID = book_ID;
        this.person_ID = person_ID;
        this.starting_date = starting_date;
        this.return_date = return_date;
        this.isReturned = false;
        this.isArchived=false;
    }

    public int getLending_ID() {
        return lending_ID;
    }
    public int getBook_ID() {
        return book_ID;
    }
    public int getPerson_ID() {
        return person_ID;
    }
}

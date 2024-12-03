package hu.nje.libraryapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

import hu.nje.libraryapp.model.Book;

@Dao
public interface BookDao {

    @Insert
    void insert(Book book);

    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks();

    @Delete
    void delete(Book book); // Törlés metódus
}
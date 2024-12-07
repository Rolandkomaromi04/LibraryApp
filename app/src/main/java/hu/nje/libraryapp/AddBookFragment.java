package hu.nje.libraryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.concurrent.Executors;

import hu.nje.libraryapp.database.BookDatabase;
import hu.nje.libraryapp.model.Book;

public class AddBookFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextAuthor;
    private Button buttonAddBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment layout betöltése
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        // UI elemek inicializálása
        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextAuthor = view.findViewById(R.id.edit_text_author);
        buttonAddBook = view.findViewById(R.id.button_add_book);

        // Gomb kattintás esemény
        buttonAddBook.setOnClickListener(v -> addBook());

        return view;
    }

    private void addBook() {
        String title = editTextTitle.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Új könyv létrehozása
        Book newBook = new Book(title, author);

        // Adatbázisművelet háttérszálon
        Executors.newSingleThreadExecutor().execute(() -> {
            BookDatabase.getInstance(getContext()).bookDao().insert(newBook);

            // Navigálás vissza a lista fragmentre a fő szálon
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Book added successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            });
        });

        // Mezők ürítése
        editTextTitle.setText("");
        editTextAuthor.setText("");
    }
}
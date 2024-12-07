package hu.nje.libraryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.nje.libraryapp.adapter.BookAdapter;
import hu.nje.libraryapp.database.BookDatabase;
import hu.nje.libraryapp.model.Book;

public class BookListFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Engedélyezzük a menüt (SearchView használatához)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment layout betöltése
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // RecyclerView inicializálása
        recyclerView = view.findViewById(R.id.recycler_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter inicializálása
        adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);

        // Törlés művelet kezelése az adapterben
        adapter.setOnDeleteClickListener(book -> {
            BookDatabase database = BookDatabase.getInstance(getContext());
            // Törlés az adatbázisból külön szálon
            new Thread(() -> {
                database.bookDao().delete(book);
            }).start();
        });

        // Adatok betöltése az adatbázisból
        BookDatabase database = BookDatabase.getInstance(getContext());
        database.bookDao().getAllBooks().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                adapter.setBooks(books); // Adapter frissítése
            }
        });

        // Navigáció a FloatingActionButton segítségével
        view.findViewById(R.id.fab_add_book).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_bookListFragment_to_addBookFragment)
        );

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu); // Menü inflatálása

        MenuItem searchItem = menu.findItem(R.id.action_search); // Keresési menü elem
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Keresési eseménykezelő
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query); // Keresés végrehajtása
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // Keresés frissítése, ahogy gépelnek
                return false;
            }
        });
    }
}




package hu.nje.libraryapp.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hu.nje.libraryapp.R;
import hu.nje.libraryapp.model.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books = new ArrayList<>();       // Eredeti lista
    private List<Book> filteredBooks = new ArrayList<>(); // Szűrt lista

    private OnDeleteClickListener onDeleteClickListener;

    // Interfész a törléshez
    public interface OnDeleteClickListener {
        void onDeleteClick(Book book);
    }

    // Metódus a törléshez szükséges listener beállításához
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book currentBook = filteredBooks.get(position); // Szűrt lista alapján dolgozik
        holder.textViewTitle.setText(currentBook.getTitle());
        holder.textViewAuthor.setText(currentBook.getAuthor());

        holder.buttonDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(currentBook);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredBooks.size(); // Szűrt lista méretét adja vissza
    }

    // Beállítja az adatokat, és alapértelmezés szerint a teljes lista szerepel a szűrt listában
    public void setBooks(List<Book> books) {
        this.books = books;
        this.filteredBooks = new ArrayList<>(books);
        notifyDataSetChanged();
    }

    // Szűrési funkció
    public void filter(String query) {
        if (query.isEmpty()) {
            // Ha nincs keresési feltétel, az összes könyvet mutatja
            filteredBooks = new ArrayList<>(books);
        } else {
            // Szűrés a címre és a szerzőre
            filteredBooks = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        notifyDataSetChanged(); // Frissíti a RecyclerView-t
    }

    // ViewHolder osztály
    class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewAuthor;
        private ImageButton buttonDelete;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewAuthor = itemView.findViewById(R.id.text_view_author);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}








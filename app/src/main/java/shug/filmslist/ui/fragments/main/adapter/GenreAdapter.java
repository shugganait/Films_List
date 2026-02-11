package shug.filmslist.ui.fragments.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import shug.filmslist.databinding.CheckboxItemBinding;
import shug.filmslist.remote.model.genres.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private final List<Genre> genres;

    public GenreAdapter(List<Genre> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CheckboxItemBinding binding = CheckboxItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GenreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.binding.checkbox.setText(genre.getName());
        holder.binding.checkbox.setChecked(genre.isSelected());

        holder.binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> genre.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public List<Genre> getSelectedGenres() {
        return genres;
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        CheckboxItemBinding binding;

        public GenreViewHolder(CheckboxItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

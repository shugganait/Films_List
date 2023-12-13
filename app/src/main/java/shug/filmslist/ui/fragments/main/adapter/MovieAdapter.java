package shug.filmslist.ui.fragments.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shug.filmslist.R;
import shug.filmslist.remote.model.movieList.MovieResult;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieResult> dataList;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    public MovieAdapter(List<MovieResult> dataList, OnItemClickListener onItemClickListener) {
        this.dataList = dataList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieResult data = dataList.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvYear.setText(data.getYear());

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(data.getImdbId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvYear;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvYear = itemView.findViewById(R.id.tv_year);
        }
    }
}


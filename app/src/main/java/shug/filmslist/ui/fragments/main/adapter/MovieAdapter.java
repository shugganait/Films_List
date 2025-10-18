package shug.filmslist.ui.fragments.main.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import shug.filmslist.R;
import shug.filmslist.remote.model.movie_list.MovieResult;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieResult> dataList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Long id);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieResult data = dataList.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvYear.setText(data.getReleaseDate());
        holder.tvRate.setText(String.valueOf(data.getVoteAverage()));

        Glide.with(holder.image).load(("https://image.tmdb.org/t/p/w500" + data.getPosterPath())).into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(data.getid());
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
        public TextView tvRate;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvYear = itemView.findViewById(R.id.tv_year);
            tvRate = itemView.findViewById(R.id.tv_rate);
            image = itemView.findViewById(R.id.img_poster);
        }
    }
}


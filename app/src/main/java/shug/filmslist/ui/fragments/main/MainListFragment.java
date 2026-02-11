package shug.filmslist.ui.fragments.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shug.filmslist.R;
import shug.filmslist.databinding.FragmentMainListBinding;
import shug.filmslist.remote.client.RetrofitClient;
import shug.filmslist.remote.model.genres.Genre;
import shug.filmslist.remote.model.genres.GenreList;
import shug.filmslist.remote.model.movie_list.MovieList;
import shug.filmslist.remote.model.movie_list.MovieResult;
import shug.filmslist.remote.service.ApiService;
import shug.filmslist.ui.fragments.main.adapter.GenreAdapter;
import shug.filmslist.ui.fragments.main.adapter.MovieAdapter;

public class MainListFragment extends Fragment {

    private FragmentMainListBinding binding;
    public static final String KEY_FOR_ID = "id_key";

    private ApiService apiService;
    private NavController navController;
    private MovieAdapter adapter;
    private ArrayList<MovieResult> movies = new ArrayList<>();
    private GenreAdapter genreAdapter;
    private List<Genre> genres = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private String currentQuery = "";
    private String selectedGenres = "";
    private int selectedYear = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        initRecyclerView();
        initFilterUI();
        initListener();
        loadGenres();

        if (movies.isEmpty()) {
            fetchMovies(currentPage);
        } else {
            adapter.notifyDataSetChanged();
            binding.cvFilter.setVisibility(View.GONE);
            binding.btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter));
            if (selectedYear != 0) binding.etYear.setText(String.valueOf(selectedYear));
        }
    }

    private void initRecyclerView() {
        adapter = new MovieAdapter(movies, id -> {
            Bundle bundle = new Bundle();
            bundle.putLong(KEY_FOR_ID, id);
            navController.navigate(R.id.detailFragment, bundle);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvList.setLayoutManager(layoutManager);
        binding.rvList.setAdapter(adapter);

        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                            loadNextPage();
                        }
                    }
                }
            }
        });
    }

    private void initListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    currentQuery = "";
                    resetPagination();
                    if (!selectedGenres.isEmpty() || selectedYear != 0) {
                        fetchMoviesWithFilters(currentPage);
                    } else {
                        fetchMovies(currentPage);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                currentQuery = query;

                if (!selectedGenres.isEmpty() || selectedYear != 0) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Фильтры активны")
                            .setMessage("Поиск с активными фильтрами на сервере не поддерживается.\n" +
                                    "Пожалуйста, очистите фильтры и повторите поиск.")
                            .setPositiveButton("OK", null)
                            .show();
                    return true;
                }

                resetPagination();
                if (query.isEmpty()) {
                    fetchMovies(currentPage);
                } else {
                    fetchMoviesByTitle(currentQuery, currentPage);
                }
                return true;
            }
            return false;
        });
    }

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    private void initFilterUI() {
        binding.btnFilter.setOnClickListener(v -> {
            boolean visible = binding.cvFilter.getVisibility() == View.VISIBLE;
            binding.cvFilter.setVisibility(visible ? View.GONE : View.VISIBLE);
            binding.btnFilter.setImageDrawable(getResources().getDrawable(visible ? R.drawable.ic_filter : R.drawable.ic_no_filter));
        });

        binding.etYear.setOnClickListener(v -> showYearPicker());

        binding.btnSet.setOnClickListener(v -> {
            hideKeyboard();

            if (!currentQuery.isEmpty()) {
                currentQuery = "";
                binding.etSearch.setText("");
            }

            StringBuilder genreIds = new StringBuilder();
            for (Genre genre : genres) {
                if (genre.isSelected()) {
                    if (genreIds.length() > 0) genreIds.append(",");
                    genreIds.append(genre.getId());
                }
            }
            selectedGenres = genreIds.toString();

            resetPagination();
            fetchMoviesWithFilters(currentPage);

            binding.cvFilter.setVisibility(View.GONE);
            binding.btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter));
        });

        binding.btnClear.setOnClickListener(v -> {
            for (Genre genre : genres) genre.setSelected(false);
            selectedGenres = "";
            selectedYear = 0;
            binding.etYear.setText("");
            if (genreAdapter != null) genreAdapter.notifyDataSetChanged();
            resetPagination();
            fetchMovies(currentPage);

            binding.cvFilter.setVisibility(View.GONE);
            binding.btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter));
        });
    }

    private void showYearPicker() {
        final Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);

        NumberPicker numberPicker = new NumberPicker(requireContext());
        numberPicker.setMinValue(1900);
        numberPicker.setMaxValue(thisYear);
        numberPicker.setValue(selectedYear != 0 ? selectedYear : thisYear);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выберите год");
        builder.setView(numberPicker);
        builder.setPositiveButton("Oк", (dialog, which) -> {
            selectedYear = numberPicker.getValue();
            binding.etYear.setText(String.valueOf(selectedYear));
        });
        builder.setNegativeButton("Отменить", (dialog, which) -> {});
        builder.create().show();
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadGenres() {
        apiService.getGenres(ApiService.KEY, "ru-RU").enqueue(new Callback<GenreList>() {
            @Override
            public void onResponse(@NonNull Call<GenreList> call, @NonNull Response<GenreList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    genres = response.body().getGenres();
                    genreAdapter = new GenreAdapter(genres);
                    binding.rvGenres.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.rvGenres.setAdapter(genreAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenreList> call, @NonNull Throwable t) {
                Log.e("shug", "loadGenres: FAIL", t);
            }
        });
    }

    private void fetchMovies(int page) {
        isLoading = true;
        apiService.getPopularMovies(ApiService.KEY, "ru-RU", page).enqueue(movieCallback);
    }

    private void fetchMoviesByTitle(String title, int page) {
        isLoading = true;
        apiService.searchMovies(ApiService.KEY, "ru-RU", title, page).enqueue(movieCallbackSearch);
    }

    private void fetchMoviesWithFilters(int page) {
        isLoading = true;
        String releaseFrom = selectedYear != 0 ? selectedYear + "-01-01" : null;
        String releaseTo = selectedYear != 0 ? selectedYear + "-12-31" : null;
        apiService.discoverMovies(ApiService.KEY, "ru-RU", page,
                selectedGenres.isEmpty() ? null : selectedGenres,
                releaseFrom, releaseTo, "popularity.desc").enqueue(movieCallback);
    }

    private final Callback<MovieList> movieCallback = new Callback<MovieList>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
            isLoading = false;
            if (response.isSuccessful() && response.body() != null) {
                List<MovieResult> newMovies = response.body().getResults();
                if (newMovies != null && !newMovies.isEmpty()) {
                    movies.addAll(newMovies);
                    adapter.notifyDataSetChanged();
                    binding.imgNothingFound.setVisibility(View.GONE);
                } else {
                    isLastPage = true;
                    if (movies.isEmpty()) binding.imgNothingFound.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
            isLoading = false;
        }
    };

    private final Callback<MovieList> movieCallbackSearch = new Callback<MovieList>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
            isLoading = false;
            if (!response.isSuccessful() || response.body() == null) return;

            List<MovieResult> apiResults = response.body().getResults();
            if (apiResults == null || apiResults.isEmpty()) {
                if (movies.isEmpty()) binding.imgNothingFound.setVisibility(View.VISIBLE);
                isLastPage = true;
                return;
            }

            boolean hasFilters = !selectedGenres.isEmpty() || selectedYear != 0;
            List<MovieResult> finalList = hasFilters ? new ArrayList<>() : apiResults;

            if (hasFilters) {
                for (MovieResult m : apiResults) {
                    if (movieMatchesFilters(m)) finalList.add(m);
                }
            }

            if (!finalList.isEmpty()) {
                movies.addAll(finalList);
                adapter.notifyDataSetChanged();
                binding.imgNothingFound.setVisibility(View.GONE);
            } else {
                isLastPage = true;
                if (movies.isEmpty()) binding.imgNothingFound.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
            isLoading = false;
        }
    };

    private boolean movieMatchesFilters(@NonNull MovieResult m) {
        if (selectedYear != 0) {
            String rel = m.getReleaseDate();
            if (rel == null || rel.length() < 4) return false;
            try {
                int movieYear = Integer.parseInt(rel.substring(0, 4));
                if (movieYear != selectedYear) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (!selectedGenres.isEmpty()) {
            List<Long> gid = m.getGenreids();
            if (gid == null || gid.isEmpty()) return false;
            String[] parts = selectedGenres.split(",");
            for (String p : parts) {
                try {
                    int need = Integer.parseInt(p.trim());
                    if (!gid.contains(Long.parseLong(String.valueOf(need)))) return false;
                } catch (NumberFormatException ignored) {
                    return false;
                }
            }
        }

        return true;
    }

    private void loadNextPage() {
        if (isLoading || isLastPage) return;

        currentPage++;
        if (!currentQuery.isEmpty()) {
            fetchMoviesByTitle(currentQuery, currentPage);
        } else if (!selectedGenres.isEmpty() || selectedYear != 0) {
            fetchMoviesWithFilters(currentPage);
        } else {
            fetchMovies(currentPage);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetPagination() {
        movies.clear();
        adapter.notifyDataSetChanged();
        currentPage = 1;
        isLastPage = false;
        isLoading = false;
    }
}
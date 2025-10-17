package shug.filmslist.remote.model.movie_list;
import java.util.List;

public class MovieList {
    private long page;
    private long totalPages;
    private List<MovieResult> results;
    private long totalResults;

    public long getPage() { return page; }
    public void setPage(long value) { this.page = value; }

    public long getTotalPages() { return totalPages; }
    public void setTotalPages(long value) { this.totalPages = value; }

    public List<MovieResult> getResults() { return results; }
    public void setResults(List<MovieResult> value) { this.results = value; }

    public long getTotalResults() { return totalResults; }
    public void setTotalResults(long value) { this.totalResults = value; }
}


package shug.filmslist.remote.model.genres;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    private boolean selected = false;

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}

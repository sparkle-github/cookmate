package edu.sjsu.android.cookmate.model;
import java.io.Serializable;

// Java bean class for each Recipe Item
public class RecipeItem implements Serializable {
    private long id;
    private String title;
    private String image;
    private String imageType;

    public RecipeItem(long id, String title, String image, String imageType) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.imageType = imageType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}

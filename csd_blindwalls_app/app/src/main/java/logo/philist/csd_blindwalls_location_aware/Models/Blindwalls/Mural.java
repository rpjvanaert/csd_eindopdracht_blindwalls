package logo.philist.csd_blindwalls_location_aware.Models.Blindwalls;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Language;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.NavigationSheetInfo;

public class Mural implements Serializable {

    public static final int TYPE = -13;

    private final int id;

    private final GeoPoint geoPoint;

    private final String address;

    private final Date date;

    private final String author;

    private final double rating;

    private String[] title;
    private String[] description;
    private String[] category;
    private String[] material;

    private List<String> images;

    public Mural(int id, GeoPoint geoPoint, String address, Date date, String author, double rating) {
        this.id = id;
        this.geoPoint = geoPoint;
        this.address = address;
        this.date = date;
        this.author = author;
        this.rating = rating;
    }

    public void setImages(List<String> images){
        this.images = images;
    }

    public List<String> getImages(){
        return this.images;
    }

    public void setTitle(String titleEN, String titleNL){
        this.title = new String[]{titleEN, titleNL};
    }

    public String getTitle(int language){
        return this.title[language];
    }

    public void setDescription(String titleEN, String titleNL){
        this.description = new String[]{titleEN, titleNL};
    }

    public String getDescription(int language){
        return this.description[language];
    }

    public void setCategory(String titleEN, String titleNL){
        this.category = new String[]{titleEN, titleNL};
    }

    public String getCategory(int language){
        return this.category[language];
    }

    public void setMaterial(String titleEN, String titleNL){
        this.material = new String[]{titleEN, titleNL};
    }

    public String getMaterial(int language){
        return this.material[language];
    }

    public int getId() {
        return id;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public String getAddress() {
        return address;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }

    public NavigationSheetInfo getSheetInfo(){
        return new NavigationSheetInfo(this.address, TYPE);
    }
}

package org.project.artconnect.model;

import java.util.ArrayList;
import java.util.List;

public class Gallery {
    private String name;
    private Integer streetNumber;
    private String streetName;
    private String city;
    private Integer zipCode;
    private String country;
    private String ownerName;
    private String openingHours;
    private String contactPhone;
    private double rating;
    private String website;
    private List<Exhibition> exhibitions = new ArrayList<>();

    public Gallery() {
    }

    public Gallery(String name, String streetName, String city, double rating) {
        this.name = name;
        this.streetName = streetName;
        this.city = city;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return (streetNumber != null ? streetNumber + " " : "") +
                (streetName != null ? streetName + ", " : "") +
                (zipCode != null ? zipCode + " " : "") +
                (city != null ? city + " " : "") +
                (country != null ? country : "");
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Exhibition> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<Exhibition> exhibitions) {
        this.exhibitions = exhibitions;
    }

    public void addExhibition(Exhibition exhibition) {
        this.exhibitions.add(exhibition);
        if (exhibition.getGallery() != this) {
            exhibition.setGallery(this);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}

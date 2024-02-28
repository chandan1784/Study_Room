package com.example.studyroom.model;

public class Library {
    private Long id;

    private String name;
    private double lat;
    private double lon;
    private String district;
    private String locality;

    private Address address;
    private String imageUrl; // Add this field for image URL

    // Getter for address
    public Address getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(Address address) {
        this.address = address;
    }
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

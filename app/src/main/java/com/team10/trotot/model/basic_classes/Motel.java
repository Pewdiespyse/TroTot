package com.team10.trotot.model.basic_classes;

import java.util.List;

/**
 * Created by Pewdiespyse on 01/11/2017.
 */

public class Motel {

    private String motelId;
    private String userId;
    private String name;
    private long price;
    private double area;
    private double evaluate;
    private long timePost;
    private long timeOpen;
    private long timeClose;
    private String phone;
    private String status;
    private Double mapLongitude;
    private Double mapLatitude;
    private String address;
    private String motelDetail;
    private List<String> photosId;

    public Motel() {
    }

    public Motel(String motelId, String userId, String name, long price, double area, double evaluate, long timePost, long timeOpen, long timeClose, String phone, String status, Double mapLongitude, Double mapLatitude, String address, String motelDetail, List<String> photosId) {
        this.motelId = motelId;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.area = area;
        this.evaluate = evaluate;
        this.timePost = timePost;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.phone = phone;
        this.status = status;
        this.mapLongitude = mapLongitude;
        this.mapLatitude = mapLatitude;
        this.address = address;
        this.motelDetail = motelDetail;
        this.photosId = photosId;
    }

    public double getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(double evaluate) {
        this.evaluate = evaluate;
    }

    public String getMotelId() {
        return motelId;
    }

    public void setMotelId(String motelId) {
        this.motelId = motelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public long getTimePost() {
        return timePost;
    }

    public void setTimePost(long timePost) {
        this.timePost = timePost;
    }

    public long getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(long timeOpen) {
        this.timeOpen = timeOpen;
    }

    public long getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(long timeClose) {
        this.timeClose = timeClose;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMapLongitude() {
        return mapLongitude;
    }

    public void setMapLongitude(Double mapLongitude) {
        this.mapLongitude = mapLongitude;
    }

    public Double getMapLatitude() {
        return mapLatitude;
    }

    public void setMapLatitude(Double mapLatitude) {
        this.mapLatitude = mapLatitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMotelDetail() {
        return motelDetail;
    }

    public void setMotelDetail(String motelDetail) {
        this.motelDetail = motelDetail;
    }

    public List<String> getPhotosId() {
        return photosId;
    }

    public void setPhotosId(List<String> photosId) {
        this.photosId = photosId;
    }

    public double getDistanceFromPlace(double curLongtitude, double curLatitude) {
        return Math.sqrt((curLongtitude - this.mapLongitude)*(curLongtitude - this.mapLongitude) + (curLatitude - this.mapLatitude)*(curLatitude - this.mapLatitude));
    }
}

package com.team10.trotot.model.basic_classes;

import java.util.List;

/**
 * Created by Pewdiespyse on 01/11/2017.
 */

public class User {

    String userId ="";
    String name = "UserName";
    String status = "";
    String phone="";
    String email="";
    String password = "";
    String avatarUrl="";
    String gender = "";
    String city = "";
    double birth = 0;
    List<String> listViewedMotelIds = null;
    List<String> listFavoriteMotelIds = null;

    public User() {
    }

    public User(String userId, String name, String status, String phone, String email, String password, String avatarUrl, String gender, String city, double birth, List<String> listViewedMotelIds, List<String> listFavoriteMotelIds) {
        this.userId = userId;
        this.name = name;
        this.status = status;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.city = city;
        this.birth = birth;
        this.listViewedMotelIds = listViewedMotelIds;
        this.listFavoriteMotelIds = listFavoriteMotelIds;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getBirth() {
        return birth;
    }

    public void setBirth(double birth) {
        this.birth = birth;
    }

    public List<String> getListViewedMotelIds() {
        return listViewedMotelIds;
    }

    public void setListViewedMotelIds(List<String> listViewedMotelIds) {
        this.listViewedMotelIds = listViewedMotelIds;
    }

    public List<String> getListFavoriteMotelIds() {
        return listFavoriteMotelIds;
    }

    public void setListFavoriteMotelIds(List<String> listFavoriteMotelIds) {
        this.listFavoriteMotelIds = listFavoriteMotelIds;
    }
}

package com.team10.trotot.model.databases.local_models;

import com.google.firebase.database.DatabaseReference;
import com.team10.trotot.model.basic_classes.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pewdiespyse on 13/11/2017.
 */

public class LocalUserModel {
    private List<User> userList;
    private DatabaseReference dataNodeUser;

    //Auto get data from local
    public LocalUserModel() {
        userList = new ArrayList<>();
        userList.add(new User("3uuEChU5waTjSlPzDyX8hH7Atol1", "Poseroa D Spyse", "Normal", "0985739532", "pdspyse@gmail.com", "123456", "gs://trotot-a1819.appspot.com/users/avatars/US001.png", "Nam", "Bảo Lộc", 321321, new ArrayList<String>(Arrays.asList("MT001", "MT002", "MT003", "MT004", "MT005")), new ArrayList<String>(Arrays.asList("MT001", "MT002", "MT003"))));
        userList.add(new User("7WVJL3aXwKRVMsuIbedDvFLl6Xu1", "Unknown 1", "Normal", "0123456789", "ndvu@gmail.com", "123456", "gs://trotot-a1819.appspot.com/users/avatars/US002.png", "Nam", "Bao Loc", 123123, new ArrayList<String>(Arrays.asList("MT001", "MT003", "MT005")), new ArrayList<String>(Arrays.asList("MT001", "MT003"))));
        userList.add(new User("8ALLzAeJNwcWjmATVMQRRtbClQc2", "Unknown 2", "Normal", "0123456789", "tttinh@gmail.com", "123456", "gs://trotot-a1819.appspot.com/users/avatars/US003.png", "Nam", "Bao Loc", 123123, new ArrayList<String>(Arrays.asList("MT001", "MT002", "MT003", "MT004", "MT005")), new ArrayList<String>(Arrays.asList("MT001", "MT002", "MT003"))));
        userList.add(new User("AHBEktRqzfc8GmreEb6guh9Kqp93", "Bui Hoai Phong", "Normal", "0123456789", "phongbh@zenithtek.biz", "123456", "gs://trotot-a1819.appspot.com/users/avatars/US004.png", "Bê đê (^_^)", "Đồng Tháp", 123123, new ArrayList<String>(Arrays.asList("MT001", "MT003", "MT005")), new ArrayList<String>(Arrays.asList("MT001", "MT003"))));
        userList.add(new User("EcoASId7bZWSbaZYoyKE6rFMU9C2", "Boots Ancient", "Normal", "0123456789", "ndvu@gmail.com", "123456", "gs://trotot-a1819.appspot.com/users/avatars/US005.png", "Nam", "Bao Loc", 123123, new ArrayList<String>(Arrays.asList("MT001", "MT002", "MT003", "MT004", "MT005")), new ArrayList<String>(Arrays.asList("MT001", "MT002", "MT003"))));
    }

    //Add a user to local
    public void addUser(User user) {
        dataNodeUser.child(user.getUserId()).setValue(user);
    }

    //Update a specific user (Same as addUser but with existing user id)
    public void updateUser(User user) {
        dataNodeUser.child(user.getUserId()).setValue(user);
    }

    //Delete an specific user on local
    public void deleteUser(String Id) {
        dataNodeUser.child(Id).setValue(null);
    }

    //Return a specific user with index in list
    public User getUser(int index) {
        return this.userList.get(index);
    }

    //Return a user with an Id
    public User getUser(String Id) {
        for (User user : this.userList) {
            if (user.getUserId() == Id)
                return user;
        }

        return null;
    }

    //Return list of user with specific condition //Not yet finish
    public List<User> getUserListWithCondition() {
        List<User> userList = new ArrayList<>();
        boolean condition = true; //Condition here //Not yet finish
        for (int i = 0; i < this.userList.size(); i++) {
            if (condition) {
                userList.add(this.userList.get(i));
            }
        }
        return userList;
    }

    //Return a list for adapter
    public List<User> getUserList() {return userList;}

    //Clear all list of userList for adapter
    public void clearList() {this.userList.clear();}

    //Return the total number of user
    public int getSize() {
        return userList.size();
    }
}

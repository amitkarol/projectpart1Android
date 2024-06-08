package com.example.myapplication.entities;

import android.util.Log;

import com.example.myapplication.entities.user;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;
    private List<user> userList;

    private UserManager() {
        userList = new ArrayList<>();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(user user) {
        userList.add(user);
    }

    public List<user> getUserList() {
        return userList;
    }

    public user validateUser(String username, String password) {
        for (user user : userList) {
            if (user.getEmail().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    public boolean isAlreadyExists(String username) {
        for (user user : userList) {
            if (user.getEmail().equals(username)) {
                return true;
            }
        }
        return false;
    }
}

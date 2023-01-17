package com.doctogo.model;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {
    private Integer id;
    private String userName;
    private String email;
    private String firstName ;
    private String lastName;
    private String phone;
    private String password;

    private boolean helper = false;
    private boolean dispatcherFlag= false;

    private Double longitude;
    private Double latitude;

    private Set<Notification> notifications;
    private Set<Notification> assignedNotifications;
    private Set<Notification> dispatchedNotifications;

    public Set<Notification> getAssignedNotifications() {
        return assignedNotifications;
    }

    public void setAssignedNotifications(Set<Notification> assignedNotifications) {
        this.assignedNotifications = assignedNotifications;
    }

    public Set<Notification> getDispatchedNotifications() {
        return dispatchedNotifications;
    }

    public void setDispatchedNotifications(Set<Notification> dispatchedNotifications) {
        this.dispatchedNotifications = dispatchedNotifications;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHelper() {
        return helper;
    }

    public void setHelper(boolean helper) {
        this.helper = helper;
    }

    public boolean isDispatcherFlag() {
        return dispatcherFlag;
    }

    public void setDispatcherFlag(boolean dispatcherFlag) {
        this.dispatcherFlag = dispatcherFlag;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
}

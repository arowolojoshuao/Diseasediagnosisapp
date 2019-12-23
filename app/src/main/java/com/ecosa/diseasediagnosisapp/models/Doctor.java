package com.ecosa.diseasediagnosisapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Lekan Adigun on 1/11/2018.
 */

@Entity
public class Doctor {

    @PrimaryKey(autoGenerate = true)
    private long uuid = 0;
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String photoPath = "";
    private String expertise = "";
    private String password = "";

    public Doctor(String email, String firstName, String lastName, String phone, String photoPath) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.photoPath = photoPath;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public long getUuid() {
        return uuid;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getExpertise() {
        return expertise;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}

package com.ecosa.diseasediagnosisapp.repo.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ecosa.diseasediagnosisapp.models.Patient;

import java.util.List;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

@Dao
public interface PatientDao {

    @Insert
    void newPatient(Patient patient);

    @Query("SELECT * FROM Patient WHERE username = :user")
    Patient getPatient(String user);

    @Query("SELECT * FROM Patient")
    List<Patient> all();

    @Update
    void update(Patient patient);
}

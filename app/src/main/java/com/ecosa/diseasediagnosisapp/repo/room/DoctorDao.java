package com.ecosa.diseasediagnosisapp.repo.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ecosa.diseasediagnosisapp.models.Doctor;

import java.util.List;

/**
 * Created by Lekan Adigun on 1/11/2018.
 */

@Dao
public interface DoctorDao {

    @Insert
    long doctor(Doctor doctor);

    @Query("SELECT * FROM Doctor WHERE uuid = :uid")
    Doctor doctor(long uid);

    @Query("SELECT * FROM Doctor")
    List<Doctor> all();

    @Query("SELECT * FROM Doctor WHERE email = :mail")
    Doctor doctor(String mail);

    @Update
    int update(Doctor doctor);
}

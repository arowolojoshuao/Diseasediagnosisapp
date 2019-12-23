package com.ecosa.diseasediagnosisapp.repo.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.models.History;
import com.ecosa.diseasediagnosisapp.models.Patient;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

@Database(entities = {Patient.class, Doctor.class, History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PatientDao getPatientDao();
    public abstract DoctorDao getDoctorDao();
    public abstract HistoryDao getHistoryDao();

}

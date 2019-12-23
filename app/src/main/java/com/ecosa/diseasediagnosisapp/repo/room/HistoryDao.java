package com.ecosa.diseasediagnosisapp.repo.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ecosa.diseasediagnosisapp.models.History;

import java.util.List;

/**
 * Created by Lekan Adigun on 1/15/2018.
 */

@Dao
public interface HistoryDao {

    @Insert
    long history(History history);

    @Query("SELECT * FROM History")
    List<History> histories();

    @Query("SELECT * FROM History WHERE diseaseID = :did")
    History history(String did);

}

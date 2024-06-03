package com.example.cameraapp.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CDataDAO {
    @Query("SELECT * FROM cdata_table ORDER BY id ASC")
    fun getSortedData(): LiveData<List<CDataEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cdata: CDataEntity)

    @Delete
    fun delete(cdata: CDataEntity)

    @Query("DELETE FROM cdata_table")
    fun deleteAll()
}
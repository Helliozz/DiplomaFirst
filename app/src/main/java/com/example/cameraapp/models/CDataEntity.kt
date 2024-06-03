package com.example.cameraapp.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cdata_table")
class CDataEntity (
    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @NonNull
    @ColumnInfo(name = "cdata")
    var value: ByteArray
)
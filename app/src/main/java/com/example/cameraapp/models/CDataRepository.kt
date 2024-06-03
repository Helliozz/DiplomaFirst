package com.example.cameraapp.models

import android.app.Application
import androidx.lifecycle.LiveData

class CDataRepository(application: Application, user_email: String) {
    private var mDAO: CDataDAO
    private var mAllData: LiveData<List<CDataEntity>>

    init {
        val db: CDataDatabase? = CDataDatabase.getDatabase(application, user_email)
        mDAO = db?.cdataDAO()!!
        mAllData = mDAO.getSortedData()
    }

    fun getAllData(): LiveData<List<CDataEntity>> {
        return mAllData
    }

    fun insert(info_cdata: CDataEntity) {
        CDataDatabase.databaseWriteExecutor.execute { mDAO.insert(info_cdata) }
    }

    fun delete(info_cdata: CDataEntity) {
        mDAO.delete(info_cdata)
    }
}
package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.*
import com.example.restaurantmanagementapp.R_mDataBase.entity.AnnouncementEntity

@Dao
interface AnnouncementDao : BaseDao<AnnouncementEntity> {
    @Query("SELECT * FROM announcements ORDER BY date DESC")
    suspend fun getAllAnnouncements(): List<AnnouncementEntity>

}
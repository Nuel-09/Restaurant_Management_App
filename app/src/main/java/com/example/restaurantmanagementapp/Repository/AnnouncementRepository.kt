package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.AnnouncementDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.AnnouncementEntity

class AnnouncementRepository(private val announcementDao: AnnouncementDao) {
    suspend fun getAllAnnouncements(): List<AnnouncementEntity> = announcementDao.getAllAnnouncements()
    suspend fun insertAnnouncement(announcement: AnnouncementEntity): Long = announcementDao.insert(announcement)
    suspend fun updateAnnouncement(announcement: AnnouncementEntity) = announcementDao.update(announcement)
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity) = announcementDao.delete(announcement)
}
package com.example.restaurantmanagementapp.R_mDataBase

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.restaurantmanagementapp.R_mDataBase.dao.AnnouncementDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.AttendanceDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.CategoryDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.FoodDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.LeaveRequestDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.ShiftDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.UserDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.AnnouncementEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.AttendanceEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.FoodEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.LeaveRequestEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Delete all rows from Food table
        db.execSQL("DELETE FROM Food")

        // Delete all rows from categories table
        db.execSQL("DELETE FROM categories")
    }
}

@Database(
    entities = [
        CategoryEntity::class,
        UserEntity::class,
        FoodEntity::class,
        ShiftEntity::class,
        LeaveRequestEntity::class,
        AnnouncementEntity::class,
        AttendanceEntity::class
       // OrderEntity::class

    ],
    version = 7,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 6, to = 7)
    ]
)


abstract class AppDatabase : RoomDatabase() {
    abstract fun  categoryDao():CategoryDao
    abstract fun  FoodDao(): FoodDao
    abstract fun  UserDao(): UserDao
    abstract fun ShiftDao(): ShiftDao
    abstract fun LeaveRequestDao(): LeaveRequestDao
    abstract fun AnnouncementDao(): AnnouncementDao
    abstract fun AttendanceDao(): AttendanceDao
//    abstract fun  OrderDao(): OrderDao


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurant_management_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object :Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate admin user
                            db.execSQL(
                                "INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')"
                            )
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

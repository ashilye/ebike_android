package net.hyntech.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import net.hyntech.baselib.app.BaseApp
import net.hyntech.common.db.dao.User
import net.hyntech.common.db.dao.UserDao

@Database(entities = [(User::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        @Synchronized
        private fun buildDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context, AppDatabase::class.java, BaseApp.instance.getAppPackage() + ".db")
                    .allowMainThreadQueries() //在主线程中查询，默认子线程
//                    .addMigrations(migration_1_2)//迁移数据库使用
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //  创建新的临时表
                database.execSQL( "CREATE TABLE users_new (id INTEGER,username TEXT, password TEXT, userId TEXT, expiresIn INTEGER, userType TEXT, accessToken TEXT, buildType TEXT, orgId TEXT, orgName TEXT, apiUrl TEXT, appWebUrl TEXT , config TEXT,ext1 TEXT, ext2 TEXT, ext3 TEXT,PRIMARY KEY(id))" )
                // 复制数据
                database.execSQL( "INSERT INTO users_new (id, username, userType ,accessToken ,buildType ,orgId, orgName,apiUrl,appWebUrl,config,ext1,ext2,ext3) SELECT id, username, build_type ,access_token, build_type,org_id ,org_name ,api_url appweb_url,config,ext1,ext2,ext3 FROM users" )
                // 删除表结构
                database.execSQL( "DROP TABLE users" )
                // 临时表名称更改
                database.execSQL( "ALTER TABLE users_new RENAME TO users" )
            }
        }
    }
}

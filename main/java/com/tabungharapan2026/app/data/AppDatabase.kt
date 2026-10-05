package com.tabungharapan2026.app.data
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TabungEntity::class, CCEntity::class, TransaksiEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tabungDao(): TabungDao
    abstract fun ccDao(): CCDao
    abstract fun transaksiDao(): TransaksiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tabung_harapan_database.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            scope.launch(Dispatchers.IO) {
                                database.tabungDao().insertTabung(TabungEntity("t1", "Simpanan Kecemasan 2026", 5000.0, 800.0, "Dana 3 bulan", "active", "2026-01-01", "2026-01-01"))
                                database.tabungDao().insertTabung(TabungEntity("t2", "Tabung Raya & Ziarah", 2000.0, 513.80, "Persiapan raya", "active", "2026-01-05", "2026-01-05"))
                                database.ccDao().insertCC(CCEntity("c1", "maybank", "Bil Kad Maybank", 500.0, 300.0, "Penyata bulanan", "active", "2026-01-02", "2026-01-02"))
                                database.ccDao().insertCC(CCEntity("c2", "alliance", "Bil Kad Alliance", 400.0, 200.0, "Penyata bulanan", "active", "2026-01-04", "2026-01-04"))
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

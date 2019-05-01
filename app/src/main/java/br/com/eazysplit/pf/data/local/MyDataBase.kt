package br.com.eazysplit.pf.data.local

import android.arch.persistence.room.*
import br.com.eazysplit.pf.data.local.dao.UserDAO
import br.com.eazysplit.pf.models.User
import br.com.eazysplit.pf.util.DateConverter

@Database(entities = [User::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MyDataBase : RoomDatabase() {

    abstract fun userDao() : UserDAO

    companion object {
        private val INSTANCE: MyDataBase? = null
    }

}
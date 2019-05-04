// Baseado no código do Professor Heider
package br.com.eazysplit.pf.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import br.com.eazysplit.pf.models.User
import java.util.*

@Dao
interface UserDAO{
    @Insert(onConflict = REPLACE)
    fun add(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    fun load(userId: String): LiveData<User>

    @Query("SELECT * FROM user WHERE email = :userEmail")
    fun loadByEmail(userEmail: String) : LiveData<User>

    @Query("SELECT * FROM user WHERE id = :userId AND lastUpdate > :lastUpdateMax LIMIT 1")
    fun hasUser(userId: String, lastUpdateMax: Date): User
}
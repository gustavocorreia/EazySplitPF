// Baseado no código do Professor Helder
package br.com.eazysplit.pf.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import br.com.eazysplit.pf.models.User
import java.util.*

@Dao
interface UserLocalDAO{
    @Insert(onConflict = REPLACE)
    fun add(user: User)

    @Query("SELECT * FROM user WHERE email = :emailPhone OR phoneNumber = :emailPhone")
    fun load(emailPhone: String): LiveData<User>

    @Query("SELECT * FROM user WHERE email = :emailPhone OR phoneNumber = :emailPhone LIMIT 1")
    fun hasUser(emailPhone: String, lastUpdateMax: Date): User
}
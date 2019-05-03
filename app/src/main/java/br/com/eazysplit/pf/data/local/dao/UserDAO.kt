// Baseado no código do Professor Helder
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

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    fun load(userId: String): User

    @Query("SELECT * FROM user WHERE email = :userEmail LIMIT 1")
    fun loadByEmail(userEmail: String) : User
}
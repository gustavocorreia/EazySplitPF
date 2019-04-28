package br.com.eazysplit.pf.models

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import java.util.*

@Entity
data class User (
    @PrimaryKey
    var id: Int,

    @Expose
    var name: String,

    @Expose
    var email: String,

    @Expose
    var phoneNumber: String,

    @Expose
    var birthDate: Date,

    @Expose
    var password: String,

    @Expose
    var url_image: String?,

    var LastUpdate: Date?
)
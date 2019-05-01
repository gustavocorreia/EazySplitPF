package br.com.eazysplit.pf.models

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import java.util.*

@Entity
data class User (
    @PrimaryKey
    var id: UUID,

    var name: String,

    @Expose
    var email: String,

    @Expose
    var phoneNumber: String,

    @Expose
    var birthDate: Date,

    var password: String,

    var url_image: String?,

    var CardList: List<Card>?,

    var lastUpdate: Date?
)
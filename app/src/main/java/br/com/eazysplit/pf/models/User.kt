package br.com.eazysplit.pf.models

import android.arch.persistence.room.*
import com.google.firebase.database.Exclude
import com.google.gson.annotations.Expose
import java.util.*

@Entity
data class User (
    @Exclude
    @PrimaryKey
    var id: String,

    @Exclude
    var name: String,

    @Exclude
    var email: String,

    @Expose // Será gravado no Firebase
    var phoneNumber: String,

    @Expose // Será gravado no Firebase
    var birthDate: Date,

    @Exclude
    var password: String,

    @Exclude
    var url_image: String?,

    @Exclude
    var CardList: List<Card>?,

    @Exclude
    var lastUpdate: Date?
)
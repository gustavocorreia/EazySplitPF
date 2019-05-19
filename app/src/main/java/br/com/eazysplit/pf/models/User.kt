package br.com.eazysplit.pf.models

import com.google.firebase.firestore.Exclude
import java.util.*

data class User (
    @Exclude
    var id: String,

    @Exclude
    var name: String,

    @Exclude
    var email: String,

    var phoneNumber: String,

    var birthDate: Date,

    @Exclude
    var password: String,

    @Exclude
    var url_image: String,

    @Exclude
    var CardList: List<Card>?,

    @Exclude
    var lastUpdate: Date?
)
package br.com.eazysplit.pf.models

import com.google.firebase.database.Exclude

data class Restaurant (
    @Exclude
    var id:String = "",

    var address: String = "",
    var description: String = "",
    var geolocalization: String = "",
    var name: String = "",
    var rating: Int = 0,
    var type: String = "",
    var url_image: String = ""
)
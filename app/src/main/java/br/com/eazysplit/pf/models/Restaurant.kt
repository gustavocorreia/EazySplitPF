package br.com.eazysplit.pf.models

data class Restaurant (
    var address: String,
    var description: String,
    var geolocalization: String,
    var name: String,
    var rating: Int,
    var type: String,
    var url_image: String?
)
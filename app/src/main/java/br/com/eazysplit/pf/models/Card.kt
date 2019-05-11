package br.com.eazysplit.pf.models

import com.google.firebase.database.Exclude

data class Card (
    @Exclude
    var id:String,

    var code_validate: String,
    var flag: String,
    var month_validate: Int,
    var card_name: String,
    var number: String,
    var year_validate: Int
)
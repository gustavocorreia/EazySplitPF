package br.com.eazysplit.pf.models

import com.google.firebase.database.Exclude

data class Card (
    @Exclude
    var id:String,

    var codeValidate: String,
    var document: String,
    var flag: String,
    var monthValidate: Int,
    var name: String,
    var number: String,
    var yearValidate: Int
)
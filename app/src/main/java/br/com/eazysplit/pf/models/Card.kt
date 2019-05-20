package br.com.eazysplit.pf.models

import com.google.firebase.firestore.Exclude


data class Card (
    @Exclude
    var id:String = "",

    var codeValidate: Int = 0,
    var document: String = "",
    var flag: String = "",
    var monthValidate: Int = 0,
    var name: String = "",
    var number: String = "",
    var yearValidate: Int = 0
)
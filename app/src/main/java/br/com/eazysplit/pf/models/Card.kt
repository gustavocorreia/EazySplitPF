package br.com.eazysplit.pf.models

data class Card (
    var code_validate: String,
    var flag: String,
    var month_validate: Int,
    var card_name: String,
    var number: String,
    var year_validate: Int
)
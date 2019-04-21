package br.com.eazysplit.pf.models

import com.google.gson.annotations.SerializedName

data class Address (
    @SerializedName("cep") val addressZipCode: String,
    @SerializedName("logradouro") val addressDescription: String,
    @SerializedName("complemento") val addressComplement: String,
    @SerializedName("bairro") val addressNeighborhood: String,
    @SerializedName("localidade") val addressLocality: String,
    @SerializedName("uf") val addressState: String
)
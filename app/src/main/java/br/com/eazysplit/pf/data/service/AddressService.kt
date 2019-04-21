package br.com.eazysplit.pf.data.service

import br.com.eazysplit.pf.models.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressService {
    @GET("ws/{cep}/json/")
    fun search(@Path("cep")zipCode: String): Call<Address>
}


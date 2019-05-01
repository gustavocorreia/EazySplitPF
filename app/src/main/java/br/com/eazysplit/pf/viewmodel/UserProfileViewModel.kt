package br.com.eazysplit.pf.viewmodel

import android.arch.lifecycle.*
import br.com.eazysplit.pf.data.repository.UserRepository
import br.com.eazysplit.pf.models.User
import javax.inject.*

class UserProfileViewModel @Inject
constructor(private val userRepo: UserRepository) : ViewModel() {
    lateinit var user: User

    fun search(userId: String) {
        user = userRepo.getUser(userId)
    }
}
// Baseado no código do Professor Heider
package br.com.eazysplit.pf.viewmodel

import android.arch.lifecycle.*
import br.com.eazysplit.pf.data.repository.UserRepository
import br.com.eazysplit.pf.models.User
import javax.inject.*

class UserProfileViewModel @Inject
constructor(private val userRepo: UserRepository) : ViewModel() {
    var user: LiveData<User> = MutableLiveData<User>()

    fun search(userId: String) {
        user = userRepo.getUser(userId)
    }
}
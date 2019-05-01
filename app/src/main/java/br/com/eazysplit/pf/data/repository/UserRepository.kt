package br.com.eazysplit.pf.data.repository

import br.com.eazysplit.pf.data.local.dao.UserDAO
import br.com.eazysplit.pf.data.remote.UserFirebase
import br.com.eazysplit.pf.models.User
import java.util.*
import java.util.concurrent.Executor
import javax.inject.*

@Singleton
class UserRepository @Inject
constructor(private val userFirebase: UserFirebase, private val userDAO: UserDAO,
            private val executor: Executor) {

    companion object {
        private const val FRESH_TIMEOUT_IN_MINUTES = 3
    }

    fun getUser(userLogin: String): User {
        //refreshUser(userLogin)
        return userDAO.loadByEmail(userLogin)
    }

    /*private fun refreshUser(userLogin: String) {
        executor.execute {
            val userExists = userLocalDAO.hasUser(userLogin, getMaxRefreshTime(Date())) != null
            if (!userExists) {
                webservice.getUser(userLogin).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        executor.execute {
                            val user = response.body()
                            user?.lastRefresh = Date()
                            if (user != null)
                                userDao.save(user)
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {}
                })
            }
        }
    }*/

    private fun getMaxRefreshTime(currentDate: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES)
        return cal.time
    }
}
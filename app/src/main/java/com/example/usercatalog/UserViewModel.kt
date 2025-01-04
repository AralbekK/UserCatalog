package com.example.usercatalog
// отдельный класс для релвизации ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// ViewModel класс для хранения списка пользователей
class UserViewModel : ViewModel() {
    // LiveData для списка пользователей
    val users: MutableLiveData<MutableList<User>> by lazy {
        MutableLiveData<MutableList<User>>().also {
            it.value = mutableListOf()
        }
    }
}

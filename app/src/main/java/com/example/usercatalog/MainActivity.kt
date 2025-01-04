package com.example.usercatalog

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    // Экземпляр ViewModel для пользователей
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Устанавливаем тулбар как action bar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Объявление элементов интерфейса
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextAge: EditText = findViewById(R.id.editTextAge)
        val buttonSave: Button = findViewById(R.id.buttonSave)
        val buttonClear: Button = findViewById(R.id.buttonClear)
        val listViewUsers: ListView = findViewById(R.id.listViewUsers)

        // Создание адаптера для ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewUsers.adapter = adapter

        // Условие ДЗ: Подписать адаптер listView на изменения списка с помощью функции observe. Ему передается новый список, снова связывается listView с адаптером, обновляется адаптер с помощью
        userViewModel.users.observe(this, Observer { newUsers ->
            adapter.clear()
            adapter.addAll(newUsers.map { "${it.name}, ${it.age}" })
            adapter.notifyDataSetChanged()
        })

        // Обработчик нажатия на кнопку "СОхранить"
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()

            if (name.isNotBlank() && age != null) {
                // Создание и добавление нового пользователя
                val user = User(name, age)
                userViewModel.users.value?.add(user)
                userViewModel.users.value = userViewModel.users.value
                editTextName.text.clear()
                editTextAge.text.clear()
                Toast.makeText(this, "Пользователь добавлен!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Введите корректные данные", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик нажатия на кнопку "Очистить список"
        buttonClear.setOnClickListener {
            userViewModel.users.value?.clear()
            userViewModel.users.value = userViewModel.users.value
            Toast.makeText(this, "Список пользователей очищен!", Toast.LENGTH_SHORT).show()
        }

        // Обработчик нажатия на элемент списка для удаления пользователя
        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            // Вызов диалогового окна для подтверждения удаления
            val dialog = MyDialog(
                onConfirm = {
                    userViewModel.users.value?.removeAt(position)
                    userViewModel.users.value = userViewModel.users.value
                    Toast.makeText(this, "Пользователь удален!", Toast.LENGTH_SHORT).show()
                },
                onCancel = {
                    // Закрытие диалога без действия
                }
            )
            dialog.show(supportFragmentManager, "ConfirmDeleteDialog")
        }
    }

    // Создание меню
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Обработчик выбора пункта меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                Toast.makeText(this, "Программа завершена", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

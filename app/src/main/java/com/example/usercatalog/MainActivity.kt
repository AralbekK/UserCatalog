package com.example.usercatalog

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    // Список для хранения пользователей
    private val users = mutableListOf<User>()
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
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users.map { "${it.name}, ${it.age}" })
        listViewUsers.adapter = adapter

        // Обработчик нажатия на кнопку "Сохранить"
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()

            if (name.isNotBlank() && age != null) {
                // Создание и добавление нового пользователя
                val user = User(name, age)
                users.add(user)
                adapter.clear()
                adapter.addAll(users.map { "${it.name}, ${it.age}" })
                adapter.notifyDataSetChanged()
                editTextName.text.clear()
                editTextAge.text.clear()
                Toast.makeText(this, "Пользователь добавлен!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Введите корректные данные", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик нажатия на кнопку "Очистить список"
        buttonClear.setOnClickListener {
            users.clear()
            adapter.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Список пользователей очищен!", Toast.LENGTH_SHORT).show()
        }

        // Обработчик нажатия на элемент списка для удаления пользователя
        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            // Вызов диалогового окна для подтверждения удаления
            val dialog = MyDialog(
                onConfirm = {
                    users.removeAt(position)
                    adapter.clear()
                    adapter.addAll(users.map { "${it.name}, ${it.age}" })
                    adapter.notifyDataSetChanged()
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

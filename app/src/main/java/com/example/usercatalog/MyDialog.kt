package com.example.usercatalog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

// диалоговое окошко для подтверждения удаления пользвоателя из списка
class MyDialog(
    private val onConfirm: () -> Unit, // коллбэк при подтверждении
    private val onCancel: () -> Unit // коллбэк при отмене
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // builder.setIcon(R.drawable.ic_trash) // здесь должна быть иконка для диалогового окошка, но мойAadnroid Studio не ищет ассеты в картинках
            builder.setMessage("Вы уверены, что хотите удалить пользователя?")
                .setPositiveButton("Да") { _, _ ->
                    onConfirm() // Вызываем коллбэк при подтверждении
                }
                .setNegativeButton("Нет") { _, _ ->
                    onCancel() // Вызываем коллбэк при отмне
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be empty") //проверка
    }
}

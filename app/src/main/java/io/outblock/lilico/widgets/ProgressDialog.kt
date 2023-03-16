package io.outblock.lilico.widgets

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import io.outblock.lilico.R

class ProgressDialog(
    private val context: Context,
    private val cancelable: Boolean = false,
) {
    private var dialog: Dialog? = null
    fun show(): Dialog {
        with(AlertDialog.Builder(context)) {
            setView(DialogView(context) { dialog?.cancel() })
            setCancelable(cancelable)
            with(create()) {
                dialog = this
                window?.setBackgroundDrawableResource(R.color.transparent)
                show()
                return this
            }
        }
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}

@SuppressLint("ViewConstructor")
private class DialogView(
    context: Context,
    private val onCancel: () -> Unit,
) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.dialog_progress, this)
    }
}


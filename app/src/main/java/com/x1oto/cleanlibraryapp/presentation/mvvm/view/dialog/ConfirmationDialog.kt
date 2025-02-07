package com.x1oto.cleanlibraryapp.presentation.mvvm.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle

class ConfirmationDialog : androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Are you sure?")
            .setMessage("Do you really want to proceed?")
            .setPositiveButton("Yes") { _, _ ->  }
            .setNegativeButton("No") { _, _ -> dismiss() }
            .create()
    }
}
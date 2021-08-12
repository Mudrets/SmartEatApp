package com.example.smarteat.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton


class BottomFormDialog(val form: Form): BottomSheetDialogFragment() {
    private lateinit var createPlanBtn: MaterialButton
    private lateinit var editPlanBtn: MaterialButton
    private lateinit var copyPlanBtn: MaterialButton
    private lateinit var deletePlanBtn: MaterialButton
    private lateinit var listener: FormDialogListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout_form, container, false)

        createPlanBtn = view.findViewById(R.id.bottom_sheet_layout_form__create_plan_btn)
        editPlanBtn = view.findViewById(R.id.bottom_sheet_layout_form__edit_form_btn)
        copyPlanBtn = view.findViewById(R.id.bottom_sheet_layout_form__copy_form_btn)
        deletePlanBtn = view.findViewById(R.id.bottom_sheet_layout_form__delete_btn)
        if (!form.hasPlan)
            createPlanBtn.visibility = View.GONE

        if (form.isActive)
            createPlanBtn.text = "Сделать анкету неактивной"

        createPlanBtn.setOnClickListener {
            listener.onButtonClicked(form, 0)
            dismiss()
        }
        editPlanBtn.setOnClickListener {
            listener.onButtonClicked(form, 1)
            dismiss()
        }
        copyPlanBtn.setOnClickListener {
            listener.onButtonClicked(form, 2)
            dismiss()
        }
        deletePlanBtn.setOnClickListener {
            listener.onButtonClicked(form, 3)
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTransparentBack)
    }

    interface FormDialogListener {
        fun onButtonClicked(form: Form, action: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as FormDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() +
                        " must implements FormDialogListener"
            )
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.bottom_sheet_layout_form, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }
}
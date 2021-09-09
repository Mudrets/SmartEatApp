package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.FormsAdapter
import com.example.smarteat.adapters.NotCompleteFormsAdapter
import com.example.smarteat.models.Form
import com.example.smarteat.models.User
import com.example.smarteat.ui.activities.MainActivity
import com.example.smarteat.ui.dialogs.BottomFormDialog

/**
 * A simple [Fragment] subclass.
 * Use the [FormsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FormsFragment(private val user: User) : Fragment() {
    internal lateinit var formsRecyclerView: RecyclerView
    internal lateinit var notCompleteFormsRecyclerView: RecyclerView
    internal var parentActivity: MainActivity? = null
    internal lateinit var notCompleteFormsHeader: TextView
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forms, container, false)
        val msg: String = if (user.plans.isEmpty())
            """Для заполнения анкеты нажмите на кнопку "Новая анкета" и укажите все необходимые данные, после чего нажмите на кнопку "Сохранить анкету""""
        else if (user.activePlan == null)
            "Для составления плана вам нужно выбрать одну из законченных анкет и составить по ней план"
        else
            """Над кнопкой "Новая анкета" располагается анкета, по которой строится текущий план питания. Чтобы создать новый план питания по другой анкете, нажмите на нее и нажмите на кнопку "Составить план""""

        formsRecyclerView = view.findViewById(R.id.fragment_forms__forms_recycler)
        formsRecyclerView.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean = false
        }
        user.plans.sortByDescending { item ->
            item.createdAt
        }
        formsRecyclerView.adapter = FormsAdapter(user, msg, this)

        notCompleteFormsRecyclerView = view.findViewById(R.id.fragment_forms__not_complete_forms_recycler)
        notCompleteFormsRecyclerView.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean = false
        }
        user.formsWithoutPlan.sortByDescending { item ->
            item.createdAt
        }
        notCompleteFormsRecyclerView.adapter = NotCompleteFormsAdapter(user.formsWithoutPlan, this)

        notCompleteFormsHeader = view.findViewById(R.id.fragment_forms__not_complete_forms_header)
        if (user.formsWithoutPlan.size == 0)
            notCompleteFormsHeader.visibility = View.GONE

        return view
    }

    fun onBtnCreateClick(view: View) {
        val newForm = Form.createEmptyForm()
        user.formsWithoutPlan.add(newForm)
        parentActivity?.updateForm(newForm, true)
    }

    internal fun showBottomDialogSheet(bottomDialog: BottomFormDialog) {
        parentActivity?.showBottomSheetDialog(bottomDialog)
    }

    internal fun setNewWarningMsg(msg: String) {
        val adapter = formsRecyclerView.adapter
        if (adapter != null && adapter is FormsAdapter) {
            if (msg != "") {
                val insert = adapter.warningMsg == ""
                adapter.warningMsg = msg
                if (!insert)
                    adapter.notifyItemChanged(0)
                else
                    adapter.notifyItemInserted(0)
            } else {
                if (adapter.warningMsg != "") {
                    adapter.warningMsg = msg
                    adapter.notifyItemRemoved(0)
                }
            }
        }
    }

}
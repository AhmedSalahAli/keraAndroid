package com.example.kera.dailyReport.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.kera.R
import com.example.kera.databinding.CustomDialogReplyBinding

class CustomDialogClass(var callback: CallBack) : DialogFragment() {
    lateinit var viewDataBinding: CustomDialogReplyBinding

    companion object {
        const val TAG = "SimpleDialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(callback: CallBack): CustomDialogClass {
            val fragment = CustomDialogClass(callback)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.custom_dialog_reply, container, false
        )
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
//        title.text = arguments?.getString(KEY_TITLE)
    }

    private fun setupClickListeners(view: View) {
        viewDataBinding.sendButton.setOnClickListener {
            callback.onSendReplyClicked(viewDataBinding.editText.text.toString())
        }
    }

    interface CallBack {
        fun onSendReplyClicked(comment: String)
    }

//    private fun onClickInscription() {
//        dismiss()
//        val frag: EventNewsPaperFormFragment = EventNewsPaperFormFragment.newInstance()
//        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
//        ft.replace(R.id.main, frag)
//        ft.addToBackStack(null)
//        ft.commit()
//    }
}
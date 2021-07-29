package com.app.kera.dailyReport.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.app.kera.R
import com.app.kera.databinding.CustomDialogReplyBinding

class CustomDialogClass(var callback: CallBack,var id:String) : DialogFragment() {
    lateinit var viewDataBinding: CustomDialogReplyBinding

    companion object {
        const val TAG = "SimpleDialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(callback: CallBack,id: String): CustomDialogClass {
            val fragment = CustomDialogClass(callback,id)
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
        getDialog()?.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onStop() {

        super.onStop()
    }

    private fun setupView(view: View) {
//        title.text = arguments?.getString(KEY_TITLE)
    }

    private fun setupClickListeners(view: View) {
        viewDataBinding.sendButton.setOnClickListener {
            callback.onSendReplyClicked(viewDataBinding.editText.text.toString(),id)
        }
    }

    interface CallBack {
        fun onSendReplyClicked(comment: String,id: String)
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
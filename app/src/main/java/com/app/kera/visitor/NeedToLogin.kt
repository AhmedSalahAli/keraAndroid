package com.app.kera.visitor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.databinding.NeedToLoginFragmentBinding
import com.app.kera.login.ui.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class NeedToLogin : Fragment() {
    val viewModel: NeedToLoginViewModel by viewModel()
    lateinit var viewDataBinding: NeedToLoginFragmentBinding
    companion object {
        fun newInstance() = NeedToLogin()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.need_to_login_fragment, container, false
        )
        return viewDataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        viewDataBinding.root.setOnClickListener(View.OnClickListener {
            viewModel.logout()
        })
        viewModel.loggedOut.observe(viewLifecycleOwner, {
            var intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })
    }




}
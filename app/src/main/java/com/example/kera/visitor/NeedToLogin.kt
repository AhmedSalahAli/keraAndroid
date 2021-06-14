package com.example.kera.visitor

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.kera.R
import com.example.kera.databinding.AppScreen1FragmentBinding
import com.example.kera.databinding.NeedToLoginFragmentBinding
import com.example.kera.login.ui.LoginActivity
import com.example.kera.registrationForm.screen1.AppScreen1
import com.example.kera.registrationForm.screen1.AppScreen1ViewModel
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
        viewDataBinding.visitorLogin.setOnClickListener(View.OnClickListener {
            viewModel.logout()
        })
        viewModel.loggedOut.observe(viewLifecycleOwner, {
            var intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })
    }




}
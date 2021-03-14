package com.example.kera.sideMenu

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kera.R
import com.example.kera.contactUs.ContactUsActivity
import com.example.kera.databinding.SideMenuFragmentBinding
import com.example.kera.login.ui.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SideMenuFragment : Fragment() {

    private val viewModel: SideMenuViewModel by viewModel()
    lateinit var viewDataBinding: SideMenuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.side_menu_fragment, container, false
        )
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.constraintContactUs.setOnClickListener {
            startActivity(Intent(context, ContactUsActivity::class.java))
        }

        viewDataBinding.constraintLogout.setOnClickListener {
            viewModel.logout()
        }
        viewModel.loggedOut.observe(viewLifecycleOwner, {
            var intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })
    }

    companion object {
        fun newInstance() = SideMenuFragment()
    }

    interface CallBack {
        fun onBottomBarActionClicked(position: Int)
    }
}
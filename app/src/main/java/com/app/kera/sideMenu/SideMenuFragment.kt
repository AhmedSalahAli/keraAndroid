package com.app.kera.sideMenu

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.kera.R
import com.app.kera.app.ForceUpdateChecker
import com.app.kera.contactUs.ContactUsActivity
import com.app.kera.databinding.SideMenuFragmentBinding
import com.app.kera.login.ui.LoginActivity
import com.app.kera.splash.ui.SplashActivity
import com.app.kera.teacherDailyReport.model.CreateReportRequestModel
import com.app.kera.utils.CommonUtils
import com.app.kera.utils.Constants.Companion.aboutLinkApp
import com.app.kera.utils.Constants.Companion.privacyPolicyLinkApp
import com.app.kera.utils.Constants.Companion.termsLinkApp
import com.app.kera.visitor.SideViewMain
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

            val dialog = AlertDialog.Builder(requireContext(), 5)
                .setCancelable(false)
                .setIcon(R.drawable.kera_box)
                .setTitle(resources.getString(R.string.log_out))
                .setMessage(resources.getString(R.string.log_out_q))
                .setNegativeButton(resources.getString(R.string.cancel)){
                        dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(
                    resources.getString(R.string.log_out)
                ) { dialog, which ->
                    viewModel.logout()
                }
            dialog.show()

        }
        viewModel.loggedOut.observe(viewLifecycleOwner) {
            var intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        viewDataBinding.constraintLogoutLang.setOnClickListener {
            val singleItems = arrayOf(getString(R.string.english), getString(R.string.arabic))
            var checkedItem = 0
            var fakeCheckedItem = 0
            if (viewModel.getLang().equals("en")){
                 checkedItem = 0
                 fakeCheckedItem = 0
        }else{
                 checkedItem = 1
                 fakeCheckedItem = 1
            }


            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1,R.style.Theme_AppCompat_Light_Dialog)
                    .setTitle(resources.getString(R.string.changelanguage))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                        // Respond to positive button press
                    }
                    // Single-choice items (initialized with checked item)
                    .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->

                            if (singleItems[which] == getString(R.string.english)){
                                viewModel.saveLang("en")
                            }else{
                                viewModel.saveLang("ar")
                            }
                        Log.e("lang",singleItems[which])
                                 startActivity(SplashActivity.getStartIntent(requireContext(), null))

                    }
                    .show()
            }
        }
        viewDataBinding.constraintAboutKera.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, SideViewMain::class.java)
            intent.putExtra("SideUrl", aboutLinkApp)
            startActivity(intent)
        })
        viewDataBinding.constraintContactUs.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, ContactUsActivity::class.java)
            startActivity(intent)
        })
        viewDataBinding.constraintContactUs2.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, SideViewMain::class.java)
            intent.putExtra("SideUrl", privacyPolicyLinkApp)
            startActivity(intent)
        })
        viewDataBinding.constraintTerms.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, SideViewMain::class.java)
            intent.putExtra("SideUrl", termsLinkApp)
            startActivity(intent)
        })
        if (viewModel.getUserType() == "visitor"){
            viewDataBinding.textView11.text = resources.getString(R.string.log_in)
            viewDataBinding.imageView11.setImageResource(R.drawable.ic_round_login_24)
        }else{
            viewDataBinding.textView11.text = resources.getString(R.string.log_out)
            viewDataBinding.imageView11.setImageResource(R.drawable.ic_logout)
        }
        viewDataBinding.txtVersionCode.text = getAppVersion(requireContext())
    }
    private fun getAppVersion(context: Context): String? {
        var result = ""
        try {
            result = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {

        }
        return "Kera v$result"
    }
    companion object {
        fun newInstance() = SideMenuFragment()
    }

    interface CallBack {
        fun onBottomBarActionClicked(position: Int)
    }
}
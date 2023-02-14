package com.app.kera.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.kera.R
import com.app.kera.contactUs.ContactUsActivity
import com.app.kera.databinding.ProfileFragmentBinding
import com.app.kera.profile.adapter.ChildrenAdapter
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), ChildrenAdapter.CallBack {

    lateinit var viewDataBinding: ProfileFragmentBinding
    val viewModel: ProfileViewModel by viewModel()
   // private var mProgressDialog: ProgressDialog? = null

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.profile_fragment, container, false
        )
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        viewDataBinding.viewModel = viewModel

        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

       // mProgressDialog = CommonUtils.showLoadingDialog(requireActivity(), R.layout.progress_dialog)
        viewModel.getProfileData()
        messageObserver()
        viewModel.logo.value = viewModel.getNurseryLogo()

        viewDataBinding.veilLayout.veil()
        viewModel.profileUIModel.observe(viewLifecycleOwner) {
            // CommonUtils.hideLoading(mProgressDialog!!)

            if (it.address.isNullOrEmpty()){
                viewDataBinding.textView122.text = getString(R.string.no_address)
            }else{
                viewDataBinding.textView122.text = it.address

            }

            viewDataBinding.veilLayout.unVeil()
            viewDataBinding.adapter =
                ChildrenAdapter(it.students!!, this, viewModel.getAppRepoInstance())

            viewDataBinding.adapter!!.notifyDataSetChanged()


            if (it.students!!.size > 0){
                viewModel.saveChildDataToSharedPref(it.students!![0])
                viewModel.selectedUser.value = it.students!![0]
            }


            if (it.students!!.size == 1) {
                viewDataBinding.imageViewExchange.visibility = View.GONE
            } else {
                viewDataBinding.imageViewExchange.visibility = View.VISIBLE

            }
        }
        viewModel.selectedUser.observe(requireActivity(), Observer {
            if (it!=null){
                Glide.with(this).load(viewModel.selectedUser.value?.profileImage).error(requireContext().resources.getDrawable(R.drawable.ic_person)).into(viewDataBinding.imageView54)
                viewModel.selectedUser.value!!.studentCode = "Code:" + (it.studentCode?.replace("Code:","")
                    ?: "")
                viewModel.selectedUser.value!!.className = "Class:" + it.className?.replace("Class:","")

                //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)


                viewDataBinding.adapter?.notifyDataSetChanged()
            }

        })

        viewDataBinding.imageViewExchange.setOnClickListener {

            if (viewDataBinding.childrenFrame.isVisible){
                stateOfChildrenFrame(false)
            }else{
                stateOfChildrenFrame(true)
            }
        }

        viewDataBinding.imageView54.setOnClickListener {
            viewDataBinding.recyclerStudents.visibility = View.VISIBLE
        }

        viewDataBinding.constraintMessageThePrinciple.setOnClickListener {
            startActivity(Intent(requireContext(), ContactUsActivity::class.java).apply {})

        }
        setupUI(viewDataBinding.nestedScrollView)
    }
    fun setupUI(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                viewDataBinding.childrenFrame.visibility = View.GONE
                false
            }
        }
    }
    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner) {
            // CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()
            showMessage(it)
        }
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            requireContext(), it,
            Toast.LENGTH_LONG
        ).show();
    }
    fun stateOfChildrenFrame(state:Boolean){
        if (state){

            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
            )
            viewDataBinding.childrenFrame.visibility = View.VISIBLE

        }else{
            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_out
                )
            )
            viewDataBinding.childrenFrame.visibility = View.GONE
        }
    }
    override fun onItemClicked(student: StudentsData) {
        stateOfChildrenFrame(false)
        viewModel.saveChildDataToSharedPref(student)
        viewModel.selectedUser.value = student

    }

//    fun hideLoading() {
//        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
//            mProgressDialog!!.cancel()
//        }
//    }
}
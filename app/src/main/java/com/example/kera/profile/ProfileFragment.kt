package com.example.kera.profile

import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kera.R
import com.example.kera.databinding.ProfileFragmentBinding
import com.example.kera.profile.adapter.ChildrenAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), ChildrenAdapter.CallBack {

    lateinit var viewDataBinding: ProfileFragmentBinding
    val viewModel: ProfileViewModel by viewModel()
    private var mProgressDialog: ProgressDialog? = null

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

        mProgressDialog = CommonUtils.showLoadingDialog(requireActivity(), R.layout.progress_dialog)
        viewModel.getProfileData()
        messageObserver()

        viewModel.profileUIModel.observe(viewLifecycleOwner, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.adapter = ChildrenAdapter(it.students!!, this)
            viewDataBinding.adapter!!.notifyDataSetChanged()
        })

        viewDataBinding.imageViewExchange.setOnClickListener {
            // should open the students recycler
            viewDataBinding.recyclerStudents.visibility = View.VISIBLE
        }

        viewDataBinding.imageView54.setOnClickListener {
            viewDataBinding.recyclerStudents.visibility = View.VISIBLE
        }

        viewDataBinding.constraintMessageThePrinciple.setOnClickListener {
            // should navigate to contact teacher page
        }
    }

    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner, {
            CommonUtils.hideLoading(mProgressDialog!!)
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            requireContext(), it,
            Toast.LENGTH_LONG
        ).show();
    }

    override fun onItemClicked(student: StudentsData) {
        viewDataBinding.recyclerStudents.visibility = View.GONE
        viewModel.saveChildDataToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value!!.studentCode = "Code:" + student.studentCode
        viewModel.selectedUser.value!!.className = "Class:" + student.className
//        viewModel.profileUIModel.value!!.selectedStudentCode = "Code:" + student.studentCode
//        viewModel.profileUIModel.value!!.className.value = "Class:" + student.className
//        viewModel.profileUIModel.value!!.name.value = student.username
//        viewModel.profileUIModel.value!!.image.value = student.profileImage
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }
}
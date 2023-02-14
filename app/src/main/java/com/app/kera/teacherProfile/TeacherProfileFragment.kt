package com.app.kera.teacherProfile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.kera.R
import com.app.kera.attendanceHistory.AttendanceHistory
import com.app.kera.databinding.TeacherProfileFragmentBinding
import com.app.kera.qrCode.QRActivity
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class TeacherProfileFragment : Fragment() {
    val viewModel: TeacherProfileViewModel by viewModel()
    lateinit var viewDataBinding: TeacherProfileFragmentBinding

    companion object {
        fun newInstance() = TeacherProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.teacher_profile_fragment, container, false
        )
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        viewDataBinding.veilLayout.veil()
        viewModel.getProfileData()
        viewModel.logo.value = viewModel.getNurseryLogo()

        viewModel.profileUIModel.observe(viewLifecycleOwner) {

            if (it.address.isNullOrEmpty()){
                viewDataBinding.textView122.text = getString(R.string.no_address)
            }else{
                viewDataBinding.textView122.text = it.address

            }
            if (!it.image.isNullOrEmpty()) {
                Glide.with(view.context).load(it.image).error(R.drawable.ic_person)
                    .into(viewDataBinding.imageView54)
            } else {
                Glide.with(view.context).load(R.drawable.ic_person).error(R.drawable.ic_person)
                    .into(viewDataBinding.imageView54)
            }
            viewDataBinding.veilLayout.unVeil()
        }
        viewDataBinding.constraintLayout10.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, QRActivity::class.java))

        })
        viewDataBinding.constraintAttendanceHistory.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AttendanceHistory::class.java))

        })
    }
}
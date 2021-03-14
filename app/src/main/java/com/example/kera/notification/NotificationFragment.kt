package com.example.kera.notification

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kera.R
import com.example.kera.databinding.NotificationFragmentBinding
import com.example.kera.notification.adapter.NotificationsListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {

    lateinit var viewDataBinding: NotificationFragmentBinding
    val viewModel: NotificationViewModel by viewModel()

    companion object {
        fun newInstance() = NotificationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.notification_fragment, container, false
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

        val notifications = ArrayList<NotificationUIModel>()

        for (i in 1..15) {
            notifications.add(
                NotificationUIModel(
                    "Announcement",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam",
                    resources.getResourceName(R.drawable.ic_notification),
                    "06:58 AM",
                    "Sunday, March 1, 2020"
                )
            )
        }

        viewDataBinding.adapter = NotificationsListAdapter(notifications)
        viewDataBinding.adapter!!.notifyDataSetChanged()
    }
}
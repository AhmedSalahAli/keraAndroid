package com.app.kera.schoolsList

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.app.kera.R
import com.app.kera.data.models.schoolList.FavouriteSchoolRequestModel
import com.app.kera.databinding.SchoolsListFragmentBinding
import com.app.kera.schoolDetails.ui.SchoolDetailsActivity
import com.app.kera.schoolsList.adapter.SchoolListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class SchoolsListFragment : Fragment(), SchoolListAdapter.CallBack {
    private var mProgressDialog: ProgressDialog? = null

    lateinit var viewDataBinding: SchoolsListFragmentBinding
    val viewModel: SchoolsListViewModel by viewModel()
    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    lateinit var manager: LinearLayoutManager
    var schoolsList = ArrayList<SchoolListUIModel.SchoolData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.schools_list_fragment, container, false
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

        //mProgressDialog = CommonUtils.showLoadingDialog(requireActivity(), R.layout.progress_dialog)
        viewModel.getSchoolsList(page)
        viewDataBinding.adapter = SchoolListAdapter(ArrayList(), this, requireContext())

        manager = LinearLayoutManager(requireActivity())
        viewDataBinding.recyclerView.setLayoutManager(manager)
        viewDataBinding.recyclerView.setAdapter(viewDataBinding.adapter) // sets your own adapter
        viewDataBinding.recyclerView.addVeiledItems(15)
        viewDataBinding.recyclerView.veil()


        val scale = resources.getDimension(R.dimen._120sdp)
        val padding_in_px = (scale + 0.5f).toInt()
        viewDataBinding.recyclerView.getRecyclerView().setPadding(
            0,
            padding_in_px, 0, 150
        )
        viewDataBinding.recyclerView .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView.getVeiledRecyclerView().setPadding(0, padding_in_px, 0, 150)
        viewDataBinding.recyclerView .getVeiledRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView.getRecyclerView().addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.childCount
                totalItems = manager.itemCount
                scrollOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false
                    if (page < totalNumberOfPages) {
                        page += 1
//                        mProgressDialog = CommonUtils.showLoadingDialog(
//                            requireActivity(),
//                            R.layout.progress_dialog
//                        )
                        //  viewDataBinding.recyclerView.veil()

                        viewModel.getSchoolsList(page)
                    }
                }
            }
        })

        viewModel.anApiFailed.observe(viewLifecycleOwner, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerView.unVeil()

        })

        messageObserver()

        viewModel.schoolsList.observe(viewLifecycleOwner, {
            // CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerView.unVeil()

            isScrolling = false
            schoolsList.addAll(it.schools)
            totalNumberOfPages = it.pages
            viewDataBinding.adapter!!.schoolsList = schoolsList
            viewDataBinding.adapter!!.notifyDataSetChanged()
        })
    }

    companion object {
        fun newInstance() = SchoolsListFragment()
    }



    override fun onItemClicked(schoolID: String?) {
        val myIntent = Intent(requireContext(), SchoolDetailsActivity::class.java)
        myIntent.putExtra("SchoolID", schoolID) //Optional parameters
        startActivity(myIntent)
    }

    override fun onFavClicked(schoolID: String, likes: String?) {
        var favSchoolRequestModel = FavouriteSchoolRequestModel(schoolID, likes?.toInt())
        viewModel.favouriteSchool(favSchoolRequestModel)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner, {
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            requireContext(), it,
            Toast.LENGTH_LONG
        ).show();
    }

    private fun getBitmapFromView(image: String?) {
        val customMarkerView: View = layoutInflater.inflate(R.layout.map_custom_marker, null)
        val imageView: ImageView =
            layoutInflater.inflate(R.layout.item_schools_list, null).findViewById(R.id.imageView_school_logo)

        Glide.with(imageView.context).load(image).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                customMarkerView.layout(
                    0,
                    0,
                    customMarkerView.measuredWidth,
                    customMarkerView.measuredHeight
                )
                Log.e("on resource ready", "entered")
                customMarkerView.buildDrawingCache()
                val returnedBitmap = Bitmap.createBitmap(
                    customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(returnedBitmap)
                canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
                val drawable = customMarkerView.background
                drawable?.draw(canvas)
                customMarkerView.draw(canvas)
                return false
            }
        }).into(imageView)

    }

}
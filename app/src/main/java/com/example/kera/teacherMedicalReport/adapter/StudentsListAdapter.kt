package com.example.kera.teacherMedicalReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.databinding.ItemStudentNameImageBinding
import com.example.kera.teacherMedicalReport.model.StudentItemUIModel
import com.example.kera.utils.BaseViewHolder

class StudentsListAdapter(
    var studentsList: List<StudentItemUIModel>,
    var callBack: StudentClicked,
    var context: Context
) : RecyclerView.Adapter<BaseViewHolder>(), Filterable {

    private var mOriginalValues = ArrayList<StudentItemUIModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mOriginalValues = studentsList as ArrayList
        return ViewHolder(
            ItemStudentNameImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    internal inner class ViewHolder(var item: ItemStudentNameImageBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = studentsList[position]
            item.constraintLayout38.setOnClickListener {
                if (studentsList[position].isSelected.get()) {
                    item.textView121.setTextColor(context.resources.getColor(R.color.light_grey_8c99a7))
                    studentsList[position].isSelected.set(false)
                    item.constraintUnselected.visibility = View.VISIBLE
                } else {
                    item.textView121.setTextColor(context.resources.getColor(R.color.blue_152d4a))
                    studentsList[position].isSelected.set(true)
                    item.constraintUnselected.visibility = View.GONE
                }
                callBack.onStudentClicked(studentsList[position].id)
            }
        }
    }

    interface StudentClicked {
        fun onStudentClicked(
            studentID: String?
        )
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                studentsList =
                    results.values as ArrayList<StudentItemUIModel> // has the filtered values
                notifyDataSetChanged() // notifies the data with new filtered values
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                var constraint: CharSequence? = constraint
                val results =
                    FilterResults() // Holds the results of a filtering operation in values
                val filteredArrList: ArrayList<StudentItemUIModel> =
                    ArrayList()
                if (mOriginalValues.isNullOrEmpty()) {
                    mOriginalValues =
                        ArrayList(studentsList) // saves the original data in mOriginalValues
                }
                /********
                 *
                 * If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 * else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 */
                if (constraint.isNullOrEmpty()) { // set the Original result to return
                    results.count = mOriginalValues.size
                    results.values = mOriginalValues
                } else {
                    constraint = constraint.toString().toLowerCase()
                    for (i in 0 until mOriginalValues.size) {
                        val displayName: String = mOriginalValues[i].name as String
                        if (displayName.toLowerCase()
                                .startsWith(constraint.toString())
                        ) {
                            filteredArrList.add(
                                mOriginalValues[i]
                            )
                        }
                    }
                    // set the Filtered result to return
                    results.count = filteredArrList.size
                    results.values = filteredArrList
                }
                return results
            }
        }
    }
}
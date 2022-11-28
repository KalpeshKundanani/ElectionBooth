package com.kalpeshkundanani.electionbooth.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kalpeshkundanani.electionbooth.R
import com.kalpeshkundanani.electionbooth.db.entity.Voters

class SearchResultAdapter(private val voters: List<Voters>, private val onVoted: (Voters) -> Unit) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(voter: Voters, onClickListener: View.OnClickListener) {
            val arrayListOf = arrayListOf(
                voter.FM_NAME_EN?.capital(),
                voter.RLN_FM_NM_EN?.capital(),
                voter.LASTNAME_EN?.capital()
            ).filter { !it.isNullOrBlank() }
            itemView.findViewById<TextView>(R.id.tv_name).text = arrayListOf.joinToString(" ")


            val arrayListOfDetails = arrayListOf(
                voter.GENDER?.capital(),
                voter.AGE?.capital(),
                voter.MOBILE_NO?.capital()
            ).filter { !it.isNullOrBlank() }
            itemView.findViewById<TextView>(R.id.tv_details).text = arrayListOfDetails.joinToString(", ")

            itemView.findViewById<TextView>(R.id.tv_village_name).text = voter.PART_NAME_EN

            itemView.findViewById<TextView>(R.id.tv_epic_number).text = voter.EPIC_NO
            itemView.findViewById<TextView>(R.id.tv_section_number).text = voter.SECTION_NO
            itemView.findViewById<TextView>(R.id.tv_address).text = voter.SECTION_NAME_EN
            itemView.findViewById<TextView>(R.id.tv_booth_number).text = voter.PART_NO
            itemView.findViewById<TextView>(R.id.tv_booth_address).text = voter.PSBUILDING_NAME_EN

            if(voter.voted == 1) {
                itemView.findViewById<View>(R.id.btn_mark_voted).visibility = View.INVISIBLE
                itemView.findViewById<View>(R.id.iv_done).visibility = View.VISIBLE
            } else {
                itemView.findViewById<View>(R.id.btn_mark_voted).visibility = View.VISIBLE
                itemView.findViewById<View>(R.id.iv_done).visibility = View.INVISIBLE
            }
            itemView.findViewById<View>(R.id.btn_mark_voted).setOnClickListener(onClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voter = voters[position]
        holder.bind(voter) {
            when(it?.id) {
                R.id.btn_mark_voted -> onVoted(voter)
            }
        }
    }

    override fun getItemCount(): Int {
        return voters.size
    }

    fun markVoted(indexOfFirst: Int) {
        voters[indexOfFirst].voted = 1
        notifyItemChanged(indexOfFirst)
    }
}

fun String.capital() : String {
   return this.substring(0, 1).toUpperCase() + this.substring(1).toLowerCase()
}
package com.example.nicejobapplication.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.R
import com.example.nicejobapplication.fragment.CVFragment
import com.example.nicejobapplication.modal.CV
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class CvAdapter(private val context: Context, private val cvArrayList: ArrayList<CV>, private val listener: OnItemClickListener)
    : RecyclerView.Adapter<CvAdapter.CvViewHolder>() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    inner class CvViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView =
            itemView.findViewById(R.id.ViewNameCV)
        val createAt: TextView =
            itemView.findViewById(R.id.ViewCreateAt)
        val edit: ImageView =
            itemView.findViewById(R.id.btnEdit)
        val delete: ImageView =
            itemView.findViewById(R.id.btnDelete)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CvAdapter.CvViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.cv_item, parent, false)

        return CvViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CvAdapter.CvViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
        //
        val item: CV = cvArrayList[position]

        holder.name.text = item.cvName
        holder.createAt.text = millisecondsToDate(item.createAt.toString(), dateFormat)

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.edit.setOnClickListener {
            listener.onItemClickUpdate(position)
        }
        holder.delete.setOnClickListener {
//            listener.onItemClickDelete(position)
            val position = cvArrayList[position]
            auth = FirebaseAuth.getInstance()
            val userEmail = auth.currentUser!!.email
            db = FirebaseFirestore.getInstance()

            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setIcon(R.drawable.ic_warning)
                .setMessage("Are you sure delete this Information")
                .setPositiveButton("Yes"){
                        dialog,_->
                    position.cvName?.let { it1 ->
                        db.collection("create_cv").document(userEmail!!).collection(userEmail).document(
                            it1
                        ).delete().addOnSuccessListener {
                            notifyDataSetChanged()
                            Toast.makeText(context,"Deleted this Information",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }.addOnFailureListener {
                            Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
                .setNegativeButton("No"){
                        dialog,_->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun getItemCount(): Int = cvArrayList.size

    private fun millisecondsToDate(milliseconds:String,dateFormat: SimpleDateFormat):String{
        val millis:Long = milliseconds.toLong()
        return dateFormat.format(millis)
    }
}
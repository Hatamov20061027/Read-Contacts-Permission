package com.example.readcontactapppermissiona

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*

class RecycleView(var onClick:OnMyItemClickListener, var contactList:ArrayList<Contact>,)
: RecyclerView.Adapter<RecycleView.MyHolder>(){

    inner class MyHolder(var itemView: View) : RecyclerView.ViewHolder(itemView){
        fun onBind(contact: Contact, position: Int){
            itemView.txt_name.text = contact.name
            itemView.txt_number.text = contact.number

            itemView.cardCall.setOnClickListener {
                onClick.callContact(contact)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        val myHolder = MyHolder(itemView)
        return myHolder
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.onBind(contactList[position], position)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    interface OnMyItemClickListener{
       fun callContact(contact: Contact)
       fun sendSMSContact(contact: Contact)
    }

}
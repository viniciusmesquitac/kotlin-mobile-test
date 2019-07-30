/*
 *
 *  * 27/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_test.model.Contact
import kotlinx.android.synthetic.main.contact_item.view.*

class ContactAdapter(val contacts: MutableList<Contact>): RecyclerView.Adapter<ContactAdapter.ContactViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(inflate)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            with(contact){
                itemView.txt_name_contact.text = name
            }
        }
    }


}
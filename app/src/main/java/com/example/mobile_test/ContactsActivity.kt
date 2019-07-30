package com.example.mobile_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_test.model.Contact
import com.example.mobile_test.model.contact
import com.example.mobile_test.model.fakeContacts
import com.mooveit.library.Fakeit
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.contact_item.*

class ContactsActivity : AppCompatActivity() {

    private lateinit var adapter: ContactAdapter
    private lateinit var myContacts : MutableList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fakeit.init()
        setContentView(R.layout.activity_contacts)

        adapter = ContactAdapter(fakeContacts())
        myContacts = fakeContacts()
        recycler_view_contacts.adapter = adapter
        recycler_view_contacts.layoutManager = LinearLayoutManager(this)

        // intents
        var intent = Intent(this, InformationContactActivity::class.java)
        recycler_view_contacts.addOnItemClickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                intent.putExtra("contact", myContacts[position])
                startActivity(intent)
                Log.d("teste",myContacts[position].name.toString())
            }
        })

        fab.setOnClickListener {
            addContact()
            recycler_view_contacts.scrollToPosition(0)
        }


    }

    private fun addContact() {
        val unique = Fakeit.getUniqueValue()
        var new_contact = contact {
            name = Fakeit.name().firstName()+" "+ Fakeit.name().lastName()
            bank = Fakeit.bank().name()
            agency = Fakeit.bank().bankCountryCode()
            account = Fakeit.bank().ibanDigits()
            current_account = unique
            savings_account = !unique
        }
        myContacts.add(0,new_contact)
        adapter.contacts.add(0,new_contact)
        adapter.notifyItemInserted(0)
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }
        })
    }

}

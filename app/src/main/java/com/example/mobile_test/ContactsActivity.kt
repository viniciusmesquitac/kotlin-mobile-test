/*
 *
 *  * 1/8/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_test.model.*
import com.mooveit.library.Fakeit
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.contact_item.*

class ContactsActivity : AppCompatActivity() {

    lateinit var adapter: ContactAdapter
    lateinit var myContacts : MutableList<Contact>
    lateinit var myUser: User;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fakeit.init()
        setContentView(R.layout.activity_contacts)

        myUser = CreateUser()
        myContacts = fakeContacts()

        val bundle: Bundle? = intent.extras
        bundle?.apply {
            myUser = getParcelable<User>("user")!!;
        }

        adapter = ContactAdapter(fakeContacts())

        recycler_view_contacts.adapter = adapter
        recycler_view_contacts.layoutManager = LinearLayoutManager(this)

        // intents
        var intent = Intent(this, InformationContactActivity::class.java)
        recycler_view_contacts.addOnItemClickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                var sortedContacts: List<Contact> = myContacts.sortedBy{a -> a.name}

                intent.putExtra("user", myUser);
                intent.putExtra("contact", sortedContacts[position])
                startActivity(intent)
                Log.d("teste",sortedContacts[position].name)
            }
        })

        fab.setOnClickListener {
            addContact()
            recycler_view_contacts.scrollToPosition(0)
        }

    }

    fun addContact() {
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
        var sortedContacts: List<Contact> = myContacts.sortedBy{a -> a.name}
        var indice = sortedContacts.indexOf(new_contact);
        adapter.contacts.add(indice, new_contact)
        adapter.notifyItemInserted(indice)
    }

    fun addContact(name: String) {
        val unique = Fakeit.getUniqueValue()
        var new_contact = contact {
            this.name = name
            this.bank = "itau"
            this.agency = "01"
            this.account = "01"
            this.current_account = true
            this.savings_account = true
        }
        myContacts.add(0,new_contact)
        var sortedContacts: List<Contact> = myContacts.sortedBy{a -> a.name}
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

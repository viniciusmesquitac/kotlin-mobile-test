/*
 *
 *  * 27/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.mobile_test.model.Contact
import com.example.mobile_test.model.CreateUser
import com.example.mobile_test.model.User
import com.example.mobile_test.model.fakeContacts
import kotlinx.android.synthetic.main.activity_information_contact.*

class InformationContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_contact)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var contact: Contact = Contact("","","","", false,false,0f,0f)
        var user: User = CreateUser()
        val bundle: Bundle? = intent.extras

        bundle?.apply {
            contact = getParcelable<Contact>("contact")!!;
                txt_name.text = contact?.name.toString()
                txt_account.text = contact?.account.toString()
                txt_bank_name.text = contact?.bank
        }


        // click event
        btn_transfer.setOnClickListener {
            var value: Float? = edit_currency.text.toString().toFloat()
            transferBalance(user, contact, value)
        }
    }

    private fun transferBalance(by: User, to: Contact, value: Float?) {
        by.current_ballance -= value!!
        to.current_ballance +=value!!
        Toast.makeText(this, "Valor de {value} transferido com sucesso!", Toast.LENGTH_SHORT).show()
        Log.d("teste",by.current_ballance.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

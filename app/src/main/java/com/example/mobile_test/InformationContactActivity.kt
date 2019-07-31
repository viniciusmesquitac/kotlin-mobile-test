/*
 *
 *  * 27/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.mobile_test.model.*
import kotlinx.android.synthetic.main.activity_information_contact.*

class InformationContactActivity : AppCompatActivity() {


    private lateinit var contact : Contact
    private lateinit var user: User;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_contact)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contact = Contact("","","","", false,false,0f,0f)
        user = User("","","","", false,false,0f,0f)

        val bundle: Bundle? = intent.extras
        //apply user information
        txt_current_balance.text = user.current_ballance.toString();
        txt_saving_balance.text = user.savings_ballance.toString();

        //apply contact information
        bundle?.apply {
            contact = getParcelable<Contact>("contact")!!;
            txt_name.text = contact?.name.toString()
            txt_account.text = contact?.account.toString()
            txt_bank_name.text = contact?.bank

            user = getParcelable<User>("user")!!;
            txt_current_balance.text = user.current_ballance.toString();
            txt_saving_balance.text = user.savings_ballance.toString();

        }

        // click event
        btn_transfer.setOnClickListener {
            var id = radio_group.checkedRadioButtonId;

            if(id==-1) { // any radio button as checked
                Toast.makeText(this,"Você deve selecionar o tipo de conta!", Toast.LENGTH_SHORT).show()
            } else {
                var value: Float? = edit_currency.text.toString().toFloat()
                val radio: RadioButton = findViewById(id);
                if(radio.text == "Corrente") {
                    transferCurrentBalance(value);
                }
                if(radio.text == "Poupança") {
                    transferSavingBalance(value);
                }

            }


        }
    }

    private fun transferCurrentBalance(value: Float?) {
        user.current_ballance -= value!!
        contact.current_ballance +=value!!
        txt_current_balance.text = user.current_ballance.toString();
        Toast.makeText(this, "Valor de ${value} transferido com sucesso!", Toast.LENGTH_SHORT).show()
        Log.d("teste",user.current_ballance.toString())
    }

    private fun transferSavingBalance(value: Float?) {
        user.savings_ballance -= value!!
        contact.savings_ballance +=value!!
        txt_saving_balance.text = user.savings_ballance.toString();
        Toast.makeText(this, "Valor de ${value} transferido com sucesso!", Toast.LENGTH_SHORT).show()
        Log.d("teste",user.savings_ballance.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

/*
 *
 *  * 27/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mobile_test.model.*
import kotlinx.android.synthetic.main.activity_information_contact.*
import java.lang.IllegalArgumentException

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
        txt_current_balance.text = user.current_ballance.toString()
        txt_saving_balance.text = user.savings_ballance.toString()

        //apply contact information
        bundle?.apply {
            contact = getParcelable("contact")!!
            txt_name.text = "Nome: "+contact?.name.toString()
            txt_account.text = "Conta: "+contact?.account.toString()
            txt_bank_name.text = "Banco: "+contact?.bank

            user = getParcelable("user")!!
            txt_current_balance.text = "R$" + user.current_ballance.toString()
            txt_saving_balance.text = "R$" + user.savings_ballance.toString()
        }

        // click event
        btn_transfer.setOnClickListener {
            showDialog();
        }
    }

    private fun applyTransference() {
        if (edit_currency.text.isEmpty()) {
            Toast.makeText(this,"Você deve digitar algum valor!", Toast.LENGTH_SHORT).show()
        } else {
            var id = radio_group.checkedRadioButtonId;
            if(id==-1) { // any radio button as checked
                Toast.makeText(this,"Você deve selecionar o tipo de conta!", Toast.LENGTH_SHORT).show()
            } else {
                var value: Float? = edit_currency.text.toString().toFloat()
                val radio: RadioButton = findViewById(id)
                if(radio.text == "Corrente") {
                    transferCurrentBalance(value)
                }
                if(radio.text == "Poupança") {
                    transferSavingBalance(value)
                }
            }
        }
    }

    private fun transferCurrentBalance(value: Float?) {

        if(user.current_ballance - value!! < 0) {
            message("Você não possui esse valor em conta!")
        } else {
            user.current_ballance -= value!!
            contact.current_ballance +=value!!
            txt_current_balance.text = user.current_ballance.toString();
            message("Valor de ${value} transferido com sucesso!")

            var intent = Intent(this, ContactsActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        Log.d("teste", "Saldo da conta de ${contact.name} agora é de ${contact.current_ballance}")
        Log.d("teste",user.current_ballance.toString())
    }

    private fun transferSavingBalance(value: Float?) {

        if(user.savings_ballance - value!! < 0) {
            message("Você não possui esse valor em conta!")
        } else {
            user.savings_ballance -= value!!
            contact.savings_ballance +=value!!
            txt_saving_balance.text = user.savings_ballance.toString();
            message("Valor de ${value} transferido com sucesso!")

            var intent = Intent(this, ContactsActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        Log.d("teste", "Saldo da conta de ${contact.name} agora é de ${contact.savings_ballance}")
        Log.d("teste",user.savings_ballance.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun message(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(){
        lateinit var dialog:AlertDialog

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Transferência")
        builder.setMessage("Você tem certeza que deseja transferir R$ ${edit_currency.text} para ${contact.name}?")

        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> applyTransference()
                DialogInterface.BUTTON_NEGATIVE -> message("Transição cancelada!")
            }
        }

        builder.setPositiveButton("SIM",dialogClickListener)
        builder.setNegativeButton("CANCELAR",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }
}


/*
 *
 *  * 27/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.mobile_test.model.*
import kotlinx.android.synthetic.main.activity_information_contact.*
import java.sql.Timestamp
import java.time.*
import java.time.ZoneId.systemDefault
import java.time.LocalTime.MIDNIGHT
import java.time.temporal.TemporalQueries.localDate



@RequiresApi(Build.VERSION_CODES.O)
class InformationContactActivity : AppCompatActivity() {


    lateinit var contact : Contact
    lateinit var user: User;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_contact)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contact = Contact("","","","", false,false,0f,0f)
        user = User("","","","", false,false,0f,0f,0f,false)


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

    fun applyTransference() {
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

                    if(!transferCurrentBalance(value)){
                        message("Você não possui esse valor em conta!")
                    } else {
                        txt_current_balance.text = user.current_ballance.toString();
                        message("Valor de ${value} transferido com sucesso!")

                        var intent = Intent(this, ContactsActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                }
                if(radio.text == "Poupança") {
                    if(!transferSavingBalance(value)) {
                        message("Você não possui esse valor em conta!")
                    } else {
                        txt_saving_balance.text = user.savings_ballance.toString();
                        message("Valor de ${value} transferido com sucesso!")

                        var intent = Intent(this, ContactsActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun transferCurrentBalance(value: Float?):Boolean {

        if(user.current_ballance - value!! < 0) {
            return false
        } else {
            if(user.transference_day + value!! > 10000f || user.exceed_transition) { message("você não pode ultrapassar transferncias de R$ 10.000 por dia")} else {
                user.current_ballance -= value!!
                user.transference_day = +value!!
                contact.current_ballance += value!!
                return true
            }
        }
        checkDay()
        return true

        Log.d("teste", "Saldo da conta de ${contact.name} agora é de ${contact.current_ballance}")
        Log.d("teste",user.current_ballance.toString())
    }

    fun transferSavingBalance(value: Float?):Boolean {

        if(user.savings_ballance - value!! < 0) {
            return false
        } else {
            if(user.transference_day + value!! > 10000f || user.exceed_transition) {
                message("você não pode ultrapassar transferncias de R$ 10.000 por dia")
            } else {
                user.savings_ballance -= value!!
                user.transference_day +=value;
                contact.savings_ballance +=value!!
                return true
            }
        }
        checkDay()
        return true

        Log.d("teste", "Saldo da conta de ${contact.name} agora é de ${contact.savings_ballance}")
        Log.d("teste",user.savings_ballance.toString())
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

    private fun checkDay() {
        val now = Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli()
        val midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (now > midnight) {
            user.transference_day = 0f // reset day
        }
    }

    fun checkDay(now: Long) {
        val midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (now > midnight) {
            user.transference_day = 0f // reset day
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun message(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}


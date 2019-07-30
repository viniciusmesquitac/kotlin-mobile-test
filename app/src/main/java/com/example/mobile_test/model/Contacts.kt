/*
 *
 *  * 27/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val bank: String,
    val agency: String,
    val account: String,
    val current_account: Boolean,
    val savings_account: Boolean,
    var current_ballance: Float,
    val savings_ballance: Float
): Parcelable

class ContactBuilder {
    var name = ""
    var bank = ""
    var agency = "001"
    var account = ""
    var current_account = false
    var savings_account = false
    var current_ballance = 0.0f
    var savings_ballance = 0.0f

    fun build(): Contact = Contact(name,bank,agency,account, false, false, current_ballance, savings_ballance)
}
fun contact(block: ContactBuilder.() -> Unit): Contact = ContactBuilder().apply(block).build()

fun fakeContacts():MutableList<Contact> = mutableListOf(
    contact {
        name = "Vinicius Mesquita"
        bank = "itau"
        agency = "011"
        account = "550.222.221-444"
        current_account = true
        savings_account = false
    }
)
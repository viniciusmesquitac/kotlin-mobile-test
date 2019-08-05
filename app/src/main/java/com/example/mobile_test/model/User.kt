/*
 *
 *  * 28/7/2019.
 *  * @Author Vinicius Mesquita
 *
 */

package com.example.mobile_test.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val name: String,
    val bank: String,
    val agency: String,
    val account: String,
    val current_account: Boolean,
    val savings_account: Boolean,
    var current_ballance: Float,
    var savings_ballance: Float,
    var transference_day: Float,
    val exceed_transition: Boolean
): Parcelable


class MyUser {
    var name = ""
    var bank = ""
    var agency = "001"
    var account = ""
    var current_account = false
    var savings_account = false
    var current_ballance = 0.0f
    var savings_ballance = 0.0f
    var transference_day = 0.0f
    var exceed_transference = false;

    fun build(): User = User(name,bank,agency,account, false, false, current_ballance, savings_ballance,transference_day,exceed_transference)
}
fun user(block: MyUser.() -> Unit): User = MyUser().apply(block).build()

fun CreateUser(): User = user {
    name = "Vinicius"
    bank = "itau"
    agency = "001"
    account = "50000"
    current_account = true
    savings_account = false
    current_ballance = 10000f
    savings_ballance = 0f
    transference_day = 0f
    exceed_transference = false;
}



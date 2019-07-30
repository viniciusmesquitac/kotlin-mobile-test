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
    val savings_ballance: Float
): Parcelable

fun CreateUser(): User = User(
    "Vinicius Mesquita",
    "itau",
    "001",
    "550.222.221-444",
    true,
    true,
    100000f,
    1000f
)



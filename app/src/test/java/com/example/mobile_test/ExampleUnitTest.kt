package com.example.mobile_test

import com.example.mobile_test.model.CreateUser
import com.example.mobile_test.model.fakeContacts
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    var contactAct = ContactsActivity()
    var infoContactAct = InformationContactActivity()

    @Test
    fun isPossibleAddContactTest() {
        contactAct.myContacts = fakeContacts()

        contactAct.addContact("vinicius")

        assertFalse(contactAct.myContacts.isEmpty())
        assertTrue(contactAct.myContacts[0].name == "vinicius")

    }

    @Test
    fun isPossibleTransferToContact() {

        contactAct.myUser = CreateUser()
        contactAct.myContacts = fakeContacts()
        contactAct.addContact("vinicius")

        contactAct.myUser.savings_ballance = 100f
        contactAct.myContacts[0].savings_ballance = 0f

        // changing activity
        infoContactAct.user = contactAct.myUser
        infoContactAct.contact = contactAct.myContacts[0]

        // transferring
        infoContactAct.transferSavingBalance(100f)

        assertEquals(infoContactAct.contact.savings_ballance, 100f)
        assertEquals(infoContactAct.user.savings_ballance, 0f)

    }

    @Test
    fun isNotPossibleTransferExceedBalance() {

        contactAct.myUser = CreateUser()

        contactAct.myUser.savings_ballance = 100f

        infoContactAct.user = contactAct.myUser

        // exceed
        infoContactAct.transferSavingBalance(101f)

        assertEquals(contactAct.myUser.savings_ballance, 100f)

    }

    @Test
    fun isNotPossibleExceedTheLimitTransferDay() {

        // the limit is R$10.0000 in one day, after this, the system should restart the transference limit.

        contactAct.myUser = CreateUser()

        contactAct.myUser.savings_ballance = 100f

        infoContactAct.user = contactAct.myUser

        // exceed
        infoContactAct.transferSavingBalance(10001f)

        // stays the same
        assertEquals(contactAct.myUser.savings_ballance, 100f)

    }

    @Test
    fun isPossibleRestartTransferenceAfterMidnight() {

        contactAct.myUser = CreateUser()
        infoContactAct.user = contactAct.myUser

        // today is 2019/08/25
        val current_time = Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli()
        val tomorrow = LocalDateTime.of(LocalDate.of(2019,8, 26), LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        infoContactAct.user.transference_day = 10000f

        infoContactAct.checkDay(tomorrow)

        assertEquals(infoContactAct.user.transference_day, 0f)

    }
}

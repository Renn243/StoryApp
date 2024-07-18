package com.raihanresa.storyapp.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import com.raihanresa.storyapp.R
import com.raihanresa.storyapp.ui.login.LoginActivity

class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginSuccess() {
        // Masukkan email dan password yang valid
        Espresso.onView(ViewMatchers.withId(R.id.emailEditText)).perform(ViewActions.typeText("blueno@gmail.com"))

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).perform(ViewActions.typeText("blueno1234"))

        Thread.sleep(1000)

        Espresso.closeSoftKeyboard()

        // Klik tombol login
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())

        Thread.sleep(6000)
    }

    @Test
    fun testLogout() {
        // Login terlebih dahulu
        testLoginSuccess()

        // Klik tombol logout
        Espresso.onView(ViewMatchers.withId(R.id.btn_logout)).perform(ViewActions.click())

        Thread.sleep(1000)

        // Verifikasi bahwa dialog konfirmasi logout muncul
        Espresso.onView(ViewMatchers.withText("Logout")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(1000)

        // Klik tombol "Yes" pada dialog konfirmasi logout
        Espresso.onView(ViewMatchers.withText("Yes")).perform(ViewActions.click())

        Thread.sleep(3000)
    }
}
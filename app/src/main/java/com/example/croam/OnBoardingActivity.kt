package com.example.croam

import android.Manifest
import android.Manifest.permission
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class OnBoardingActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance(
                title = "Welcome to the safe world...",
                description = "Add emergency contacts and turn on the service to stay protected",
                imageDrawable = R.drawable.contact,
//                backgroundDrawable = R.drawable.gradient1
                backgroundColor = R.drawable.gradient1
//                titleTypefaceFontRes = R.font.opensans_regular,
//                descriptionTypefaceFontRes = R.font.opensans_regular,
        ))

        addSlide(AppIntroFragment.newInstance(
                title = "Speak 'help' or 'bachao' to ask for help",
                description = "A help message with your location will be sent to your emergency contacts and police when you ask for help",
                imageDrawable = R.drawable.help_location,
//                backgroundDrawable = R.drawable.gradient2
                backgroundColor = R.drawable.gradient2

//                titleTypefaceFontRes = R.font.opensans_regular,
//                descriptionTypefaceFontRes = R.font.opensans_regular,
        ))

        addSlide(AppIntroFragment.newInstance(
                title = "Report the incidents near you",
                description = "You can post an image or a video of incidents happening near you with a description",
                imageDrawable = R.drawable.report_illustration,
//                backgroundDrawable = R.drawable.gradient3,
                backgroundColor = R.drawable.gradient3,
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED
//                backgroundColor = Color.BLUE
//                titleTypefaceFontRes = R.font.opensans_regular,
//                descriptionTypefaceFontRes = R.font.opensans_regular,
        ))

        addSlide(AppIntroFragment.newInstance(
                title = "Security on your tips",
                description = "Press power button 3 times to directly call emergency number!",
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED,
                backgroundColor = R.drawable.gradient4,
                imageDrawable = R.drawable.power_button_call
//                backgroundDrawable = R.drawable.gradient4

//                titleTypefaceFontRes = R.font.opensans_regular,
//                descriptionTypefaceFontRes = R.font.opensans_regular,
        ))

        addSlide(AppIntroFragment.newInstance(
                title = "Give permissions to get started",
                description = "We use permissions only to protect you and by no means we can use it for some other purpose.",
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED,
                backgroundColor = R.drawable.gradient4,
                imageDrawable = R.drawable.permission
//                backgroundDrawable = R.drawable.gradient4

//                titleTypefaceFontRes = R.font.opensans_regular,
//                descriptionTypefaceFontRes = R.font.opensans_regular,
        ))

        setTransformer(AppIntroPageTransformerType.Flow)
        isColorTransitionsEnabled = true
        setImmersiveMode()
        askForPermissions(
                permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, permission.RECEIVE_SMS, permission.SEND_SMS, permission.WRITE_EXTERNAL_STORAGE, permission.RECORD_AUDIO),
                slideNumber = 5,
                required = true)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext) //Get the preferences
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.putBoolean("new", false);
        edit.commit();
        val i = Intent(baseContext, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext) //Get the preferences
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.putBoolean("new", false);
        edit.commit();
        val i = Intent(baseContext, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}

package com.example.budgetProject

import android.content.Intent
import android.media.FaceDetector
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.facebook.*
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.gson.Gson
import java.util.*

private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity(), BudgetListFragment.Callbacks {

    val fragmentContainer: FrameLayout by lazy { findViewById<FrameLayout>(R.id.fragment_container) }
    private lateinit var loginButton: LoginButton
    private var callbackManager: CallbackManager? = null
    private var dto: FacebookDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton = findViewById(R.id.login_button)

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("FBLOGIN", loginResult.accessToken.token.toString())
                Log.d("FBLOGIN", loginResult.recentlyDeniedPermissions.toString())
                Log.d("FBLOGIN", loginResult.recentlyGrantedPermissions.toString())


                val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                    try {
                        //here is the data that you want
                        Log.d(TAG, "Data is $`object`")
                        var gson = Gson()
                        var dataString = `object`.toString()
                        dto = gson.fromJson(dataString, FacebookDTO::class.java)

                        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                        if (currentFragment == null) {
                            val fragment = BudgetListFragment(dto!!)
                            supportFragmentManager
                                .beginTransaction()
                                .add(R.id.fragment_container, fragment)
                                .commit()
                        }
                        Log.d(TAG, "Name ${dto!!.name}")
                        if (`object`.has("id")) {
//                            handleSignInResultFacebook(`object`)
                        } else {
                            Log.e(TAG, `object`.toString())
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "name,email,id")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                Log.e("FBLOGIN_FAILD", "Cancel")
            }

            override fun onError(error: FacebookException) {
                Log.e("FBLOGIN_FAILD", "ERROR", error)
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

    }

    override fun onBudgetSelected(budgetId: String) {
        val fragment = BudgetFragment.newInstance(budgetId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewBudget() {
        val fragment = BudgetFragment.newInstance(null)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

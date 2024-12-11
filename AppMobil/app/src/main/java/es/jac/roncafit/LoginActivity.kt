package es.jac.roncafit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.jac.roncafit.fragments.LoginFragment

class LoginActivity : AppCompatActivity(), LoginFragment.FragmentLogInListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    //Funciones del LogIn fragment
    override fun onLogInBtnClicked() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
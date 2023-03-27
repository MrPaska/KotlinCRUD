package com.example.kotlincrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val registertext: TextView = findViewById(R.id.textView_register_now)

        registertext.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java) // Component communication
            startActivity(intent)
        }

        val loginbtn: Button = findViewById(R.id.button_login)
        val login_guest: TextView = findViewById(R.id.textView_guest)

        loginbtn.setOnClickListener {
            login()
        }
        login_guest.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val email: EditText = findViewById(R.id.editText_email_login)
        val password: EditText = findViewById(R.id.editText_password_login)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val inputEmail = email.text.toString()
        val inputpass = password.text.toString()
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(inputEmail)
        progressBar.visibility = View.VISIBLE

        if (inputEmail.isEmpty() || inputpass.isEmpty()) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if(!matcher.matches()){
            progressBar.visibility = View.GONE;
            Toast.makeText(this, "Invalid Email format :(", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.signInWithEmailAndPassword(inputEmail, inputpass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        this, "Welcome back!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this, "Log-in failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "ERORR occured ${it.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }
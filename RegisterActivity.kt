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

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val logintext: TextView = findViewById(R.id.textView_login_now)
        val registerbtn: Button = findViewById(R.id.button_register)

        logintext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        registerbtn.setOnClickListener {
            signUp()
        }
    }
    private fun signUp() {
        val email = findViewById<EditText>(R.id.editText_email_register)
        val password = findViewById<EditText>(R.id.editText_password_register)
        val password_rpt = findViewById<EditText>(R.id.editText_password_register_rpt)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val inputEmail = email.text.toString()
        val inputpass = password.text.toString()
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(inputEmail)
        progressBar.visibility = View.VISIBLE;

        if (inputEmail.isEmpty() || inputpass.isEmpty() || password_rpt.text.isEmpty()) {
            progressBar.visibility = View.GONE;
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
        if(password.length() < 6){
            progressBar.visibility = View.GONE;
            Toast.makeText(this, "Password needs to be at least 6 symbols long", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (inputpass != password_rpt.text.toString()){
            progressBar.visibility = View.GONE;
            Toast.makeText(this, "Passwords do not match :( ", Toast.LENGTH_SHORT).show()
            return
        }

            auth.createUserWithEmailAndPassword(inputEmail, inputpass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        progressBar.visibility = View.GONE;
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            this, "User created successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        progressBar.visibility = View.GONE;
                        Toast.makeText(
                            this, "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE;
                    Toast.makeText(this, "ERORR: ${it.localizedMessage}", Toast.LENGTH_SHORT) // Prints a localized description of error
                        .show()
                }
        }
    }

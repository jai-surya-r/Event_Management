package com.example.ocasio_genie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        var signInEmail: EditText = findViewById(R.id.email)
        var signInPassword: EditText = findViewById(R.id.password)
        var signInBtn: Button = findViewById(R.id.lg_btn)
        var signUpText: TextView = findViewById(R.id.tvSignup)

        auth = FirebaseAuth.getInstance()

        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener {
            val email = signInEmail.text.toString()
            val password = signInPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty())
                    signInEmail.error = "Enter valid Email address"
                if (password.isEmpty())
                    signInPassword.error = "Enter valid Password"
                Toast.makeText(this, "Enter valid credentials", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

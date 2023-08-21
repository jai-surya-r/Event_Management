package com.example.ocasio_genie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var etName: EditText
    private lateinit var etNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        etName = findViewById(R.id.userName)
        etNumber = findViewById(R.id.editTextNumberDecimal)
        etEmail = findViewById(R.id.email2)
        etPassword = findViewById(R.id.password2)
        btnSignUp = findViewById(R.id.sup_btn)

        btnSignUp.setOnClickListener{
            val name = etName.text.toString()
            val number = etNumber.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty())
            {
                if(name.isEmpty())
                    etName.error = "Enter a valid name"
                if(number.isEmpty())
                    etNumber.error = "Enter a valid Mobile Number"
                if(email.isEmpty())
                    etEmail.error = "Enter a valid Email address"
                if(password.isEmpty())
                    etPassword.error = "Enter a valid Password"
                Toast.makeText(this, "Enter Valid details", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val databaseRef = database.reference.child("users").child(auth.currentUser!!.uid)
                        val users: Users = Users(name, email, number, auth.currentUser!!.uid)
                        databaseRef.setValue(users).addOnCompleteListener {task ->
                            if(task.isSuccessful) {
                                Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else
                        Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

data class Users (
    val name: String? = null,
    val email: String? = null,
    var phone: String? = null,
    val uid: String? = null
)
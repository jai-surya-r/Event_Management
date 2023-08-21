package com.example.ocasio_genie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val partyBtn: LinearLayout = findViewById(R.id.eventCardLayout)
        val cmdBtn: LinearLayout = findViewById(R.id.eventCardLayout2)

        partyBtn.setOnClickListener{
            val intent = Intent(this, PartyActivity::class.java)
            startActivity(intent)
        }

        cmdBtn.setOnClickListener {
            val intent = Intent(this, ComedyActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            showSignOutDialog()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun showSignOutDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Sign Out")
        dialogBuilder.setMessage("Are you sure you want to sign out?")
        dialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}

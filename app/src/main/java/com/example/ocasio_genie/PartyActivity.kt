package com.example.ocasio_genie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.example.ocasio_genie.EventUtils


class PartyActivity : AppCompatActivity() {

    private val evName = "DJ Party by John Wick"
    private val evDate = "20-07-2023"
    private val evTime = "23:00"
    private val evLoc = "Vagator Beach, Goa, India"

    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party)

        val backButton: ImageView = findViewById(R.id.backButton)
        val messageButton: MaterialButton = findViewById(R.id.messageButton)
        val joinButton: MaterialButton = findViewById(R.id.joinButton)

        backButton.setOnClickListener {
            // Handle back button click
            onBackPressed()
        }

        joinButton.setOnClickListener {
            // Handle join button click
            updateEventDetails()
        }

        messageButton.setOnClickListener {
            // Handle message button click
            openChatBox()
        }
        val database = FirebaseDatabase.getInstance()
        val userEventsRef = database.reference.child("users").child(currentUserUid.toString()).child("events")

        // Check if the user is already enrolled in the event
        userEventsRef.orderByChild("title").equalTo(evName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isEnrolled = dataSnapshot.exists()

                if (isEnrolled) {
                    // User is enrolled, show "UN-ENROLL"
                    joinButton.text = "UN-ENROLL"
                } else {
                    // User is not enrolled, show "ENROLL"
                    joinButton.text = "ENROLL"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled if needed
                Toast.makeText(this@PartyActivity, "Backend error, please try again after sometime!", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateEventDetails() {
        val database = FirebaseDatabase.getInstance()
        val userEventsRef = database.reference.child("users").child(currentUserUid.toString()).child("events")

        // Check if the user has already enrolled in the event
        userEventsRef.get().addOnSuccessListener { dataSnapshot ->
            val isEnrolled = dataSnapshot.children.any { it.child("title").getValue(String::class.java) == evName }

            if (isEnrolled) {
                // User is already enrolled, show "Un-enroll"
                userEventsRef.orderByChild("title").equalTo(evName).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (eventSnapshot in dataSnapshot.children) {
                            val eventKey = eventSnapshot.key
                            eventKey?.let { userEventsRef.child(it).removeValue() }
                        }
                        Toast.makeText(this@PartyActivity, "Un-enrolled", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@PartyActivity, "Error updating the changes", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // User is not enrolled, enroll them
                val eventKey = userEventsRef.push().key
                val event = Event(evDate, evTime, evLoc, evName)

                eventKey?.let { userEventsRef.child(it).setValue(event) }

                // Show pop-up dialog
                showDialog("Enrolled successfully,\nYou will be notified about an hour before the event starts.\nGet ready to party hard! \uD83C\uDF89\uD83C\uDF8A", true)
                EventUtils.showEventReminderNotification(this@PartyActivity, evTime)
            }
        }
    }

    private fun showDialog(message: String, goBack: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

                if (goBack) {
                    onBackPressed() // Go back to the previous page
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    private  fun openChatBox() {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }
}

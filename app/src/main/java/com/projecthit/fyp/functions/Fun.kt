package com.projecthit.fyp.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projecthit.fyp.BuildConfig


val database = FirebaseDatabase.getInstance(BuildConfig.URL)


fun readState(relayNumber: String, onRead: (value: Boolean) -> Unit){
    val myRef = FirebaseDatabase.getInstance().getReference("myesp32/relay/$relayNumber")

    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(Boolean::class.java) ?: false
            onRead(value)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error here
        }
    })
}

fun readTimer(relayNumber: String, onRead: (value: Int) -> Unit){
    val myRef = FirebaseDatabase.getInstance().getReference("myesp32/relay/$relayNumber")

    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(Int::class.java) ?: 0
            onRead(value)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error here
        }
    })
}




fun writeState(relayNumber: String,value: Boolean){
    val myRef = FirebaseDatabase.getInstance().getReference("myesp32/relay/$relayNumber/status")
    myRef.setValue(value)
}

fun writeTimer(relayNumber: String,value: Int){
    val myRef = FirebaseDatabase.getInstance().getReference("myesp32/relay/$relayNumber/timer")
    myRef.setValue(value)
}




fun readTemp(onRead: (value: Float) -> Unit){
    val myRef = database.getReference("myesp32/sensor/temperature")
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(Float::class.java)
            onRead(value!!)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

}

fun readHumidity(onRead: (value: Float) -> Unit){
    val myRef = database.getReference("myesp32/sensor/humidity")
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(Float::class.java)
            onRead(value!!)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

}




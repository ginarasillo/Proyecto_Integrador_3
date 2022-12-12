package com.giniita.guarderia

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.giniita.guarderia.databinding.ActivityInsertionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etDogName: EditText
    private lateinit var etDogAge: EditText
    private lateinit var etDogBreed: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etDogName = findViewById(R.id.etDogName)
        etDogAge = findViewById(R.id.etDogAge)
        etDogBreed = findViewById(R.id.etDogBreed)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Dogs")

        btnSaveData.setOnClickListener {
            saveDogData()
        }
    }

    private fun saveDogData() {

        //getting values
        val dogName = etDogName.text.toString()
        val dogAge = etDogAge.text.toString()
        val dogBreed = etDogBreed.text.toString()

        if (dogName.isEmpty()) {
            etDogName.error = "Ingresa un nombre"
        }
        if (dogAge.isEmpty()) {
            etDogAge.error = "Ingresa una edad"
        }
        if (dogBreed.isEmpty()) {
            etDogBreed.error = "Ingresa una raza"
        }

        val dogId = dbRef.push().key!!

        val dog = DogModel(dogId, dogName, dogAge, dogBreed)

        dbRef.child(dogId).setValue(dog)
            .addOnCompleteListener {
                Toast.makeText(this, "Se ha guardado la informacion correctamente", Toast.LENGTH_LONG).show()

                etDogName.text.clear()
                etDogAge.text.clear()
                etDogBreed.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
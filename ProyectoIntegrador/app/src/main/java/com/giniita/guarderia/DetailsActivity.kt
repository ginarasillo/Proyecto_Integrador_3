package com.giniita.guarderia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.giniita.guarderia.databinding.ActivityDetailsBinding
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var tvDogId: TextView
    private lateinit var tvDogName: TextView
    private lateinit var tvDogAge: TextView
    private lateinit var tvDogBreed: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("dogId").toString(),
                intent.getStringExtra("dogName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("dogId").toString()
            )
        }
    }

    private fun initView() {
        tvDogId = findViewById(R.id.tvDogId)
        tvDogName = findViewById(R.id.tvDogName)
        tvDogAge = findViewById(R.id.tvDogAge)
        tvDogBreed = findViewById(R.id.tvDogBreed)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvDogId.text = intent.getStringExtra("dogId")
        tvDogName.text = intent.getStringExtra("dogName")
        tvDogAge.text = intent.getStringExtra("dogAge")
        tvDogBreed.text = intent.getStringExtra("dogBreed")

    }

    private fun openUpdateDialog(
        dogId: String,
        dogName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etDogName = mDialogView.findViewById<EditText>(R.id.etDogName)
        val etDogAge = mDialogView.findViewById<EditText>(R.id.etDogAge)
        val etDogBreed = mDialogView.findViewById<EditText>(R.id.etDogBreed)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etDogName.setText(intent.getStringExtra("dogName").toString())
        etDogAge.setText(intent.getStringExtra("dogAge").toString())
        etDogBreed.setText(intent.getStringExtra("dogBreed").toString())

        mDialog.setTitle("Actualizando datos de $dogName")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateDogData(
                dogId,
                etDogName.text.toString(),
                etDogAge.text.toString(),
                etDogBreed.text.toString()
            )

            Toast.makeText(applicationContext, "Datos del perro actualizados", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvDogName.text = etDogName.text.toString()
            tvDogAge.text = etDogAge.text.toString()
            tvDogBreed.text = etDogBreed.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateDogData(
        id: String,
        name: String,
        age: String,
        breed: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Dogs").child(id)
        val dogInfo = DogModel(id, name, age, breed)
        dbRef.setValue(dogInfo)
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Dogs").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Datos del perro borrados", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
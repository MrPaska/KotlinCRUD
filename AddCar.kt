package com.example.kotlincrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddCar : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        val car_brand = findViewById<EditText>(R.id.editText_car_brand)
        val car_model = findViewById<EditText>(R.id.editText_car_model)
        val car_body = findViewById<EditText>(R.id.editText_car_body)
        val car_year = findViewById<EditText>(R.id.editText_car_year)
        val submit = findViewById<Button>(R.id.button_submit)

        submit.setOnClickListener {
            val in_car_brand = car_brand.text.toString()
            val in_car_model = car_model.text.toString()
            val in_car_body = car_body.text.toString()
            val in_car_year = car_year.text.toString()

            if (in_car_brand.isEmpty() || in_car_model.isEmpty() || in_car_body.isEmpty() || in_car_year.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val databaseReference = FirebaseDatabase.getInstance().reference // Gets database reference
                db = FirebaseDatabase.getInstance().getReference("cars") // Get database table name
                val id = databaseReference.push().key // Generates ID
                val car = CarDataSet(id, in_car_brand, in_car_model, in_car_body, in_car_year) // Put values from input to dataSet

                db.child(id.toString()).setValue(car).addOnSuccessListener {
                    car_brand.text.clear()
                    car_model.text.clear()
                    car_body.text.clear()
                    car_year.text.clear()
                    Toast.makeText(this, "Data Saved Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Data Save Failed :( ", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }
}
package com.example.kotlincrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CarDetails : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        val update_btn = findViewById<Button>(R.id.btnUpdate)
        val delete_btn = findViewById<Button>(R.id.btnDelete)

        val heading_car_brand = findViewById<TextView>(R.id.heading_car_brand)
        val tv_car_brand = findViewById<TextView>(R.id.tv_car_brand)
        val tv_car_model = findViewById<TextView>(R.id.tv_car_model)
        val tv_car_body = findViewById<TextView>(R.id.tv_car_body)
        val tv_car_year = findViewById<TextView>(R.id.tv_car_year)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null)
        {
            update_btn.visibility = View.GONE
            delete_btn.visibility = View.GONE
        }
        setValuesToViews(
            heading_car_brand,
            tv_car_brand,
            tv_car_model,
            tv_car_body,
            tv_car_year
        )
        update_btn.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("carID").toString(),
                intent.getStringExtra("carBrand").toString(),
                heading_car_brand,
                tv_car_brand,
                tv_car_model,
                tv_car_body,
                tv_car_year
            )
        }
        delete_btn.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("carID").toString()
            )
        }
    }

    private fun setValuesToViews(
        heading_car_brand: TextView,
        tv_car_brand: TextView,
        tv_car_model: TextView,
        tv_car_body: TextView,
        tv_car_year: TextView
    ) {
        heading_car_brand.text = intent.getStringExtra("carBrand")
        tv_car_brand.text = intent.getStringExtra("carBrand")
        tv_car_model.text = intent.getStringExtra("carModel")
        tv_car_body.text = intent.getStringExtra("carBody")
        tv_car_year.text = intent.getStringExtra("carYear")
    }

    private fun openUpdateDialog(
        carID: String,
        carBrand: String,
        heading_car_brand: TextView,
        tv_car_brand: TextView,
        tv_car_model: TextView,
        tv_car_body: TextView,
        tv_car_year: TextView
    ){
        val cDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val cDialogView = inflater.inflate(R.layout.update_dialog, null) // Inflate a new view hierarchy from the specified xml resource

        cDialog.setView(cDialogView)

        val et_car_brand = cDialogView.findViewById<EditText>(R.id.et_car_brand)
        val et_car_model = cDialogView.findViewById<EditText>(R.id.et_car_model)
        val et_car_body = cDialogView.findViewById<EditText>(R.id.et_car_body)
        val et_car_year = cDialogView.findViewById<EditText>(R.id.et_car_year)
        val btnUpdateData = cDialogView.findViewById<Button>(R.id.btnUpdateData)

        et_car_brand.setText(intent.getStringExtra("carBrand").toString())
        et_car_model.setText(intent.getStringExtra("carModel").toString())
        et_car_body.setText(intent.getStringExtra("carBody").toString())
        et_car_year.setText(intent.getStringExtra("carYear").toString())

        cDialog.setTitle("Updating $carBrand record")

        val alertDialog = cDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCarData(
                carID,
                et_car_brand.text.toString(),
                et_car_model.text.toString(),
                et_car_body.text.toString(),
                et_car_year.text.toString()

            )
            Toast.makeText(applicationContext, "Car data updated", Toast.LENGTH_LONG).show()

            heading_car_brand.text = et_car_brand.text.toString()
            tv_car_brand.text = et_car_brand.text.toString()
            tv_car_model.text = et_car_model.text.toString()
            tv_car_body.text = et_car_body.text.toString()
            tv_car_year.text = et_car_year.text.toString()

            alertDialog.dismiss()
        }
    }
    private fun updateCarData(
        id:String,
        brand:String,
        model:String,
        body:String,
        year:String

    ){
        if (brand.isEmpty() || model.isEmpty() || body.isEmpty() || year.isEmpty()){
            Toast.makeText(this, "Please fill all inputs", Toast.LENGTH_SHORT).show()
            return
        }else{
            val db = FirebaseDatabase.getInstance().getReference("cars").child(id)
            val car = CarDataSet(id, brand, model, body, year)
            db.setValue(car)
        }
    }
    private fun deleteRecord(ID: String) {
        val db = FirebaseDatabase.getInstance().getReference("cars").child(ID)
        val remove = db.removeValue()

        remove.addOnSuccessListener {
            Toast.makeText(this, "Car Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to delete :( ", Toast.LENGTH_SHORT).show()
        }
    }
}
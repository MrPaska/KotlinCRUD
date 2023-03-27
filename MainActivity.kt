package com.example.kotlincrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var carRecyclerView: RecyclerView
    private lateinit var carList: ArrayList<CarDataSet>
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance(); // gets a string of url (location)

        val add_car = findViewById<FloatingActionButton>(R.id.add_button)
        if (auth.currentUser == null)
        {
            add_car.visibility = View.GONE
        }
            add_car.setOnClickListener {
                val intent = Intent(this, AddCar::class.java)
                startActivity(intent)
        }
            carRecyclerView = findViewById(R.id.carRecycleViewer) // UI Data List
            carRecyclerView.layoutManager = LinearLayoutManager(this)
            carList = arrayListOf<CarDataSet>()

            getCarData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.logout, menu)  // adding button
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item1 -> {
                auth.signOut()
                Toast.makeText(this, "Bye", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun getCarData() {
        carRecyclerView.visibility = View.GONE // Hide RecyclerView

        db = FirebaseDatabase.getInstance().getReference("cars") // Get database table name

        db.addValueEventListener(object : ValueEventListener{ // Listens for data changes to a specific location ("cars")
            override fun onDataChange(snapshot: DataSnapshot) { // DataSnapshot instance contains data from a Firebase Database
                carList.clear()
                if (snapshot.exists()){ // IF data exists
                    for (carSnap in snapshot.children){
                        val carData = carSnap.getValue(CarDataSet::class.java) // Gets all values from car DB by carDataSet class
                        carList.add(carData!!) // !! != null
                    }
                    val cAdapter = CarAdapter(carList) // Assign and send values to adapter
                    carRecyclerView.adapter = cAdapter // Assign RW for „cAdapter“

                    cAdapter.setOnItemClickListerner(object : CarAdapter.onItemClickListerner{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, CarDetails::class.java)

                            intent.putExtra("carID", carList[position].carID)
                            intent.putExtra("carBrand", carList[position].carBrand)
                            intent.putExtra("carModel", carList[position].carModel)
                            intent.putExtra("carBody", carList[position].carBody)
                            intent.putExtra("carYear", carList[position].carYear)

                            startActivity(intent)
                        }
                    })
                    carRecyclerView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
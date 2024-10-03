// StoreActivity.kt
package com.example.productstore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class StoreActivity : AppCompatActivity() {

    private val products = mutableListOf<Product>()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var productImageView: ImageView
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        setSupportActionBar(findViewById(R.id.toolbar))
        val productNameInput: EditText = findViewById(R.id.productNameInput)
        val productPriceInput: EditText = findViewById(R.id.productPriceInput)
        productImageView = findViewById(R.id.productImageView)
        val addProductButton: Button = findViewById(R.id.addProductButton)
        val productListView: ListView = findViewById(R.id.productListView)
        val productDescriptionInput: EditText = findViewById(R.id.productDescriptionInput)


        customAdapter = CustomAdapter(this, products)
        productListView.adapter = customAdapter

        productImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        addProductButton.setOnClickListener {
            val name = productNameInput.text.toString()
            val price = productPriceInput.text.toString().toDoubleOrNull()
            val description = productDescriptionInput.text.toString()

            if (name.isEmpty() || price == null || description.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val product = Product(name, price, description, imageUri.toString())
                products.add(product)
                customAdapter.notifyDataSetChanged()
                clearInputs()
            }
        }
    }

    private fun clearInputs() {
        val productNameInput: EditText = findViewById(R.id.productNameInput)
        val productPriceInput: EditText = findViewById(R.id.productPriceInput)
        val productDescriptionInput: EditText = findViewById(R.id.productDescriptionInput)
        val productImageView: ImageView = findViewById(R.id.productImageView)

        productNameInput.text.clear()
        productPriceInput.text.clear()
        productDescriptionInput.text.clear()
        productImageView.setImageURI(null)
        imageUri = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finishAffinity()
                Toast.makeText(this, "Программа завершена", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            productImageView.setImageURI(imageUri)
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val updatedProduct = data?.getParcelableExtra<Product>("updatedProduct")
            val position = data?.getIntExtra("position", -1) ?: -1
            if (updatedProduct != null && position != -1) {
                products[position] = updatedProduct
                customAdapter.notifyDataSetChanged()
            }
        }
    }
}

package com.example.productstore



import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productDetailImageView: ImageView
    private lateinit var productDetailName: EditText
    private lateinit var productDetailPrice: EditText
    private lateinit var productDetailDescription: EditText
    private var imageUri: Uri? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        productDetailImageView = findViewById(R.id.productDetailImageView)
        productDetailName = findViewById(R.id.productDetailName)
        productDetailPrice = findViewById(R.id.productDetailPrice)
        productDetailDescription = findViewById(R.id.productDetailDescription)
        val saveButton: Button = findViewById(R.id.saveButton)

        val product = intent.getParcelableExtra<Product>("product")
        position = intent.getIntExtra("position", -1)
        product?.let {
            productDetailName.setText(it.name)
            productDetailPrice.setText(it.price.toString())
            productDetailDescription.setText(it.description)
            imageUri = Uri.parse(it.imageUri)
            productDetailImageView.setImageURI(imageUri)
        }

        productDetailImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)
        }

        saveButton.setOnClickListener {
            val updatedProduct = Product(
                productDetailName.text.toString(),
                productDetailPrice.text.toString().toDoubleOrNull() ?: 0.0,
                productDetailDescription.text.toString(),
                imageUri.toString()
            )
            val resultIntent = Intent()
            resultIntent.putExtra("updatedProduct", updatedProduct)
            resultIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
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
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            productDetailImageView.setImageURI(imageUri)
        }
    }
}

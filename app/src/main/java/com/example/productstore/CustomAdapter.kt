// CustomAdapter.kt
package com.example.productstore

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(private val context: Context, private val products: MutableList<Product>) : BaseAdapter() {

    override fun getCount(): Int { return products.size }

    override fun getItem(position: Int): Any { return products[position] }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)

        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)

        val product = products[position]
        productName.text = product.name
        productPrice.text = product.price.toString()
        productImage.setImageURI(Uri.parse(product.imageUri))

        view.setOnClickListener {
            showOptionsDialog(position)
        }

        return view
    }

    private fun showOptionsDialog(position: Int) {
        val options = arrayOf("Изменить", "Удалить")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите действие")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Изменить
                    val intent = Intent(context, ProductDetailActivity::class.java)
                    intent.putExtra("product", products[position])
                    intent.putExtra("position", position)
                    (context as Activity).startActivityForResult(intent, 2)
                }
                1 -> {
                    // Удалить
                    products.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
        builder.show()
    }
}

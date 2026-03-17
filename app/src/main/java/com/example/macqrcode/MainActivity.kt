package com.example.macqrcode

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvMacAddress = findViewById<TextView>(R.id.tvMacAddress)
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)

        // Read MAC from intent extra, which is now the primary method
        val intentMacAddress = intent.getStringExtra("MAC_ADDRESS")
        if (!intentMacAddress.isNullOrEmpty()) {
            tvMacAddress.text = "MAC: $intentMacAddress"
            val barcodeBitmap = generateBarcode(intentMacAddress)
            qrCodeImageView.setImageBitmap(barcodeBitmap)
            // Disable the button as it's not needed when launched via script
            btnGenerate.isEnabled = false
            btnGenerate.text = "MAC from Host"
        } else {
            // If the app is launched manually, guide the user to use the script
            tvMacAddress.text = "Launch via the script to show a MAC address barcode."
            qrCodeImageView.setImageDrawable(null)
        }

        btnGenerate.setOnClickListener {
            Toast.makeText(this, "Automatic detection is not possible. Please use the script.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Converts a string into a Barcode Bitmap
     */
    private fun generateBarcode(text: String): Bitmap? {
        try {
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 800, 300)
            val barcodeEncoder = BarcodeEncoder()
            return barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}

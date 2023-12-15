package com.usyssoft.stripepaymentandroid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.usyssoft.stripepaymentandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b : ActivityMainBinding

    lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)





        b.apply {
            customCreditPayment.setOnClickListener {
                startActivity(Intent(this@MainActivity,CustomCreditCardPaymentActivity::class.java))
            }
            PaymentSheetBtn.setOnClickListener {
                PaymentSheetFunction()
            }
        }
    }

    private fun PaymentSheetFunction() {
        val paymentIntentClientSecret = "paymentIntentClientSecret"
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret)
    }


    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(applicationContext, "successful payment", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(applicationContext, "cancaled payment", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(applicationContext, "error payment: ${paymentResult.error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
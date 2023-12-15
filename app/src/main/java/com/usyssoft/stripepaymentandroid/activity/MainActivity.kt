package com.usyssoft.stripepaymentandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            PaymentSheetBtn.setOnClickListener {
                PaymentSheetFunction()
            }
        }
    }

    private fun PaymentSheetFunction() {
        val paymentIntentClientSecret = "payment_intent_client_secret_key"
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret)
    }


    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                println("Handle successful payment")
            }
            is PaymentSheetResult.Canceled -> {
                println("Handle canceled payment")
            }
            is PaymentSheetResult.Failed -> {
                println("Handle failed payment error:${paymentResult.error.message}")
            }
        }
    }

}
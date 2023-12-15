package com.usyssoft.stripepaymentandroid.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.usyssoft.stripepaymentandroid.R
import com.usyssoft.stripepaymentandroid.databinding.ActivityCustomCreditCardPaymentBinding
import com.usyssoft.stripepaymentandroid.util.timeHideKeyboard
import kotlinx.coroutines.launch

class CustomCreditCardPaymentActivity : AppCompatActivity() {
    private lateinit var b : ActivityCustomCreditCardPaymentBinding

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCustomCreditCardPaymentBinding.inflate(layoutInflater)
        setContentView(b.root)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }

        })

        stripeFunction()

        b.apply {
            backBtn.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            cardnamesurname.timeHideKeyboard()
            cardNumber.timeHideKeyboard()
            cardExpirationDate.timeHideKeyboard()
            cardCvc.timeHideKeyboard()

            paymentFinishBtn.setOnClickListener {
                val cvc = b.cardCvc.text.toString()
                val cardNumber = b.cardNumber.text.toString()
                //val cardNumber = "4242424242424242" //Test Card Number
                val nameSurname = b.cardnamesurname.text.toString().trim()
                val cardExpirationDate = b.cardExpirationDate.text.toString()
                if (cvc.length != 3) {
                    Toast.makeText(applicationContext, "Cvc Error: Empty", Toast.LENGTH_SHORT).show()
                } else if (cardExpirationDate.length != 5) {
                    Toast.makeText(applicationContext, "card Expiration Date Error: Empty", Toast.LENGTH_SHORT).show()
                } else if (cardNumber.length != 19) {
                    Toast.makeText(applicationContext, "card Number Error: Empty or 19 lenght <", Toast.LENGTH_SHORT).show()
                } else {
                    val parts = cardExpirationDate.split("/")
                    val month = parts[0]
                    val year = parts[1]
                    val card = PaymentMethodCreateParams.Card.Builder()
                        .setNumber(cardNumber.replace("-","").trim())
                        .setExpiryMonth(month.toInt())
                        .setExpiryYear(year.toInt())
                        .setCvc(cvc)
                        .build()
                    val paymentMethodCreateParams = PaymentMethodCreateParams.create(card)
                    /*
                    val card = PaymentMethodCreateParams.Card(cardNumber, expMonth, expYear, cvc)
                    val paymentMethodCreateParams = PaymentMethodCreateParams.create(
                        card
                    )*/
                    val confirmParamsSS = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(
                            paymentMethodCreateParams,
                            paymentIntentClientSecret
                        )
                    lifecycleScope.launch {
                        paymentLauncher.confirm(confirmParamsSS)
                    }

                }

            }
        }
    }
    private fun stripeFunction() {
        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            this@CustomCreditCardPaymentActivity,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId, ::onPaymentResult
        )
        startCheckout()
    }
    @SuppressLint("SetTextI18n")
    private fun startCheckout() {
        val client_secret = intent.getStringExtra("client_secret")
        val payment_price = intent.getStringExtra("paymentprice")

        if (payment_price != null) {
            b.paymentFinishBtn.text = "$payment_price Pay"
        }else {
            b.paymentFinishBtn.text = "Pay"
        }

        if (client_secret != null) {
            this.paymentIntentClientSecret = client_secret
        }else {
            this.paymentIntentClientSecret = "payment_intent_client_secret_key"
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {

        when (paymentResult) {
            is PaymentResult.Completed -> {
                println("Handle successful payment")
            }

            is PaymentResult.Canceled -> {
                println("Handle canceled payment")
            }

            is PaymentResult.Failed -> {
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                println("Handle failed payment error:${paymentResult.throwable.message}")
            }
        }
    }
}
package com.usyssoft.stripepaymentandroid

import android.app.Application
import com.stripe.android.PaymentConfiguration

class App : Application() {
    override fun onCreate() {
        super.onCreate()


        stripeInit()

    }

    private fun stripeInit() {
        PaymentConfiguration.init(
            applicationContext,
            "stripe_publishableKey"
        )
    }
}
package com.example.wlallet

import java.time.temporal.TemporalAmount
import java.util.*

class Market {
    private var CHF: Int = 1
    private var TC: Int = 100
    private var BTC: Int = 20500

    fun getMarketValues(): String {
        return "The Value of CHF is: $CHF\n" +
                "The Value of TC is: $TC CHF\n" +
                "The Value of BTC is: $BTC CHF\n "
    }

    fun swapTo(currentCurrency: String, currency: String , balance: Int): Int {
        var swappedBalance = balance
        if(currentCurrency == "CHF") {
            if (currency == "TC") {
                swappedBalance = swappedBalance * CHF / TC
                return swappedBalance
            } else if(currency == "BTC") {
                swappedBalance = swappedBalance * CHF / BTC
                return swappedBalance
            }
        }

        if(currentCurrency == "TC") {
            if (currency == "CHF") {
                swappedBalance = swappedBalance * TC / CHF
                return swappedBalance
            } else if(currency == "BTC") {
                swappedBalance = swappedBalance * TC / BTC
                return swappedBalance
            }
        }

        if(currentCurrency == "BTC") {
            if (currency == "CHF") {
                swappedBalance = swappedBalance * BTC / CHF
                return swappedBalance
            } else if(currency == "TC") {
                swappedBalance = swappedBalance * BTC / TC
                return swappedBalance
            }
        }

        return swappedBalance
    }
}
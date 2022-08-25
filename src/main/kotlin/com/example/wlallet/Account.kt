package com.example.wlallet
import java.util.*
import kotlin.collections.ArrayList


class Account(password: String) {
    var accountName: String
    private var password: String
    private val recoveryPhrase: List<String>
    private val privateKey: String
    private val publicKey: String
    private val file = DataFile()
    private var balance: Int
    private var currency: String

    init {
        this.password = password
        this.recoveryPhrase = generateRandomRecoveryPhrase()
        this.privateKey = generatePrivateKey()
        this.publicKey = generatePublicKey()
        this.accountName = "Account"
        this.balance = 0
        this.currency = "TC"
    }

    fun getCurrentUser(users: ArrayList<User>): User {
        val user = User(
            name = accountName + (users.size + 1),
            recoveryPhrase = getRecoveryPhrase().toString(),
            password = password,
            privateKey = privateKey,
            publicKey = publicKey,
            balance = balance,
            currency = currency
        )

        return user
    }

    fun getUserByLogin(users: ArrayList<User>, accountName: String, password: String): User? {
        var foundUser: User? = null
        for (user in users){
            if (accountName == user.name && password == user.password) {
                foundUser = user
            }
        }
        return foundUser
    }

    fun send(users: ArrayList<User>, accountName: String, password: String) {
        val user = getUserByLogin(users, accountName, password)
        println("Please enter the address of the wallet which you would like to send TC")
        val publicKeyInput = readLine()!!
        println("Please enter the amount that you would like to send")
        val amount = readLine()!!.toInt()
        val foundUserArray = users.filter { it.publicKey == publicKeyInput }
        val foundUser = foundUserArray[0]
        if (user != null) {
            if (user.balance - amount >= 0) {
                foundUser.balance = foundUser.balance + amount
                user.balance = user.balance - amount
                balance -= amount
                println("Your transaction has been completed successfully")
            } else {
                println("Something went wrong during your transaction")
            }
        }
    }

    fun buy(users: ArrayList<User>, accountName: String, password: String) {
        val user = getUserByLogin(users, accountName, password)
        println("Please enter the amount that you would like to buy")
        val amountInput = readLine()!!.toInt()
        user!!.balance += amountInput
        balance += amountInput
        println("Your transaction has been completed successfully")
        println("Your current balance is at: $balance TC")

    }

    fun swap(users: ArrayList<User>, accountName: String, password: String) {
        val market = Market()
        val user = getUserByLogin(users, accountName, password)
        println("Please enter the currency that you would like to swap to")
        val currencyInput = readLine()!!
        user!!.balance = market.swapTo(currency, currencyInput, balance)
        user.currency = currencyInput
        println("Your current balance is at: $balance $currency")
    }

    fun getRecoveryPhrase(): List<String>{
        return recoveryPhrase
    }

    fun getCurrentAccountDetails(users: ArrayList<User>): String {
        return "\nYour account name is: ${accountName + (users.size + 1)}\n" +
                "Your public key is: $publicKey\n" +
                "Your account balance is: $balance TC"
    }

    fun getAccountDetails(users: ArrayList<User>, accountName: String, password: String): String {
        val user = getUserByLogin(users, accountName, password)
        if (user != null) {
            return "\nYour account name is: ${user.name}\n" +
                    "Your public key is: ${user.publicKey}\n" +
                    "Your account balance is: ${user.balance} ${user.currency}\n"
        }
        return "Wrong Password"
    }

    private fun generatePrivateKey(): String{
        val privateUuid = UUID.randomUUID()

        return privateUuid.toString()
    }

    private fun generatePublicKey(): String{
        val publicUuid = UUID.randomUUID()

        return publicUuid.toString()
    }

    private fun generateRandomRecoveryPhrase(): List<String> {
        val possibleWords = arrayListOf<String>()
        val generatedRecoveryPhrase = arrayListOf<String>()
        val wordsFile = file.getWordsFile()

        if (wordsFile.exists()){
            wordsFile.forEachLine { possibleWords.add(it) }
        }
        else if (!wordsFile.exists()) {
            println("An error has occurred during generation of recovery phrase")
        }

        for (i in 0..4){
            val randomWord = possibleWords[(0..possibleWords.count()).random()]
            if(!generatedRecoveryPhrase.contains(randomWord)){
                generatedRecoveryPhrase.add(randomWord)
            }
        }

        return generatedRecoveryPhrase
    }
}
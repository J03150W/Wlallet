package com.example.wlallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import java.security.MessageDigest
import java.util.*
import kotlin.system.exitProcess

@SpringBootApplication
class WlalletApplication

fun main() {
    val users: ArrayList<User> = ArrayList()
    var account: Account? = null;
    var action = Command.HELP

    try {
        do {
            account = handleAction(action, account, users)
            action = getAction()
        } while (action != Command.END)
    } catch (IllegalArgumentException : IllegalArgumentException) {
        println(IllegalArgumentException.message)
    }
}

fun getAction(): Command {
    val action = readLine()!!.lowercase(Locale.getDefault())

    for (command in Command.values()){
        if (command.command == action){
            return command
        }
    }

    throw IllegalArgumentException("Command does not exist")
}

fun handleAction(action: Command,  account: Account?, users: ArrayList<User>): Account? {
    var tempAccount = account
    var market = Market()

    when (action) {
        Command.HELP -> {
            help()
        }
        Command.CREATE -> {
            tempAccount = createWallet(users)
            users.add(tempAccount.getCurrentUser(users))
        }
        Command.END -> {
            exitProcess(0)
        }
        Command.ACCOUNT -> {
            println("Please enter your account name")
            val accountNameInput = readLine()!!
            println("Please enter your password")
            val passwordInput = hash(readLine()!!)
            if (tempAccount != null) {
                if (tempAccount.getUserByLogin(users, accountNameInput, passwordInput) != null) {
                    try {
                        do {
                            getAccountActions()
                            val accountAction = readLine()!!
                            when(accountAction) {
                                Command.DETAILS.command -> {
                                    println(tempAccount.getAccountDetails(users, accountNameInput, passwordInput))
                                }
                                Command.SEND.command -> {
                                    println(tempAccount.send(users, accountNameInput, passwordInput))
                                }
                                Command.BUY.command -> {
                                    tempAccount.buy(users, accountNameInput, passwordInput)
                                }
                                Command.SWAP.command -> {
                                    tempAccount.swap(users, accountNameInput, passwordInput)
                                }
                                Command.MARKET.command -> {
                                    println(market.getMarketValues())
                                }
                                Command.LOGOUT.command -> {
                                    help()
                                }
                            }
                        } while (accountAction != Command.LOGOUT.command)
                    } catch (IllegalArgumentException : IllegalArgumentException) {
                        println(IllegalArgumentException.message)
                    }
                } else {
                    println("Password not")
                    help()
                }
            }
        }
    }

    return tempAccount;
}

fun hash(s: String): String {
    val bytes = s.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)

    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun createWallet(users: ArrayList<User>): Account {
    var account: Account? = null

    while (account == null){
        println("\nPlease enter a password")
        val passwordInput = hash(readLine()!!)
        println("Please enter the same password to confirm")
        val passwordConfirmInput = hash(readLine()!!)
        if(passwordInput == passwordConfirmInput && passwordInput != hash("")) {
            account = Account(passwordInput)
            val recoveryPhrase = account.getRecoveryPhrase()

            println("\n" + Position.FIRST.position + " word: " + recoveryPhrase[0])
            println(Position.SECOND.position + " word: " + recoveryPhrase[1])
            println(Position.THIRD.position + " word: " + recoveryPhrase[2])
            println(Position.FOURTH.position + " word: " + recoveryPhrase[3])
            println(Position.FIFTH.position + " word: " + recoveryPhrase[4])

            var countOfConfirmedWords = 0
            val positions = arrayListOf(0, 1, 2, 3, 4)
            positions.shuffle()

            do {
                do {
                    println("\nPlease enter the ${Position.values()[positions[countOfConfirmedWords]].position.lowercase(
                        Locale.getDefault()
                    )} word")
                    val confirmRecoveryPhrase = readLine()!!
                    val isRecoveryPhraseValid = recoveryPhrase[positions[countOfConfirmedWords]] == confirmRecoveryPhrase
                }
                while (!isRecoveryPhraseValid)

                countOfConfirmedWords += 1;
            }
            while (countOfConfirmedWords != 5)
            println(account.getCurrentAccountDetails(users))
            help()
        }
        else if (passwordInput == hash("") && passwordConfirmInput == hash("")) {
            println("\nPassword cannot be empty")
        }
        else {
            println("\nPasswords do not match")
        }
    }
    return account
}

fun help() {
    println("\nType 'create' to create a new wallet"  )
    println("Type 'account' to access account actions")
    println("Type 'end' to end the program\n")
}

fun getAccountActions() {
    println("\nType 'details' to access account details")
    println("Type 'send' to send another wallet an amount")
    println("Type 'swap' to swap currency")
    println("Type 'buy' to buy coins")
    println("Type 'market' to access market values\n")
    println("Type 'logout' top log out of your account")
}
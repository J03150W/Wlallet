package com.example.wlallet

enum class Command(val command: String?) {
    HELP("help"),
    CREATE("create"),
    END("end"),
    ACCOUNT("account"),
    DETAILS("details"),
    SEND("send"),
    BUY("buy"),
    SWAP("swap"),
    MARKET("market"),
    LOGOUT("logout")
}

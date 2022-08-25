package com.example.wlallet

import javax.persistence.GeneratedValue
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class User(
    @Id @GeneratedValue var id: Long? = null,
    var name: String = "Account$id",
    var recoveryPhrase: String,
    var password: String,
    var privateKey: String,
    var publicKey: String,
    var balance: Int,
    var currency: String,
)
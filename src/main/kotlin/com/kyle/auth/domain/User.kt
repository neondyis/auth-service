package com.kyle.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "users", indexes = [Index(columnList = "username", unique = true)])
class User(
    @Id @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var username: String,
    @Column(nullable = false, unique = true)
    var email: String,
    @Column(nullable = false)
    var passwordHash: String,
    var enabled: Boolean = true,
)
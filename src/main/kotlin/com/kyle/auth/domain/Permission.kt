package com.kyle.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Table
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id

@Entity
@Table(name = "permissions")
class Permission(
    @Id @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var code: String,
)
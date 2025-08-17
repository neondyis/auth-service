package com.kyle.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Table
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Index

@Entity
@Table(name = "apps", indexes = [Index(columnList = "key", unique = true)])
class AppClient(
    @Id @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var specialKey: String,
    @Column(nullable = false)
    var name: String,
    var active: Boolean = true,
)
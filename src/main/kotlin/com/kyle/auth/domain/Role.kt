package com.kyle.auth.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Id @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    var name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    var app: AppClient,
    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")],
    )
    var permissions: MutableSet<Permission> = linkedSetOf(),
)
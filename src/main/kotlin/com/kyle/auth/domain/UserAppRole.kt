package com.kyle.auth.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "user_app_roles",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "app_id", "role_id"])],
)
class UserAppRole(
    @Id @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    var user: User,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "app_id")
    var app: AppClient,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "role_id")
    var role: Role,
)
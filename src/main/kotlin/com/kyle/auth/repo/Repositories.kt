package com.kyle.auth.repo

import com.kyle.auth.domain.AppClient
import com.kyle.auth.domain.Role
import com.kyle.auth.domain.User
import com.kyle.auth.domain.UserAppRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}

interface AppClientRepository : JpaRepository<AppClient, Long> {
    fun findBySpecialKey(key: String): AppClient?
}

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByAppSpecialKeyAndName(app: String, name:String): Role?

    @Query(
        """
        select r from Role r
        where r.app.specialKey = :appKey and r.name in :names
        """,
    )
    fun findAllByAppAndNames(appKey: String, names: Collection<String>): List<Role>
}

interface UserAppRoleRepository : JpaRepository<UserAppRole, Long> {
    @Query(
        """
        select uar from UserAppRole uar
        join fetch uar.role r
        join fetch r.permissions p
        where uar.user.username = :username and uar.app.specialKey = :appKey
        """,
    )
    fun findByUsernameAndAppKey(username: String, appKey: String): List<UserAppRole>
}
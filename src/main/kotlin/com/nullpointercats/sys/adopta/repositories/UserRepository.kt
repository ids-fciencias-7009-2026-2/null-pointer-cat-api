package com.nullpointercats.sys.adopta.repositories

import com.nullpointercats.sys.adopta.entities.UserEntity
import com.nullpointercats.sys.adopta.domain.User
import org.springframework.data.repository.CrudRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : CrudRepository<UserEntity, Int> {

    @Query("select u from UserEntity u where u.token = :token")
    fun findByToken(token: String): UserEntity?
    
    @Query("select u from UserEntity u where u.email = :email and u.password = :password")
    fun findUserByPasswordAndEmail(email: String, password: String): UserEntity?

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.token = :token where u.id = :id")
    fun updateTokenById(id: Int, token: String?)
    
    //fun findByEmail(email: String): UserEntity?

}
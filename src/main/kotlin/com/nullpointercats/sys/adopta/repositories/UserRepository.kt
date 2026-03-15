package com.nullpointercats.sys.adopta.repositories

import com.nullpointercats.sys.adopta.entities.UserEntity
import com.nullpointercats.sys.adopta.domain.User
import org.springframework.data.repository.CrudRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : CrudRepository<UserEntity, Int> {

fun findByEmail(email: String): UserEntity?

}
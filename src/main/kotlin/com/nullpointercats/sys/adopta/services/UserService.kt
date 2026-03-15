package com.nullpointercats.sys.adopta.services

import com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.domain.toUser
import com.nullpointercats.sys.adopta.entities.UserEntity
import com.nullpointercats.sys.adopta.repositories.UserRepository
import com.nullpointercats.sys.adopta.repositories.toUserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    val logger = LoggerFactory.getLogger(UserService::class.java)

    fun addNewUser(user: User): User {
        val userEntity = user.toUserEntity()
        userRepository.save(userEntity)
        user.password = "****"
        logger.info("User saved")
        return user
    }

    fun login(email: String, password: String): String {

    val user = userRepository.findByEmail(email)
        ?: throw RuntimeException("User not found")

    val hashedPassword = hash(password)

    if (user.password != hashedPassword) {
        throw RuntimeException("Invalid credentials")
    }

    val token = UUID.randomUUID().toString()

    user.token = token
    userRepository.save(user)

    return token
}


}
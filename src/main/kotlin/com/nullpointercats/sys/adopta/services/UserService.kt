package com.nullpointercats.sys.adopta.services

import com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.domain.toUser
import com.nullpointercats.sys.adopta.entities.UserEntity
import com.nullpointercats.sys.adopta.repositories.UserRepository
import com.nullpointercats.sys.adopta.repositories.toUserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.UUID


/*
*  UserService acts as an intermediary between the controllers and the UserReposity.
* */
@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    val logger = LoggerFactory.getLogger(UserService::class.java)

    /*
    * addNewUser()
    * Register a new user into the system.
    *
    * @param user domain object.
    * @return the saved user with the password field replace by "****".
    * */
    fun addNewUser(user: User): User {

        val userEntity = user.toUserEntity() // Map domain to database entity
        userRepository.save(userEntity)
        user.password = "****"
        logger.info("User saved")
        return user
    }

    /**
     * Authenticates a user using their email and password.
     * If the authentication is successful, the generated token is
     * returned to the caller so it can be sent back to the client.
     *
     * @param email The email address used to identify the user.
     * @param password The plaintext password provided by the client.
     * 
     * @return A unique session token associated with the authenticated user.
     * 
     *  @throws RuntimeException If the user does not exist or the credentials are invalid.
    */

    fun login(email: String, password: String): String {
        
        val user = userRepository.findByEmail(email)
        ?: throw RuntimeException("User not found")
        
        val hashedPassword = hashPassword(password)

        if (user.password != hashedPassword) {
            throw RuntimeException("Invalid credentials")
        }
        
        val token = UUID.randomUUID().toString()
        user.token = token
        userRepository.save(user)
        
        return token
    }
    
    fun hashPassword(password: String): String {
        val bytes = MessageDigest
        .getInstance("SHA-256")
        .digest(password.toByteArray())
        
        return bytes.joinToString("") { "%02x".format(it) }
    }

}
package com.nullpointercats.sys.adopta.user.services

import com.nullpointercats.sys.adopta.user.domain.User
import com.nullpointercats.sys.adopta.user.domain.toUser
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import com.nullpointercats.sys.adopta.user.repositories.toUserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID


/**
*  UserService acts as an intermediary between the controllers and the UserReposity.
* */
@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
    * Registers a new user into the system.
    *
    * @param user The user domain object to be registered.
    * @return user The saved user with the password field replace by "****".
     * or null when the email has been already registered before.
    * */
    fun addNewUser(user: User): User? {

        val existingUser = userRepository.findByEmail(user.email)
        if (existingUser != null) {
            logger.warn("There is already an account with email '${user.email}'")
            return null
        }

        val userEntity = user.toUserEntity() // Map domain to database entity
        userRepository.save(userEntity)
        user.password = "****"
        logger.info("User saved")
        return user
    }

    /** 
     * Authenticates a user using their email and password.
     * The credentials are validated against the data stored in the database.
     * If a matching user is found, a new session token is generated and
     * stored for that user.
     *
     * @param email The email address used to identify the user.
     * @param password The hashed password used for authentication.
     *
     * @return A User object containing the authenticated user's information,
     * or null if the credentials are invalid.
    */

    fun login(email: String, password: String): User? {
        val userEntity = userRepository
            .findUserByPasswordAndEmail(email, password)

        if (userEntity != null) {
            val token = UUID.randomUUID().toString()
            userEntity.token = token
            userRepository.save(userEntity)
        }
        return userEntity?.toUser()
    }

    /**
     * Retrieves a user associated with the given authentication token.
     *
     * @param token The session token assigned to a logged-in user.
     * @return The corresponding User if the token exists, otherwise null.
    */
    fun findByToken(token: String): User? {
        val userLogged = userRepository.findByToken(token)
        logger.info("User exists: ${userLogged.toString()}")
        return userLogged?.toUser()
    }

    /**
     * Logs out a user by invalidating their session token.
     *
     * @param token The session token to invalidate.
     * @return true if a user with that token was found and logged out, false otherwise.
     */
    fun logout(token: String): Boolean {
        val userEntity = userRepository.findByToken(token)
        return if (userEntity != null) {
            userRepository.clearTokenByToken(token)
            logger.info("User ${userEntity.email} logged out successfully")
            true
        } else {
            logger.warn("Logout attempted with invalid or expired token: $token")
            false
        }
    }

    /**
     * Update the information of an existing user.
     *
     * @param user The user domain object with the email to search and the new data to be updated.
     * @return user The updated user with the password field replace by "****".
     * or null when there is no user matching the provide email.
     * */
    fun updateUser(user: User): User? {
        val userEntity = userRepository.findByEmail(user.email)

        if (userEntity == null) {
            logger.warn("Not found user with email: ${user.email}")
            return null
        }

        userEntity.firstname = user.firstname
        userEntity.username = user.username
        userEntity.lastname = user.lastname
        userEntity.zipcode = user.zipcode

        val savedEntity = userRepository.save(userEntity)
        val updatedUser = savedEntity.toUser()
        updatedUser.password = "****"

        return updatedUser
    }

}
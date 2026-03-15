package com.nullpointercats.sys.adopta.repositories

import com.nullpointercats.sys.adopta.entities.UserEntity
import com.nullpointercats.sys.adopta.domain.User
import org.springframework.data.repository.CrudRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

/**
 * Repository responsible for accessing and managing UserEntity data
 * in the database. It provides custom queries used for authentication
 * and user session management.
 */
interface UserRepository : CrudRepository<UserEntity, Int> {

    /**
     * Finds a user by its authentication token.
     *
     * @param token The session token assigned to a logged-in user.
     * @return The corresponding UserEntity, or null if no user matches the token.
     */
    @Query("select u from UserEntity u where u.token = :token")
    fun findByToken(token: String): UserEntity?

    /**
     * Finds a user matching the given email and password.
     * Used during the login process to validate credentials.
     *
     * @param email The user's email.
     * @param password The hashed password.
     * @return The matching UserEntity if credentials are valid, otherwise null.
     */
    @Query("select u from UserEntity u where u.email = :email and u.password = :password")
    fun findUserByPasswordAndEmail(email: String, password: String): UserEntity?

    /**
     * Updates the token of a specific user.
     *
     * @param id The user identifier.
     * @param token The new token assigned to the user.
     */
    @Modifying
    @Transactional
    @Query("update UserEntity u set u.token = :token where u.id = :id")
    fun updateTokenById(id: Int, token: String?)
}
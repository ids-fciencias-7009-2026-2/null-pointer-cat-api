package com.nullpointercats.sys.adopta.repositories

import com.nullpointercats.sys.adopta.entities.UserEntity
import com.nullpointercats.sys.adopta.domain.User
import org.springframework.data.repository.CrudRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : CrudRepository<UserEntity, Int> {


 /**
     * Retrieves a user from the database using their email address.
     *
     * Spring Data JPA automatically derives the SQL query from
     * the method name. Internally, this corresponds to a query
     * similar to:
     *
     * SELECT * FROM adoption_user WHERE email = ?
     *
     * @param email The email address used to search for the user.
     * @return The UserEntity if a user with the given email exists,
     *         or null if no matching user is found.
     */
fun findByEmail(email: String): UserEntity?

}
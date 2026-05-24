package com.nullpointercats.sys.adopta.user.services

import com.nullpointercats.sys.adopta.user.domain.User
import com.nullpointercats.sys.adopta.user.domain.toDomain
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import com.nullpointercats.sys.adopta.user.repositories.toUserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import java.time.LocalDateTime



/**
*  UserService acts as an intermediary between the controllers and the UserReposity.
* */
@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    lateinit var mailSender: JavaMailSender

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
        return userEntity?.toDomain()
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
        return userLogged?.toDomain()
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
        val updatedUser = savedEntity.toDomain()
        updatedUser.password = "****"

        return updatedUser
    }

    fun forgotPassword(email: String): Boolean {
        val userEntity = userRepository.findByEmail(email) ?: return false

        val token = UUID.randomUUID().toString()
        val expiry = LocalDateTime.now().plusHours(1)

        userEntity.resetToken = token
        userEntity.resetTokenExpiry = expiry
        userRepository.save(userEntity)

        sendResetEmail(email, userEntity.firstname, token)
        return true
    }

    fun resetPassword(token: String, newPassword: String): Boolean {
        val userEntity = userRepository.findByResetToken(token) ?: return false

        if (userEntity.resetTokenExpiry == null ||
            userEntity.resetTokenExpiry!!.isBefore(LocalDateTime.now())) {
            logger.warn("Reset token expired for user ${userEntity.email}")
            return false
        }

        userEntity.password = hashResetPassword(newPassword)
        userEntity.resetToken = null
        userEntity.resetTokenExpiry = null
        userRepository.save(userEntity)

        logger.info("Password reset successfully for ${userEntity.email}")
        return true
    }

    private fun hashResetPassword(password: String): String {
        val bytes = java.security.MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun sendResetEmail(toEmail: String, firstName: String, token: String) {
        try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setTo(toEmail)
            helper.setSubject("🐾 Reset your AdoptaPet password")
            helper.setFrom("AdoptaPet <rebeca07e.r@gmail.com>")

            val resetLink = "http://localhost:5173/reset-password?token=$token"

            val html = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <h2 style="color: #1d423f;">🐾 Password Reset Request</h2>
                <p>Hi $firstName! We received a request to reset your AdoptaPet password.</p>
                <p>Click the button below. This link expires in <strong>1 hour</strong>.</p>
                <div style="text-align: center; margin: 30px 0;">
                    <a href="$resetLink"
                       style="background:#88b2a6;color:white;padding:14px 28px;border-radius:10px;
                              text-decoration:none;font-weight:bold;font-size:16px;">
                        Reset Password
                    </a>
                </div>
                <p style="color:#666;font-size:12px;">
                    If you didn't request this, ignore this email. Your password won't change.
                </p>
                <p style="color:#666;font-size:12px;">— AdoptaPet</p>
            </div>
        """.trimIndent()

            helper.setText(html, true)
            mailSender.send(message)
            logger.info("Reset email sent to $toEmail")
        } catch (e: Exception) {
            logger.error("Error sending reset email: ${e.message}")
        }
    }
}
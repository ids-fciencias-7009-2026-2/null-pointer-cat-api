package com.nullpointercats.sys.adopta.config

import com.nullpointercats.sys.adopta.user.services.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val userService: UserService
) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(AuthInterceptor::class.java)

    @Override
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any): Boolean {

        if (request.method.equals("OPTIONS")) return true;
        val token = request.getHeader("Authorization")
        val cleanToken = token?.removePrefix("Bearer ")?.trim().orEmpty()

        logger.info("[AuthInterceptor] [ATTEMPT] From token [${cleanToken.take(10)}]")

        if (cleanToken.isEmpty()) {
            logger.warn("[AuthInterceptor] [FAILED] No token given.")
            response.status = 401
            return false
        }

        val userFound = userService.findByToken(cleanToken)

        if (userFound == null) {
            logger.warn("[AuthInterceptor] [FAILED] No user found. Token may be invalid or expired.")
            response.status = 401
            return false
        }

        request.setAttribute("authenticatedUser", userFound)
        return true
    }
}
package com.cropyback.cropyback.authentication.filter

import com.cropyback.cropyback.authentication.service.JwtTokenService
import com.cropyback.cropyback.service.UserService
import com.nimbusds.jwt.JWTParser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.web.filter.OncePerRequestFilter


class JWTAuthenticationFilter(
    private val jwtService: JwtTokenService,
    private val service: UserService
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (!authorizationHeader.isNullOrEmpty() && authorizationHeader.startsWith("Bearer ")) {
            try {
                val token = JWTParser.parse(authorizationHeader.substring(7))

                if (jwtService.validateToken(token.serialize())) {
                    val username = token.jwtClaimsSet.claims["username"] as String
                    val userDetails = service.loadUserByUsername(username) // Fetch user details
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
                    return
                }
            } catch (e: JwtException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired")
                return
            }
        }
        filterChain.doFilter(request, response)

    }

}

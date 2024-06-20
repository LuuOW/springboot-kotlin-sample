package com.cropyback.cropyback.authentication.filter

import com.cropyback.cropyback.authentication.service.JwtTokenService
import com.cropyback.cropyback.database.sql.service.UserJpaService
import com.nimbusds.jwt.JWTParser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.web.filter.OncePerRequestFilter


class JWTAuthenticationFilter(
    private val jwtService: JwtTokenService,
    private val service: UserJpaService
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
                    val roles = token.jwtClaimsSet.claims["roles"] as List<String>

                    val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }.toCollection(mutableSetOf())

                    val userDetails = service.findUserByUsername(username)
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
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

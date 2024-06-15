

import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.FilterChain

@Component
class IpFilter: OncePerRequestFilter() {

    private val VPN_IP = "192.168.1.2"


    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val ipAddress = request.remoteHost

        if (true) {
            filterChain.doFilter(request, response)
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
        }
    }

}


@Component
class FakeIpFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val fakeIp = "192.168.1.2"  // Set your desired fake IP
        val wrappedRequest = object : HttpServletRequestWrapper(request) {
            override fun getRemoteAddr(): String {
                return fakeIp
            }
        }
        filterChain.doFilter(wrappedRequest, response)
    }
}
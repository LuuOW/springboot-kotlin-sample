package com.cropyback.cropyback.api.admin

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class AdminUserController(
    @Lazy private val authenticationManager: AuthenticationManager,
    ) {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/login")
    fun getLogin(): String {
        return """
            <!DOCTYPE html>
            <html xmlns:th="http://www.thymeleaf.org">
            <head>
                <title>Login</title>
            </head>
            <body>
            <h1>Login</h1>
            <form th:action="/api/login" method="POST" enctype="application/x-www-form-urlencoded">
                <div>
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div>
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit">Login</button>
            </form>
            </body>
            </html>
        """
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/admin/users")
    fun getAllUsers(model: Model): String {
        val users = userService.getAllUsers()
        model.addAttribute("users", users)
        return """
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
    <title>Admin Users</title>
    <style>
        /* Basic table styling */
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>

<h1>Admin Users</h1>

<table>
    <thead>
        <tr>
            <th>Role</th>
            <th>Username</th>
            <th>Email</th> 
            <th>Created at</th>
            </tr>
    </thead>
    <tbody>
        <tr th:each="user : ${users}">
            <td th:text="${users[0].role}"></td>
            <td th:text="${users[0].username}"></td>
            <td th:text="${users[0].email}"></td> 
            <td th:text="${users[0].created_at}"></td> 
            </tr>
    </tbody>
</table>

</body>
</html>
        """
    }


}

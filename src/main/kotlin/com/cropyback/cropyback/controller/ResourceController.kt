package com.cropyback.cropyback.controller

import ResetPasswordRequest
import com.cropyback.cropyback.api.reset.PasswordResetService
import com.cropyback.cropyback.authentication.utils.CryptoUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse


@RestController
@RequestMapping("/api")
class ResourceController {
    @GetMapping("/home")
    fun homeEndpoint(): ResponseEntity<String> {
        return ResponseEntity.ok("Welcome to the home page")
    }
}

@RestController
@RequestMapping("/")
class MainController {

    @GetMapping("/")
    fun mainMapping(): String {
        ResponseEntity.ok("Welcome to Cropy API V1")
        val htmlForm = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Cropy API V1 Login</title>
            </head>
            <body>
                <h2>Welcome to Cropy API V1</h2>
                <form id="loginForm"> </form> 
                <div id="responseMessage"></div>
            
                <script>
                    // ... (rest of the JavaScript code from the previous response)
                </script>
            
                <script>
                    const loginForm = document.getElementById('loginForm');
            
                    // Create form elements dynamically
                    const usernameLabel = document.createElement('label');
                    usernameLabel.setAttribute('for', 'username');
                    usernameLabel.textContent = 'Username:';
                    loginForm.appendChild(usernameLabel);
            
                    const usernameInput = document.createElement('input');
                    usernameInput.setAttribute('type', 'text');
                    usernameInput.setAttribute('id', 'username');
                    usernameInput.setAttribute('name', 'username');
                    loginForm.appendChild(usernameInput);
            
                    loginForm.appendChild(document.createElement('br'));
                    loginForm.appendChild(document.createElement('br'));
            
                    const passwordLabel = document.createElement('label');
                    passwordLabel.setAttribute('for', 'password');
                    passwordLabel.textContent = 'Password:';
                    loginForm.appendChild(passwordLabel);
            
                    const passwordInput = document.createElement('input');
                    passwordInput.setAttribute('type', 'password');
                    passwordInput.setAttribute('id', 'password');
                    passwordInput.setAttribute('name', 'password');
                    loginForm.appendChild(passwordInput);
            
                    loginForm.appendChild(document.createElement('br'));
                    loginForm.appendChild(document.createElement('br'));
            
                    const submitButton = document.createElement('input');
                    submitButton.setAttribute('type', 'submit');
                    submitButton.setAttribute('value', 'Login');
                    loginForm.appendChild(submitButton);
            
                    // Event listener code (same as before)
                    loginForm.addEventListener('submit', async (event) => {
                        // ... (rest of the event listener code from the previous response)
                    });
                </script>
            </body>
            </html>
                     
    """

        return htmlForm
    }

}

@RestController
@RequestMapping("/")
class MainAPIController {

    @GetMapping("/api")
    fun mainMapping(): ResponseEntity<String> {
        return ResponseEntity.ok("API Verison : 1")
    }

}
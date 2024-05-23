package com.cropyback.cropyback.api.ui.controller

import com.cropyback.cropyback.api.ui.models.DefaultColors
import com.cropyback.cropyback.api.ui.models.LoginUI
import com.cropyback.cropyback.api.ui.models.SignUpUI
import com.cropyback.cropyback.api.ui.service.AppUIService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AppUIController {

    @Autowired
    private lateinit var uiService: AppUIService


    @PostMapping("/ui/colors")
    fun updateColors(@RequestBody colors: DefaultColors): MutableList<DefaultColors> {
        uiService.updateDefaultColors(colors)
        return uiService.getColorsUI()
    }

    @PostMapping("/ui/login")
    fun updateLogin(@RequestBody newUI: LoginUI): MutableList<LoginUI> {
        uiService.updateLoginUI(newUI)
        return uiService.getLoginUI()
    }

    @PostMapping("/ui/signup")
    fun updateSignUp(@RequestBody newUI: SignUpUI): MutableList<SignUpUI> {
        uiService.updateSignUpUI(newUI)
        return uiService.getSignUpUI()
    }

    @GetMapping("/ui/login")
    fun getLoginUI(): MutableList<LoginUI> {
        return uiService.getLoginUI()
    }


    @GetMapping("/ui/signup")
    fun getSignUpUI(): MutableList<SignUpUI> {
        return uiService.getSignUpUI()
    }

}
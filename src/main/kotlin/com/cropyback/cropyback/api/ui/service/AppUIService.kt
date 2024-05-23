package com.cropyback.cropyback.api.ui.service

import com.cropyback.cropyback.api.ui.models.DefaultColors
import com.cropyback.cropyback.api.ui.models.LoginUI
import com.cropyback.cropyback.api.ui.models.SignUpUI
import com.cropyback.cropyback.api.ui.repository.ColorsUIRepository
import com.cropyback.cropyback.api.ui.repository.LoginUIRepository
import com.cropyback.cropyback.api.ui.repository.SignUpUIRepository
import org.springframework.stereotype.Service


@Service
class AppUIService(
    private val loginUIRepository: LoginUIRepository,
    private val signUpUIRepository: SignUpUIRepository,
    private val colorsRepository: ColorsUIRepository
) {

    fun updateLoginUI(newUI: LoginUI) {
        loginUIRepository.save(newUI)
    }

    fun updateSignUpUI(newUI: SignUpUI) {
        signUpUIRepository.save(newUI)
    }

    fun updateDefaultColors(newColors: DefaultColors) {
        colorsRepository.save(newColors)
    }

    fun getLoginUI(): MutableList<LoginUI> {
        return loginUIRepository.findAll()
    }

    fun getSignUpUI(): MutableList<SignUpUI> {
        return signUpUIRepository.findAll()
    }

    fun getColorsUI(): MutableList<DefaultColors> {
        return colorsRepository.findAll()
    }

}
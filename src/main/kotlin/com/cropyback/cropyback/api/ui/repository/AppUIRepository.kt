package com.cropyback.cropyback.api.ui.repository

import com.cropyback.cropyback.api.ui.models.DefaultColors
import com.cropyback.cropyback.api.ui.models.LoginUI
import com.cropyback.cropyback.api.ui.models.SignUpUI
import org.springframework.data.mongodb.repository.MongoRepository

interface ColorsUIRepository: MongoRepository<DefaultColors, String> {}

interface LoginUIRepository: MongoRepository<LoginUI, String> {}

interface SignUpUIRepository: MongoRepository<SignUpUI, String> {}
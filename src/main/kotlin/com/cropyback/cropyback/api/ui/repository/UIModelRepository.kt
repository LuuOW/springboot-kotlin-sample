package com.cropyback.cropyback.api.ui.repository
import com.cropyback.cropyback.api.ui.models.UIModel
import org.springframework.data.jpa.repository.JpaRepository

interface UIModelRepository: JpaRepository<UIModel, Long> {}
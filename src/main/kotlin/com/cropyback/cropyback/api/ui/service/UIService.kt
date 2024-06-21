package com.cropyback.cropyback.api.ui.service
import com.cropyback.cropyback.api.ui.models.UIModel
import com.cropyback.cropyback.api.ui.repository.UIModelRepository
import org.springframework.stereotype.Service


@Service
class UIService(
    private val uiModelsRepository: UIModelRepository
) {

    fun updateLoginUI(newUI: UIModel) {
        uiModelsRepository.save(newUI)
    }

    fun getComponentById(id: Long): UIModel {
        return uiModelsRepository.findById(id).get()
    }

    fun getAllComponents(): MutableList<UIModel> {
        return uiModelsRepository.findAll()
    }

}
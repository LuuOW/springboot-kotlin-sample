package com.cropyback.cropyback.api.ui.service
import org.springframework.stereotype.Service
import org.bson.types.ObjectId
import com.cropyback.cropyback.api.ui.repository.UIModelRepository
import com.cropyback.cropyback.api.ui.models.UIModel


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
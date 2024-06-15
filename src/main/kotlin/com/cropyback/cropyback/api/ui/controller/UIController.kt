package com.cropyback.cropyback.api.ui.controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.http.ResponseEntity
import org.bson.types.ObjectId
import com.cropyback.cropyback.api.ui.models.UIModel
import com.cropyback.cropyback.api.ui.service.UIService


@RestController
@RequestMapping("/api")
class UICOntroller {

    @Autowired
    private lateinit var uiService: UIService

    @GetMapping("/ui")
    fun getAllUI(): MutableList<UIModel> {
        return uiService.getAllComponents()
    }

    @GetMapping("/ui/{id}")
    fun getComponentById(@RequestParam id: String): ResponseEntity<UIModel> {
        val model =  uiService.getComponentById(ObjectId(id))
        return ResponseEntity.ok(model)
    }

    @PostMapping("/ui")
    fun updateComponents(@RequestBody model: UIModel): MutableList<UIModel> {
        uiService.updateLoginUI(model)
        return getAllUI()
    }

}
package com.cropyback.cropyback.api.ui.controller
import com.cropyback.cropyback.api.ui.models.UIModel
import com.cropyback.cropyback.api.ui.service.UIService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Role
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class UIController {

    @Autowired
    private lateinit var uiService: UIService

    @GetMapping("/ui/all")
    fun getAllUI(): ResponseEntity<List<UIModel>> {
        val allUI = uiService.getAllComponents()
        return ResponseEntity.ok(allUI)
    }

    @GetMapping("/ui")
    fun getComponentById(@RequestParam id: String): ResponseEntity<UIModel> {
        val model =  uiService.getComponentById(id.toLong())
        return ResponseEntity.ok(model)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/ui")
    fun updateComponents(@RequestBody model: UIModel): ResponseEntity<List<UIModel>> {
        uiService.updateLoginUI(model)
        return getAllUI()
    }

}
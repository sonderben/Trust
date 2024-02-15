package com.sonderben.trust.controller.config

import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.db.dao.EnterpriseDao
import entity.EnterpriseEntity
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import java.net.URL
import java.time.LocalDate
import java.util.*

class BusinessInfoController : Initializable {
    private val enterprise: ObservableList<EnterpriseEntity> = EnterpriseDao.enterprises

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        setBusiness()
    }

    private fun setBusiness() {
        if (enterprise.size>0){
            val e = enterprise[0]
            directionTextField.text = e.direction
            val cal = e.foundation
            foundationDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

            val cat = e.category.name.toLowerCase().replaceFirstChar { it.toUpperCase() }
            categoryChoiceBox.selectionModel.select( categoryChoiceBox.items.indexOf( cat ) )
            nameTextField.text = e.name
            telephoneTextField.text = e.telephone
            websiteTextField.text = e.website
        }
    }

    @FXML
    private lateinit var categoryChoiceBox: ChoiceBox<String>

    @FXML
    private lateinit var directionTextField: TextField

    @FXML
    private lateinit var foundationDatePicker: DatePicker

    @FXML
    private lateinit var nameTextField: TextField

    @FXML
    private lateinit var telephoneTextField: TextField

    @FXML
    private lateinit var websiteTextField: TextField

}
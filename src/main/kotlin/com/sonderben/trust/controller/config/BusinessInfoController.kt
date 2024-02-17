package com.sonderben.trust.controller.config

import com.sonderben.trust.CategoryEnum
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
import java.time.ZoneId
import java.util.*

class BusinessInfoController : Initializable {
     var enterprise: EnterpriseEntity? = null
         set(value) {
             field=value
             setBusiness()
             println(" li kouri: "+value)
         }



    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        setBusiness()
    }



    private fun setBusiness() {

            if (enterprise!=null){
                directionTextField.text = enterprise!!.direction
                val cal = enterprise!!.foundation
                foundationDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

                if (enterprise!!.category!=null){
                    val cat = enterprise!!.category.name.toLowerCase().replaceFirstChar { it.toUpperCase() }
                    categoryChoiceBox.selectionModel.select( categoryChoiceBox.items.indexOf( cat ) )
                }

                nameTextField.text = enterprise!!.name
                telephoneTextField.text = enterprise!!.telephone
                websiteTextField.text = enterprise!!.website
            }

    }

    @FXML
     lateinit var categoryChoiceBox: ChoiceBox<String>

    @FXML
     lateinit var directionTextField: TextField

    @FXML
     lateinit var foundationDatePicker: DatePicker

    @FXML
     lateinit var nameTextField: TextField

    @FXML
     lateinit var telephoneTextField: TextField

    @FXML
     lateinit var websiteTextField: TextField

}
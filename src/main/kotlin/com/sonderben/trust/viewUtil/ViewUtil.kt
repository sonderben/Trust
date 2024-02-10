package com.sonderben.trust.viewUtil

import com.sonderben.trust.HelloApplication
import javafx.scene.control.Alert
import javafx.scene.layout.VBox
import javafx.stage.Modality

class ViewUtil {
    companion object{
        fun createAlert(alertType: Alert.AlertType, title:String, headerText:String):Alert{
            val alert = Alert(alertType/*Alert.AlertType.WARNING*/)
            alert.initModality(Modality.APPLICATION_MODAL)
            alert.initOwner(HelloApplication.primary)
            alert.title = title
            alert.headerText = headerText
            return alert
        }


    }
}
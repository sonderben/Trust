package com.sonderben.trust.controller

import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.Util
import com.sonderben.trust.constant.Constant
import com.sonderben.trust.db.service.CategoryService
import com.sonderben.trust.textTrim
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CategoryEntity
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.io.IOException
import java.net.URL
import java.util.*

class CategoryDialog : Dialog< List<CategoryEntity> >(), Initializable {
    private var categories:ObservableList<CategoryEntity> = CategoryService.getInstance().entities

    private var categoriesChanged:List<CategoryEntity> = mutableListOf()

    private var categorySelected:CategoryEntity?=null
    init {

        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/GoToCategoryDialog.fxml"),Constant.resource)
        fxmlLoader.setController(this)
        try {
            dialogPane = fxmlLoader.load()
             //ButtonType("Ok", ButtonBar.ButtonData.OK_DONE)
            dialogPane.buttonTypes.addAll( ButtonType("Ok", ButtonBar.ButtonData.OK_DONE) )


            codeCol.setCellValueFactory { d ->SimpleStringProperty(d.value.code) }
            descriptionCol.setCellValueFactory { d ->SimpleStringProperty(d.value.description) }
            discountCol.setCellValueFactory { d ->SimpleStringProperty(d.value.discount.toString()) }


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @FXML
    private lateinit var codeCol: TableColumn<CategoryEntity,String>

    @FXML
    private lateinit var codeTf: TextField

    @FXML
    private lateinit var descriptionCol: TableColumn<CategoryEntity, String>

    @FXML
    private lateinit var descriptionTf: TextField

    @FXML
    private lateinit var discountCol: TableColumn<CategoryEntity, String>

    @FXML
    private lateinit var discountTf: TextField

    @FXML
    private lateinit var tableView: TableView<CategoryEntity>

    @FXML
    fun deleteBtn() {
        if (categorySelected?.id  != null ){
            CategoryService.getInstance().delete( categorySelected!!.id )
                .subscribe({
                    clearBtn()
                },{th-> println(th.message) })

        }else{
            ViewUtil.customAlert(ViewUtil.WARNING, "please first select a category.").show()
        }
    }

    @FXML
    fun saveBtn() {

        if ( validateCategory() ){
            CategoryService.getInstance().save(
                CategoryEntity(codeTf.text,descriptionTf.text,discountTf.text.toDouble())
            ).subscribe({
                clearBtn()
            },{th-> println(th.message) })
        }


    }

    @FXML
    fun clearBtn(){
        deleteBtn.isDisable = true
        updateBtn.isDisable = true
        saveBtn.isDisable = false
        codeTf.text = ""
        descriptionTf.text = ""
        discountTf.text = ""
        tableView.selectionModel.clearSelection()
        categorySelected = null
    }

    @FXML
    fun updateBtn() {

        if ( validateCategory(true) ){

            categorySelected?.let {
                it.code = codeTf.textTrim()
                it.description = descriptionTf.textTrim()
                it.description = descriptionTf.textTrim()
                CategoryService.getInstance().update(it)
                    .subscribe({ clearBtn() },{th-> println( th.message ) })
            }
        }
    }
    private fun validateCategory(validateId:Boolean = false): Boolean {
        if (validateId)
            if ( categorySelected?.id == null){
                ViewUtil.customAlert(ViewUtil.WARNING, "please first select a category.").show()
            }

        //////////

        if ( Util.areBlank( codeTf, descriptionTf, discountTf ) ){
            ViewUtil.customAlert(
                ViewUtil.WARNING,
                "please make sure you fill out all the text fields."
            ).show()
            return false
        }
        return true
    }




    @FXML lateinit var saveBtn:Button
    @FXML lateinit var updateBtn:Button
    @FXML lateinit var deleteBtn:Button


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        tableView.items = categories

        tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {

                categorySelected = newValue
                codeTf.text = newValue.code
                descriptionTf.text = newValue.description
                discountTf.text = newValue.discount.toString()

                saveBtn.isDisable = true
                updateBtn.isDisable = false
                deleteBtn.isDisable = false

            }
        }
    }


}


























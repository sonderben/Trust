package com.sonderben.trust.controller

import com.sonderben.trust.HelloApplication
import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.dao.CategoryDao
import com.sonderben.trust.db.dao.RoleDao
import com.sonderben.trust.model.Role
import com.sonderben.trust.model.Screen
import dto.CategoryDto
import entity.CategoryEntity
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.io.IOException
import java.net.URL
import java.util.*

class CategoryDialog : Dialog<CategoryEntity>(), Initializable {
    private var categories:ObservableList<CategoryEntity> = CategoryDao.categories

    private var categorySelected:CategoryEntity?=null
    init {

        RoleDao.save(
            Role("Admin", mutableListOf(
                Screen(ScreenEnum.LOGIN, mutableListOf(Action.READ,Action.ADD)),
                Screen(ScreenEnum.SALE, mutableListOf(Action.READ,Action.ADD,Action.DELETE))
            )
            )
        )
        println(RoleDao.roles)
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/GoToCategoryDialog.fxml"))
        fxmlLoader.setController(this)
        try {
            dialogPane = fxmlLoader.load()
             ButtonType("Ok", ButtonBar.ButtonData.OK_DONE)
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
    fun deleteBtn(event: ActionEvent) {
        if (categorySelected != null && categorySelected!!.id != null){
           val isDelete = CategoryDao.delete( categorySelected!!.id )
            if (isDelete){
                //categories.remove( categorySelected )
                clearTextField()
            }
        }
    }

    @FXML
    fun saveBtn(event: ActionEvent) {

        CategoryDao.save(
            CategoryEntity(codeTf.text,descriptionTf.text,discountTf.text.toDouble())
        )

        clearTextField()
    }

    @FXML
    fun updateBtn(event: ActionEvent) {
        val index = categories.indexOf( categorySelected )
        if (index>=0){
            /*val cat = mCategoryDto.update( CategoryEntity(codeTf.text,descriptionTf.text,discountTf.text.toDouble()) )
            categories[index] = cat
            clearTextField()*/
        }
    }



    override fun initialize(location: URL?, resources: ResourceBundle?) {

        tableView.items = categories

        tableView.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (newValue != null) {
                categorySelected = newValue
                codeTf.text = newValue.code
                descriptionTf.text = newValue.description
                discountTf.text = newValue.discount.toString()

            }
        }
    }

    fun clearTextField(){
        codeTf.text = ""
        discountTf.text = ""
        descriptionTf.text = ""
    }
}
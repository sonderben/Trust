package com.sonderben.trust.controller

import com.sonderben.trust.HelloApplication
import com.sonderben.trust.db.dao.CategoryDao
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
    private var categories:ObservableList<CategoryEntity> = CategoryDao.categories

    private var categoriesChanged:List<CategoryEntity> = mutableListOf()

    private var categorySelected:CategoryEntity?=null
    init {




        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/GoToCategoryDialog.fxml"))
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
        if (categorySelected != null && categorySelected!!.id != null){
            CategoryDao.delete( categorySelected!!.id )
                .subscribe({
                    clearTextField()
                },{th-> println(th.message) })

        }
    }

    @FXML
    fun saveBtn() {

        CategoryDao.save(
            CategoryEntity(codeTf.text,descriptionTf.text,discountTf.text.toDouble())
        ).subscribe({
                    clearTextField()
        },{th-> println(th.message) })

        clearTextField()
    }

    @FXML
    fun updateBtn() {
        val index = categories.indexOf( categorySelected )
        if (index>=0){
            /*val cat = mCategoryDto.update( CategoryEntity(codeTf.text,descriptionTf.text,discountTf.text.toDouble()) )
            categories[index] = cat
            clearTextField()*/
        }
    }



    override fun initialize(location: URL?, resources: ResourceBundle?) {

        tableView.items = categories

        tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
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
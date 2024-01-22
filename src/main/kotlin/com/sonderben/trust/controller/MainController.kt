package com.sonderben.trust.controller

import SingletonView
import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.dao.CategoryDao
import com.sonderben.trust.model.Role
import com.sonderben.trust.model.Screen
import dto.EmployeeDto
import dto.ProductDto
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import entity.ScheduleEntity
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*


class MainController : Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        var da = Database.connect()
        //val cu = CategoryDao()
        //CategoryDao.save( CategoryEntity("5644","my description",1.1) )
        //prepopulateDb()
        /*stackPane.children.add( SingletonView.get("view/sale.fxml") )
        onClickLateralButton( vboxLateral.children[0] )*/
        onSelect()


    }
    @FXML
    private lateinit var vboxLateral: VBox

    @FXML
    private lateinit var stackPane: StackPane

    private fun onSelect() {
        vboxLateral.children.forEachIndexed { index, it ->
            it.setOnMouseClicked { event ->
                if (event != null) {
                    onClickLateralButton(event.source as Node)
                    println("num: $index")
                    when (index) {
                        0 -> changeView("view/sale.fxml")
                        1 -> print("1")
                        2 -> changeView("view/product.fxml")
                        3 -> changeView("view/employee.fxml")
                        4 -> print("4")
                    }
                }
            }
        }
    }

    private fun changeView(relativeUrl: String) {
        val a = SingletonView.get(relativeUrl)
        if (a != null && !stackPane.children.contains(a)) {
            stackPane.children.add(a)
        }
        a?.toFront()
    }


    private fun onClickLateralButton(node:Node ){

        vboxLateral.children.forEach {
            if (node === it){
                it.style = "-fx-background-color: #032d3b"
            }else{
                it.style = ""
            }
        }
    }

    private fun prepopulateDb(){
        val pdto = ProductDto()
        val edto = EmployeeDto()

        val rol = Role("Sale", mutableListOf(  Screen(
            ScreenEnum.SALE, mutableListOf( Action.ADD,
                Action.DELETE,
                Action.READ,
                Action.UPDATE ))  ) )
        val scheduleEntity = ScheduleEntity(null,1,1F,1.2F)



         val employee = EmployeeEntity("Ben", "jhjh", "Pha", "M",
             "Lascirie", "dd@gmail.com", "56546466",
             Calendar.getInstance(), "hghgffh", "benpha", "1234",
             listOf<Role>(rol), listOf<ScheduleEntity>(scheduleEntity)
        )


        pdto.save(
            ProductEntity("1111","corn flak",17.0,12.1,1.1,1.1,2,
                Calendar.getInstance(), Calendar.getInstance(),CategoryEntity("1111","description",0.0),
                employee
            )
        )

    }
}
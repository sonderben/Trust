package com.sonderben.trust.controller.queries

import com.sonderben.trust.db.dao.InvoiceDao
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.chart.BarChart
import javafx.scene.chart.PieChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.*

class ProductSoldController:Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println(InvoiceDao.productSealed())

        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code) }
        descriptionCol.setCellValueFactory { data -> SimpleStringProperty(data.value.description) }
        quantityCol.setCellValueFactory { data -> SimpleStringProperty(data.value.totalQuantity) }
        dateCol.setCellValueFactory { data -> SimpleStringProperty(data.value.dateCreated) }
        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code) }
        categoryCol.setCellValueFactory { data -> SimpleStringProperty( data.value.category) }
        totalPrice.setCellValueFactory { data -> SimpleStringProperty(data.value.totalPrice) }

        val pds = InvoiceDao.productSealed()

        tableView.items.addAll( pds )

        val pieChartDta = FXCollections.observableArrayList<PieChart.Data>()
        val productSealedByCat = FXCollections.observableArrayList<InvoiceDao.ProductSealed>()
        for (a in pds) {
            val series = XYChart.Series<String,Double>()

            series.name = a.description
            series.data.add(XYChart.Data(a.description, a.totalPrice.toDouble()))

            barChart.data.add( series)


            if (productSealedByCat.any { it.category.equals(a.category) }){
                val tempProductSealed =
                productSealedByCat.filter { it.category .equals( a.category) }[0]//.totalPrice+=a.totalPrice
                val sum = tempProductSealed.totalPrice.toDouble() + a.totalPrice.toDouble()
                tempProductSealed.totalPrice = sum.toString()
            }else{
                productSealedByCat.add(a.copy())
            }

        }
        barChart.data.forEach {

        }

        barChart.lookup("")



        for (i in productSealedByCat){
            pieChartDta.add(PieChart.Data(i.category,i.totalPrice.toDouble()))
        }


        pieChart.data = pieChartDta



    }

    @FXML
    private lateinit var barChart: BarChart<String, Double>

    @FXML
    private lateinit var categoryCol: TableColumn<InvoiceDao.ProductSealed, String>

    @FXML
    private lateinit var codeCol: TableColumn<InvoiceDao.ProductSealed, String>

    @FXML
    private lateinit var dateCol: TableColumn<InvoiceDao.ProductSealed, String>

    @FXML
    private lateinit var totalPrice:TableColumn<InvoiceDao.ProductSealed,String>

    @FXML
    private lateinit var descriptionCol: TableColumn<InvoiceDao.ProductSealed, String>

    @FXML
    private lateinit var productSold: AnchorPane

    @FXML private lateinit var pieChart:PieChart

    @FXML
    private lateinit var quantityCol: TableColumn<InvoiceDao.ProductSealed, String>

    @FXML
    private lateinit var tableView: TableView<InvoiceDao.ProductSealed>
}
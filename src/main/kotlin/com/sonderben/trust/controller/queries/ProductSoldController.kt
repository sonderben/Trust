package com.sonderben.trust.controller.queries

import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.db.service.InvoiceService
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.chart.BarChart
import javafx.scene.chart.PieChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.*

class ProductSoldController:Initializable, BaseController() {
    override fun initialize(location: URL?, resources: ResourceBundle?) {


        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code) }
        descriptionCol.setCellValueFactory { data -> SimpleStringProperty(data.value.description) }
        quantityCol.setCellValueFactory { data -> SimpleStringProperty(data.value.totalQuantity) }
        dateCol.setCellValueFactory { data -> SimpleStringProperty(data.value.dateCreated) }
        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code) }
        categoryCol.setCellValueFactory { data -> SimpleStringProperty( data.value.category) }
        totalPrice.setCellValueFactory { data -> SimpleStringProperty(data.value.totalPrice) }

        val pds = InvoiceService.getInstance().productSealed()
            .subscribe({
                tableView.items.addAll( it )

                val pieChartDta = FXCollections.observableArrayList<PieChart.Data>()
                val productSealedByCat = FXCollections.observableArrayList<InvoiceDao.ProductSealed>()
                for (a in it) {
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
                for (i in productSealedByCat){
                    pieChartDta.add(PieChart.Data(i.category,i.totalPrice.toDouble()))
                }


                pieChart.data = pieChartDta
            },{})



        barChart.lookup("")







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
    override fun onDestroy() {

    }
}
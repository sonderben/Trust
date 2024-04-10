package com.sonderben.trust.controller.queries

import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.controller.ProductDetails
import com.sonderben.trust.db.service.ProductDetailService
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.chart.BarChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import java.net.URL
import java.util.*

class BestSellingProduct:Initializable, BaseController() {
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        codeCol.setCellValueFactory { d->SimpleStringProperty(d.value.code) }
        descriptionCol.setCellValueFactory { d->SimpleStringProperty(d.value.description.toString()) }
        quantityCol.setCellValueFactory { d->SimpleStringProperty(d.value.quantity.toString()) }
        frequencyCol.setCellValueFactory { d->SimpleStringProperty(d.value.frequency.toString()) }
        pointCol.setCellValueFactory { d->SimpleStringProperty(d.value.points.toString()) }
        benefitCol.setCellValueFactory { d->SimpleStringProperty(d.value.benefit.toString()) }




        //val pds =*/ ProductDetailService..bestSellers()
        ProductDetailService.getInstance().bestSellers( qty =  2f, frequency = 3, benefit = 1f)
            .subscribe({pds->
                tableView.items = FXCollections.observableArrayList( pds )

                for (a in pds) {
                    val series = XYChart.Series<String,Double>()

                    series.name = a.description
                    series.data.add(XYChart.Data(a.description, a.points ))

                    barchart.data.add( series)


                }
            },{})




    }

    override fun onDestroy() {

    }

    @FXML
    private lateinit var barchart: BarChart<String, Double>

    @FXML
    private lateinit var benefitCol: TableColumn<ProductDetails.BestSeller, String>

    @FXML
    private lateinit var codeCol: TableColumn<ProductDetails.BestSeller, String>

    @FXML
    private lateinit var descriptionCol: TableColumn<ProductDetails.BestSeller, String>

    @FXML
    private lateinit var frequencyCol: TableColumn<ProductDetails.BestSeller, String>

    @FXML
    private lateinit var pointCol: TableColumn<ProductDetails.BestSeller, String>

    @FXML
    private lateinit var quantityCol: TableColumn<ProductDetails.BestSeller, String>

    @FXML
    private lateinit var tableView: TableView<ProductDetails.BestSeller>

}
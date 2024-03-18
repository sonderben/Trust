//package com.sonderben.trust.customView
//
//import com.sonderben.trust.constant.ScreenEnum
//import com.sonderben.trust.model.Role
//import javafx.beans.property.SimpleObjectProperty
//import javafx.collections.FXCollections
//import javafx.collections.ListChangeListener
//import javafx.collections.ObservableList
//import javafx.geometry.Pos
//import javafx.scene.control.Label
//import javafx.scene.layout.HBox
//import javafx.scene.layout.Pane
//import javafx.scene.layout.Region
//import javafx.scene.layout.VBox
//
//
//class RoleTableView:Pane() {
//    private val padding_ = 10.0
//    private var nameColumn = VBox()
//    private var screenColumn = VBox()
//    private var actionColumn = VBox()
//    private var headerNameCol = Label("Name")
//    private var headerScreenCol = Label("Screens")
//    private var headerActionCol = Label("Actions")
//    private var labelColor = "-fx-text-fill: black;"
//    public var roles: ObservableList< SimpleObjectProperty<Role> > = FXCollections.observableArrayList()
//    init {
//
//        prefWidth = Double.MAX_VALUE
//        prefHeight =200.0
//        maxWidth = Region.USE_COMPUTED_SIZE
//
//
//        val style1 = """
//            -fx-border-bottom-color:#ffffff;
//            -fx-background-color:#d3d3d3;
//            -fx-border-width:1;
//        """.trimIndent()
//
//        val styleLabel = """
//            -fx-text-fill: black;
//            -fx-font-weight: bold;
//            -fx-font-size: 22;
//        """.trimIndent()
//
//        nameColumn.style = style1
//        screenColumn.style = style1
//        actionColumn.style = "-fx-background-color:#d3d3d3;-fx-border-bottom-color:white;"
//
//        headerNameCol.style = styleLabel
//        headerScreenCol.style = styleLabel
//        headerActionCol.style = styleLabel
//
//        nameColumn.alignment = Pos.CENTER
//        screenColumn.alignment = Pos.CENTER
//        actionColumn.alignment = Pos.CENTER
//
//
//
//        nameColumn.children.add(headerNameCol)
//        screenColumn.children.add(headerScreenCol)
//        actionColumn.children.add(headerActionCol)
//
//        nameColumn.spacing = 5.0
//        screenColumn.spacing = 5.0
//        actionColumn.spacing = 5.0
//
//
//        children.add( nameColumn )
//        children.add( screenColumn )
//        children.add( actionColumn )
//
//
//
//
//        roles.addListener( object :ListChangeListener<SimpleObjectProperty<Role>>{
//            override fun onChanged(c: ListChangeListener.Change<out SimpleObjectProperty<Role>>?) {
//                if (c?.next() == true){
//                    if ( c.wasAdded() ){
//
//                        for ( role in c.addedSubList ){
//
//                            val nameVBox = VBox()
//                            val screenVBox = VBox()
//                            val actionVBox = VBox()
//                            nameVBox.apply {
//                                alignment = Pos.CENTER
//                            }
//                            if ( roles.indexOf(role) %2 ==0 ){
//
//                                nameVBox.style = "-fx-background-color:white;"
//                                screenVBox.style = "-fx-background-color:white;"
//                                actionVBox.style = "-fx-background-color:white;"
//                            }
//                            else{
//
//                                nameVBox.style = "-fx-background-color:#CCCCCC;"
//                                screenVBox.style = "-fx-background-color:#CCCCCC;"
//                                actionVBox.style = "-fx-background-color:#CCCCCC;"
//                            }
//
//                            val roleNameLabel = Label( role.value.name )
//                            roleNameLabel.style = labelColor
//                            nameVBox.children.add( roleNameLabel )
//                            nameColumn.children.add( nameVBox )
//
//                            for ( screen in role.value.screens ){
//                                val roleScreenLabel = Label( screen.screen.name )
//                                roleScreenLabel.style = labelColor
//                                screenVBox.children.add( roleScreenLabel )
//
//                                val lisActionByScreenHBox = HBox()
//                                lisActionByScreenHBox.spacing = 5.0
//                                for ( action in screen.actions ){
//                                    val actionLabel = Label(action.name)
//                                    actionLabel.style = labelColor
//                                    lisActionByScreenHBox.children.add( actionLabel )
//                                }
//                                actionVBox.children.add( lisActionByScreenHBox )
//
//                            }
//                            screenColumn.children.add( screenVBox )
//                            actionColumn.children.add( actionVBox )
//                            nameVBox.prefHeightProperty().bind( screenVBox.heightProperty() )
//                            actionVBox.prefHeightProperty().bind( screenVBox.heightProperty() )
//
//                        }
//                    }
//                }
//            }
//        })
//
//        roles.add(
//            SimpleObjectProperty(
//                Role(
//                    "Admin", mutableListOf(
//                        Screen( ScreenEnum.ROLE, mutableListOf( Action.ADD,Action.DELETE ) ),
//                        Screen( ScreenEnum.SALE, mutableListOf( Action.ADD,Action.DELETE,Action.READ ) )
//                    )
//                )
//            )
//        )
//        roles.add(
//            SimpleObjectProperty(
//                Role(
//                    "Employee", mutableListOf(
//                        Screen( ScreenEnum.LOGIN, mutableListOf( Action.UPDATE,Action.READ ) ),
//                        Screen( ScreenEnum.ROLE, mutableListOf( Action.ADD,Action.DELETE ) ),
//                        Screen( ScreenEnum.SALE, mutableListOf( Action.ADD,Action.DELETE,Action.READ,Action.UPDATE ) )
//                    )
//                )
//            )
//        )
//
//        roles.add(
//            SimpleObjectProperty(
//                Role(
//                    "Employee", mutableListOf(
//                        Screen( ScreenEnum.LOGIN, mutableListOf( Action.UPDATE,Action.READ ) ),
//                        Screen( ScreenEnum.ROLE, mutableListOf( Action.ADD,Action.DELETE ) ),
//                        Screen( ScreenEnum.SALE, mutableListOf( Action.ADD,Action.DELETE,Action.READ,Action.UPDATE ) )
//                    )
//                )
//            )
//        )
//
//    }
//
//    override fun resize(width: Double, height: Double) {
//        super.resize(width, height)
//        resize()
//
//    }
//
//    private fun resize(  ){
//
//
//        actionColumn.apply {
//            prefWidth = this@RoleTableView.width*4.99/10
//            layoutX = this@RoleTableView.width - actionColumn.width
//            layoutY = 0.0
//        }
//
//        screenColumn.apply {
//            prefWidth = this@RoleTableView.width*2.99/10
//            layoutX = actionColumn.layoutX - this.width
//            layoutY = 0.0
//        }
//
//        nameColumn.apply {
//            prefWidth = this@RoleTableView.width*1.99/10
//            layoutX = 0.0
//            layoutY = 0.0
//        }
//
//    }
//}
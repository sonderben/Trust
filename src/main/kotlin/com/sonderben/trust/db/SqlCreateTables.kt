package com.sonderben.trust.db



object SqlCreateTables {
    const val categories = "Categories"
    const val screen = "Screens"
    const val roles = "Roles"
    const val schedules = "Schedules"
    const val employees = "Employee"
    const val products = "products"
    const val customers = "customers"
    const val invoices = "invoices"
    const val invoiceProduct = "invoiceProduct"
    const val productSealed = "productSealed"




    private const val createCategoryTable = """
        create table if not exists ${categories} (
        discount float default 0 check(discount >=0 and discount<100),
        id integer primary key autoincrement,
        code nvarchar(255),
        description nvarchar(255) unique
    );
    """
    private const val createScreenTable = """
        CREATE TABLE IF NOT EXISTS $screen(
        id integer primary key autoincrement,
        screenEnum varchar(30),
        actions varchar(255),
        id_role integer,
        FOREIGN KEY (id_role)
            REFERENCES $roles (id) on delete cascade
    );
    """
    private const val createRolesTable = """
        CREATE TABLE IF NOT EXISTS $roles(
        id integer primary key autoincrement,
        name varchar(30) unique
    );
    """
    private var createCustomerTable =  """
    create table if not exists $customers (
        birthDay timestamp,
        code varchar unique,
        id integer primary key autoincrement,
        direction varchar(255),
        point integer default 0,
        email varchar(255) unique,
        firstName varchar(255),
        genre varchar(30),
        lastName varchar(255),
        passport varchar(255),
        telephone varchar(255)
    );
    
    """

    private var createInvoiceTable =  """
    create table if not exists $invoices (
        dateCreated timestamp,
        id integer primary key autoincrement,
        codeBar varchar,
        id_employee integer not null,
        id_customer integer,
        
        foreign key(id_employee)
            references $employees(id),
        foreign key(id_customer)
            references $customers(id)
    );
    
    """
    public var createInvoiceProductTable = """
        create table if not exists $invoiceProduct(
        id integer primary key autoincrement,
                id_invoice integer not null,
                id_product_sealed integer not null,
                foreign key(id_invoice) 
                    references $invoices(id),
                foreign key(id_product_sealed) 
                    references $productSealed(id)
        );
        
    """.trimIndent()

    private var createEmployeeTable =  """
    create table if not exists $employees (
        birthDay timestamp,
        code varchar,
        id integer primary key autoincrement,
        bankAccount varchar(255) unique,
        direction varchar(255),
        email varchar(255) unique,
        firstName varchar(255),
        genre varchar(30),
        lastName varchar(255),
        passport varchar(255),
        password varchar(255),
        telephone varchar(255),
        userName varchar(255) unique not null,
        id_role integer,
        FOREIGN KEY (id_role) 
        REFERENCES $roles (id)
    );
    
    """

    private val createProductsTable = """
        CREATE TABLE IF NOT EXISTS $products( 
        id integer primary key autoincrement,  
        discount float default 0 check( discount >= 0 and discount < 100 ),
        itbis float not null,
        purchasePrice float not null,
        quantity integer not null default 0 check( quantity>-1 ),
        quantityRemaining integer not null default 0 check( quantityRemaining >-1 and quantityRemaining <= quantity ),
        sellingPrice float not null,
        id_category bigint ,
        dateAdded timestamp,
        id_employee bigint ,
        expirationDate timestamp,

        code varchar(10) not null unique,
        description varchar(255),

        FOREIGN KEY(id_category) 
            REFERENCES $categories (id) 
        FOREIGN KEY (id_employee) 
            REFERENCES $employees (id) 
        )
    """.trimIndent()
    //Integer workDay; private Float startHour, endHour;
    private val createScheduleTable = """
        CREATE TABLE IF NOT EXISTS $schedules (
        id integer primary key autoincrement,
        workDay integer,
        start_hour float(4),
        end_hour float(4),
        id_employee integer,
        FOREIGN key (id_employee)
            REFERENCES $employees (id)
        );
    """.trimIndent()

    private val createProductSealed = """
        CREATE TABLE IF NOT EXISTS $productSealed(
        id integer primary key autoincrement,
        code varchar,
        description nvarchar,
        category varchar,
        price float,
        quantity float,
        discount float,
        itbis float,
        total float,
        wasDiscountCategory boolean
        )
    """.trimIndent()

    val tables = listOf(createCategoryTable,
        createCustomerTable, createScreenTable, createRolesTable, createEmployeeTable, /*createRoleEmployee,*/
        createScheduleTable, createProductsTable, createInvoiceTable, createInvoiceProductTable,createProductSealed/*,
        getFullName*/)
    var deleteCategoryTable = """
        drop table if exists ${categories};
    """.trimIndent()
}

package com.sonderben.trust.db



object SqlCreateTables {
    const val categories = "Categories"
    const val screen = "Screens"
    const val roles = "Roles"
    const val schedules = "Schedules"
    const val employees = "Employee"
    const val employeesRoles = "employees_roles"

    private const val createCategoryTable = """
        create table if not exists ${categories} (
        discount float(53) not null,
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
            REFERENCES $roles (id)
    );
    """
    private const val createRolesTable = """
        CREATE TABLE IF NOT EXISTS $roles(
        id integer primary key autoincrement,
        name varchar(30) unique
    );
    """

    private var createEmployeeTable =  """
    create table if not exists $employees (
        birthDay timestamp,
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
        userName varchar(255) unique not null
    );
    
    """

    private val createRoleEmployee = """
        CREATE TABLE IF NOT EXISTS $employeesRoles( 
        id integer primary key autoincrement,  
        id_role integer not null, 
        id_employee integer not null, 
        FOREIGN KEY(id_role) 
            REFERENCES $roles (id) 
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

    val tables = listOf(createCategoryTable, createScreenTable, createRolesTable, createEmployeeTable, createRoleEmployee,
        createScheduleTable)
    var deleteCategoryTable = """
        drop table if exists ${categories};
    """.trimIndent()
}

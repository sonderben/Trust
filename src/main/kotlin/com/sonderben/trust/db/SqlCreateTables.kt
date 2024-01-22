package com.sonderben.trust.db



object SqlCreateTables {
    const val categories = "Categories"
    const val screen = "Screens"
    const val roles = "Roles"
    const val screen_roles = "ScreenRoles"

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

    private var createScreeRolesTable1 =  """
    CREATE TABLE IF NOT EXISTS $screen_roles (
        id integer primary key autoincrement,
        id_screen INTEGER,
        id_role INTEGER,
        FOREIGN KEY (id_screen)
            REFERENCES $screen (id),
        FOREIGN KEY (id_role)
            REFERENCES $roles (id)
    );
    
    """

    val tables = listOf(createCategoryTable, createScreenTable, createRolesTable/*, createScreeRolesTable*/)
    var deleteCategoryTable = """
        drop table if exists ${categories};
    """.trimIndent()
}

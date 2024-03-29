package com.sonderben.trust.db

object SqlDml {
    val insertCategory = """
        insert into
             ${SqlDdl.categories}(code, description, discount) 
            values (?, ?, ?)
    """.trimIndent()
    val selectAllCategory = "SELECT * FROM ${SqlDdl.categories}"
    val deleteCategory = "DELETE  FROM ${SqlDdl.categories} WHERE id = ?"
    val updateCategory = " UPDATE ${SqlDdl.categories} set code = ?, description = ?,discount = ? WHERE id = ?"


    val INSERT_CUSTOMER = buildString {
        append("Insert into ${SqlDdl.customers} ")
        append("( birthDay,code,direction,point,email,firstName,genre,lastName,passport,telephone ) ")
        append("values (?,?,?,?,?,?,?,?,?,?) ")
    }
    val DELETE_CUSTOMER = "DELETE FROM ${SqlDdl.customers} WHERE id = ?"
    val FIND_CUSTOMER_BY_ID = "SELECT * FROM ${SqlDdl.customers} WHERE id = ?"
    val SELECT_ALL_CUSTOMER = "SELECT * FROM ${SqlDdl.customers}"
    val UPDATE_CUSTOMER = """
            update  ${SqlDdl.customers}
             set birthDay =   ?,direction = ? ,email = ? ,firstName = ? ,genre = ? ,lastName = ? ,passport = ? ,
             telephone = ? where id = ?
        """.trimIndent()
    val FIND_CUSTOMER_BY_CODE = "select * from ${SqlDdl.customers} where code = ?"
    val SPENDING_OR_FREQUENT_CUSTOMER = """
        SELECT  c.code as customerCode,c.point,sum(ps.quantity) as totalProductBought,sum(ps.total) as totalSpend ,count(iv.id) as frequency
            from customers as c
            INNER JOIN invoices as iv on c.id = iv.id_customer
            INNER join invoiceProductSealed as ips on ips.id_invoice = iv.id
            INNER join productSealed as ps on ps.id = ips.id_product_sealed
            GROUP BY customerCode
            ORDER by ? DESC limit 100
    """.trimIndent()


    val INSERT_EMPLOYEE = buildString {
        append("Insert into ${SqlDdl.employees} ")//passport
        append("(bankAccount,direction,email,firstName,genre,lastName,passport,password,telephone,userName,birthDay,id_role) ")
        append("values (?,?,?,?,?,?,?,?,?,?,?,?) ")
    }


    val INSERT_SCHEDULE = buildString {
        append("INSERT INTO ${SqlDdl.schedules }")
        append(" (workDay,start_hour,end_hour,id_employee) ")
        append(" values( ?,?,?,?);")
    }
    val DELETE_EMPLOYEE = "delete from ${SqlDdl.employees} where id = ?"
    val FIND_EMPLOYEE_BY_ID = "SELECT * FROM ${SqlDdl.employees} where id = ?"
    //val SELECT_ALL_EMPLOYEE = "SELECT * FROM ${SqlDdl.employees}"
    val SELECT_ALL_EMPLOYEE = """
        SELECT Employee.id, birthDay,bankAccount,direction,email,firstName,genre,lastName,passport,password,telephone,userName,
        Roles.id as roleId,name,screens
        from ${SqlDdl.employees} INNER JOIN ${SqlDdl.roles}
        on Employee.id_role = Roles.id
    """.trimIndent()
    val EMPLOYEE_LOGIN = "select * from ${SqlDdl.employees} where userName= ? and password =? ;"

    val UPDATE_EMPLOYEE = """
        update ${SqlDdl.employees} set
        bankAccount = ?,direction = ?,email = ?,firstName = ?,
        genre = ?,lastName = ?,passport = ? ,password = ?,
        telephone = ?,userName = ?,birthDay = ?,id_role = ? where id = ? """

    val INSERT_ENTERPRISE = buildString {
        append("Insert into ${SqlDdl.enterprise} ")
        append("(name,direction,telephone,foundation,website,category,invoiceTemplate,invoiceTemplateHtml,id_employee) ")
        append("values (?,?,?,?,?,?,?,?,?) ")
    }
    val  SELECT_ALL_ENTERPRISE = """
            SELECT enterprise.id as enid , enterprise.name as ne,enterprise.direction as ed,enterprise.telephone as et,enterprise.foundation as ef ,enterprise.website as ew,
            enterprise.category as ec ,enterprise.invoiceTemplate as ei,enterprise.invoiceTemplateHtml as ein,
            Employee.birthDay as empb,Employee.id as empid,Employee.bankAccount as empbank,Employee.direction as empd,Employee.email as empe,Employee.firstName as empf,Employee.lastName as empl,
            Employee.genre as empg,Employee.passport as empp,Employee.password as emppwd,Employee.telephone as emptel,Employee.userName as empu,
            Roles.name as rm,Roles.id as ri
            from enterprise
            INNER JOIN Employee on enterprise.id_employee == Employee.id
            INNER join Roles on Roles.id = Employee.id_role """
    val UPDATE_ENTERPRISE = """
        UPDATE ${SqlDdl.enterprise} 
        set name = ?, direction = ?,telephone = ?,foundation = ?, 
        website = ?,category = ?,invoiceTemplate = ?, invoiceTemplateHtml = ?,
         id_employee = ? WHERE id = ?
    """.trimIndent()

    val PRODUCT_SOLD_BY_CODE = """
        SELECT ps.code,
            ps.description,
            SUM(ps.price * ps.quantity) as total_price,
            SUM(ps.quantity) as total_quantity,
            ps.category,
            ip.id_invoice,
            iv.dateCreated
            FROM ${SqlDdl.productSealed} as ps
            INNER JOIN ${SqlDdl.invoiceProductSealed}  as ip ON ip.id_product_sealed = ps.id
            INNER JOIN ${SqlDdl.invoices} as iv ON iv.id = ip.id_invoice
            INNER JOIN ${SqlDdl.employees} as emp ON emp.id = iv.id_employee
            /*WHERE Date(iv.dateCreated/1000,'unixepoch') = date('now')*/
            GROUP BY ps.code;
    """.trimIndent()

    val FIND_BY_INVOICE_CODE = """
            SELECT ps.code,
            ps.description,
			ps.price,ps.quantity,
			(ps.price * ps.quantity) as total_price,
            ps.category,
            iv.dateCreated,
			ps.id as productSoldId,
			iv.id as invoicesId,
            ps.isReturned
			
            FROM productSealed as ps
            INNER JOIN invoiceProductSealed as ip ON ip.id_product_sealed = ps.id
            INNER JOIN invoices as iv ON iv.id = ip.id_invoice
			
			WHERE  iv.codeBar = ? """

    val INSERT_PRODUCT_RETURN = "insert into ${SqlDdl.productReturned} (id_invoice,id_employee,reason,action) values(?,?,?,?)"
    val SELECT_PRODUCT_RETURNED = """
        SELECT iv.codeBar as invoice_codebar, iv.dateCreated as dateBought,pr.dateCreate as dateReturned,pr.reason,pr.action,emp.firstName || ' ' || emp.lastName as employee,
            customers.code as customerCode,ps.code as productCode,ps.description,ps.quantity,ps.total/*,ps.expirationDate*/
            from productReturned as pr
            INNER JOIN Employee as emp on pr.id_employee = emp.id
            INNER join invoices as iv on pr.id_invoice
            INNER join invoiceProductSealed as ips on ips.id_invoice = iv.id
            inner JOIN  productSealed as ps on ps.id = ips.id_product_sealed
            INNER join  customers on iv.id_customer = customers.id
            where ps.isReturned = true
    """.trimIndent()

    val UPDATE_STATUS_PRODUCT_IS_RETURNED = "update ${SqlDdl.productSealed} set isReturned = 1 where id = ?"

    val SELECT_ALL_PRODUCT = """
            SELECT products.quantityRemaining,products.sellby,products.id,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category,userName
             from ${SqlDdl.products} INNER JOIN Employee  on 
            products.id_employee = Employee.id
            INNER JOIN Categories on 
            products.id_category = Categories.id
        """.trimIndent()

    val UPDATE_PRODUCT = """update  ${SqlDdl.products} 
            set discount = ?, 
            itbis = ?, 
            purchasePrice = ?, 
            quantity = ?, 
            sellingPrice = ?,
            id_category = ?,  
            id_employee = ?, 
            expirationDate = ?, 
            code = ?, 
            description = ?,
            quantityRemaining = ? ,
            sellby = ?
            where id = ?;""".trimMargin()

    val SELECT_PRODUCT_BY_CODE = """
            SELECT  products.id,products.sellby,products.quantityRemaining,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category
             from products 
            INNER JOIN Categories on 
            products.id_category = Categories.id
            where code_product = ?
        """.trimIndent()

    val FIND_PRODUCT_EXPIRED = """
            SELECT products.quantityRemaining,products.id,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category,userName
             from ${SqlDdl.products} INNER JOIN Employee  on 
            products.id_employee = Employee.id
            INNER JOIN Categories on 
            products.id_category = Categories.id
            WHERE products.quantityRemaining > 0 and Date(expirationDate/1000,'unixepoch') <= date('now')
        """.trimIndent()

    val UPDATE_QUANTITY_PRODUCT_REMAININHG =
        "update  ${SqlDdl.products} set quantityRemaining = quantityRemaining - ? where id = ?"

    val SELECT_BESTSELLER_GROUP_BY_CODE = """
            SELECT code,description,quantity,frequency  ,benefit,( (frequency * ?) + (benefit * ?) + (quantity * ?) ) as points  from
	            (SELECT ps.code,ps.description,ps.quantity,count(ps.code) as frequency,( (sellingPrice-purchasePrice)*ps.quantity ) as benefit from 
                    ${SqlDdl.productSealed} as ps 
	                INNER JOIN ${SqlDdl.products} as p on ps.code = p.code
	            GROUP by ps.code) ORDER by points DESC LIMIT 100
        """.trimIndent()

      val INSERT_ROLE = buildString {
        append("INSERT INTO ")
        append(SqlDdl.roles)
        append(" (name,screens) values (?,?)")
    }
    val UPDATE_ROLE = "UPDATE ${SqlDdl.roles} SET name = ?,screens = ? WHERE id = ?;"


    const val SELECT_ALL_ROLE = "SELECT * FROM ${SqlDdl.roles}"

    val DELETE_ROLE = "delete from ${SqlDdl.roles} where id = ?"

    val UPDATE_SCHEDULER =  """update ${SqlDdl.schedules} 
                set workDay = ?,
                start_hour = ?,
                end_hour = ?,
                id_employee = ?
                where id = ?
            """.trimMargin()




}
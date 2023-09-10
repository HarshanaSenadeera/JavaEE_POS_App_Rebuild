getAll();
function getAll() {

    $("#tblItem").empty();

    $.ajax({

       url:"http://localhost:8080/POS/pages/item",

        success:function (item) {
            for (let i in item) {

                let it=item[i];
                let code=it.code;
                let item_name=it.item_name;
                let item_contity=it.item_contity;
                let item_price=it.unit_price;

                let row=`<tr><td>${code}</td><td>${item_name}</td><td>${item_contity}</td><td>${item_price}</td></tr>`;

                $("#tblItem").append(row);
            }

            bindRowClickOnAction();
            clearRow("","","","");
        }

    });
}

$("#btnItem").click(function () {

    let form = $("#itemForm").serialize();

    $.ajax({

        url: "http://localhost:8080/POS/pages/item",
        method:"post",
        data:form,

        success:function (res) {

            alert(
                res.message,
            )

            getAll();
            clearRow("","","","");
        },

        error:function (error) {

            alert(JSON.parse(error.responseText).message);
        }

    });

});

$("#btnItemUpdate").click(function () {

    let code = $("#txtItemCode").val();
    let name = $("#txtItemName").val();
    let qty = $("#txtItemQty").val();
    let price = $("#txtItemPrice").val();

    let itemDB={
        code:code,
        item_name:name,
        item_contity:qty,
        unit_price:price
    }

    $.ajax({

        url:"http://localhost:8080/POS/pages/item",
        method: "put",
        data: JSON.stringify(itemDB),
        contentType:"application/json",

        success:function (res) {

            alert(
                res.message
            );
            getAll();
            clearRow("","","","");
        },

        error:function (error) {

            alert(JSON.parse(error.responseText).message)
        }

    });
});


$("#btnItemDelete").click(function () {

    let code = $("#txtItemCode").val();

    $.ajax({

       url:"http://localhost:8080/POS/pages/item?code="+code,
       method:"delete",

        success:function (res) {

           alert(
               res.message
           );
           getAll();
            clearRow("","","","");
        },

        error:function (error) {

           alert(JSON.parse(error.responseText).message);
        }
    });
});

function bindRowClickOnAction() {
    $("#tblItem>tr").click(function () {

        let code = $(this).children(":eq(0)").text();
        let name = $(this).children(":eq(1)").text();
        let qty = $(this).children(":eq(2)").text();
        let price = $(this).children(":eq(3)").text();

        $("#txtItemCode").val(code);
        $("#txtItemName").val(name);
        $("#txtItemQty").val(qty);
        $("#txtItemPrice").val(price);

    });

}

/*<!--    clear row function  -->*/

function clearRow(code,name,qty,price) {
    $("#txtItemCode").val(code);
    $("#txtItemName").val(name);
    $("#txtItemQty").val(qty);
    $("#txtItemPrice").val(price);
}
getAll();

//-------------getAll--------------------------
function getAll() {

    $("#tblCustomer").empty();

    $.ajax({
       url:"http://localhost:8080/POS/pages/customer",

        success:function (response) {
           let customer=response.data;
            for (let i in customer) {

                let cus=customer[i];
                let id=cus.id;
                let name=cus.name;
                let address=cus.address;

                let row=`<tr><td>${id}</td><td>${name}</td><td>${address}</td></tr>`

                $("#tblCustomer").append(row);
            }
            bindRowClickOnAction();
            clearRow("","","");
        }
    });

}

//-------------Save--------------------------
$("#btnCustomer").click(function () {

    let data = $("#customerForm").serialize();

    $.ajax({
        url: "http://localhost:8080/POS/pages/customer",
        method:"post",
        data:data,

        success:function (res) {

            alert(
                res.message
            );

            getAll();
            clearRow("","","");
        },

        error:function (error) {
            alert(JSON.parse(error.responseText).message);
        }
    });
});

//-------------Update--------------------------

$("#btnUpdate").click(function () {

    let cusId = $("#txtCustomerID").val();
    let cusName = $("#txtCustomerName").val();
    let cusAddress = $("#txtCustomerAddress").val();

    let cusDB={
        id:cusId,
        name:cusName,
        address:cusAddress
    }

    $.ajax({

       url:"http://localhost:8080/POS/pages/customer",
       method: "put",
       data:JSON.stringify(cusDB),
       contentType:"application/json",

       success:function (res) {

           alert(
               res.message
           );
           getAll();
           clearRow("","","");
       },

        error:function (error) {
           alert(JSON.parse(error.responseText).message)
        }
    });
})

$("#btnCusDelete").click(function () {

    let id = $("#txtCustomerID").val();

    $.ajax({

        url:"http://localhost:8080/POS/pages/customer?id=" +id,

        method:"delete",

        success:function (res) {


            alert(
                res.message
            );

            getAll();
            clearRow("","","");
        },

        error:function (error) {
            alert(
                JSON.parse(error.responseText).message
            )
        }
    });
});


function bindRowClickOnAction() {
    $("#tblCustomer>tr").click(function () {

        let id = $(this).children(":eq(0)").text();
        let name = $(this).children(":eq(1)").text();
        let address = $(this).children(":eq(2)").text();

        $("#txtCustomerID").val(id);
        $("#txtCustomerName").val(name);
        $("#txtCustomerAddress").val(address);

    });

}

/*<!--    clear row function  -->*/

function clearRow(id,name,address) {
    $("#txtCustomerID").val(id);
    $("#txtCustomerName").val(name);
    $("#txtCustomerAddress").val(address);
}
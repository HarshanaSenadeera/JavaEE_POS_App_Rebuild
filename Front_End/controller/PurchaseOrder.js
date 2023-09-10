getItemCodes();
getCusIds();

function getItemCodes() {
    $("#selectItemCode").empty();

    $.ajax({
        url: 'http://localhost:8080/POS/pages/purchase-order',
        dataType: "json",
        success: function (codeObject) {
            for (let i in codeObject) {
                let item = codeObject[i];
                let code = item.code;

                if (code == undefined){
                    continue;
                }else {
                    let option = "<option>"+code+"</option>";
                    $("#selectItemCode").append(option);
                }
            }
        },
        error: function (error) {
            console.log(error)
        }
    });
}

function getCusIds() {
    $("#selectCusID").empty();

    $.ajax({
        url: 'http://localhost:8080/POS/pages/purchase-order',
        dataType: "json",
        success: function (cusObject) {
            for (let i in cusObject) {
                let cus = cusObject[i];
                let cusId = cus.id;

                let check = undefined;

                if (cusId == check){
                    continue;
                }else {
                    let option = "<option>"+cusId+"</option>";
                    $("#selectCusID").append(option);
                }
            }
        },
        error: function (error) {
            console.log(error)
        }
    });
}

$("#selectCusID").click(function () {
    let val = $("#selectCusID").val();

    $.ajax({
        url: 'http://localhost:8080/POS/pages/purchase-order',
        contentType: "application/json",
        method: "put",
        dataType: "json",
        data: JSON.stringify({
            "id" : val
        }),
        success: function (customer) {
            let id = customer.id;
            let name = customer.name;
            let address = customer.address;


            $("#orderCustomerID").val(id);
            $("#orderCustomerName").val(name);
            $("#orderCustomerAddress").val(address);

        },
        error: function (error) {
            console.log(error);
        }
    });
});

$("#selectItemCode").click(function () {
    let val = $("#selectItemCode").val();

    $.ajax({
        url: 'http://localhost:8080/POS/pages/purchase-order',
        contentType: "application/json",
        method: "delete",
        dataType: "json",
        data: JSON.stringify({
            "code" : val
        }),
        success: function (item) {
            let code = item.code;
            let name = item.item_name;
            let qty = item.item_contity;
            let price = item.unit_price;

            $("#txtItemCode").val(code);
            $("#txtItemName").val(name);
            $("#txtQTYOnHand").val(qty);
            $("#txtItemPrice").val(price);
        },
        error: function (error) {
            console.log(error);
        }
    });
});

let orderItems= [];

$("#addToCart").click(function (){
    let code = $("#selectItemCode").val();
    let name = $("#txtItemName").val();
    let price = $("#txtItemPrice").val();
    let qty = $("#txtQty").val();
    let total = price*qty;

    orderItems.push({
        code:code,
        description:name,
        unitPrice:price,
        qty:qty,
        total:total
    });

    $("#TotalOfItem").text(total.toFixed(2));


    updateTotals();
});




function updateTotals() {
    let totalAmount = 0;

    for (let item of orderItems) {
        totalAmount += item.total;
    }

    let subtotalAmount = totalAmount;
    let cashAmount = parseFloat($("#txtCash").val()) || 0;
    let discountAmount = parseFloat($("#txtDiscount").val()) || 0;

    let balanceAmount = cashAmount - discountAmount - subtotalAmount;

    $("#total").text(totalAmount.toFixed(2));
    $("#subtotal").text(subtotalAmount.toFixed(2));
    $("#txtBalance").val(balanceAmount.toFixed(2));
}

$("#placeOrder").click(function () {
    let oId = $("#txtOrderID").val();
    let cash = $("#txtCash").val();
    let balance = $("#txtBalance").val();
    let date = $("#txtDate").val();
    let cusId = $("#orderCustomerID").val();

    getItemDetails();
    $.ajax({
        url: 'http://localhost:8080/POS/pages/purchase-order',
        contentType: "application/json",
        method: "post",
        data: JSON.stringify({
            "oId" : oId,
            "cash" : cash,
            "balance" : balance,
            "date" : date,
            "id" : cusId,
            "itemData" : getTableData()
        }),
        success: function (res) {
            alert(res.message);
        },
        error: function (error) {
            console.log(error);
        },


    });
});

function getTableData() {
    let row = $("#orderTable").children.length;
    let array = [];
    for (let i = 0; i < row; i++) {
        let itCode = $("#orderTable").children().eq(i).children(":eq(0)").text();
        let itQty = $("#orderTable").children().eq(i).children(":eq(3)").text();
        array.push({code: itCode, qty: itQty});
    }
    return array;
}

$("#txtBalance").click(function () {
    let cash =parseFloat($("#txtCash").val());
    let sub = parseFloat($("#subtotal").text());

    let balance = cash - sub;
    let demo =parseFloat($("#txtBalance").val());
    $("#txtBalance").val(balance.toFixed(2));
});

function getItemDetails(){
    let rows = $("#orderTable").children().length;

    var array=[];

    for (let i = 0; i < rows; i++) {
        let code = $("#orderTable").children().eq(i).children(":eq(0)").text();
        let byQty = $("#orderTable").children().eq(i).children(":eq(3)").text();
        let qty = $("#orderTable").children().eq(i).children(":eq(4)").text();
        let price = $("#orderTable").children().eq(i).children(":eq(2)").text();
        array.push({code: code, byQty: byQty, qty: qty, price: price});
    }
}



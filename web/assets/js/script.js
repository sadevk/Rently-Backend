$(document).ready(function () {
    $("#containerbar").prepend($("<div>").load("../includes/leftbar.html"));
    $("#top").prepend($("<div>").load("../includes/topbar.html"));
    $("#comingsoon").prepend($("<div>").load("comingsoon.html"));
});

function adminLogin() {
    const email = $("#email").val();
    const password = $("#password").val();

    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLogin", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({email, password})
    })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    window.location.href = "index.html";
                } else {
                    alert(data.message);
                }
            });
}

function loadProducts() {
    const tbody = document.getElementById("products_table");
    tbody.innerHTML = "";

    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLoadProducts")
            .then(res => res.json())
            .then(data => {
                if (data.authenticated) {
                    $(".profilename").html(data.admin);
                    if (data.products && data.products.length > 0) {
                        data.products.forEach((product, index) => {
                            const row = document.createElement("tr");
                            row.innerHTML = `
                        <th scope="row">#${index + 1}</th>
                        <td><img src="../product-images/product_${product.id}.png" class="img-fluid" width="35" alt="Product Image"></td>
                        <td>${product.name}</td>
                        <td>${product.rent_per_day} LKR</td>
                        <td>${product.category.name}</td>
                        <td>
                            <div class="button-list">
                                <a href="#" onclick="loadProduct(${product.id});" class="btn btn-success-rgba"><i class="fa-regular fa-pen-to-square"></i></a>
                                <a href="#" class="btn btn-danger-rgba"><i class="fa-solid fa-trash"></i></a>
                            </div>
                        </td>
                    `;
                            tbody.appendChild(row);
                        });
                    } else {
                        tbody.innerHTML = `<tr><td colspan="6">${data.message}</td></tr>`;
                    }
                } else {
                    alert(data.message);
                    window.location.href = "login.html";
                }
            })
            .finally(() => $(".loading").hide());
}

function loadUsers() {
    const tbody = document.getElementById("users_table");
    tbody.innerHTML = "";

    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLoadUsers")
            .then(res => res.json())
            .then(data => {
                if (data.authenticated) {
                    $(".profilename").html(data.admin);
                    if (data.users && data.users.length > 0) {
                        data.users.forEach((user, index) => {
                            const row = document.createElement("tr");
                            row.innerHTML = `
                        <th scope="row">#${index + 1}</th>
                        <td><img src="../profile-images/profile_${user.id}.png" class="img-fluid" width="35" alt="User Image"></td>
                        <td>${user.fname}</td>
                        <td>${user.lname}</td>
                        <td>${user.email}</td>
                        <td>${user.mobile}</td>
                        <td>
                            <div class="button-list">
                                <a href="#" class="btn btn-success-rgba"><i class="fa-regular fa-pen-to-square"></i></a>
                                <a href="#" class="btn btn-danger-rgba"><i class="fa-solid fa-trash"></i></a>
                            </div>
                        </td>
                    `;
                            tbody.appendChild(row);
                        });
                    } else {
                        tbody.innerHTML = `<tr><td colspan="6">${data.message}</td></tr>`;
                    }
                } else {
                    alert(data.message);
                    window.location.href = "login.html";
                }
            })
            .finally(() => $(".loading").hide());
}

function loadRentals() {
    const tbody = document.getElementById("rentals_table");
    tbody.innerHTML = "";

    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLoadRentals")
            .then(res => res.json())
            .then(data => {
                if (data.authenticated) {
                    $(".profilename").html(data.admin);
                    if (data.rentals && data.rentals.length > 0) {
                        data.rentals.forEach((rental, index) => {
                            const formattedStartDate = new Date(rental.start_date).toLocaleDateString("en-US", {year: "numeric", month: "short", day: "2-digit"});
                            const formattedEndDate = new Date(rental.end_date).toLocaleDateString("en-US", {year: "numeric", month: "short", day: "2-digit"});

                            const row = document.createElement("tr");
                            row.innerHTML = `
                        <th scope="row">#${index + 1}</th>
                        <td>${rental.product.name}</td>
                        <td>${formattedStartDate} - ${formattedEndDate}</td>
                        <td>${rental.user.fname} ${rental.user.lname}</td>
                        <td>${rental.product.owner.fname} ${rental.product.owner.lname}</td>
                        <td>${rental.total} LKR</td>
                        <td>
                            <div class="button-list">
                                <a href="#" class="btn btn-success-rgba"><i class="fa-regular fa-pen-to-square"></i></a>
                                <a href="#" class="btn btn-danger-rgba"><i class="fa-solid fa-trash"></i></a>
                            </div>
                        </td>
                    `;
                            tbody.appendChild(row);
                        });
                    } else {
                        tbody.innerHTML = `<tr><td colspan="6">${data.message}</td></tr>`;
                    }
                } else {
                    alert(data.message);
                    window.location.href = "login.html";
                }
            })
            .finally(() => $(".loading").hide());
}

function loadPayments() {
    const tbody = document.getElementById("orders_table");
    tbody.innerHTML = "";

    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLoadPayments")
            .then(res => res.json())
            .then(data => {
                if (data.authenticated) {
                    $(".profilename").html(data.admin);
                    if (data.orders && data.orders.length > 0) {
                        data.orders.forEach((order, index) => {
                            const formattedPaymentDate = new Date(order.payment_date).toLocaleDateString("en-US", {year: "numeric", month: "short", day: "2-digit"});

                            const row = document.createElement("tr");
                            row.innerHTML = `
                        <th scope="row">#${order.order_id}</th>
                        <td>${order.rentals.user.fname} ${order.rentals.user.lname}</td>
                        <td>${order.rentals.product.owner.fname} ${order.rentals.product.owner.lname}</td>
                        <td>${order.amount} LKR</td>
                        <td>${formattedPaymentDate}</td>
                        <td>
                            <div class="button-list">
                                <a href="#" class="btn btn-success-rgba"><i class="fa-solid fa-info"></i></a>
                            </div>
                        </td>
                    `;
                            tbody.appendChild(row);
                        });
                    } else {
                        tbody.innerHTML = `<tr><td colspan="6">${data.message}</td></tr>`;
                    }
                } else {
                    alert(data.message);
                    window.location.href = "login.html";
                }
            })
            .finally(() => $(".loading").hide());
}

function loadProduct(id) {
    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLoadProduct?id=" + id)
            .then(res => res.json())
            .then(data => {
                if (data.authenticated) {
                    localStorage.setItem("product", JSON.stringify(data.product));
                    window.location.href = "product.html";
                } else {
                    alert(data.message);
                    window.location.href = "login.html";
                }
            }).finally(() => {
        $(".loading").hide();
    });
}

function retrieveProduct() {

    if (localStorage.getItem("product") != null) {

        const product = JSON.parse(localStorage.getItem("product"));
        $("#productTitle").val(product.name);
        $("#productDesc").val(product.description);
        $("#rentPerDay").val(product.rent_per_day);
        $("#ownerName").val(product.owner.fname + " " + product.owner.lname);
        $("#ownerEmail").val(product.owner.email);
        $("#ownerMobile").val(product.owner.mobile);
        $("#productCategory").html(product.category.name);
        $("#productImage").attr("src", `../product-images/product_${product.id}.png`);
        $("#locationAnchor").attr("href", "https://www.google.com/maps?q=" + product.owner.latitude + "," + product.owner.longitude);

        localStorage.removeItem("product");
    } else {
        window.location.href = "products.html";
    }

}

function addCategory() {

    const category = $("#categoryInp").val();

    if (category === "") {
        alert("Please enter category name");
    } else {
        fetch("https://full-hog-harmless.ngrok-free.app/Rently/AddCategory", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                category
            })
        })
                .then(res => res.json())
                .then(data => {
                    if (data.authenticated) {

                        if (data.success) {
                            alert(data.message);
                            window.location.reload();
                        } else {
                            alert("Something Went Wrong");
                        }

                    } else {
                        alert(data.message);
                        window.location.href = "login.html";
                    }
                });
    }
}


function logout() {

    fetch("https://full-hog-harmless.ngrok-free.app/Rently/AdminLogout")
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    window.location.href = "login.html";
                } else {
                    alert(data.message);
                }
            });
}
function validateForm() {
    var priceInput = document.getElementById("price").value;
    if (isNaN(priceInput)) {
        alert("Giá trị của trường Price không phải là số. Vui lòng nhập số.");
        return false; // Ngăn người dùng gửi biểu mẫu
    } else {
        return true; // Cho phép người dùng gửi biểu mẫu nếu giá trị là số 
    }
}

function goBack() {
    window.location.href = "list.html";
}
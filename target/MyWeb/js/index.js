window.onload = function () {
    let counts = document.getElementsByClassName("count");
    for (let i = 0; i < counts.length; i++) {
        let lis = counts[i].getElementsByTagName("li");
        //alert(lis[0].innerHTML)
        let num_jia = lis[2];
        let num_jian = lis[0];
        let input_num = lis[1].getElementsByTagName("input")[0];
        num_jia.onclick = function () {
            input_num.value = parseInt(input_num.value) + 1;
        }

        num_jian.onclick = function () {
            if (input_num.value <= 0) {
                input_num.value = 0;
            } else {
                input_num.value = parseInt(input_num.value) - 1;
            }
        }
    }
    let trs = document.getElementsByClassName("tr");
    for (let i = 0; i < trs.length; i++) {
        let tds = trs[i].getElementsByTagName("td")
        let add_to_shop_car = tds[4]
        let num = tds[3].getElementsByTagName("input")[0];
        add_to_shop_car.onclick = function () {
            window.location.href = "./index?shop=shop&phone_id=" + tds[0].getAttribute("id") + "&name=" + tds[0].innerText +
                "&type=" + tds[1].innerText + "&num=" + num.value;
        }
    }

}
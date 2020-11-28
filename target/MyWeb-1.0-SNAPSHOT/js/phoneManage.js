window.onload = function () {
    {   //删除商品链接拼接
        let trs = document.getElementsByClassName("tr");
        for (let i = 0; i < trs.length; i++) {
            let tds = trs[i].getElementsByTagName("td");
            if (i !== trs.length-1) {
                tds[4].onclick = function () {  //del
                    window.location.href = "./phoneManage?delete=delete&phone_id=" + tds[0].getAttribute("id");
                }
                tds[5].onclick = function () {  //del
                    let name = tds[0].getElementsByTagName("input")[0].value
                    let type = tds[1].getElementsByTagName("input")[0].value;
                    let stock = tds[3].getElementsByTagName("input")[0].value;
                    let price = tds[2].getElementsByTagName("input")[0].value;
                    window.location.href = "./phoneManage?update=update&phone_id=" + tds[0].getAttribute("id")+"&name=" + name + "&type=" + type + "&stock=" + stock + "&price=" + price;
                }
            }else{
                tds[4].onclick = function () {   //add
                    let name = tds[0].getElementsByTagName("input")[0].value
                    let type = tds[1].getElementsByTagName("input")[0].value;
                    let stock = tds[3].getElementsByTagName("input")[0].value;
                    let price = tds[2].getElementsByTagName("input")[0].value;
                    window.location.href = "./phoneManage?add=add&name=" + name + "&type=" + type + "&stock=" + stock + "&price=" + price;
                }
            }
        }
    }

}
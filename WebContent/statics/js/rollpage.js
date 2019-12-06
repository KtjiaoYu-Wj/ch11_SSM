/*当用户在rollpage.jsp页面中点击上一页，下一页，首页，末页，就会触 
发page_nav函数，此函数 中第一个参数为userlist.jsp页面中的第一个
form表示对象， 第二个参数为进行加减计算后的当前页码 */
function page_nav(frm,num){  //js中数据类型是以弱类型，即在声明函数时，不需要指定参数类型。
	 	/*<input type="hidden" name="pageIndex" value="1"/>*/
		frm.pageIndex.value = num; //将计算好后的当前页码值赋值给name="pageIndex"的form表单组件
		frm.submit();  
		//将userlist.jsp页面中第一个form表提交。
		//此form表单中有三个参数：
		//1、queryname用户名
		//2、queryUserRole 用户角色ID
		//3、pageIndex 当前页码
}

//跳转至指定的页码中
function jump_to(frm,num){
    //alert(num);
	//验证用户的输入
	var regexp=/^[1-9]\d*$/;
	var totalPageCount = document.getElementById("totalPageCount").value;
	//alert(totalPageCount);
	if(!regexp.test(num)){
		alert("请输入大于0的正整数！");
		return false;
	}else if((num-totalPageCount) > 0){
		alert("请输入小于总页数的页码");
		return false;
	}else{
		page_nav(frm,num);
	}
}
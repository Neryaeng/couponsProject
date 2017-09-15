
var app = angular.module('couponApp',['angularUtils.directives.dirPagination','ngRoute','file-model']);

app.config(function($routeProvider) {
	//setup routes
	//default
	
	$routeProvider
		//Login
		.when('/', {
		templateUrl: 'PARTIALS/login.html'})
		//Admin - Customer management 
		.when('/admin_cust',{
		templateUrl: 'PARTIALS/Admin/admin_customer.html'})
		//Admin - Company management 
		.when('/admin_company',{
		templateUrl: 'PARTIALS/Admin/admin_company.html'})
		//Company 
		.when('/Company',{
		templateUrl: 'PARTIALS/Company/company.html'})
		//Company - Create new coupon
		.when('/createCoupon',{
		templateUrl: 'PARTIALS/Company/createCoupon.html'})
		//Upload image page
		.when('/upload/:id', {
		templateUrl: 'PARTIALS/Company/uploadImage.html'})	
		//Customer
		.when('/Customer',{
		templateUrl: 'PARTIALS/Customer/Customer.html'})
		//
		.when('/CustomerPurchase',{
		templateUrl: 'PARTIALS/Customer/PurchasedCoupons.html'})
		//Customers review coupon page
		.when('/view', {
		templateUrl: 'PARTIALS/Customer/viewCoupon.html'})
		.otherwise({
		redirectTo: '/'});
	
});


app.controller('login',function($scope,$http,$location,$rootScope){
	
	//onload
	//Setup navbars ng-view to false
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = false;
	$rootScope.customerNavBar = false;
	
	//default value of user type
	$scope.type="customer";
	
	$scope.login = function() {		
		var role = $scope.type;
		var parameter = {"role":$scope.type ,"username":$scope.userName ,"password":$scope.password };
		//$http.post("rest/webLogin/login/" + role  + "/" + $scope.userName  + "/" + $scope.password )
		var config={headers :{"Content-Type":"application/json"}}
		$http.post("rest/webLogin/login/", parameter,config)
		.success((response) =>{
			console.log(response)
			//redirect according to role , server response false in case of unexists user.
			if (response != "false"){
				switch (role)
				{
				case "customer":
					$location.path("/Customer");
					break;
				case "company":
					$location.path("/Company");
					break;
				case "admin":
					$location.path("/admin_cust");
					break;
				default:
					$location.path("/");
					break;
				}
			}
			else {
				$scope.loginRes = "Username or password is wrong. try again.";
				}
			});
	}
});		



app.controller("navBars_Controller", function($scope,$http,$rootScope,$location){


	$rootScope.username = "";	
	$scope.logOff = function() {
		
		$http.get("rest/webLogin/logoff").success(function(response){	
			$location.path(response);	
		}).error(function() {
		});		
	}
	
});


/*************************
 * 
 * 		ADMIN
 * 
 *************************/


app.controller("admin_cust", function($scope,$http,$rootScope,$location){
	//on load
	//check weather the user is logged
	
	chkLogin($http, $location, $rootScope, "admin");
	refreshCustData();
	
	//sets the admin's navbar ng-view true
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = true;
	$rootScope.customerNavBar = false;			
	
	//Remove Customer (after confirm)
	$scope.deleteCust = function(customer) {
        $scope.actionStatus ="";
		var res = confirm("Are you sure you want to delete " + customer.custName + "?");
		if (res == true){
            var id = customer.id;
            var custName = customer.custName;
            var password= customer.password;
            var ptr = {"id": id };
            var config={headers :{"Content-Type":"application/json"}}

            $http.post("rest/adminService/removeCust" ,ptr, config).success(function(response){
				$scope.actionStatus = response;				
				refreshCustData();
				
			}) 
		}
	};
	
	 
	//Add Customer Panel		
	$scope.addPanel = function() {
		$scope.addCustomerPanel = !$scope.addCustomerPanel;
		$scope.updateCustomerPanel = false;
	};
	//Add Customer
	$scope.addCustomer = function() {
        $scope.actionStatus =""
        var parameter = JSON.stringify({id:0 ,custName:$scope.addNameCust ,password:$scope.addPassCust });
		$http.post("rest/adminService/addCust",parameter)
		.success(function(response){
				$scope.actionStatus = response;
				refreshCustData();
		})
		.error(function(error) {
	        // error
	        console.log(JSON.stringify("Failed to get : " + error));
	    })			
	};
	
	//Cancel adding User
	$scope.addCustomerCanceled = function(){
        $scope.actionStatus ="";
		$scope.addCustomerPanel = false;			
	};

	
	//Load Customer into update Panel
	$scope.selectCustomerPanel=function (customer) {
        $scope.actionStatus ="";
		$scope.updateCustomerPanel = true;
		$scope.addCustomerPanel = false;
		$scope.updateCustId = customer.id;
		$scope.updateCustName = customer.custName;
		$scope.updateCustPass = customer.password;			
	};
	
	//Canceled Customer Update
	$scope.updateCustomerCanceled=function() {
        $scope.actionStatus ="";
		$scope.updateCustomerPanel = false;				
	};
	
	//update Customer via panel
	$scope.updateCustomer = function() {
        $scope.actionStatus ="";
        var parameter = JSON.stringify({id:$scope.updateCustId,custName:$scope.updateCustName ,password:$scope.updateCustPass });
		$http.put("rest/adminService/updateCust",parameter).success(function(response){
			
			$scope.actionStatus = response;
			refreshCustData();
		})
	};
	
	//go to log page of the selected customer
	$scope.viewIncomeLog = function viewIncomeLog(name) {			
		$location.path("/adminPage/logs/" + name);			
	};
	
	//Refresh Controllers 
	function refreshCustData(){ 
		$http.get("rest/adminService/getAllCust").success(function(response){
				$scope.viewCustomers=response;	
		})
		$scope.updateCustomerPanel = false;
		$scope.addCustomerPanel = false;
	}
	

});


//Admin company controller


app.controller('Admin_company_Controller', function($scope,$rootScope, $http , $location){
//onload	
//check login 
chkLogin($http, $location, $rootScope, "admin");


//sets the admin's navbar ng-view true
$rootScope.companyNavBar = false;
$rootScope.adminNavBar = true;
$rootScope.customerNavBar = false;	


//get all companies
$http.get("rest/adminService/getAllComp").success(function(response){
	$scope.viewCompanies=response;
	$scope.addCompanyPanel = false;
});	

//show add company panel
$scope.showAddPanel = function() {
	$scope.addCompanyPanel = !$scope.addCompanyPanel;
	$scope.updateCompanyPanel = false;
	
}

//add company canceled
$scope.addCompanyCanceled = function() {
    $scope.actionStatus ="";
	$scope.addCompanyPanel = false;
}

//add company 	
$scope.addCompany = function() {
    $scope.actionStatus ="";
	var parameter = JSON.stringify({id:0 ,compName:$scope.addNameComp ,password:$scope.addPassComp ,email:$scope.addEmailComp });
    var config={headers :{"Content-Type":"application/json"}}
	//+$scope.addNameComp+"/"+$scope.addPassComp+"/"+$scope.addEmailComp
	$http.post("rest/adminService/addComp",parameter, config).success(function(response){
			$scope.actionStatus = response;
			refreshCompData();
    })
        .error(function(error) {
            // error
            console.log(JSON.stringify("Failed to get : " + error));

        })
};

//update company panel
$scope.selectCompanyPanel = function(company) {
    $scope.actionStatus ="";
	$scope.updateCompanyPanel = true;
	$scope.addCompanyPanel = false;
	$scope.updateCompId = company.id;
	$scope.updateCompName = company.compName;
	$scope.updateCompPass = company.password;
	$scope.updateEmailComp = company.email;		
}

//update company canceled
$scope.updateCompanyCanceled=function() {
    $scope.actionStatus ="";
	$scope.updateCompanyPanel = false;				
}

//view income JMS log of the selected company
$scope.viewIncomeLog = function viewIncomeLog(name) {		
	$location.path("/adminPage/logs/" + name);			
}

//update company's attributes
$scope.updateCompany= function() {
    $scope.actionStatus ="";
	var parameter = JSON.stringify({id:$scope.updateCompId  ,compName:$scope.updateCompName ,password:$scope.updateCompPass ,email:$scope.updateEmailComp });
	$http.put("rest/adminService/updateComp",parameter).success(function(response){
		$scope.actionStatus = response;
		refreshCompData();
	})
};		

//remove company (after confirm)
$scope.deleteComp = function(company) {
    $scope.actionStatus ="";
	var res = confirm("Are you sure you want to delete " + company.compName + "?");
		if (res == true){
			var id = company.id;
			var compName = company.compName;
			var password= company.password;
			var email= company.email;

            var ptr = {"id": id  };
            var config={headers :{"Content-Type":"application/json"}}
            $http.post("rest/adminService/removeComp",ptr, config).success(function(response){
			$scope.actionStatus = response;
			refreshCompData();
		})
	}
};
//load companies data from server
function refreshCompData(){ 
	$http.get("rest/adminService/getAllComp").success(function(response){
		$scope.viewCompanies=response;
    })
    $scope.updateCompanyPanel = false;
    $scope.addCompanyPanel = false;
}

});


////Admin Logger


app.controller('Admin_logs_by_Name', function($scope,$rootScope, $http , $location, $routeParams){
//login chk
chkLogin($http, $location, $rootScope, "admin");

//sets the admin's navbar ng-view true
$rootScope.companyNavBar = false;
$rootScope.adminNavBar = true;
$rootScope.customerNavBar = false;

//retrieve name param from URL
var name = $routeParams.name;	
$scope.name = name;

//load logs from JMS 
$http.get("rest/srvAdmin/logs/" + name).success(function(response){			
	$scope.logs = response;		
});	
});


app.controller('Admin_allLogs_Controller', function($scope,$rootScope,$location, $http){
//onload	
chkLogin($http, $location, $rootScope, "admin");

//sets the admin's navbar ng-view true
$rootScope.companyNavBar = false;
$rootScope.adminNavBar = true;
$rootScope.customerNavBar = false;

//load logs from JMS
$http.get("rest/srvAdmin/allLogs/").success(function(response){			
	$scope.logs = response;		
});

	
});



/*************************
 * 
 * 		COMPANY
 * 
 *************************/



app.controller("company_Controller", function($scope,$http,$rootScope,$location,$routeParams){

	//check login
	chkLogin($http, $location, $rootScope, "company");
	
	refreshGrid();
	
	
	//set conpanie's navbar to true
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true; //true
	$rootScope.customerNavBar = false;
	
	//expand coupon's details
	$scope.expand = function(coupon) {	
		$rootScope.imageCoupon = coupon;
		$scope.details = !$scope.details;
		$scope.editable = false;
	
		refreshControls(coupon);
		
	}
	
	//hide coupon's details
	$scope.hide = function () {
		$scope.details = false;
		$scope.editable = false;
	}
	
	//edit coupon's details	
	$scope.edit = function(coupon) {
        $scope.UpdateRes="";
		$scope.editable = true;
		$scope.details = false;
        $rootScope.mainCompany = false;
		//load coupon into the panel
		refreshControls(coupon);		
		$scope.id_edit = coupon.id;
		$scope.price_edit = coupon.price;
		$scope.endDate_edit = new Date(coupon.endDate);	//	new Date(coupon.endDate)
	}
	
	//save the coupons new params  --  only end date and price allowed
	$scope.save = function() {
		//???????
        //var coupId = coupon.id;
        //??????
		//var str = $scope.id_edit + "/" + dateToString($scope.endDate_edit) + "/" + $scope.price_edit;

        $scope.UpdateRes="";
             ptr = {
                 "id": $scope.id_edit,
                 "title": $scope.title,
                 //"startDate": response.startDate,
                 "endDate": $scope.endDate_edit,
                 //"amount": response.amount,
                 //"type": response.type,
                 //"message": response.message,
                 "price": $scope.price_edit
                 //"image": response.image
             };
             var config={headers :{"Content-Type":"application/json"}}
             $http.put("rest/companyService/updateCoup",ptr,config).success(function(response){
                 $scope.UpdateRes=response.title + " has been updated";
                 refreshGrid();
                 $rootScope.mainCompany = true;
             }).error(function (e) {
             	console.log(e);
                 $rootScope.mainCompany = true;
             })

             //???????????????????
         



		
		//hide the panel
		$scope.editable = false;
		$scope.details = false;
	}
	

	//coupon removal after confirm
	$scope.remove = function(coupon) {
        $scope.UpdateRes="";
		var res = confirm("Are you sure you want to delete " + coupon.title + "?");
		if (res == true){
            var id = coupon.id;
            var title = coupon.title;
            var startDate = coupon.startDate;
            var endDate = coupon.endDate;
            var amount = coupon.amount;
            var type = coupon.type;
            var message = coupon.message;
            var price = coupon.price;
            var image = coupon.image;

            //var ptr = {"id": id ,"title": title, "startDate": startDate, "endDate": endDate, "amount": amount, "type": type, "message": message, "price": price,"image": image};
            var ptr = {"id": id };
            var config={headers :{"Content-Type":"application/json"}}
				$http.post("rest/companyService/removeCoup",ptr,config).success(function(response){
                    $scope.UpdateRes=title + " has been removed";
					refreshGrid();
				});	
		}	
	};
	
	
	
	//goto update image by given coupon's id
	$scope.editPic = function(coupon) {
		$rootScope.imageCoupon = coupon;
		//coupon.id = selected (pressed) coupon.
		$location.path("/upload/" + coupon.id);
	}
	
	//refresh page controls
	function refreshGrid() {
		$http.get("rest/companyService/getAllCoup").success(function(response){
			console.log(typeof response + response.length);
			$scope.allCoupons=response;
		}).error(function(response){

            $scope.allCoupons= [];
        });
        $scope.allTypes=["SPORTS","FOOD","ELECTRICITY","HEALTH","RESTURANTS","CAMPING","TRAVELLING"];
		/*
		$http.get("rest/companyService/getCoupByType?coupType=").success(function(response){
			$scope.allTypes=response;
		});	
		*/
		$scope.searchPanel = false;
	
	}
	
	//cancel operation
	$scope.cancel = function(coupon) {	
		
		$scope.editable = false;
		$scope.details = false;
        $rootScope.mainCompany = true;
	}
	
	//Filter Data Table Panel
	$scope.showSearch = function() {
		
		$scope.searchPanel = !$scope.searchPanel;
		$scope.priceFilter=1;
		$scope.endDateFilter = new Date();
		
	}
	
	//Filter Data Table OnClick - change the filter panel according to user selection
	$scope.filterPanel = function() {
	
		var filter = $scope.filter;
		var value ="";
        var filtering="";
		
		if (filter == "price") {
			value = $scope.priceFilter;
            var filtering="getCoupByPrice?coupPrice=";
		}		
		else if (filter == "date") {
			value = dateToString($scope.endDateFilter);
            var filtering="getCoupByEndDate?coupEndDate=";
		}
		else if (filter == "type") {
			value = $scope.typeFilter.toUpperCase();
            var filtering="getCoupByType?coupType=";
						
		}
	
		$http.get("rest/companyService/"+filtering+value).success(function(response){
					
					$scope.allCoupons=response;
		}).error(function(response){
			
			$scope.allCoupons= [];
        }); 	
	}
	
	$scope.clearFilter = function() {
		refreshGrid();
	}
	
	$scope.showPanel = function(coupon) {		
		coupon.buttonsPanel = true;
	}

	$scope.hidePanel = function(coupon) {	
		coupon.buttonsPanel = false;
	}
	
	
	function refreshControls(coupon) {
		//update arguments of the coupon to the panel
		$scope.amount = coupon.amount;
		$scope.title = coupon.title;
		$scope.price = coupon.price;
		$scope.price_edit = coupon.price;
		$scope.startDate = new Date(coupon.startDate);
		$scope.min = coupon.startDate;		
		$scope.endDate = new Date(coupon.endDate);
		$scope.endDate_edit = new Date(coupon.endDate);
		$scope.cat = coupon.type;
		$scope.desc = coupon.messages;
		
	}
	
	
	
});


app.controller('create_coupon_Controller', function($scope, $http,$location,$rootScope){
	
	chkLogin($http, $location, $rootScope, "company");
	
	//set conpanie's navbar to true
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true; //true
	$rootScope.customerNavBar = false;
    $scope.allTypes=["SPORTS","FOOD","ELECTRICITY","HEALTH","RESTURANTS","CAMPING","TRAVELLING"];
/*
	$http.get("rest/srvCompany/getTypes").success(function(response){	
		$scope.allTypes=response;
	});	
	*/
	resetForm();
	
	//create coupon
	$scope.submit = function()	
	{
		/*
		var title = $scope.title;
		 //Restful purposes - modifying date to string
		var startDate = dateToString($scope.start);
		 //Restful purposes - modifying date to string
		var endDate = dateToString($scope.end);
		var amount = $scope.amount;
		 //Restful purposes - modifying type to be Enum-friendly.
		var type = $scope.type.value.toUpperCase();
		var message = $scope.desc;
		var price = $scope.price;
		var image = $scope.image;
		
		var str = title+ "/" + startDate + "/" + endDate + "/" + amount + "/" + type + "/" + message + "/" + price + "/" + image;
		*/
		var parameter = {

			title:$scope.title,
			startDate: dateToString($scope.start),
			endDate: dateToString($scope.end),
			amount: $scope.amount, 
			type: $scope.type.value.toUpperCase(),
			message:$scope.desc,
			price:$scope.price,
			image:$scope.image
		}

        var config={headers :{"Content-Type":"application/json"}}
		$http.post("rest/companyService/addCoup",parameter,config).success(function(response){
			$scope.result = "Type: "+ response.type + " Name: " +response.title;
		}).error(function(error){

          	console.log(error);
        });
	}
	
	$scope.reset = function() {
		resetForm();
	}
	
	//update the min value of the calendar
	$scope.updateMin = function() {		
		$scope.min = $scope.start;
		if ($scope.start >= $scope.end)
			$scope.end = $scope.start;
	}
	
	function resetForm() {
		$scope.title = "";		
		$scope.start = new Date();
		$scope.end = new Date();
		$scope.amount = "";
		$scope.desc = "";
		$scope.price = "";
		$scope.image = "";
	}	

});

app.controller("uploadImage", function($scope,$http,$location,$routeParams,$rootScope) {
	
	chkLogin($http, $location, $rootScope, "company");
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true; 
	$rootScope.customerNavBar = false;

	$scope.file = null;
	
	var file;
	//retrieve the coupon's id from URL
	var id = $routeParams.id;	
	
	$scope.result = "";		
	refreshImg();	
	
	//get the file name from the controller (used by library)
	$scope.$watch('file', function (newVal) {
       if (newVal) {
         file = newVal;        
       }
     });
	
    //send the image to the server via form (file validation is triggered by value change)
	$scope.show = function() {
    	 var fd = new FormData();
         fd.append('file', file);
         fd.append('id',id);

         $http.post("rest/srvCompany/imageUpload",
        		   fd, {
        	 				transformRequest: angular.identity, 
        	 				headers: {'Content-Type': undefined 
        	 			}
        }).success(function(response) {
        	$scope.result = "File was successfuly loaded.";
        	refreshImg();
        }).error(function(response){
        	$scope.result = "Something went wrong, try again";
        });         

     }
     
     function refreshImg(){
    	 $http.get("rest/srvCompany/getCouponByID/"+id).success(function(response){		
    			$scope.coupon = response;
    			if ($scope.coupon == "") { //someone tries to sniff coupons from address... cause login.
    				$location.path("/");
    			}
    		}).error(function(response){
    		});
     }
});

app.controller("Company_logs_Controller", function($scope,$location, $rootScope,$http){
	chkLogin($http, $location, $rootScope, "company");
	$rootScope.companyNavBar = true;
	$rootScope.adminNavBar = false;
	$rootScope.mainCompany = true; //true
	$rootScope.customerNavBar = false;
	
	//company JMS logs
	$http.get("rest/srvCustomer/company/logs").success(function(response){			
		$scope.logs = response;		
	});

});



/*************************
 * 
 * 		CUSTOMER
 * 
 *************************/

app.controller("Customer_Controller", function($scope,$http,$rootScope,$location){
	
	chkLogin($http, $location, $rootScope, "customer");	
	
	$rootScope.companyNavBar = false;
	$rootScope.adminNavBar = false;
	$rootScope.customerNavBar = true;
	
	
	$http.get("rest/customerService/getAllCoupons").success(function(response){
		$scope.allCoupons=response;
	}).error(function(response){

		$scope.allCoupons= [];
    }); 	
	
	
	$http.get("rest/customerService/getAllPurchasedCoupons").success(function(response){
		$scope.myCoupons=response;
	}).error(function(response){
		
		$scope.allCoupons= [];
    }); 
	
	
	
	$scope.view = function(coupon) {
		
		$rootScope.selectedCoupon = coupon; 
		$location.path("/view");
	}
	
	$scope.showPanel = function() {
	$scope.searchPanel = !$scope.searchPanel;
	$scope.priceFilter=1;
	$scope.allTypes=["SPORTS","FOOD","ELECTRICITY","HEALTH","RESTURANTS","CAMPING","TRAVELLING"];
/*
	$http.get("rest/customerService/getCoupByType?purchasedCouponType="+ coupon.type).success(function(response){
		$scope.allTypes=response;
	});
*/
	}
	
	//Filter Data Table OnClick
	$scope.filterPanel = function() {
	
		var choise = $scope.filter;
		var value ="";
		var filtering="";
		
		if (choise == "price") {
			value = $scope.priceFilter;
            var filtering="generalCouponsByPrice?filterByPrice=";
		}
		else if (choise == "type") {
			value = $scope.typeFilter.toUpperCase();
            var filtering="generalCouponsByType?filterByType=";
			
		}
		$http.get("rest/customerService/"+filtering+value).success(function(response){
			$scope.allCoupons=response;			
		}).error(function(response){
			
			$scope.allCoupons= [];
	    }); 	
	}
	
	$scope.clearFilter = function() {
		$http.get("rest/customerService/getAllCoupons").success(function(response){
			$scope.allCoupons=response;
	}).error(function(response){
		
		$scope.allCoupons= [];
    }); 
	
	}
	
	$scope.buyCoupon = function(coupon) {
	
	var res = confirm("Are you sure you want to buy " + coupon.title + "?");
		if (res == true){
			var id = coupon.id;
            var parameter = {"id": id };
            //$http.post("rest/webLogin/login/" + role  + "/" + $scope.userName  + "/" + $scope.password )
            var config={headers :{"Content-Type":"application/json"}}
			$http.put("rest/customerService/purchaseCoup",parameter,config).success(function(response){
				if(response!="" || response!=null)
				$scope.result= "Thank you for buying: " + response.title;
			}).error(function(response){
				
				$scope.result= [];
		    }); 			
		}		
	}	

});







////////////////////////////////////////////////////
////////////////////////////////////////////////////
/////////////   Other Functions   //////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////


//date stringify 
function dateToString (date) {
var year = date.getFullYear();
var month = date.getMonth()+1;
var day = date.getDate();

if (day < 10) { day = "0"+day;}
if (month <10) {month = "0"+month;}
//return day+"/"+month+"/"+year;
    return year+"-"+month+"-"+day;
}

//login check on each page loading
function chkLogin(http,loc,scope,usertype) {
	
	http.get("rest/webLogin/chkLogin/"+usertype).success(function(response){
		
		if (response == "false") {
			loc.path("/");
		}
		else {
			scope.username = response;
		}
	}).error(function(response){
		console.log(response);
		//$scope.result= [];
    }); 	
}

//file extention validation
function validateFile(file) {

	//Function designed to permit only images file to be uploaded to server.
	
	//allowd files extentions: 
	var allowed = ["jpg","jpeg","gif","png","bmp"];
	
	//get file name from control
	var fullPath = document.getElementById('fileInput').value;
	if (fullPath) 
	{
		var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
		var filename = fullPath.substring(startIndex);
		if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
			filename = filename.substring(1);
		}
	
		//get file extention
		var ext = filename.substr(filename.lastIndexOf(".")+1);
		
		//chk if extention is allowed (exists in array) - -1 will be return when false
		if (allowed.indexOf(ext.toLowerCase()) < 0)
		{	  		
			document.getElementById("uploadBTN").disabled = true;
			document.getElementById("error").style.visibility = "visible";
		}
		else{
			document.getElementById("uploadBTN").disabled = false;
			document.getElementById("error").style.visibility = "hidden";
		}
	}
}




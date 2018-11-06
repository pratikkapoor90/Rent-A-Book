var myApp = angular.module("myApp", [ "ngRoute", "ngSanitize", 'ngCookies',
		'naif.base64' ]);

myApp.config([ '$routeProvider', function($routeProvider, $locationProvider) {

	$routeProvider

	// home page
	.when('/home', {
		templateUrl : 'pages/home.html',
		controller : 'mainController'
	}).when('/userhome', {
		templateUrl : 'pages/userhome.html',
		controller : 'userhomeController'
	}).when('/login', {
		templateUrl : 'pages/login.html',
		controller : 'loginController'
	}).when('/logout', {
		templateUrl : 'pages/login.html',
		controller : 'logoutController'
	}).when('/register', {
		templateUrl : 'pages/register.html',
		controller : 'registerController'
	}).when('/addbook', {
		templateUrl : 'pages/addbook.html',
		controller : 'addbookController'
	}).when('/viewbook/:bookid', {
		templateUrl : 'pages/viewbook.html',
		controller : 'viewbookController'
	}).when('/viewbook2/:bookid', {
		templateUrl : 'pages/viewbook2.html',
		controller : 'viewbook2Controller'
	}).when('/payment/:bookid', {
		templateUrl : 'pages/payment.html',
		controller : 'paymentController'
	}).when('/contactselller/:uname', {
		templateUrl : 'pages/contactseller.html',
		controller : 'contactcontroller'
	}) 
	//		.when('/paymentConfirm/:bookid', {
//		templateUrl : 'pages/paymentConfirm.html',
//		controller : 'paymentConfirmController'
//	})
	.when('/userprofile', {
		templateUrl : 'pages/userprofile.html',
		controller : 'userprofileController'
	}).when('/userprofile2', {
		templateUrl : 'pages/userprofile2.html',
		controller : 'userprofile2Controller'
	}).when('/postreview/:uname', {
		templateUrl : 'pages/postreview.html',
		controller : 'setFeedbackController'
	}).when('/getreview/:uname', {
		templateUrl : 'pages/viewreview.html',
		controller : 'getFeedbackController'
	}).when('/contact', {
		templateUrl : 'pages/contact.html',
	}).when('/about',{
		templateUrl : 'pages/about.html',
	})

	.otherwise({
		redirectTo : '/userhome'
	});

} ]);

myApp.controller("mainController", function ($scope, $rootScope, $cookieStore,
        $http, $timeout) {
    if ($cookieStore.get('token') !== "") {
        $scope.access = false;
        window.open("#userhomeController", "_self");
    }

    $http.get("rest/book/getAllBooks", {
        headers : {
            "Authorization":  $cookieStore.get('token')
        }   
    }).then(function (response) {
        $scope.services = response.data;
        $allBooks = [];
        for (var i = 0; i < response.data.allBooks.length; i++) {
            $allBooks[i] = JSON.parse(response.data.allBooks[i]);
        }
        $rootScope.allbooks = $allBooks;
        //                 alert(JSON.stringify($allBooks));
    });

});

myApp.controller("userhomeController", function ($scope, $rootScope,
        $cookieStore, $http, $timeout) {
    if ($cookieStore.get('token') === "") {
        $scope.access = false;
        window.open("#login", "_self");
    } else {
        $scope.access = true;
        $http.get("rest/book/getAllBooks", {
            headers : {
                "Authorization":  $cookieStore.get('token')
            }   
        }).then(function (response) {
            $scope.services = response.data;
            $allBooks = [];
            for (var i = 0; i < response.data.allBooks.length; i++) {
                $allBooks[i] = JSON.parse(response.data.allBooks[i]);
            }
            $rootScope.allbooks = $allBooks;
            //                 alert(JSON.stringify($allBooks));
        });
    }

});

myApp.controller("viewbookController", function ($http, $scope, $rootScope,
        $routeParams) {
    $http.get("rest/book/getAllBooks").then(function (response) {
        $scope.services = response.data;
        $allBooks = [];
        for (var i = 0; i < response.data.allBooks.length; i++) {
            $allBooks[i] = JSON.parse(response.data.allBooks[i]);
        }
        $rootScope.allbooks = $allBooks;
        //          alert(JSON.stringify($allBooks));
        $scope.book = {};

        for (var i = 0; i < response.data.allBooks.length; i++) {
            if ($allBooks[i].bookId == $routeParams.bookid)
                $scope.book = $rootScope.allbooks[i];
        }

    });
});
myApp.controller("viewbook2Controller", function ($http, $scope, $rootScope,
        $routeParams) {
    $http.get("rest/book/getAllBooks").then(function (response) {
        $scope.services = response.data;
        $allBooks = [];
        for (var i = 0; i < response.data.allBooks.length; i++) {
            $allBooks[i] = JSON.parse(response.data.allBooks[i]);
        }
        $rootScope.allbooks = $allBooks;
        //          alert(JSON.stringify($allBooks));
        $scope.book = {};

        for (var i = 0; i < response.data.allBooks.length; i++) {
            if ($allBooks[i].bookId == $routeParams.bookid)
                $scope.book = $rootScope.allbooks[i];
        }

    });
});


myApp.controller("paymentController", function ($http, $cookieStore, $scope, $rootScope,
        $routeParams) {


    $scope.sendPaymentRequest = function () {
        // alert($scope.form_password + ":" +
        // $scope.form_user_name); 
    	if ($scope.name === undefined
				|| $scope.name === "")
			$scope.errormessage = "Enter a valid CardHolder Name";
		else if ($scope.card === undefined
				|| $scope.card === "")
			$scope.errormessage = "Enter a valid Card Number";
		else if ($scope.date === undefined
				|| $scope.date === "")
			$scope.errormessage = "Enter a valid Date";
		else if ($scope.cvv === undefined
				|| $scope.cvv === "")
			$scope.errormessage = "Enter a valid CVV";
		else
			{
        $http(
                {
                    url: 'rest/book/payment',
                    method: "POST",

                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "="
                                    + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: {
                        book_id: $routeParams.bookid,
                        token: $cookieStore.get("token"),
                    },

                }).success(function (data, status, headers, config) {

            $scope.response = data.msg;
            //				$scope.message = data;
            if (data.status === "failed") {
                    $scope.response = "";
        
                $scope.errormessage = data.errorMsg;
            } else {
                $scope.errormessage = "";
//                $scope.loginStatus = true;
                $scope.loginresponse = "Payment made succesfully";
//                alert($scope.loginresponse);
//                window.open("#login", "_self");
            }

            //                                        $scope.loginresponse = "User registration successfull";
            //                                        window.open("#login", "_self");
            //				$scope.$apply()
            //				alert("Success : " + JSON.stringify({data: data}));

        }).error(function (data, status, headers, config) {
            alert("failure message: " + JSON.stringify({
                data: data
            }));
        });
        //                    alert("Registration successful");
        //                    window.open("#login", "_self");

    }
    }

});



myApp
		.controller(
				"loginController",
				function($scope, $rootScope, $http, $cookieStore) {
					$rootScope.loginStatus = false;
					$rootScope.allUsers = [];
					$scope.sendLoginRequest = function() {
						if ($scope.form_user_name === undefined
								|| $scope.form_user_name === "")
							$scope.errormessage = "Enter a valid Username";
						else if ($scope.form_password === undefined
								|| $scope.form_password === "")
							$scope.errormessage = "Enter a valid Password";
						else {
							$http(
									{
										url : 'rest/login/doLogin',
										method : "POST",

										headers : {
											'Content-Type' : 'application/x-www-form-urlencoded'
										},
										transformRequest : function(obj) {
											var str = [];
											for ( var p in obj)
												str
														.push(encodeURIComponent(p)
																+ "="
																+ encodeURIComponent(obj[p]));
											return str.join("&");
										},
										data : {
											lg_username : $scope.form_user_name,
											lg_password : $scope.form_password,
										},

									})
									.success(
											function(data, status, headers,
													config) {
												// $scope.message = data;
												if (!data.login) {
													$scope.errormessage = data.error_msg;
												} else {
													$scope.errormessage = "";
													$rootScope.loginStatus = true;

													// alert(data.profile.username);
													//                                            alert(data.token);
													$cookieStore
															.put(
																	'username',
																	$scope.form_user_name);
													$cookieStore.put('token',
															data.token);

													//                                            alert($cookieStore.get('token'));
													window.open("#userhome",
															"_self");
													// set login user in a session

												}
												$scope.loginresponse = JSON
														.stringify({
															data : data
														});

												//                                        window.open("#profile/"
												//                                                + data.role, "_self");

												// $scope.$apply()
												// alert("Success : " +
												// JSON.stringify({data:
												// data}));

											}).error(
											function(data, status, headers,
													config) {
												alert("failure message: "
														+ JSON.stringify({
															data : data
														}));
											});

						}
					}

				});



myApp
		.controller(
				"registerController",
				function($scope, $rootScope, $http) {
					$rootScope.loginStatus = false;
					$scope.sendRegisterRequest = function() {
						// alert($scope.form_password + ":" +
						// $scope.form_user_name);
						// alert($scope.form_user_name);
						if ($scope.form_username === undefined
								|| $scope.form_username === "")
							$scope.errormessage = "Enter a valid Username";
						else if ($scope.form_password === undefined
								|| $scope.form_password === "")
							$scope.errormessage = "Enter a valid Password";
						else if ($scope.form_fullname === undefined
								|| $scope.form_fullname === "")
							$scope.errormessage = "Enter a valid Full Name";
						else if ($scope.form_password !== $scope.form_password_confirm)
							$scope.errormessage = "Passwords must match";
						else if ($scope.form_number === undefined
								|| $scope.form_number === "")
							$scope.errormessage = "Enter a valid Contact Number";
						else {
							$http(
									{
										url : 'rest/user/register',
										method : "POST",

										headers : {
											'Content-Type' : 'application/x-www-form-urlencoded'
										},
										transformRequest : function(obj) {
											var str = [];
											for ( var p in obj)
												str
														.push(encodeURIComponent(p)
																+ "="
																+ encodeURIComponent(obj[p]));
											return str.join("&");
										},
										data : {
											reg_username : $scope.form_username,
											reg_password : $scope.form_password,
											reg_email : $scope.form_username,
											reg_fullname : $scope.form_fullname,
											reg_number : $scope.form_number,
										},

									})
									.success(
											function(data, status, headers,
													config) {
												//				$scope.message = data;
												if (data.status === "failed") {
													$scope.errormessage = data.errorMsg;
												} else {
													$scope.errormessage = "";
		                                            $scope.loginStatus = true;
		                                            $scope.loginresponse = "User registration successfull";
		                                            alert($scope.loginresponse);
		                                            window.open("#login", "_self");
												}
											}).error(
											function(data, status, headers,
													config) {
												//   alert("failure message: " + JSON.stringify({data: data}));
											});
						}
					}

				});


myApp.controller("logoutController", function($scope, $rootScope, $http,
		$cookieStore) {
	$rootScope.loginStatus = false;
	$rootScope.allUsers = [];
	$cookieStore.put('username', "");
	$cookieStore.put('name', "");
	$cookieStore.put('email', "");
	$cookieStore.put('gender', "");
	$cookieStore.put('token', "");
	$cookieStore.put('role', "");
	$cookieStore.put('profile', "");
	window.open("#login", "_self");

});
myApp.controller("addbookController", function($scope, $rootScope, $http,   
		$cookieStore) {
	if ($cookieStore.get('token') === "") {
		$scope.access = false;
		window.open("#login", "_self");
	}
	$scope.sendAddBookRequest = function() {
		// alert($scope.form_password + ":" +
		// $scope.form_user_name); 
		if ($scope.form_title === undefined
				|| $scope.form_title === "")
			$scope.errormessage = "Enter a valid Book Title";
		else if ($scope.form_authors === undefined
				|| $scope.form_authors === "")
			$scope.errormessage = "Enter a valid Book Author";
		else if ($scope.form_period === undefined
				|| $scope.form_period === "")
			$scope.errormessage = "Enter a valid Duration";
		else if ($scope.form_price === undefined
				|| $scope.form_price === "")
			$scope.errormessage = "Enter a valid Book Price";
		else if ($scope.form_category === undefined
				|| $scope.form_category === "")
			$scope.errormessage = "Enter a valid Book Category";
		else if ($scope.form_description === undefined
				|| $scope.form_description === "")
			$scope.errormessage = "Enter a valid Book Description";
		else
			{
		$http(
				{
					url : 'rest/book/add',
					method : "POST",

					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					},
					transformRequest : function(obj) {
						var str = [];
						for ( var p in obj)
							str.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(obj[p]));
						return str.join("&");
					},
					data : {
						title : $scope.form_title,
						authors : $scope.form_authors,
						publisher : $scope.form_publisher,
						price : $scope.form_price,
						period : $scope.form_period,
						category : $scope.form_category,
						description : $scope.form_description,
						cover_page : $scope.form_coverpage.base64,
						token : $cookieStore.get("token"),
						user: $cookieStore.get("username"),
						quantity : $scope.form_quantity
					},

				}).success(function(data, status, headers, config) {

			$scope.response = data.msg;
			//				$scope.message = data;
			if (data.status === "failed") {
                $scope.errormessage = data.errorMsg;
            }else {
				$scope.errormessage = "";
				$rootScope.loginStatus = true;
			}
			$scope.loginresponse = JSON.stringify({
				data : data
			});
			alert(stringify({
				data : data
			}));

			//                                        $scope.loginresponse = "User registration successfull";
			//                                        window.open("#login", "_self");
			//				$scope.$apply()
			//				alert("Success : " + JSON.stringify({data: data}));

		}).error(function(data, status, headers, config) {
			console.log("failure message: " + JSON.stringify({
				data : data
			}));
		});
		//                    alert("Registration successful");
		//                    window.open("#login", "_self");
			}
	}

});

myApp.controller("userprofileController", function($scope, $http, $cookieStore,
		$rootScope, $routeParams) {
	//            $rootScope.myrole = $routeParams.type;
	if ($cookieStore.get('token') === "") {
		$scope.access = false;
		window.open("#login", "_self");
	}else {
		uname = $cookieStore.get("username");
		$scope.access = true;
		$http.get("rest/book/getUserBorrowedBooks?uname="+uname).then(function(response) {
			$scope.services = response.data;
			$allBooks = [];
			for (var i = 0; i < response.data.borrowedBooks.length; i++) {
				$allBooks[i] = JSON.parse(response.data.borrowedBooks[i]);
			}
			$rootScope.allbooks = $allBooks;
			//                 alert(JSON.stringify($allBooks));
			if($allBooks.length == 0)
				$scope.response = 'No books are found.';
		});
	}
});
myApp.controller("userprofile2Controller", function($scope, $http, $rootScope,
		$cookieStore, $routeParams) {
	//            $rootScope.myrole = $routeParams.type;
	if ($cookieStore.get('token') === "") {
		$scope.access = false;
		window.open("#login", "_self");
	} else {
		uname = $cookieStore.get("username");
		$scope.access = true;
		$http.get("rest/book/getUserUploadedBooks?uname="+uname).then(function(response) {
			$scope.services = response.data;
			$allBooks = [];
			for (var i = 0; i < response.data.allBooks.length; i++) {
				$allBooks[i] = JSON.parse(response.data.allBooks[i]);
			}
			$rootScope.allbooks = $allBooks;
			//                 alert(JSON.stringify($allBooks));
			if($allBooks.length == 0)
				$scope.response = 'No books are found.';
		});
	}
});


myApp.controller("setFeedbackController", function($scope, $rootScope, $http,   
		$cookieStore,$routeParams) {
	if ($cookieStore.get('token') === "") {
		$scope.access = false;
		window.open("#login", "_self");
	}
	$scope.sendFeedbackRequest = function() {
		// alert($scope.form_password + ":" +
		// $scope.form_user_name); 
		if ($scope.form_feedback === undefined
				|| $scope.form_feedback === "")
			$scope.errormessage = "Enter a valid Feedback";
		
		
		else
			{
		$http(
				{
					url : 'rest/book/addreview',
					method : "POST",

					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					},
					transformRequest : function(obj) {
						var str = [];
						for ( var p in obj)
							str.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(obj[p]));
						return str.join("&");
					},
					data : {
						touser : $routeParams.uname,
						feedback : $scope.form_feedback,
						token : $cookieStore.get("token"),
					},

				}).success(function(data, status, headers, config) {

			$scope.response = data.msg;
			//				$scope.message = data;
			if (data.status === "failed") {
                $scope.errormessage = data.errorMsg;
            }else {
				$scope.errormessage = "";
				$scope.loginresponse = "Review posted succesfully";
				// $rootScope.loginStatus = true;
			}
			$scope.loginresponse = JSON.stringify({
				data : data
			});
			alert(stringify({
				data : data
			}));

			//                                        $scope.loginresponse = "User registration successfull";
			//                                        window.open("#login", "_self");
			//				$scope.$apply()
			//				alert("Success : " + JSON.stringify({data: data}));

		}).error(function(data, status, headers, config) {
			alert("failure message: " + JSON.stringify({
				data : data
			}));
		});
		//                    alert("Registration successful");
		//                    window.open("#login", "_self");
			}
	}

});


myApp.controller("getFeedbackController", function($scope, $http, $rootScope,
		$cookieStore, $routeParams) {
	//            $rootScope.myrole = $routeParams.type;
	if ($cookieStore.get('token') === "") {
		$scope.access = false;
		window.open("#login", "_self");
	} else {
		touser = $routeParams.uname;
		$scope.access = true;
		$http.get("rest/book/getreviews?touser="+touser).then(function(response) {
			$scope.services = response.data;
			$userReviews = [];
			for (var i = 0; i < response.data.userReviews.length; i++) {
				$userReviews[i] = JSON.parse(response.data.userReviews[i]);
			}
			$rootScope.userReviews = $userReviews;
			//                 alert(JSON.stringify($allBooks));
		});
	}
});

myApp.controller("contactcontroller", function($scope, $http, $rootScope,
		$cookieStore, $routeParams) {
	if ($cookieStore.get('token') === "") {
		$scope.access = false;
		window.open("#login", "_self");
	} else {
		
		/*uname = $cookieStore.get("username");*/
		uname = $routeParams.uname;
		$scope.access = true;
		$http.get("rest/book/contactdetails?uname="+uname).then(function(response) {
			$scope.services = response.data;
			$contactdetails = [];
			for (var i = 0; i < response.data.contactdetails.length; i++) {
				$contactdetails[i] = JSON.parse(response.data.contactdetails[i]);
			}
			$rootScope.contactdetails = $contactdetails;
		});
	}
});

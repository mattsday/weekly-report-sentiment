function loadCustomerList(jsonData) {
	jsonData.forEach(function(customer) {
		$("#cust_select").append(
				$('<option></option>').val(customer).html(customer));
	})
	updateDropDowns();
	$('#cust_select').trigger("chosen:updated");
}

function loadDateList(jsonData) {
	jsonData.forEach(function(date) {
		$("#date_select").append(
				$('<option></option>').val(date).html(date));
	})
	updateDropDowns();
	$('#date_select').trigger("chosen:updated");
}

$(document).ready(function() {
	startup()
});

function updateDropDowns() {
	var a;
	var customer = "_all";
	var date = "_all";
	try {
		var a = JSON
				.parse(decodeURIComponent(window.location.hash.substring(1)));
		customer = a.customer;
		date = a.date;
	} catch (e) {
		a = new Object();
	}
	$('select[name^="customer"] option:selected').attr("selected", null);
	$('select[name^="customer"] option[value="' + customer + '"]').attr(
			"selected", "selected");
	$('#cust_select').trigger("chosen:updated");
	
	$('select[name^="date"] option:selected').attr("selected", null);
	$('select[name^="date"] option[value="' + date + '"]').attr(
			"selected", "selected");
	$('#date_select').trigger("chosen:updated");
}

function startup() {
	// Update the charts and dropdown whenever the window hash changes
	$(window).bind('hashchange', function() {
		loadCharts();
		updateDropDowns();
	});
	// Make dropdown trigger location changes
	$("#cust_select").change(function() {
		var customer = $("#cust_select option:selected").val();
		var a;
		try {
			var a = JSON
			.parse(decodeURIComponent(window.location.hash.substring(1)));
		} catch (e) {
			a = new Object();
		}
		if (!a.date) {
			a.date = "_all"
		}
		a.customer = customer;
		location.hash = JSON.stringify(a);
	});
	$("#date_select").change(function() {
		var date = $("#date_select option:selected").val();
		var a;
		try {
			var a = JSON
			.parse(decodeURIComponent(window.location.hash.substring(1)));
		} catch (e) {
			a = new Object();
		}
		if (!a.customer) {
			a.customer = "_all"
		}
		a.date = date;
		location.hash = JSON.stringify(a);
	});

	// Load customer list
	$.when($.ajax({
		url : "/v1/customers",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadCustomerList(jsonData);
	});
	$.when($.ajax({
		url : "/v1/dates",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadDateList(jsonData);
	});
}
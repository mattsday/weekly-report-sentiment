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
		$("#date_select").append($('<option></option>').val(date).html(date));
	})
	updateDropDowns();
	$('#date_select').trigger("chosen:updated");
}

function loadPlaysList(jsonData) {
	jsonData.forEach(function(play) {
		$("#play_select").append($('<option></option>').val(play).html(play));
	})
	updateDropDowns();
	$('#play_select').trigger("chosen:updated");
}

function loadSentimentList(jsonData) {
	jsonData.forEach(function(sentiment) {
		$("#sentiment_select").append(
				$('<option></option>').val(sentiment).html(sentiment));
	})
	updateDropDowns();
	$('#sentiment_select').trigger("chosen:updated");
}

function loadPaList(jsonData) {
	jsonData.forEach(function(pa) {
		$("#pa_select").append($('<option></option>').val(pa).html(pa));
	})
	updateDropDowns();
	$('#pa_select').trigger("chosen:updated");
}

function loadCategoryList(jsonData) {
	jsonData.forEach(function(category) {
		$("#category_select").append(
				$('<option></option>').val(category).html(category));
	})
	updateDropDowns();
	$('#category_select').trigger("chosen:updated");
}

$(document).ready(function() {
	startup()
});

function updateDropDowns() {
	var a = getHash();
	var customer = "_all";
	var date = "_all";
	var sentiment = "_all";
	var play = "_all";
	try {
		var a = JSON
				.parse(decodeURIComponent(window.location.hash.substring(1)));
		customer = a.customer;
		date = a.date;
	}
	catch (e) {
		a = new Object();
		a.customer = customer;
		a.date = date;
	}
	$('select[name^="customer"] option:selected').attr("selected", null);
	$('select[name^="customer"] option[value="' + customer + '"]').attr(
			"selected", "selected");
	$('#cust_select').trigger("chosen:updated");

	$('select[name^="date"] option:selected').attr("selected", null);
	$('select[name^="date"] option[value="' + date + '"]').attr("selected",
			"selected");
	$('#date_select').trigger("chosen:updated");

	$('select[name^="sentiment"] option:selected').attr("selected", null);
	$('select[name^="sentiment"] option[value="' + sentiment + '"]').attr(
			"selected", "selected");
	$('#sentiment_select').trigger("chosen:updated");

	$('select[name^="play"] option:selected').attr("selected", null);
	$('select[name^="play"] option[value="' + play + '"]').attr("selected",
			"selected");
	$('#play_select').trigger("chosen:updated");
}

function getHash() {
	var a;
	try {
		var a = JSON
				.parse(decodeURIComponent(window.location.hash.substring(1)));
	}
	catch (e) {
		a = new Object();
	}
	if (!a.date) {
		a.date = "_all"
	}
	return a;
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
		var a = getHash();
		a.customer = customer;
		location.hash = JSON.stringify(a);
	});
	$("#date_select").change(function() {
		var date = $("#date_select option:selected").val();
		var a = getHash();
		a.date = date;
		location.hash = JSON.stringify(a);
	});
	$("#sentiment_select").change(function() {
		var sentiment = $("#sentiment_select option:selected").val();
		var a = getHash();
		a.sentiment = sentiment;
		location.hash = JSON.stringify(a);
	});
	$("#play_select").change(function() {
		var play = $("#play_select option:selected").val();
		var a = getHash();
		a.sales_play = play;
		location.hash = JSON.stringify(a);
	});
	$("#pa_select").change(function() {
		var pa = $("#pa_select").val();
		var a = getHash();
		a.pas = pa;
		location.hash = JSON.stringify(a);
	});
	$("#category_select").change(function() {
		var category = $("#category_select").val();
		var a = getHash();
		a.category = category;
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
	// Load dates
	$.when($.ajax({
		url : "/v1/dates",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadDateList(jsonData);
	});

	// Load plays
	$.when($.ajax({
		url : "/v1/plays",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadPlaysList(jsonData);
	});

	// Load sentiment
	$.when($.ajax({
		url : "/v1/sentiment",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadSentimentList(jsonData);
	});

	// Load pas
	$.when($.ajax({
		url : "/v1/pas",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadPaList(jsonData);
	});

	// Load categories
	$.when($.ajax({
		url : "/v1/categories",
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		loadCategoryList(jsonData);
	});

	$("#cust_select_form").on("submit", function(e) {
		e.preventDefault();
		$.when($.ajax({
			url : "/v1/update",
			dataType : "text",
			async : true
		})).done(function(response) {
			loadCharts();
			updateDropDowns();
		});
	});

}
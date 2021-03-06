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
	if (a.customer) {
		$("#cust_select").val(a.customer);
		$('#cust_select').trigger("chosen:updated");
	}
	if (a.date) {
		$("#date_select").val(a.date);
		$('#date_select').trigger("chosen:updated");
	}
	if (a.sales_play) {
		$("#play_select").val(a.sales_play);
		$('#play_select').trigger("chosen:updated");
	}
	if (a.pas) {
		$("#pa_select").val(a.pas);
		$('#pa_select').trigger("chosen:updated");
	}
	if (a.sentiment) {
		$("#sentiment_select").val(a.sentiment);
		$('#sentiment_select').trigger("chosen:updated");
	}
	if (a.category) {
		$("#category_select").val(a.category);
		$('#category_select').trigger("chosen:updated");
	}
	if (a.q) {
		$("#q").val(a.q);
	}
}

function getHash() {
	var a;
	try {
		var a = JSON
				.parse(decodeURIComponent(window.location.hash.substring(1)));
	} catch (e) {
		a = new Object();
		a.date = [];
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
		var customer = $("#cust_select").val();
		var a = getHash();
		a.customer = customer;
		location.hash = JSON.stringify(a);
	});
	$("#date_select").change(function() {
		var date = $("#date_select").val();
		var a = getHash();
		a.date = date;
		location.hash = JSON.stringify(a);
	});
	$("#sentiment_select").change(function() {
		var sentiment = $("#sentiment_select").val();
		var a = getHash();
		a.sentiment = sentiment;
		location.hash = JSON.stringify(a);
	});
	$("#play_select").change(function() {
		var play = $("#play_select").val();
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
	$("#andor_select").change(function() {
		var category = $("#andor_select").val();
		var a = getHash();
		a.andor = category;
		location.hash = JSON.stringify(a);
	});
	$('#q').on('input', function() {
		var query = $("#q").val();
		var a = getHash();
		a.q = query;
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

	function updateGraphRefreshing(time) {
		// Only do it whilst we're actually refreshing
		if ($("#refresh").prop('disabled') == true) {
			loadCharts();
			updateDropDowns();
			setTimeout(updateGraphRefreshing, time, time);
		}
	}

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
		$("#refresh").prop('value', 'Updating...');
		$('#refresh').prop('disabled', true);
		// Refresh the graph every 5 seconds
		updateGraphRefreshing(1000);
		$.when($.ajax({
			url : "/v1/update",
			dataType : "text",
			async : true
		})).done(function(response) {
			$('#refresh').prop('disabled', false);
			$("#refresh").prop('value', 'Update');
			loadCharts();
			updateDropDowns();

		});
	});

}
google.charts.load('current', {
	callback : loadCharts,
	packages : [ 'corechart', 'table' ]
});

function drawSentimentChart(jsonData) {
	var data = new google.visualization.DataTable(jsonData);
	var options = {
		title : 'Sentiment over time',
		curveType : 'function',
		legend : {
			position : 'bottom'
		},
		pointSize : 5,
		vAxis : {
			minValue : -1,
			maxValue : 1
		}
	};

	var chart = new google.visualization.LineChart(document
			.getElementById('curve_chart'));
	google.visualization.events.addListener(chart, 'click', clickHandler);
	function clickHandler(e) {
		var m = e.targetID.match(/.*#\d+#(\d+)/);
		if (m) {
			var date = data.getValue(parseInt(m[1]), 0);
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
		}
	}
	chart.draw(data, options);
}

function drawReportTable(jsonData) {
	var data = new google.visualization.DataTable(jsonData);
	var table = new google.visualization.Table(document
			.getElementById('report_table'));
	table.draw(data, {
		showRowNumber : true,
		width : '100%',
		height : '100%'
	});
}

function loadCharts() {
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

	var sentimentChartUrl = "/v1/chart/sentiment";
	var tableUrl = "/v1/chart/table"

	// Is the anchor pointing at a date?
	if (customer.match(/\d{4}-\d{2}-\d{2}/)) {
		// Do nothing for now
	} else if ((customer != "_all") && (customer.length > 0)) {
		// Assume it's a customer
		sentimentChartUrl = "/v1/chart/sentiment/customer/" + customer;
		tableUrl = "/v1/chart/table/customer/" + customer;
	}

	$.when($.ajax({
		url : sentimentChartUrl,
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		drawSentimentChart(jsonData);
	});

	$.when($.ajax({
		url : tableUrl,
		dataType : "json",
		async : true
	})).done(function(jsonData) {
		drawReportTable(jsonData);
	});
}
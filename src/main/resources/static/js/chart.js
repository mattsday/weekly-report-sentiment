google.charts.load('current', {
	callback : loadCharts,
	packages : [ 'corechart', 'table' ]
});

function drawDateChart(jsonData) {
	var data = new google.visualization.DataTable(jsonData);
	var options = {
		title : 'Sales Plays',
		hAxis : {
			title : 'Entry'
		},
		vAxis : {
			minValue : -1,
			maxValue : 1,
			title : 'Sales Play'
		},
		bubble : {
			textStyle : {
				fontSize : 11
			}
		}

	};
	var chart = new google.visualization.BubbleChart(document
			.getElementById('curve_chart'));

	// google.visualization.events.addListener(chart, 'click', clickHandler);

	chart.draw(data, options);
}

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
			var a = getHash();
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
	var a = getHash();
	var customer = "_all";
	var date = "_all";
	try {
		var a = getHash();
		customer = a.customer;
		date = a.date;
	}
	catch (e) {
		a = new Object();
		a.customer = customer;
		a.date = date;
	}

	var sentimentChartUrl = "/v1/chart/sentiment";
	var tableUrl = "/v1/chart/table"
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content")
	if (a.date == "_all") {
		delete a.date;
	}
	if (a.customer == "_all") {
		delete a.customer;
	}
	if (a.sales_play == "_all") {
		delete a.sales_play;
	}
	if (a.pas == "_all") {
		delete a.pas;
	}
	if (a.sentiment == "_all") {
		delete a.sentiment;
	}
	if (a.category == "_all") {
		delete a.category;
	}
	var dataString = JSON.stringify(a);

	if (date != "_all") {
		// Draw a different type of chart:

		$.when($.ajax({
			type : "POST",
			url : "/v1/chart/date",
			contentType : 'application/json',
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			processData : false,
			data : dataString,
			dataType : "json"
		})).done(function(jsonData) {
			drawDateChart(jsonData);
		});
	}
	else {
		// Draw the usual chart
		$.when($.ajax({
			type : "POST",
			url : "/v1/chart/sentiment",
			contentType : 'application/json',
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			processData : false,
			data : dataString,
			dataType : "json"
		})).done(function(jsonData) {
			drawSentimentChart(jsonData);
		});
	}
	$.when($.ajax({
		type : "POST",
		url : "/v1/chart/table",
		contentType : 'application/json',
		beforeSend : function(request) {
			request.setRequestHeader(header, token);
		},
		processData : false,
		data : dataString,
		dataType : "json"
	})).done(function(jsonData) {
		drawReportTable(jsonData);
	});
}
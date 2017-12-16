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
	var date = a.date;
	date = a.date;
	var sentimentChartUrl = "/v2/chart/sentiment";
	var tableUrl = "/v2/chart/table"
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content")
	var dataString = JSON.stringify(a);

	if (date.length == 1) {
		// Draw a different type of chart:

		$.when($.ajax({
			type : "POST",
			url : "/v2/chart/date",
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
			url : "/v2/chart/sentiment",
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
		url : "/v2/chart/table",
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
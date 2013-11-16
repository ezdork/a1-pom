function displayStockList(stockCode) {

	var containerName = 'container'+stockCode;
	appendContainer(containerName);
	
	var analysisData = [];
	$.getJSON('getAnalysisData.do?stockCode=' + stockCode, function(data) {
		for ( var i = 0; i < data.length; i++) {

			var price = data[i]['price']; 
			var obj = {
				x :  parseToDateTime(data[i]['date']),
				title : price < 0 ? '<a style="font-size:12px">買</a>' : '<a style="font-size:12px">賣</a>',
				text : price < 0 ? '買進點: '+(-1*price) : '賣出點: '+price,
				color : price < 0 ? '#FF5050' : '#50FF50'
			};
			analysisData.push(obj);
		}
		
		$.getJSON('getData.do?stockCode=' + stockCode, function(data) {

			// split the data set into ohlc and volume
			var ohlc = [], volume = [], dataLength = data.length;

			for ( var i = 0; i < dataLength; i++) {
				var datetime = parseToDateTime(data[i]['date']);
				ohlc.push([ datetime, // the date
				data[i]['open'], // open
				data[i]['high'], // high
				data[i]['low'], // low
				data[i]['close'] // close
				]);

				volume.push([ datetime, // the date
				data[i]['volumn'] // the volume
				]);
			}

			// create the chart
			$('#'+containerName).highcharts('StockChart', {

				rangeSelector : {
					selected : 1
				},
				title : {
					text : stockCode + ' 買賣歷史'
				},
				yAxis : [ {
					title : {
						text : 'K線'
					},
					height : 200,
					lineWidth : 2
				}, {
					title : {
						text : '成交量'
					},
					top : 300,
					height : 100,
					offset : 0,
					lineWidth : 2
				} ],

				tooltip : {
					xDateFormat : '%Y-%m-%d',
					shared : true
				},
				
				plotOptions: {
					candlestick:{
					color: '#6adc3a',
					upColor: '#f40117'
							
				}},

				series : [ {
					type : 'candlestick',
					name : stockCode,
					data : ohlc,
					id : 'flags',
					dataGrouping:{enabled:false}
				}, {
					type : 'column',
					name : 'Volume',
					data : volume,
					yAxis : 1,
					dataGrouping:{enabled:false}
				}, {
					type : 'flags',
					data : analysisData,
					onSeries : 'flags',
					shape : 'circlepin',
					width : 16
				}, {
					type : 'flags',
					data : [],
					width : 15,
					shape : 'squarepin'

				}, {
					type : 'flags',
					data : [],
					width : 5,
					onSeries : '',
					shape : 'circlepin'

				} ]
			});
		});
	});

	
}

function parseToDateTime(str){
	var yyyy = str.substring(0, 4) - 0;
	var MM = str.substring(4, 6) - 1;
	var dd = str.substring(6, 8) - 0;
	return Date.UTC(yyyy, MM, dd);
}

function appendContainer(containerName){
	if($('#'+containerName).length == 0){
		$('#content').append('<div id="'+containerName+'" style="height: 500px; min-width: 500px"></div>');
	}
}

$(function() {
	accounting.settings = {
		currency: {
			symbol : "$",   // default currency symbol is '$'
			format: "%s%v", // controls output: %s = symbol, %v = value/number (can be object: see below)
			decimal : ".",  // decimal point separator
			thousand: ",",  // thousands separator
			precision : 0   // decimal places
		},
		number: {
			precision : 0,  // default precision on numbers is 0
			thousand: ",",
			decimal : "."
		}
	};
	var totalEarnMoney = 0;
	$.getJSON('getAllStockOrderByEarnMoney.do', function(data) {
		for ( var i = 0; i < data.length; i++) {
			var code = $.trim(data[i]['code']);
			totalEarnMoney = totalEarnMoney + data[i]['earnMoney'];
			var earnMoney = accounting.formatMoney(data[i]['earnMoney']);
			var html = '<input type="button" onclick="displayStockList(\''+code+'\')" value="股票代號('+code+'), 獲利:'+earnMoney+'">';
			$('#content').append(html);
			appendContainer('container'+code);
		}
		$('#totalEarnMoney').val(accounting.formatMoney(totalEarnMoney));
	});
});
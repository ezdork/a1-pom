function displayStockList(stockCode, needFocus, event) {

	if (event && event.keyCode != 13) {
		return false; // returning false will prevent the event from bubbling
		// up.
	}

	if ($('#container' + stockCode + 'Button').length == 0) {
		appendButton(stockCode, '', '');
	}
	var containerName = 'container' + stockCode;
	appendContainer(containerName, needFocus);

	var analysisData = [];

	$.getJSON('getAnalysisData.do?stockCode=' + stockCode, function(analysisResultData) {

		for ( var i = 0; i < analysisResultData.length; i++) {

			var buyDate = analysisResultData[i]['buyDate'];
			if (buyDate) {
				var obj = {
					x : parseToDateTime(analysisResultData[i]['buyDate']),
					title : '<a style="font-size:12px">買</a>',
					text : '買進點: ' + analysisResultData[i]['buyPrice'],
					color : '#FF5050'
				};
				analysisData.push(obj);
			}

			var sellDate = analysisResultData[i]['sellDate'];
			if (sellDate) {
				obj = {
					x : parseToDateTime(sellDate),
					title : '<a style="font-size:12px">賣</a>',
					text : '賣出點: ' + analysisResultData[i]['sellPrice'],
					color : '#50FF50'
				};
				analysisData.push(obj);
			}

		}

		$.getJSON('getData.do?stockCode=' + stockCode, function(data) {

			// split the data set into ohlc and volume
			var ohlc = [], ma5 = [], ma10 = [], bias = [], high240 = [], volume = [], dataLength = data.length;

			if (dataLength == 0) {
				$('#container' + stockCode + 'Button').remove();
				$('#container' + stockCode).remove();
				alert('沒有' + stockCode + '資料');
				return;
			}

			for ( var i = 0; i < dataLength; i++) {
				var datetime = parseToDateTime(data[i]['date']);
				ohlc.push([ datetime, // the date
				data[i]['open'], // open
				data[i]['high'], // high
				data[i]['low'], // low
				data[i]['close'] // close
				]);

				ma5.push([ datetime, // the date
				data[i]['ma5'] // the volume
				]);

				ma10.push([ datetime, // the date
				data[i]['ma10'] // the volume
				]);

				bias.push([ datetime, // the date
				(((data[i]['ma5'] / data[i]['ma10'])) * 1) // the volume
				]);

				high240.push([ datetime, // the date
				data[i]['high240'] // the volume
				]);

				volume.push([ datetime, // the date
				data[i]['volumn'] // the volume
				]);
			}

			// create the chart
			$('#' + containerName).highcharts('StockChart', {

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

				plotOptions : {
					candlestick : {
						color : '#6adc3a',
						upColor : '#f40117'
					}
				},

				series : [ {
					type : 'candlestick',
					name : stockCode,
					data : ohlc,
					id : 'flags',
					dataGrouping : {
						enabled : false
					}
				}, {
					type : 'column',
					name : 'Volume',
					data : volume,
					yAxis : 1,
					dataGrouping : {
						enabled : false
					}
				}, {
					type : 'line',
					name : '前一天5日均線',
					data : ma5,
					color : '#1947A3',
					yAxis : 0
				}, {
					type : 'line',
					name : '前一天10日均線',
					data : ma10,
					color : '#969696',
					yAxis : 0
				}, {
					type : 'line',
					name : '一年最高收盤價',
					data : high240,
					color : '#DAA520',
					yAxis : 0
				}, {
					type : 'flags',
					data : analysisData,
					onSeries : 'flags',
					shape : 'circlepin',
					width : 16
				} ]
			});
		});

	});

}

function parseToDateTime(str) {
	var yyyy = str.substring(0, 4) - 0;
	var MM = str.substring(4, 6) - 1;
	var dd = str.substring(6, 8) - 0;
	return Date.UTC(yyyy, MM, dd);
}

function appendContainer(containerName, needFocus) {
	if ($('#' + containerName).length == 0) {
		$('#content').append('<div id="' + containerName + '" style="height: 500px; min-width: 500px"></div>');
	}
	if (needFocus) {
		$('#' + containerName + 'Button').focus();
	}
}

function appendButton(code, formatEarnMoney, formatFee) {
	var html = '<input id="container' + code + 'Button" type="button" onclick="displayStockList(\'' + code + '\')" value="股票代號(' + code + ')\t淨利(扣除手續費):' + formatEarnMoney + '\t手續費:' + formatFee
			+ '">';
	$('#content').append(html);
}

$(function() {
	accounting.settings = {
		currency : {
			symbol : "$", // default currency symbol is '$'
			format : "%s%v", // controls output: %s = symbol, %v =
			// value/number (can be object: see below)
			decimal : ".", // decimal point separator
			thousand : ",", // thousands separator
			precision : 0
		// decimal places
		},
		number : {
			precision : 0, // default precision on numbers is 0
			thousand : ",",
			decimal : "."
		}
	};

	// 買進價*1.425/1000+賣出價*1.425/1000+賣出價*3/1000
	var totalEarnMoney = 0;
	var totalFee = 0;

	$.getJSON('getAllStockOrderByEarnMoney.do', function(data) {
		for ( var i = 0; i < data.length; i++) {
			var code = $.trim(data[i]['code']);
			var earnMoney = data[i]['earnMoney'];
			var fee = data[i]['fee'];

			if (earnMoney) {
				totalEarnMoney = totalEarnMoney + earnMoney;
			}
			if (fee) {
				totalFee = totalFee + fee;
			}

			var formatEarnMoney = accounting.formatMoney(earnMoney - fee);
			var formatFee = accounting.formatMoney(fee);

			appendButton(code, formatEarnMoney, formatFee);
			appendContainer('container' + code);

		}

		$('#totalEarnMoney').val(accounting.formatMoney(totalEarnMoney - totalFee));
		$('#totalFee').val(accounting.formatMoney(totalFee));

	});
});
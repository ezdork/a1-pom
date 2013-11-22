function getWantedStockList(date, clearCache, event) {

	if (event && event.keyCode != 13) {
		return false; // returning false will prevent the event from bubbling
						// up.
	}

	$('#content').html('loading...');
	var url = 'getWantedStockList.do?date=' + date;
	if (clearCache) {
		url += '&clearCache=true';
	}
	$.getJSON(url, function(data) {
		if (data) {
			$('#content').empty();
			appendTable('resultList1', data);
			appendTable('resultList2', data);
			appendTable('resultList3', data);
			appendTable('resultList5', data);
			appendTable('resultList10', data);
			appendTable('resultListAll', data);
			appendCurrentBuyTable('currentBuyList', data);
		}
	});
}

function appendCurrentBuyTable(tableId, data) {
	var msg = '持有股票';
	$('#content').append(
			'<table id="' + tableId + '" ><THEAD><tr><td colspan="9">' + msg
					+ '</td></tr><tr><td>股票代號</td><td>5日均</td><td>跌停價</td><td>張數</td><td>購買日期</td><td>購買價</td><td>現價</td><td>淨利</td><td>手續費</td></tr></THEAD></table>');
	var $table = $('#' + tableId);
	var list = data[tableId];
	var length = list.length;
	$table.append('<TBODY>');
	for ( var i = 0; i < length; i++) {
		
		var earnMoney = list[i]['buyAmount'] * Math.floor((list[i]['nowPrice'] - list[i]['buyPrice']) * 1000);
		var fee = list[i]['buyAmount'] * list[i]['nowPrice'] * 1.425 + list[i]['buyAmount'] * list[i]['nowPrice'] * 4.425;
		
		var html = (earnMoney - fee)>0 ? '<tr style="color:red">' : '<tr style="color:green">';
		html += '<td>' + list[i]['code'] + '</td>';
		html += '<td>' + list[i]['ma5'] + '</td>';
		html += '<td>' + list[i]['lowestPrice'] + '</td>';
		html += '<td>' + list[i]['buyAmount'] + '</td>';
		html += '<td>' + list[i]['buyDate'] + '</td>';
		html += '<td>' + list[i]['buyPrice'] + '</td>';
		html += '<td>' + list[i]['nowPrice'] + '</td>';
		html += '<td style="text-align:right">' + accounting.formatMoney(earnMoney - fee) + '</td>';
		html += '<td style="text-align:right">' + accounting.formatMoney(fee) + '</td>';
		html += '</tr>';
		$table.append(html);
	}
	$table.append('</TBODY>');
}

function appendTable(tableId, data) {
	var msg = '接近' + tableId.substring(10) + '年新高';
	$('#content').append('<table id="' + tableId + '" ><THEAD><tr><td colspan="3">' + msg + '</td></tr><tr><td>股票代號</td><td>漲停價</td><td>張數</td></tr></THEAD></table>');
	var $table = $('#' + tableId);
	var list = data[tableId];
	var length = list.length;
	$table.append('<TBODY>');

	for ( var i = 0; i < length; i++) {
		var html = list[i]['alreadyBuy'] ? '<tr style="color:blue">' : '<tr>';
		html += '<td>' + list[i]['code'] + '</td>';
		html += '<td>' + list[i]['nextHighestPrice'] + '</td>';
		html += '<td>' + list[i]['buyAmount'] + '</td>';
		html += '</tr>';
		$table.append(html);
	}
	$table.append('</TBODY>');
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
	$.getJSON('getLatestStockDate.do', function(data) {
		if (data) {
			$('#date').val(data);
			getWantedStockList(data);
		}
	});
});
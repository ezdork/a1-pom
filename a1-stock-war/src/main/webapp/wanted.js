function hideAlreadyBuy() {
	$('#hideAlreadyBuy').is(':checked') ? $('.already_buy').hide() : $('.already_buy').show();
}

function hideNoBuy() {
	$('#hideNoBuy').is(':checked') ? $('.no_buy').hide() : $('.no_buy').show();
}

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
			appendCurrentBuyTable('currentBuyList', data, date);
			appendCurrentSellTable('currentSellList', data, date);
			
			hideAlreadyBuy();
			hideNoBuy();
		}
	});
}

function appendCurrentSellTable(tableId, data, date) {
	var msg = date + '賣出股票';
	$('#content').append(
			'<table id="' + tableId + '" ><THEAD><tr><td colspan="10">' + msg + '</td></tr><tr>' + '<td>名稱</td>' + '<td>股票代號</td>' + '<td>購買張數</td>' + '<td>購買日期</td>' + '<td>購買價</td>'
					+ '<td>賣出張數</td>' + '<td>賣出日期</td>' + '<td>賣出價</td>' + '<td>淨利</td>' + '<td>手續費</td>' + '</tr></THEAD></table>');
	var $table = $('#' + tableId);
	var list = data['currentBuyList'];
	var length = list.length;

	var total = 0;
	$table.append('<TBODY>');
	for ( var i = 0; i < length; i++) {
		if (list[i]['sellDate'] == date) {
			var earnMoney = list[i]['buyAmount'] * Math.floor((list[i]['sellPrice'] - list[i]['buyPrice']) * 1000);
			var fee = list[i]['buyAmount'] * list[i]['buyPrice'] * 1.425 + list[i]['sellAmount'] * list[i]['sellPrice'] * 4.425;

			total += (earnMoney - fee);
			var html = (earnMoney - fee) > 0 ? '<tr style="color:red">' : '<tr style="color:green">';
			if (list[i]['buyAmount'] == 0) {
				html = '<tr style="color:#53B4EE">';
			}
			var code = $.trim(list[i]['code']);
			html += '<td>' + stockMap[code] + '</td>';
			html += '<td>' + code + '</td>';
			html += '<td>' + list[i]['buyAmount'] + '</td>';
			html += '<td>' + list[i]['buyDate'] + '</td>';
			html += '<td>' + list[i]['buyPrice'] + '</td>';
			html += '<td>' + list[i]['sellAmount'] + '</td>';
			html += '<td>' + list[i]['sellDate'] + '</td>';
			html += '<td>' + list[i]['sellPrice'] + '</td>';
			html += '<td style="text-align:right">' + accounting.formatMoney(earnMoney - fee) + '</td>';
			html += '<td style="text-align:right">' + accounting.formatMoney(fee) + '</td>';
			html += '</tr>';
			$table.append(html);
		}
	}
	var html = total > 0 ? '<tr style="color:red">' : '<tr style="color:green">';
	html += '<td colspan="10"> 總淨利: ' + accounting.formatMoney(total) + '</td>';
	$table.append(html);
	$table.append('</TBODY>');
}

function appendCurrentBuyTable(tableId, data, date) {
	var msg = date + '持有股票';
	$('#content').append(
			'<table id="' + tableId + '" ><THEAD><tr><td colspan="10">' + msg
					+ '</td></tr><tr><td>名稱</td><td>股票代號</td><td>5日均</td><td>跌停價</td><td>張數</td><td>購買日期</td><td>購買價</td><td>現價</td><td>淨利</td><td>手續費</td></tr></THEAD></table>');
	var $table = $('#' + tableId);
	var list = data[tableId];
	var length = list.length;

	var total = 0;
	$table.append('<TBODY>');
	for ( var i = 0; i < length; i++) {
		if (list[i]['sellDate'] == date) {
			continue;
		}
		var earnMoney = list[i]['buyAmount'] * Math.floor((list[i]['nowPrice'] - list[i]['buyPrice']) * 1000);
		var fee = list[i]['buyAmount'] * list[i]['nowPrice'] * 1.425 + list[i]['buyAmount'] * list[i]['nowPrice'] * 4.425;

		total += (earnMoney - fee);
		var html = (earnMoney - fee) > 0 ? '<tr style="color:red">' : '<tr style="color:green">';
		if (list[i]['buyAmount'] == 0) {
			html = '<tr style="color:#53B4EE">';
		}
		var code = $.trim(list[i]['code']);
		html += '<td>' + stockMap[code] + '</td>';
		html += '<td>' + code + '</td>';
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
	var html = total > 0 ? '<tr style="color:red">' : '<tr style="color:green">';
	html += '<td colspan="10"> 總淨利: ' + accounting.formatMoney(total) + '</td>';
	$table.append(html);
	$table.append('</TBODY>');
}

function appendTable(tableId, data) {
	var msg = '接近' + tableId.substring(10) + '年新高股票';
	$('#content').append('<table id="' + tableId + '" ><THEAD><tr><td colspan="4">' + msg + '</td></tr><tr><td>名稱</td><td>股票代號</td><td>漲停價</td><td>張數</td></tr></THEAD></table>');
	var $table = $('#' + tableId);
	var list = data[tableId];
	var length = list.length;
	$table.append('<TBODY>');

	for ( var i = 0; i < length; i++) {
		var html = list[i]['alreadyBuy'] ? '<tr class="already_buy" style="color:blue">' : '<tr>';
		if (list[i]['buyAmount'] == 0) {
			html = '<tr class="no_buy" style="color:#53B4EE">';
		}
		var code = $.trim(list[i]['code']);
		html += '<td>' + stockMap[code] + '</td>';
		html += '<td>' + code + '</td>';
		html += '<td>' + list[i]['nextHighestPrice'] + '</td>';
		html += '<td>' + list[i]['buyAmount'] + '</td>';
		html += '</tr>';
		$table.append(html);
	}
	$table.append('</TBODY>');
}

var stockMap = {};
$(function() {

	$.getJSON('selectAllStockName.do', function(data) {
		for ( var i = 0; i < data.length; i++) {
			try {
				var code = $.trim(data[i]['code']);
				// var name = $.trim(data[i]['name']);
				var name = decodeURIComponent($.trim(data[i]['name']));
				stockMap[code] = name;
			} catch (e) {
			}
		}
	});

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
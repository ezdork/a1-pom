function getWantedStockList(date, clearCache, event) {

	if(event && event.keyCode != 13){
		return false; // returning false will prevent the event from bubbling up.
	}

	$('#content').html('loading...');
	var url = 'getWantedStockList.do?date=' + date;
	if(clearCache){
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
		}
	});
}

function appendTable(tableId, data) {
	var msg = '接近' + tableId.substring(10) + '年新高';
	$('#content').append('<table id="' + tableId + '" ><THEAD><tr><td colspan="3">' + msg + '</td></tr><tr><td>股票代號</td><td>漲停價</td><td>張數</td></tr></THEAD></table>');
	var $table = $('#' + tableId);
	var list = data[tableId];
	var length = list.length;
	$table.append('<TBODY>');
	for ( var i = 0; i < length; i++) {
		var html = '<tr>';
		html += '<td>' + list[i]['code'] + '</td>';
		html += '<td>' + list[i]['nextHighestPrice'] + '</td>';
		html += '<td>' + list[i]['buyAmount'] + '</td>';
		html += '</tr>';
		$table.append(html);
	}
	$table.append('</TBODY>');
}

$(function() {
	$.getJSON('getLatestStockDate.do', function(data) {
		if (data) {
			$('#date').val(data);
			getWantedStockList(data);
		}
	});
});
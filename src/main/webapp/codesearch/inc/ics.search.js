var cacheClear = false;
var pj = null;
var q = null;
      
$(function(){

  pj = jQuery("input[id=pj]:checked").val();
  q = jQuery("input[id=q]").val();

  var maintab =$('#tabs').tabs({
    add: function(e, ui) {
      var url = $.data(ui.tab, 'load.tabs');
      var fileName = $(ui.tab).parents('li:first').find('a span:first').text();
      $(ui.tab).parents('li:first').find('a span:first').dblclick(function() {
    	  jQuery('#internalSourceView[name="' + fileName + '"]').animate({scrollTop:0}, 'slow');
      });
      $(ui.tab).parents('li:first')
        .append('<span class="ui-icon ui-icon-close" title="Close Tab"></span>')
        .find('span.ui-icon-close')
        .click(function() {
          maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
          var tabCache = sessionStorage.getItem('tabCache');
          if (tabCache != null) {
            var tabCacheArray = JSON.parse(tabCache);
            for (i in tabCacheArray) {
              if (tabCacheArray[i][0] == url) {
                tabCacheArray.splice(i, 1);
              }
            }
            if ("stat.jsp" == url.substring(0, 8)) {
              sessionStorage.removeItem("statTabId");
            }
            sessionStorage.removeItem('tabCache');
            sessionStorage.setItem('tabCache', JSON.stringify(tabCacheArray));
          }
        });
        
      if (fileName.indexOf("Compare - ") != -1) {
      	$(ui.tab).parents('li:first').prepend('<div style="float:left"><span class="ui-icon ui-icon-newwin"></span></div>');
     	} else if (fileName.indexOf("Search") != -1 && fileName.length == 6) {
     		$(ui.tab).parents('li:first').prepend('<div style="float:left"><span class="ui-icon ui-icon-search"></span></div>');
     	} else if (fileName.indexOf("Statistics") != -1 && fileName.length == 10) {
     		$(ui.tab).parents('li:first').prepend('<div style="float:left"><span class="ui-icon ui-icon-gear"></span></div>');
     	} else {
      	$(ui.tab).parents('li:first').prepend('<div style="float:left"><span class="ui-icon ui-icon-document"></span></div>');
      }
			
			/*
      if (maintab.loadCache == null || maintab.loadCache == false) {
        maintab.tabs('select', '#' + ui.panel.id);
        var tabCache = sessionStorage.getItem('tabCache');
        var tabCacheArray;
        if (tabCache == null) {
          tabCacheArray = new Array();
        } else {
          tabCacheArray = JSON.parse(tabCache);
        }
        tabCacheArray.push(new Array(url, fileName, ui.panel.id));
        sessionStorage.removeItem('tabCache');
        sessionStorage.setItem('tabCache', JSON.stringify(tabCacheArray));
      } else {
        var tabCache = sessionStorage.getItem('tabCache');
        var tabCacheArray;
        if (tabCache == null) {
          tabCacheArray = new Array();
        } else {
          tabCacheArray = JSON.parse(tabCache);
        }
        tabCacheArray.push(new Array(url, fileName, ui.panel.id));
        sessionStorage.removeItem('tabCache');
        sessionStorage.setItem('tabCache', JSON.stringify(tabCacheArray));
        $('#' + ui.panel.id).addClass("ui-tabs-hide");
      }
      */
      var tabCache = sessionStorage.getItem('tabCache');
      var tabCacheArray;
      if (tabCache == null) {
        tabCacheArray = new Array();
      } else {
        tabCacheArray = JSON.parse(tabCache);
      }
      tabCacheArray.push(new Array(url, fileName, ui.panel.id));
      sessionStorage.removeItem('tabCache');
      sessionStorage.setItem('tabCache', JSON.stringify(tabCacheArray));
      if (maintab.loadCache == null || maintab.loadCache == false) {
        maintab.tabs('select', '#' + ui.panel.id);
      } else {
        $('#' + ui.panel.id).addClass("ui-tabs-hide");
      }

      if ("stat.jsp" == url.substring(0, 8)) {
      sessionStorage.setItem("statTabId", ui.panel.id);
      }
    },
    select: function(e, ui) {
      var url = $.data(ui.tab, 'load.tabs');
      sessionStorage.setItem('selTab', url);
    },
    cache: true,
    load: function(e, ui) {
      var winHeight = $(window).height();
      var winWidth = $(window).width();
      var tabWidth = $('#tabs').width();
      var tabHeight = $('#tabs').height();
      $('.sourceview').css('height', winHeight-165);
      $('.sourceview').css('width', tabWidth-25);
      $('.diffview').css('width', tabWidth/2 - 25);
      $('.diffallview').css('height', winHeight-165);
      $('.diffallview').css('width', tabWidth);
      /*
      // background.
      var bgColor = localStorage.getItem('sourceviewBgColor');
      if (bgColor != null && '' != bgColor) {
      	$('.sourceview').css('backgroundColor', bgColor);
      	$('.diffview').css('backgroundColor', bgColor);
      }
      // comments.
      var cColor = localStorage.getItem('sourceviewCommentColor');
      if (cColor != null && '' != cColor) {
        $('.c').css('color', cColor);
      }
      // strings.
      var sColor = localStorage.getItem('sourceviewStringColor');
      if (sColor != null && '' != sColor) {
        $('.s').css('color', sColor);
      }
      // numbers.
      var nColor = localStorage.getItem('sourceviewNumberColor');
      if (nColor != null && '' != nColor) {
        $('.n').css('color', nColor);
      }
      // reserved stg.
      var bColor = localStorage.getItem('sourceviewReservedColor');
      if (bColor != null && '' != bColor) {
        $('.b').css('color', bColor);
      }
      */
      //$('.diffview').jScrollPane();
    }
  });
    
  $("#tabs").tabs().find(".ui-tabs-nav").sortable({ axis: "x" });

  var gridParamsCache = localStorage.getItem('gridParamsCache');
  var gridInfo = null;
  if (gridParamsCache != null) {
    gridInfo = $.parseJSON(gridParamsCache);
    if (gridInfo.colModel != null) {
      gridInfo.colModel.splice(0, 1);
    }
    if (gridInfo.colNames != null) {
      gridInfo.colNames.splice(0, 1);
    }
    var names = ['Full Path', 'Prod ID', 'Prod Code','File Type', 'File Path','File Name', 'Grep'];
    var ids = ['id', 'pid', 'pcode','type', 'path','name', 'grep'];
    var newColModel = new Array();
    for (i = 0; i < gridInfo.colNames.length; i++) {
      for (j = 0; j < names.length; j++) {
        if (names[j] == gridInfo.colNames[i]) {
          for (k = 0; k < gridInfo.colModel.length; k++) {
            if (gridInfo.colModel[k].name == ids[j]) {
              newColModel.push(gridInfo.colModel[k]);
            }
          }
        }
      }
    }
    gridInfo.colModel = newColModel;
  } else {
    //var winWidth = $(window).width();
    //var othersWidth = 50+30+50+250+250;
    //var grepWidth = winWidth - othersWidth - 30;
    gridInfo = new Object();
    gridInfo.colNames = ['Full Path', 'Prod ID', 'Prod Code','File Type', 'File Path','File Name', 'Grep'];
    gridInfo.colModel = [ 
      {name:'id', index:'id', hidden:true, title:false}, 
      {name:'pid', index:'pid', hidden:true, title:false},
      {name:'pcode', index:'pcode', width:'30', title:false}, 
      {name:'type', index:'type', width:'50', title:false}, 
      {name:'path', index:'path', width:'250', title:false},
      {name:'name', index:'name', width:'250', title:false},
      {name:'grep', index:'grep', width:'400', sortable:false, search:false, title:false}];
      //{name:'aru', index:'aru', width:'35', sortable:false, search:false}];
    gridInfo.rowNum = 20;
    gridInfo.page = 1;
    gridInfo.sortorder = 'desc';
    gridInfo.sortname = '';
    gridInfo.height = '100%';
    gridInfo.caption = 'Search Result';
  }

  var queryCacheKey = pj + ':' + q;
  var queryCache = sessionStorage.getItem('q_cache');
  if (queryCache == null || queryCache != queryCacheKey) {
    sessionStorage.removeItem('grepCache');
    sessionStorage.removeItem('tabCache');
    sessionStorage.removeItem('statTabId');
    sessionStorage.removeItem('selTab');
    sessionStorage.setItem('q_cache', queryCacheKey);
    gridInfo.sortorder = 'desc';
  }
    
  $('#list').jqGrid({
    sortable: true,
    url:'data?pj=' + pj + '&q=' + escape(q) + '&tg=list',
    datatype: 'json',
    colNames: gridInfo.colNames,
    colModel : gridInfo.colModel,
    pager: '#pager',
    page: gridInfo.page,
    rowNum: gridInfo.rowNum,
    rowList:[20,30,50,100,200,300,500,1000],
    sortname: gridInfo.sortname,
    sortorder: gridInfo.sortorder,
    viewrecords: true,
    hoverrows: false,
    //viewsortcols: true,
    //gridview: true,
    //width: 1200,
    height: '100%',
    rownumbers: true,
    //altRows:true,
    //altclass:'altRowClass',
    caption: gridInfo.caption,
    jsonReader: {
      repeatitems: false
    },
    onSelectRow: function(rowid, status) {
      gridInfo.selrow = rowid;
    },
    gridComplete: function() {
      if ('' == q || q == null) {
      	
        $("#grepLink").hide();
        $("#gridWrapper").hide();
        $.get('help.jsp', function(data) {
          $("#helpInfo").html(data);
          $("#helpInfo").show();
        });
      } else {
        $("#grepLink").show();
        $("#gridWrapper").show();
        $("#helpInfo").hide();
      }
    },
    loadComplete: function(data) {
      var tgList = "#list";
      var grid = $(tgList);
      var totalRows = grid.jqGrid('getGridParam','records');
      grid.setCaption('Search Result: ' + totalRows + ' hits');
      var grepCache = sessionStorage.getItem('grepCache');
      if (grepCache != null) {
        var grepCacheArray = JSON.parse(grepCache);
        $.each(grepCacheArray, function() {
          var id = this[0];
          var data = this[1];
          if (data != '') {
            data = $.base64.decode(data);
            var type = grid.getCell(id, 'type');
            grid.jqGrid('setRowData', id, {grep:'<span style="cursor: pointer;" class="ui-icon ui-icon-zoomout grepshown" onClick="javascript:hideGrep(\'' + id + '\',\'' + type + '\',\'' + tgList + '\');"></span><span title="Grep Result"><pre style="font-size: 100.01%; font-family: arial, sans-serif; margin-bottom:0px; margin-top:0px;">' + data + '</pre></span>'});
          }
        });
      }
      if ( gridInfo.selrow != null) {
        grid.jqGrid('setSelection',  gridInfo.selrow);
      }
    }
  }).navGrid('#pager',{edit:false,add:false,del:false,search:false});
  var grid = $('#list');
  grid.jqGrid('gridResize',{minWidth:350,maxWidth:"100%",minHeight:80, maxHeight:"100%"});
  grid.jqGrid('navButtonAdd','#pager',{
    caption: "Columns",
    title: "Reorder Columns",
    onClickButton : function (){
      $("#list").jqGrid('columnChooser');
    }
  });
  

      $('#exportDialog').dialog({
        autoOpen: false,
        width: 300,
        height: 200,
        title: 'Export',
        modal:true,
        resizable:true,
        buttons: {
    	    "Yes": function() {
    		     var url = $("#list").jqGrid('getGridParam', 'url');
             $("#list").jqGrid('excelExport', {url:url + '&exp=true&grep=true'});
             $(this).dialog("close");
    	    },
    	    "No": function() {
    		     var url = $("#list").jqGrid('getGridParam', 'url');
             $("#list").jqGrid('excelExport', {url:url + '&exp=true&grep=false'});
             $(this).dialog("close");
    	    },
    	    "Cancel": function() {
    		     $(this).dialog("close");
    	    }
        }
      });
  
  grid.jqGrid('navButtonAdd','#pager',{
    caption: "Export",
    title: "Export to Excel",
    buttonicon: "ui-icon-bookmark",
    onClickButton : function (){
      $('#exportDialog').dialog('open');
    }
  });
  grid.jqGrid('navButtonAdd','#pager',{
    caption: "Statistics",
    title: "Statistics",
    buttonicon: "ui-icon-gear",
    onClickButton : function (){
      var statTabId = sessionStorage.getItem('statTabId');
      if (statTabId != null) {
        $("#tabs").tabs('select', statTabId);
      } else {
        localStorage.removeItem('statGridParamsCache');
        localStorage.removeItem('statSubGridParamsCache');
        $('#tabs').tabs('add', 'stat.jsp?pj=' + pj + '&q=' + escape(q), 'Statistics');
      }
    }
  });
  
  // Review feature.
  $('#reviewDialog').dialog({
    autoOpen: false,
    width: 700,
    title: 'Review',
    modal:true,
    resizable:true,
    buttons: {
    	"Start Review": function() {
    	  var checkFlag = 'ok';
    	  $("#reviewform").find(".required").each(function(){
          if($(this).val()==""){
            checkFlag = null;
            if ($(this).attr('checkFlag') == null) {
              $(this).attr('checkFlag', 'On');
              //$(this).wrap("<div id='errorDiv' class='ui-state-error ui-corner-all'></div>");
              $(this).parent().prepend("<div style='width:120px;' id='errorDiv' class='ui-state-error ui-corner-all'><span id='errorSpan' style='display: inline-block;' class='ui-icon ui-icon-alert'></span><span id='errorMsg'>Mandatory Field</span></div>")
            }
          } else {
            if ($(this).attr('checkFlag') == 'On') {
              $(this).parent().find('#errorSpan').remove();
              $(this).parent().find('#errorMsg').remove();
              $(this).parent().parent().find('#errorDiv').remove();
              $(this).removeAttr('checkFlag');
            }
          }
         });
         var validateFlag = 'ok';
         $("#reviewform").find(".validate").each(function(){
         		var validFormat = 'ok';
         		if ($(this).val() != null) {
           		var elements = $(this).val().split('|');
           		for (var i = 0; i < elements.length; i++) {
								var element = elements[i].split(':');
								if (element.length == 2 && element[0] != null && "" != element[0] && element[1] != null && "" != element[1]) {
						  		if (!(!isNaN(parseFloat(element[0])) && isFinite(element[0]))) {
						  			validFormat = null;
						  			break;
						  		}
								} else {
									validFormat = null;
									break;
								}
							}
						} else {
							validFormat = null;
						}
						if (validFormat == null) {
						  validateFlag = null;
						  if ($(this).attr('validFlag') == null) {
             		$(this).attr('validFlag', 'On');
             		$(this).parent().prepend("<div style='width:120px;' id='errorValidDiv' class='ui-state-error ui-corner-all'><span id='errorValidSpan' style='display: inline-block;' class='ui-icon ui-icon-alert'></span><span id='errorValidMsg'>Invalid Format</span></div>")
           		}
						} else {
							if ($(this).attr('validFlag') == 'On') {
             		$(this).parent().find('#errorValidSpan').remove();
             		$(this).parent().find('#errorValidMsg').remove();
             		$(this).parent().parent().find('#errorValidDiv').remove();
             		$(this).removeAttr('validFlag');
           		}
						}
         	});

    	   if (checkFlag != null && validateFlag != null) {
    		   $("#reviewform").submit();
    		   $(this).dialog("close");
    		}
    	},
    	"Cancel": function() {
    		$(this).dialog("close");
    	}
    }
  });
  
  grid.jqGrid('navButtonAdd','#pager',{
    caption: "Review",
    title: "Review",
    buttonicon: "ui-icon-document",
    onClickButton : function (){
			$('#reviewDialog').dialog('open');
			var userCache = localStorage.getItem('userCache');
			if (userCache != null) {
			  $("#reviewform input[name='userid']").attr("value", userCache);
			}
			var target = $('#gview_list');
			if ($('#tabs').height() > $(window).height()) {
        $("#reviewDialog").dialog("widget").position({
           my: 'center middle',
           at: 'center middle',
           of: target
        });
      } else {
        $("#reviewDialog").dialog("widget").position({
           my: 'center middle',
           at: 'center middle',
           of: target
        });
      }
    }
  });
  // End of review feature.

  $.jgrid.formatter.integer.thousandsSeparator=',';

  var tabCache = sessionStorage.getItem('tabCache');
  if (tabCache != null) {
    var tabCacheArray = JSON.parse(tabCache);
    maintab.loadCache = true;
    sessionStorage.removeItem('tabCache');
    for (i in tabCacheArray) {
      maintab.tabs('add', tabCacheArray[i][0], tabCacheArray[i][1]);
    }
    maintab.loadCache = false;
  }
  
  var selTabUrl = sessionStorage.getItem('selTab');
  if (selTabUrl != null) {
    var newTabCache = sessionStorage.getItem('tabCache');
  	var selTabId = null;
  	if (newTabCache != null) {
      var newTabCacheArray = JSON.parse(newTabCache);
      for (i in newTabCacheArray) {
        if (selTabUrl.indexOf(newTabCacheArray[i][0]) != -1) {
          selTabId = newTabCacheArray[i][2];
        }
      }
    }
    if (selTabId != null) {
    	$("#tabs").tabs("select", '#' + selTabId);
    	$("#tabs").tabs("load", '#' + selTabId);
    } else {
      $("#tabs").tabs("select", '#tabs-1');
      $("#tabs").tabs("load", '#tabs-1');
    }
  }
});

function showGrep(id, type, tgList) {
  var grid = $(tgList);
  grid.jqGrid('setSelection', id);
  grid.jqGrid('setRowData', id, {grep:'<span title="Grep Result">Loading...</span>'});
  $.get('showgrep?pj=' + pj + '&q=' + escape(q) + '&type=' + type + '&path=' + id, function(data) {
    grid.jqGrid('setRowData', id, {grep:'<span style="cursor: pointer;" class="ui-icon ui-icon-zoomout grepshown" title="Hide Grep" onClick="javascript:hideGrep(\'' + id + '\',\'' + type + '\',\'' + tgList + '\');"></span><span title="Grep Result"><pre style="font-size: 100.01%; font-family: arial, sans-serif; margin-bottom:0px; margin-top:0px;">' + data + '</pre></span>'});
    var grepCache = sessionStorage.getItem('grepCache');
    var grepCacheArray;
    if (grepCache == null) {
      grepCacheArray = new Array();
    } else {
      grepCacheArray = JSON.parse(grepCache);
    }
    if (data != null && data != '') {
      grepCacheArray.push(new Array(id, $.base64.encode(data)));
    }
    sessionStorage.setItem('grepCache', JSON.stringify(grepCacheArray));
  })
  .success(function() {
    //alert("second success");
  })
  .error(function() {
    alert("Error to get grep result.");
    grid.jqGrid('setRowData', id, {grep:'<span title="Grep Result">Error</span>'});
  })
  .complete(function() {
    //alert("complete");
  });
}

function hideGrep(id, type, tgList) {
  var grepCache = sessionStorage.getItem('grepCache');
  if (grepCache != null) {
    var grepCacheArray = JSON.parse(grepCache);
    for (i in grepCacheArray) {
      if (grepCacheArray[i][0] == id) {
        grepCacheArray.splice(i, 1);
      }
    }
    sessionStorage.removeItem('grepCache');
    sessionStorage.setItem('grepCache', JSON.stringify(grepCacheArray));
  }
  $(tgList).jqGrid('setRowData', id, {grep:'<span style="cursor: pointer;" class="ui-icon ui-icon-zoomin grephidden" title="Show Grep" onClick="javascript:showGrep(\'' + id + '\',\'' + type + '\',\'' + tgList + '\');"></span>'});
}

function reloadTab(href) {
	var tabId = $("#tabs").tabs('option', 'selected');
	$("#tabs").tabs('url', tabId, href);
	$("#tabs").tabs('load', tabId);
}

function addTab(id, href, title, tgList) {
  if (id != null) {
    $(tgList).jqGrid('setSelection', id);
  }
  var tabCache = sessionStorage.getItem('tabCache');
  var tabId = null;
  if (tabCache != null) {
    var tabCacheArray = JSON.parse(tabCache);
    for (i in tabCacheArray) {
      if (tabCacheArray[i][0].indexOf(href) != -1) {
        tabId = tabCacheArray[i][2];
      }
    }
  }
  if (tabId == null) {
    $('#tabs').tabs('add', href, title);
  } else {
    $('#tabs').tabs('select', tabId);
  }
  return false;
}

function addDiffTab(path, href, title) {
  var leftversion = jQuery("#leftversion_" + path).val();
  var rightversion = jQuery("#rightversion_" + path).val();
  var url = href + '&leftversion=' + leftversion + '&rightversion=' + rightversion;
  var tab = $('#tabs');
  if (tab.length > 0) {
    var tabCache = sessionStorage.getItem('tabCache');
    var tabId = null;
    if (tabCache != null) {
      var tabCacheArray = JSON.parse(tabCache);
      for (i in tabCacheArray) {
        if (tabCacheArray[i][0].indexOf(url) != -1) {
          tabId = tabCacheArray[i][2];
        }
      }
    }
    if (tabId == null) {
      tab.tabs('add', url, title);
    } else {
      tab.tabs('select', tabId);
    }
    return false;
  } else {
    window.open(url, '_blank');
    return false;
  }
}

function showGrepAll(tgList) {
  var rows = $(tgList).jqGrid('getRowData');
  $.each(rows, function() {
    showGrep(this.id, this.type, tgList);
  });
}

function hideGrepAll(tgList) {
  var rows = $(tgList).jqGrid('getRowData');
  sessionStorage.removeItem('grepCache');
  $.each(rows, function() {
    hideGrep(this.id, this.type, tgList);
  });
}

function hideTabAll() {
  var tabCache = sessionStorage.getItem('tabCache');
  if (tabCache != null) {
    var tabCacheArray = JSON.parse(tabCache);
    for (i in tabCacheArray) {
      $("#tabs").tabs('remove', tabCacheArray[i][2]);
    }
  }
  sessionStorage.removeItem('statTabId');
  sessionStorage.removeItem('tabCache');
  sessionStorage.removeItem('selTab');
  $("#tabs").tabs('select', '#tabs-1');
}

function clearCache() {
  sessionStorage.removeItem('selTab');
  sessionStorage.removeItem('q_cache');
  sessionStorage.removeItem('grepCache');
  sessionStorage.removeItem('tabCache');
  localStorage.removeItem('gridParamsCache');
  localStorage.removeItem('statGridParamsCache');
  localStorage.removeItem('statSubGridParamsCache');
  sessionStorage.removeItem('statTabId');
  localStorage.removeItem('sourceviewBgColor');
  localStorage.removeItem('sourceviewCommentColor');
  localStorage.removeItem('sourceviewStringColor');
  localStorage.removeItem('sourceviewNumberColor');
  localStorage.removeItem('sourceviewReservedColor');
  cacheClear = true;
  //location.reload();
  location.href='search.jsp';
}

$(window).unload(function() {
  if (!cacheClear) {
    
    if ($("#list").length > 0) {
      var grid = $("#list");
      var gridInfo = new Object();
      //gridInfo.url = grid.jqGrid('getGridParam', 'url');
      gridInfo.sortname = grid.jqGrid('getGridParam', 'sortname');
      gridInfo.sortorder = grid.jqGrid('getGridParam', 'sortorder');
      gridInfo.selrow = grid.jqGrid('getGridParam', 'selrow');
      gridInfo.page = grid.jqGrid('getGridParam', 'page');
      gridInfo.rowNum = grid.jqGrid('getGridParam', 'rowNum');
      //gridInfo.postData = grid.jqGrid('getGridParam', 'postData');
      gridInfo.colModel = grid.jqGrid('getGridParam', 'colModel');
      gridInfo.colNames = grid.jqGrid('getGridParam', 'colNames');
      gridInfo.height = grid.jqGrid('getGridParam', 'height');
      localStorage.removeItem('gridParamsCache');
      localStorage.setItem('gridParamsCache', JSON.stringify(gridInfo));
      if ($("#statlist").length > 0) {
        var statGrid = $("#statlist");
        var statGridInfo = new Object();
        statGridInfo.selrow = statGrid.jqGrid('getGridParam', 'selrow');
        statGridInfo.sortname = statGrid.jqGrid('getGridParam', 'sortname');
        statGridInfo.sortorder = statGrid.jqGrid('getGridParam', 'sortorder');
        statGridInfo.page = statGrid.jqGrid('getGridParam', 'page');
        statGridInfo.rowNum = statGrid.jqGrid('getGridParam', 'rowNum');
        localStorage.removeItem('statGridParamsCache');
        localStorage.setItem('statGridParamsCache', JSON.stringify(statGridInfo));
        if ($("#statlist_d").length > 0) {
          var statSubGrid = $("#statlist_d");
          var statSubGridInfo = new Object();
          statSubGridInfo.selrow = statSubGrid.jqGrid('getGridParam', 'selrow');
          statSubGridInfo.sortname = statSubGrid.jqGrid('getGridParam', 'sortname');
          statSubGridInfo.sortorder = statSubGrid.jqGrid('getGridParam', 'sortorder');
          statSubGridInfo.page = statSubGrid.jqGrid('getGridParam', 'page');
          statSubGridInfo.rowNum = statSubGrid.jqGrid('getGridParam', 'rowNum');
          localStorage.removeItem('statSubGridParamsCache');
          localStorage.setItem('statSubGridParamsCache', JSON.stringify(statSubGridInfo));
        }
      }
    }
  }
});

$(window).submit(function() {
  var grid = $("#list");
  grid.jqGrid('setGridParam', {sortname:''});
  grid.jqGrid('setGridParam', {page:1});
  grid.jqGrid('setGridParam', {sortorder:'desc'});
});

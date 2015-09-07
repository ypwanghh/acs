<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<!-- meta http-equiv="Content-Type" content="text/html; charset=UTF-8" -->
<title>Applications Source Code Search</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>codesearch/inc/jquery-ui-1.8.16.custom.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>codesearch/inc/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>codesearch/inc/ui.multiselect.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>codesearch/inc/jquery.ui.tabs.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>codesearch/inc/jquery.contextmenu.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>codesearch/inc/style.css">
	
<link href="<%=basePath%>codesearch/shl/styles/shCore.css" rel="stylesheet" type="text/css" />
<!--link href="<%=basePath%>codesearch/shl/styles/shThemeDefault.css" rel="stylesheet" type="text/css" /-->
<link href="<%=basePath%>codesearch/shl/styles/shThemeEclipse.css" rel="stylesheet" type="text/css" />

<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery-1.6.2.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/ui.multiselect.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery.ui.widget.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery.ui.core.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery.ui.resizable.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/grid.locale-en.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery.jqGrid.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/grid.custom.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/jquery.base64.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/json2.js"></script>
<script type="text/javascript"
	src="<%=basePath%>codesearch/inc/ics.search.js"></script>

<script src="<%=basePath%>codesearch/inc/xregexp.js"></script>	
<script type="text/javascript" src="<%=basePath%>codesearch/shl/src/shCore.js"></script>
<script type="text/javascript" src="<%=basePath%>codesearch/shl/src/shLegacy.js"></script>
<script type="text/javascript" src="<%=basePath%>codesearch/shl/scripts/shBrushJScript.js"></script>
<script type="text/javascript" src="<%=basePath%>codesearch/shl/scripts/shBrushXml.js"></script>
<script type="text/javascript" src="<%=basePath%>codesearch/shl/scripts/shBrushJava.js"></script>
 
<script type="text/javascript">
     SyntaxHighlighter.config.clipboardSwf = '<%=basePath%>codesearch/shl/scripts/clipboard.swf';
     SyntaxHighlighter.all()
</script>

<style type="text/css">
.syntaxhighlighter div { font-size:12px !important; }
</style>

</head>
<body>
	<table id="pageHeaderTbl" width="100%" border="0" cellpadding="0"
		cellspacing="0">
		<tbody>
			<tr valign="center">
				<td valign="center" height="40"
					background="<%=basePath%>codesearch/inc/headerBg.jpg">
					<table width="100%" border="0">
						<tbody>
							<tr>
								<td width="10"><img height="20" width="10"
									src="<%=basePath%>codesearch/inc/spacer.gif"></td>
								<td align="left" valign="bottom"><font class="brandHeader">&nbsp;&nbsp;Applications
									Source Code Search - Beta</font></td>
							</tr>
							<tr>
								<td colspan="5"><span class="rightnote">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<span class="rightnote_wh" onclick="javascript:clearCache()">Reset</span>
									<span class="rightnote">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <span
									class="rightnote_wh"
									onclick="javascript:addTab('', 'help.jsp', 'Help','');">Help</span>
									<span class="rightnote">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <span
									class="rightnote_wh"
									onclick="javascript:location.href='search.jsp'">New
										Search</span> <span class="rightnote">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<span class="rightnote_wh"
									onclick="javascript:location.href='review.jsp'">Review
										List</span> <span class="rightnote">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<span class="rightnote_wh" onclick="javascript:hideTabAll()">Close
										Tabs</span></td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>

	<div id="tabs" class="searchtab">
		<ul id="tablist" class="ui-tabs-nav">
			<li class="ui-state-default ui-corner-top">
				<a href="#tabs-1">Search</a>
			</li>
		</ul>
		<div id="tabs-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td>
							<form name="search" action="<%=basePath%>search">
								<table bordre="0">
									<tbody>
										<tr>
											<td><b>Target : </b></td>
											<td><input type="radio" name="pj" id="pj" value="999"
												${checkedMain}>RO main <input type="radio" name="pj"
												id="pj" value="400" ${checked400}>R4.0.x <input type="radio"
												name="pj" id="pj" value="350" ${checked350}>R3.5.x <input
												type="radio" name="pj" id="pj" value="340" ${checked340}>R3.4.x <input
												type="radio" name="pj" id="pj" value="330" ${checked330}>R3.3.x <input
												type="radio" name="pj" id="pj" value="DBT">DBTools
												<input type="radio" name="pj" id="pj" value="OTHERS">Others
											</td>
										</tr>
										<tr>
											<td align="right"><b>Text : </b></td>
											<td><input type="text" name="q" id="q"
												value="${queryValue}" size="70"> &nbsp;
												<button
													class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
													type="submit">
													<span class="ui-button-text">Search</span>
												</button></td>
										</tr>
									</tbody>
								</table>
							</form>
						</td>
					</tr>


					<tr>
						<td>
							<div id="grepLink" style="display: none;">
								<span class="rightnote"> Grep:[<a
									href="javascript:showGrepAll('#list')">Show All</a> / <a
									href="javascript:hideGrepAll('#list')">Hide All</a>]
								</span>
							</div>
						</td>
					</tr>

					<tr>
						<td>
							<div id="gridWrapper" style="display: none;">
										<div class="ui-jqgrid-bdiv"
											style="height: 100%; width: 1005px;">
											<div style="position: relative;">
												<div></div>
												<table id="list" tabindex="1" cellspacing="0"
													cellpadding="0" border="0" role="grid"
													aria-multiselectable="false" aria-labelledby="gbox_list"
													class="ui-jqgrid-btable" style="width: 1005px;">
												</table>
											</div>
										</div>
									</div>
									<div class="ui-jqgrid-resize-mark" id="rs_mlist">&nbsp;</div>
									<div id="pager" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr" style="width: 1005px;">
										
									</div>
									<div class="ui-resizable-handle ui-resizable-e"></div>
									<div class="ui-resizable-handle ui-resizable-s"></div>
									<div
										class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se"
										style="z-index: 1001;"></div>
								</div>

							</div>
						</td>
					</tr>

					<tr>
						<td>
							<div id="helpInfo">
								<br> <br> See <span class="notelink"
									onclick="javascript:addTab('', 'note.jsp', 'Release Note', '');">release
									note</span> for new features and known issues. (Last updated: December
								15, 2014) <br> <br> As for query syntax, here are some
								examples. <br> <br> <b>Examples:</b>
								<blockquote>
									<table cellpadding="5">
										<tbody>
											<tr>
												<td class="CommandLineText">GlobalParam</td>
											</tr>
										</tbody>
									</table>
									Search files that contains "GlobalParam".
									<br>
									<br>
									<table cellpadding="5">
										<tbody>
											<tr>
												<td class="CommandLineText">GlobalParam NOT
													format</td>
											</tr>
										</tbody>
									</table>
									Search files that contains "GlobalParam" but not contain
									format.
									<br>
									<br>
									<table cellpadding="5">
										<tbody>
											<tr>
												<td class="CommandLineText">Global*ram</td>
											</tr>
										</tbody>
									</table>
									Search files that contains "Global*ram". "*" symbol is
									recognized as a wildcard for multiple characters.
									<br>
									<br>
									<table cellpadding="5">
										<tbody>
											<tr>
												<td class="CommandLineText">fileType:C</td>
											</tr>
										</tbody>
									</table>
									Search files whose file type is C.
									<br>
									<br>
									<table cellpadding="5">
										<tbody>
											<tr>
												<td class="CommandLineText">VARCHAR2 AND fileType:PLSQL
												</td>
											</tr>
										</tbody>
									</table>
									Search files that contains "VARCHAR2" in PL/SQL files.
									<br>
									<br>
									<table cellpadding="5">
										<tbody>
											<tr>
												<td class="CommandLineText">VARCHAR2 AND (fileType:PLSQL
													OR fileType:SQL)</td>
											</tr>
										</tbody>
									</table>
									Search files that contains "VARCHAR2" in PL/SQL fnd SQL files.
									<br>
									<br>

								</blockquote>

								For syntax details, see <u> <a
									href="http://lucene.apache.org/core/old_versioned_docs/versions/3_3_0/queryparsersyntax.html"
									target="_blank">Apache Lucene - Query Parser Syntax</a></u>. <br>
								<br> The table blow shows the fields you can use. <br>
								<br>



								<table class="helpTable" border="0" cellpadding="1"
									cellspacing="1">
									<tbody>
										<tr>
											<td class="tableColumnHeader">Fields</td>
											<td class="tableColumnHeader">Descrption</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">fileContents</td>
											<td class="tableCellText">String in a file - Default
												Field (no need to specify)</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">prodCode</td>
											<td class="tableCellText">Product Code</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">prodName</td>
											<td class="tableCellText">Product Name</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">filePath</td>
											<td class="tableCellText">File Path - relative path from
												product top</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">fileName</td>
											<td class="tableCellText">File Name</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">fileExtension</td>
											<td class="tableCellText">File Extension - java, pls,
												etc.</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">fileType</td>
											<td class="tableCellText">File Type - JAVA, PLSQL, SQL,
												AS, MXML, PL, BAT, PKB, PKS, C, DAT</td>
										</tr>
										<tr>
											<td align="center" class="tableCellText">fileDeclare</td>
											<td class="tableCellText">Class, method, variable name</td>
										</tr>
									</tbody>
								</table>
								<br>
								<div class="centernote">
									Questions and comments, send email to <a
										href="mailto:ypwanghh@126.com">James Wang</a>
								</div>

							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<script>
document.search.q.focus();
</script>

	<div class="ui-widget ui-widget-content ui-corner-all ui-jqdialog"
		id="alertmod" dir="ltr" tabindex="-1" role="dialog"
		aria-labelledby="alerthd" aria-hidden="true"
		style="width: 200px; height: auto; z-index: 950; overflow: hidden; top: 436px; left: 553.5px;">
		<div
			class="ui-jqdialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"
			id="alerthd" style="cursor: move;">
			<span class="ui-jqdialog-title" style="float: left;">Warning</span><a
				href="javascript:void(0)"
				class="ui-jqdialog-titlebar-close ui-corner-all"
				style="right: 0.3em;"><span class="ui-icon ui-icon-closethick"></span></a>
		</div>
		<div class="ui-jqdialog-content ui-widget-content" id="alertcnt">
			<div>Please, select row</div>
			<span tabindex="0"><span tabindex="-1" id="jqg_alrt"></span></span>
		</div>
		<div
			class="jqResize ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se"></div>
	</div>
	<div
		class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"
		tabindex="-1" role="dialog"
		aria-labelledby="ui-dialog-title-exportDialog"
		style="display: none; z-index: 1000; outline: 0px; position: absolute;">
		<div
			class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
			<span class="ui-dialog-title" id="ui-dialog-title-exportDialog">Export</span><a
				href="<%=basePath%>search#"
				class="ui-dialog-titlebar-close ui-corner-all" role="button"><span
				class="ui-icon ui-icon-closethick">close</span></a>
		</div>
		<div id="exportDialog" style=""
			class="ui-dialog-content ui-widget-content">
			Would you like to include grep results? <br> <br> Note:
			Including grep results may take time depending on rows to be
			exported.
		</div>
		<div class="ui-resizable-handle ui-resizable-n"></div>
		<div class="ui-resizable-handle ui-resizable-e"></div>
		<div class="ui-resizable-handle ui-resizable-s"></div>
		<div class="ui-resizable-handle ui-resizable-w"></div>
		<div
			class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se"
			style="z-index: 1001;"></div>
		<div class="ui-resizable-handle ui-resizable-sw"
			style="z-index: 1002;"></div>
		<div class="ui-resizable-handle ui-resizable-ne"
			style="z-index: 1003;"></div>
		<div class="ui-resizable-handle ui-resizable-nw"
			style="z-index: 1004;"></div>
		<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
			<div class="ui-dialog-buttonset">
				<button type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
					role="button" aria-disabled="false">
					<span class="ui-button-text">Yes</span>
				</button>
				<button type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
					role="button" aria-disabled="false">
					<span class="ui-button-text">No</span>
				</button>
				<button type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
					role="button" aria-disabled="false">
					<span class="ui-button-text">Cancel</span>
				</button>
			</div>
		</div>
	</div>
	<div
		class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"
		tabindex="-1" role="dialog"
		aria-labelledby="ui-dialog-title-reviewDialog"
		style="display: none; z-index: 1000; outline: 0px; position: absolute;">
		<div
			class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
			<span class="ui-dialog-title" id="ui-dialog-title-reviewDialog">Review</span><a
				href="http://localhost:8080/maBBS/codesearch/search.jsp#"
				class="ui-dialog-titlebar-close ui-corner-all" role="button"><span
				class="ui-icon ui-icon-closethick">close</span></a>
		</div>
		<div id="reviewDialog" style=""
			class="ui-dialog-content ui-widget-content">
			<form id="reviewform"
				action="<%=basePath%>reviewInit.jsp"
				target="_blank" method="post">
				<table>
					<input type="hidden" name="pj" value="12">
					<input type="hidden" name="q" value="">
					<tbody>
						<tr>
							<td>Review Name:</td>
							<td><input type="text" name="name" size="50"
								class="required"><br> <span class="note">Name
									of review</span></td>
						</tr>
						<tr>
							<td>Description:</td>
							<td><input type="text" name="description" size="100"><br>
								<span class="note">Short description of this review. </span></td>
						</tr>
						<tr>
							<td>User ID:</td>
							<td><input type="text" name="userid" size="10"
								class="required"><span class="note"><br>e.g.
									wangjam - A review project belongs to this user name. </span></td>
						</tr>
						<tr>
							<td>Target:</td>
							<td>RO main</td>
						</tr>
						<tr>
							<td>Output Excel File Name:</td>
							<td><input type="text" name="outfile" size="10"
								value="out.xls" class="required"><br> <span
								class="note">e.g. project.xls - This will be used for the
									output excel file name. </span></td>
						</tr>
						<tr>
							<td>Query:</td>
							<td></td>
						</tr>
						<tr>
							<td>Result Choices:</td>
							<td><input id="resultChoice" type="text" name="results"
								size="30" value="0:OK|1:Not OK|2:Other"
								class="required validate"><br> <span class="note">Format
									is "KEY1:VALUE1|KEY2:VALUE2|KEY3:VALUE3|...". KEY has to be a
									number.</span></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		<div class="ui-resizable-handle ui-resizable-n"></div>
		<div class="ui-resizable-handle ui-resizable-e"></div>
		<div class="ui-resizable-handle ui-resizable-s"></div>
		<div class="ui-resizable-handle ui-resizable-w"></div>
		<div
			class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se"
			style="z-index: 1001;"></div>
		<div class="ui-resizable-handle ui-resizable-sw"
			style="z-index: 1002;"></div>
		<div class="ui-resizable-handle ui-resizable-ne"
			style="z-index: 1003;"></div>
		<div class="ui-resizable-handle ui-resizable-nw"
			style="z-index: 1004;"></div>
		<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
			<div class="ui-dialog-buttonset">
				<button type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
					role="button" aria-disabled="false">
					<span class="ui-button-text">Start Review</span>
				</button>
				<button type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
					role="button" aria-disabled="false">
					<span class="ui-button-text">Cancel</span>
				</button>
			</div>
		</div>
	</div>
</body>
</html>
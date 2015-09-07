<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<table width="98%" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td>
				<table cellspacing="5">
					<tbody>
						<tr>
							<td><span class="leftnote"><b>Text: </b>${queryValue}</span>
							</td>
							<td><span class="leftnote"><b>Target: </b>${project}</span>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
			<td><a class="rightnote" target="_blank"
				href="<%=basePath%>showfile?q=${queryValue}&pj=${project}&type=${type}&target=${target}&prod=${prod}&grep=false&line=true&outside=true&path=${path}&version=120.42">
					<span style="float: right">View in browser tab</span> <span
					title="View in browser tab(window)" class="ui-icon ui-icon-extlink"
					style="float: right; display: inline-block; cursor: pointer; vertical-align: middle;">
				</span>
			</a> <br></td>
		</tr>
	</tbody>
</table>
<div id="headerPart">
	<table width="98%" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td><span class="leftnote"><b>File: </b> ${path}&nbsp; <select
						id="version__egodev_ego_12_0_java_extfwk_user_presentation_webui_EgoExtFwkSingleRowRenderCO_java"
						onchange="javascript:reloadTab('showfile.jsp?q=${queryValue}&pj=${project}&type=${type}&target=${target}&prod=${prod}&grep=false&line=true&outside=false&path=${path}&version=' + $('#version__egodev_ego_12_0_java_extfwk_user_presentation_webui_EgoExtFwkSingleRowRenderCO_java').val());">
							<option value="4.0" selected="">4.0</option>
							<option value="3.5">3.5</option>
							<option value="3.4.5">3.4.5</option>
							<option value="3.4.0">3.4.0</option>
					</select></span> <a class="rightnote"
					href="http://aru.us.mood.com:8080/ARUForms/FileHistory/process_form?release=1500&product=&file=EgoExtFwkSingleRowRenderCO.java&dir=&version=120.42&version_type=1&type=&status=&search_style=Simple"
					target="_blank"> <span title="Go to ARU"
						style="float: left; display: inline-block; cursor: pointer; vertical-align: middle;"
						class="ui-icon ui-icon-extlink"></span>
				</a></td>
				<td><span class="rightnote"> <select
						id="leftversion__egodev_ego_12_0_java_extfwk_user_presentation_webui_EgoExtFwkSingleRowRenderCO_java">
							<option value="4.0" selected="">4.0</option>
							<option value="3.5">3.5</option>
							<option value="3.4.5">3.4.5</option>
							<option value="3.4.0">3.4.0</option>
					</select> AND <select
						id="rightversion__egodev_ego_12_0_java_extfwk_user_presentation_webui_EgoExtFwkSingleRowRenderCO_java">
							<option value="4.0" selected="">4.0</option>
							<option value="3.5">3.5</option>
							<option value="3.4.5">3.4.5</option>
							<option value="3.4.0">3.4.0</option>
					</select>
						<button
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
							onclick="addDiffTab('_egodev_ego_12_0_java_extfwk_user_presentation_webui_EgoExtFwkSingleRowRenderCO_java', 'showdiff.jsp?q=EgoExtFwkSingleRowRenderCO.java&pj=12&path=/egodev/ego/12.0/java/extfwk/user/presentation/webui/EgoExtFwkSingleRowRenderCO.java&type=JAVA&prod=ego&outside=false', 'Compare - EgoExtFwkSingleRowRenderCO.java');">
							<span class="ui-button-text">Compare</span>
						</button>
				</span></td>
			</tr>
		</tbody>
	</table>
</div>
<table border="0" cellpadding="2" cellspacing="5">
	<tbody>
		<tr>
			<td style="border: 1px solid; border-color: #80acd6;">
				<div id="project" style="display: none">${project}</div>
				<div id="product" style="display: none">${prod}</div>
				<div id="internalSourceView" path="${path}" name="${queryValue}"
					class="sourceview"
					style="overflow: auto; height: 697px; width: 1249px;">
					&nbsp;<a name="t1417520110696_0" id="name="t1417520110696_0"></a> <span
						title="Go first"
						style="display: inline-block; cursor: pointer; vertical-align: middle;"
						class="ui-icon-s ui-icon-circle-triangle-s-s"
						onclick="location.href='#t1417520110696_1'"></span>
					<pre class="brush: ${typeLowCase}">${context}</pre>
					&nbsp; <a name="t1417520110696_2" id="t1417520110696_2"></a> <span
						title="Go previoius"
						style="display: inline-block; cursor: pointer; vertical-align: middle;"
						class="ui-icon-s ui-icon-circle-triangle-n-s"
						onclick="location.href='#t1417520110696_1'"></span>
				</div>
			</td>
		</tr>
	</tbody>
</table>

</html>
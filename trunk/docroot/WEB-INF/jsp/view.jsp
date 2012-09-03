<%@include file="common/includes.jsp"%>
<liferay-ui:search-container>
	<liferay-ui:search-container-results results="${users}" />
		<liferay-ui:search-container-row
			className="com.liferay.portal.model.User" modelVar="item"
			keyProperty="userId">
			<liferay-ui:search-container-column-text>${item.fullName}
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>
	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<portlet:resourceURL var="PDF_URL"></portlet:resourceURL>

<aui:button href="${PDF_URL}" value="PDF"></aui:button>
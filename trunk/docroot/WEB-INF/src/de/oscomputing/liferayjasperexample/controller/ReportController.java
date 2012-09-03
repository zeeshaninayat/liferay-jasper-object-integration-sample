package de.oscomputing.liferayjasperexample.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

@Controller
@RequestMapping("view")
public class ReportController {

	@RequestMapping()
	public ModelAndView handleRenderDefault(RenderRequest renderRequest,
			RenderResponse renderResponse) throws SystemException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest
				.getAttribute(WebKeys.THEME_DISPLAY);
		List<User> users = UserLocalServiceUtil.getGroupUsers(themeDisplay
				.getScopeGroupId());
		System.out.println(users.size());
		ModelAndView mav = new ModelAndView("view");
		mav.addObject("users", users);
		return mav;
	}

	@ResourceMapping()
	public void handlePDF(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws SystemException {
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest
				.getAttribute(WebKeys.THEME_DISPLAY);
		ServletResponse servletResponse = PortalUtil
				.getHttpServletResponse(resourceResponse);
		servletResponse.setContentType("application/pdf");
		try {
			List<User> users = UserLocalServiceUtil.getGroupUsers(themeDisplay
					.getScopeGroupId());
			ServletOutputStream sops = servletResponse.getOutputStream();
			String reportsDir = PropsUtil.get("reports.dir");
			String fileName = reportsDir + "LiferayObjectsAndServices.jasper";
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
					users);
			Locale locale = themeDisplay.getLocale();
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", reportsDir);
			parameters.put("REPORT_LOCALE", locale);
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(new File(fileName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, dataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, sops);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

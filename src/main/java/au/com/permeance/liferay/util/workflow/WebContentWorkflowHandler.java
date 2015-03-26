/**
 * Copyright (C) 2013 Permeance Technologies
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package au.com.permeance.liferay.util.workflow;

import com.liferay.portal.NoSuchWorkflowDefinitionLinkException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.BaseWorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.WorkflowDefinitionLink;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalFolderLocalServiceUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

/**
 * Workflow Handler which is similar to JournalArticleWorkflowHandler, except it is not editable in
 * the Workflow Configuration and assigns the WorkflowDefinitionLink by structure id
 * 
 * @author Chun Ho <chun.ho@permeance.com.au>
 */
public class WebContentWorkflowHandler extends BaseWorkflowHandler<JournalArticle> {

    private static final Log _log = LogFactoryUtil.getLog(WebContentWorkflowHandler.class);

    // Start Section obtained from JournalArticleWorkflowHandler
    public static final String CLASS_NAME = JournalArticle.class.getName();

    public String getClassName() {
        return CLASS_NAME;
    }

    public String getType(Locale locale) {
        return ResourceActionsUtil.getModelResource(locale, CLASS_NAME);
    }

    /**
     * This implementation puts out a stack trace for any exception because Liferay can swallow them
     * and this is where potential PACL-related exceptions can occur.
     */
    @Override
    public JournalArticle updateStatus(int status, Map<String, Serializable> workflowContext) throws PortalException {
        try {
            long userId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
            long classPK = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));

            JournalArticle article = JournalArticleLocalServiceUtil.getArticle(classPK);

            ServiceContext serviceContext = (ServiceContext) workflowContext.get("serviceContext");

            String articleURL = PortalUtil.getControlPanelFullURL(serviceContext.getScopeGroupId(), PortletKeys.JOURNAL, null);

            return JournalArticleLocalServiceUtil.updateStatus(userId, article, status, articleURL, serviceContext, workflowContext);

        } catch (RuntimeException e) {
            _log.error("Error updating status: " + e.getMessage(), e);
            throw e;
        } catch (PortalException e) {
            _log.error("Error updating status: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    protected String getIconPath(ThemeDisplay themeDisplay) {
        return themeDisplay.getPathThemeImages() + "/common/history.png";
    }

    // End Section obtained from JournalArticleWorkflowHandler

    // Start Section overriding default workflow handler behaviour
    @Override
    public WorkflowDefinitionLink getWorkflowDefinitionLink(long companyId, long groupId, long classPK) throws PortalException,
            SystemException {

        // fix for 6.1.30:
        // in 6.1.20 - function can throw NoSuchWorkflowDefinitionLinkException which is caught in
        // caller
        // in 6.1.30 - function cannot throw NoSuchWorkflowDefinitionLinkException - as it will
        // break flow (uncaught in caller).
        try {
            JournalArticle article = JournalArticleLocalServiceUtil.getArticle(classPK);

            WorkflowDefinitionLink link = null;
            
            long folderId = JournalFolderLocalServiceUtil.getInheritedWorkflowFolderId(article.getFolderId());

    		DDMStructure ddmStructure = article.getDDMStructure();

    		if(ddmStructure != null) {
    			link = WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
    				companyId, groupId, JournalFolder.class.getName(), folderId,
    				ddmStructure.getStructureId(), true);
    		}
    		
    		if (link == null) {
    			link = WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
    						companyId, groupId, JournalFolder.class.getName(),
    						folderId, JournalArticleConstants.DDM_STRUCTURE_ID_ALL,
    						true);
    		}
            
            if(link == null) {
                long structureId = GetterUtil.getLong(article.getStructureId(), 0);
            	link = WorkflowDefinitionLinkLocalServiceUtil.getWorkflowDefinitionLink(companyId, groupId, getClassName(), structureId, 0);
            }
            
            return link;
        } catch (NoSuchWorkflowDefinitionLinkException e) {
            if (_log.isDebugEnabled()) {
                _log.debug("Caught NoSuchWorkflowDefinitionLinkException, returning null");
                _log.debug(e);
            }
            return null;
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }
    // End Section overriding default workflow handler behaviour

}

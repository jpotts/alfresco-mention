package com.metaversant.alfresco.mentions.service;

import com.metaversant.alfresco.mentions.exceptions.MentionNotifierException;
import org.alfresco.model.ContentModel;
import org.alfresco.model.ForumModel;
import org.alfresco.repo.action.executer.MailActionExecuter;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for notifying a list of users about a new comment node in which they are mentioned.
 *
 * Created by jpotts, Metaversant on 7/31/17.
 */
public class MentionNotifier {

    // Dependencies
    private ActionService actionService;
    private NodeService nodeService;
    private SearchService searchService;

    private Logger logger = Logger.getLogger(MentionNotifier.class);

    private static final String NOTIFICATION_SUBJECT = "You were mentioned in an Alfresco comment";
    private static final String NOTIFICATION_TEMPLATE_PATH = "/app:company_home/app:dictionary/app:email_templates/app:notify_email_templates/cm:notify_comment_email.html.ftl";
    private static final QName TYPE_LINK = QName.createQName("http://www.alfresco.org/model/linksmodel/1.0", "link");

    public void notifyMentionedUsers(NodeRef nodeRef, List<String> userNameList) throws MentionNotifierException {
        for (String userName : userNameList) {
            sendNotification(userName, nodeRef);
        }
    }

    private void sendNotification(String recipient, NodeRef nodeRef) throws MentionNotifierException {
        logger.debug("Sending notification to: " + recipient + " for: " + nodeRef);

        // Get the template to use for the notification
        NodeRef notificationTemplate = getNotificationTemplate();

        // Get the node this comment is attached to
        NodeRef contextNodeRef = getContextNodeRef(nodeRef);

        if (TYPE_LINK.equals(nodeService.getType(contextNodeRef))) {
            // No support for links at the moment
            logger.debug("Context node is a link, skipping");
            return;
        }

        // Set up a map of additional template params to be used for rendering the message
        Map<String, Serializable> templateParams = getTemplateContext(nodeRef, contextNodeRef);

        // Use action service to invoke the mail action executer
        Action mailAction = actionService.createAction(MailActionExecuter.NAME);
        mailAction.setParameterValue(MailActionExecuter.PARAM_TO, recipient);
        mailAction.setParameterValue(MailActionExecuter.PARAM_SUBJECT, NOTIFICATION_SUBJECT);
        mailAction.setParameterValue(MailActionExecuter.PARAM_TEMPLATE, notificationTemplate);
        mailAction.setParameterValue(MailActionExecuter.PARAM_TEMPLATE_MODEL, (Serializable) templateParams);
        mailAction.setExecuteAsynchronously(true);
        actionService.executeAction(mailAction, nodeRef);

        logger.debug("Leaving sendNotification");
    }

    private NodeRef getContextNodeRef(NodeRef commentNodeRef) {
        NodeRef commentParent = nodeService.getPrimaryParent(commentNodeRef).getParentRef();
        NodeRef discussionNodeRef = nodeService.getPrimaryParent(commentParent).getParentRef();
        return nodeService.getPrimaryParent(discussionNodeRef).getParentRef();
    }

    private Map<String, Serializable> getTemplateContext(NodeRef nodeRef, NodeRef contextNodeRef) {
        Map<String, Serializable> templateParams = new HashMap<String, Serializable>();
        templateParams.put("contextNodeRef", contextNodeRef);
        return templateParams;
    }

    private NodeRef getNotificationTemplate() throws MentionNotifierException {
        String query = "PATH:\"" + NOTIFICATION_TEMPLATE_PATH + "\"";
        ResultSet resultSet = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
                SearchService.LANGUAGE_FTS_ALFRESCO,
                query
        );

        if (resultSet.length() == 0) {
            throw new MentionNotifierException("Could not fetch notification template");
        }

        if (resultSet.length() > 1) {
            logger.warn("Found more than one matching notification template");
        }

        return resultSet.getNodeRef(0);
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}

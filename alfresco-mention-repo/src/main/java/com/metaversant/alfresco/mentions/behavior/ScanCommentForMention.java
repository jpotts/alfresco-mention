package com.metaversant.alfresco.mentions.behavior;

import com.metaversant.alfresco.mentions.exceptions.MentionNotifierException;
import com.metaversant.alfresco.mentions.service.MentionNotifier;
import com.metaversant.alfresco.mentions.service.MentionScanner;
import org.alfresco.model.ContentModel;
import org.alfresco.model.ForumModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;

import static org.alfresco.model.ContentModel.PROP_CONTENT;

/**
 * This behavior is triggered when comment nodes are updated. It then uses the MentionScanner to scan the comment
 * body for @mentions and the MentionNotifier to notify the list of users that were mentioned.
 *
 * Created by jpotts, Metaversant on 7/31/17.
 */
public class ScanCommentForMention implements NodeServicePolicies.OnUpdateNodePolicy {

    // Dependencies
    private NodeService nodeService;
    private PolicyComponent policyComponent;
    private ContentService contentService;
    private MentionNotifier mentionNotifier;

    // Behaviours
    private Behaviour onUpdateNode;

    private Logger logger = Logger.getLogger(ScanCommentForMention.class);

    public static final String NODE_KEY = ScanCommentForMention.class.getName() + ".node";

    public void init() {
        if (logger.isDebugEnabled()) logger.debug("Initializing mention behaviors");

        // Create behaviours
        this.onUpdateNode = new JavaBehaviour(this, "onUpdateNode", Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        // Bind behaviours to node policies
        this.policyComponent.bindClassBehaviour(
                QName.createQName(NamespaceService.ALFRESCO_URI, "onUpdateNode"),
                ForumModel.TYPE_POST,
                this.onUpdateNode);
    }

    public void onUpdateNode(NodeRef commentNode) {
        if (logger.isDebugEnabled()) logger.debug("Inside onUpdateNode");
        findAndNotifyMentionedUsers(commentNode);
    }

    private void findAndNotifyMentionedUsers(NodeRef commentNode) {
        if (nodeService.exists(commentNode)) {
            if (isCommentNode(commentNode)) {
                logger.debug("Post is a comment");
                ContentReader contentReader = contentService.getReader(commentNode, PROP_CONTENT);
                InputStream is = contentReader.getContentInputStream();
                List<String> userNameList = MentionScanner.getUsers(is);
                if (userNameList.size() > 0) {
                    try {
                        mentionNotifier.notifyMentionedUsers(commentNode, userNameList);
                    } catch (MentionNotifierException mne) {
                        logger.error(mne);
                    }
                }
            }
        }
    }

    private boolean isCommentNode(NodeRef postNode) {
        ChildAssociationRef childRef = nodeService.getPrimaryParent(postNode);
        NodeRef parent = childRef.getParentRef();
        if (ForumModel.TYPE_TOPIC.equals(nodeService.getType(parent))) {
            String parentName = (String) nodeService.getProperty(parent, ContentModel.PROP_NAME);
            if (parentName != null && "Comments".equals(parentName)) {
                return true;
            }
        }
        return false;
    }

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public PolicyComponent getPolicyComponent() {
        return policyComponent;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setMentionNotifier(MentionNotifier mentionNotifier) {
        this.mentionNotifier = mentionNotifier;
    }
}

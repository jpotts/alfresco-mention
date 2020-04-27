# Alfresco Mention Notifications

This add-on adds the ability to add a mention, like "@tuser", to a comment or
discussion post in order to trigger a notification to all mentioned users.

At the present time, this is a repo-tier customization only. There is not any
front-end code included.

## How it works

The code includes a behavior that watches for comment or discussion nodes to be
created or updated. When that happens, the MentionScanner class uses a regex to
find all instances of strings starting with "@". It then sends the list of
mentioned users and the nodeRef to MentionNotifier.

The MentionNotifier class uses Alfresco's mail action to send notifications. It
finds the original nodeRef the comment or post is in reference to and adds that
to the context so that it is available for use by the notification template.

For mentions in comments, the notification includes the name of the folder or
document the comment is in reference to and the content of the comment.

For discussion posts, the notification includes the topic, the site, and the
content of the post.

In both cases the notification includes a link to Share where the mention
took place.

## Caveats

There is currently no front-end code in this add-on so users must know the
username of the user they want to mention. Adding typeahead or other front-end
features might be useful at some point.

This only supports comments on documents and folders and Share discussion
topics and posts. Support for comments on links is possible but left as a to-do
for now.

Usernames containing a period (".") are supported, but they must not end with a
period. For example, "test.user" will work but not "testuser.".

The entire content of the comment, topic, or post is read into a String and
parsed using regex. If comments, topics, or posts are routinely very lengthy,
this might be memory intensive. Because most comments and posts are typically
short, this is currently left as a to-do.

Everyone mentioned in a comment or post will be notified every time the comment
is updated. Comments and posts are not updated that often so this should not be
a problem.   

## Installation

Either download a pre-built AMP or build from source. To build from source,
check out the source code, switch to the alfresco-mention-repo directory, then
run `mvn clean package` to assemble the AMP file.

Copy the AMP file to your server under $ALFRESCO_HOME/amps, then use the MMT to
install the AMP, which is typically done by running `bin/apply_amps.sh`.

## Configuration

This module introduces two new properties that can be set in the alfresco-global.properties
file to control whether comments or discussions are scanned for mentions. By
default, comments are scanned but discussions are not. You can change this by
setting the following properties to the desired values:

    mention.comments.enabled=true
    mention.posts.enabled=false

## Contributions

Contributions are welcome.
   
  
 

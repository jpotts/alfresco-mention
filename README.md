# Alfresco Mention Notifications

This add-on adds the ability to add a mention, like "@tuser", to a comment in
order to trigger a notification to all mentioned users.

At the present time, this is a repo-tier customization only. There is not any
front-end code included.

## How it works

The code includes a behavior that watches for comment nodes to be created or
updated. When that happens, the MentionScanner class uses a regex to find all
instances of strings starting with "@". It then sends the list of mentioned
users and the comment nodeRef to MentionNotifier.

The MentionNotifier class uses Alfresco's mail action to send notifications. It
finds the original nodeRef the comment is in reference to and adds that to the
context so that it is available for use by the notification template.

The notification will include the name of the folder or document the comment is
in reference to and the content of the comment.

## Caveats

There is currently no front-end code in this add-on so users must know the
username of the user they want to mention. Adding typeahead or other front-end
features might be useful at some point.

This only supports comments on documents and folders. Support for comments on
links is possible but left as a to-do for now.

Usernames containing a period (".") are supported, but they must not end with a
period. For example, "test.user" will work but not "testuser.".

The entire content of the comment is read into a String and parsed using regex.
If comments are routinely very lengthy, this might be memory intensive. Because
most comments are typically short, this is currently left as a to-do.

Everyone mentioned in a comment will be notified every time the comment is
updated. Comments are not updated that often so this should not be a problem.   

## Installation

Either download a pre-built AMP or build from source. To build from source,
check out the source code, switch to the alfresco-mention-repo directory, then
run `mvn clean package` to assemble the AMP file.

Copy the AMP file to your server under $ALFRESCO_HOME/amps, then use the MMT to
install the AMP, which is typically done by running `bin/apply_amps.sh`.

## Contributions

Contributions are welcome.
   
  
 

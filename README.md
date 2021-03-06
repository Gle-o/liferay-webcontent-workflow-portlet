# Liferay Web Content Workflow Configuration Portlet

*liferay-webcontent-workflow-portlet*

This portlet to allow assignment of different Liferay workflows to individual Web Content Structures, similar to the request here: [Liferay JIRA LPS-13617](https://issues.liferay.com/browse/LPS-13617 "LPS-13617")


## Supported Products

* Liferay Portal 6.1 CE GA2, GA3 (6.1.1+)
* Liferay Portal 6.1 EE GA2, GA3 (6.1.20+)
* Liferay Portal 6.2 CE GA1 (6.2.0+)
* Liferay Portal 6.2 EE GA1 (6.2.10+)
* Liferay Portal 7.0 M4 (7.0.0+)

Branch 6.1.x holds plugin code for Liferay Portal 6.1 versions.
Branch 6.2.x holds plugin code for Liferay Portal 6.2 versions.
Branch 7.0.x holds plugin code for Liferay Portal 7.0 versions.
Master is sync'ed with branch 7.0.x


### Liferay 7.0

Liferay 7.0 now allows workflow by structure configuration via folders. While users can only set a default workflow in the root/home folder, users can now set either a single workflow, or a workflow by structure configuration per folder.

* If you are starting a new instance of Liferay 7.0, we recommend that you use Liferay's out of the box folder-based workflow by structure configuration
* If you are migrating from Liferay 6.0, you can continue to use this plugin (especially since all your web content is in the root/home folder). The plugin will defer to Liferay's configuration first, and only take effect if Liferay's configuration did not result in a workflow defined. This allows you to migrate from this plugin to Liferay 7.0's configuration.


## Downloads

The latest releases are available from [SourceForge](http://sourceforge.net/projects/permeance-apps/files/liferay-web-content-workflow-configuration/ "Liferay Web Content Workflow Configuration Portlet").

It is also available from the [Liferay Marketplace](http://www.liferay.com/marketplace/-/mp/application/25619448 "Liferay Web Content Workflow Configuration Portlet").


## Usage

This is expected to be installed with a Liferay Workflow Engine, and has been tested with the Liferay Kaleo Workflow Engine. On installing the plugin:

When viewing the Workflow Configuration for a specific community/site, you will see that the "Web Content" asset type has moved to the second section of the page.

![Workflow Configuration](/doc/images/wcwfp3.PNG "Workflow Configuration")

You will also find a new portlet "Web Content Workflow Configuration" below Workflow Configuration in the Content section of the Control Panel in 6.1.

![Control Panel](/doc/images/wcwfp1.PNG "Control Panel")

Or in the Content section of Site Administration area in Control Panel in 6.2.

![Web Content Workflow Configuration](/doc/images/wcwf-6.2-1.png "Web Content Workflow Configuration")

This portlet allows the administrator to select specific Workflows for each structure type available to the site (either in the site itself or in the global scope).

![Web Content Workflow Configuration](/doc/images/wcwf-6.1-1.png "Web Content Workflow Configuration")

## Building

Step 1. Checkout source from GitHub project

    % git  clone  https://github.com/permeance/liferay-webcontent-workflow-portlet

Step 2. Build and package

    % mvn  -U  clean  package

This will build "liferay-webcontent-workflow-portlet-XXX.war" in the targets tolder.

NOTE: You will require JDK 1.6+ and Maven 3.


## Installation

### Liferay Portal + Apache Tomcat Bundle

eg.

Deploy "liferay-webcontent-workflow-portlet-LPX.X-X.X.X.X.war" to "$LIFERAY_HOME/deploy" folder.

## License

Liferay Web Content Workflow Configuration Portlet is available under GNU Public License version 3.0 (GPLv3). A copy of the license is attached in the code package.

## Project Team

* Chun Ho - chun.ho@permeance.com.au

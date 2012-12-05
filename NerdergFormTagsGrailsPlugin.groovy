class NerdergFormTagsGrailsPlugin {
    // the plugin version
    def version = "2.1.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/index.gsp"
    ]

    def title = "Nerderg Formtags Plugin" // Headline display name of the plugin
    def author = "Peter McNeil"
    def authorEmail = "peter@nerderg.com"
    def description = '''\
A taglib for forms. see <a href="http://nerderg.com/Nerderg+Form+Taglib">documentation</a>

Bringing Readability, Convention, Consistency and CSS to form design.

It gives you:

    * Labels and structure without cluttering up your GSP form
    * Standard error marking and handling without cluttering up your GSP form
    * Compatibility with browsers
    * Javascript assistance with pickers and choosers where appropriate using jQuery
    * CSS themeable rendering of forms

e.g.
{code:xml}
<nerderg:formfield label='Select one' field='age' bean='${myCommand}'> <g:select name="age" from="${18..65}" value="${myCommand.age}"/>
</nerderg:formfield>
<nerderg:inputfield label='First name' field='firstname' bean='${myCommand}'/>
<nerderg:datefield label='Date of birth' field='dob' bean='${myCommand}' format='dd/MM/yyyy'/>
<nerderg:timefield label='Time of accident' field='toa' bean='${myCommand}' format='hh:mm a'/>
<nerderg:datetimefield label='Start' field='eventStart' bean='${myCommand}' format='dd/MM/yyyy hh:mm'/>
<nerderg:checkboxgroup label='Options' field='selOptions' from='${allOptions}' bean='${myCommand}'/>
<nerderg:asJSArray var="options" items='["opt1", "opt2", "opt3"]'/>
{code}

'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/nerderg-formtags"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "nerdErg Pty Ltd", url: "http://nerderg.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "JIRA", url: "https://github.com/pmcneil/nerderg-formtags/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/pmcneil/nerderg-formtags" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}

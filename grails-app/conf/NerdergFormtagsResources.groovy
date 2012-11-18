modules = {
    nerdergFormTags {
        dependsOn 'jquery, jquery-ui'
        resource url:[plugin: 'nerdergFormtags', dir:'js', file:"jquery.mousewheel.min.js"]
        resource url:[plugin: 'nerdergFormtags', dir:'js', file:"jquery.timeentry.min.js"]
        resource url:[plugin: 'nerdergFormtags', dir:'js', file:"nerdergFormtags.js"]
        resource url:[plugin: 'nerdergFormtags', dir:'css', file:"nerdergFormtags.css"]

    }
}
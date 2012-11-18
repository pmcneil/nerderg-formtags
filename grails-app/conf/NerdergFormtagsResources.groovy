modules = {
    nerdergFormTags {
        dependsOn 'jquery, jquery-ui'
        resource url:[plugin: 'nerdergFormTags', dir:'js', file:"jquery.mousewheel.min.js"]
        resource url:[plugin: 'nerdergFormTags', dir:'js', file:"jquery.timeentry.min.js"]
        resource url:[plugin: 'nerdergFormTags', dir:'js', file:"nerdergFormtags.js"]
        resource url:[plugin: 'nerdergFormTags', dir:'css', file:"nerdergFormtags.css"]

    }
}
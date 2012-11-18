package com.nerderg.taglib

import org.codehaus.groovy.grails.validation.Validateable

/**
 * Test command class
 * @author pmcneil
 */
@Validateable
class FauxCommand {
    String value
    Date date
    List<String> options = ["opt1","opt2"]
    static constraints = {
        value(nullable: false, blank: false, maxSize: 10)
        options(nullable: false)
    }
}

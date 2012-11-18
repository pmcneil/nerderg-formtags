/*
Copyright 2009, 2010 Peter McNeil

This file is part of NerdergFormTags.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.nerderg.taglib

import com.mdimension.jchronic.Chronic
import java.text.SimpleDateFormat

class NerdergFormtagsTagLib {
    static namespace = "nerderg"


    void outputAttributes(attrs) {
        attrs.remove('tagName') // Just in case one is left
        def writer = getOut()
        attrs.each {k, v ->
            switch (k) {
                case 'disabled':
                case 'checked':
                case 'readonly':
                    if (v && !(v ==~ /(?i)false/)) {
                        writer << " $k='$k'"
                    }
                    break
                default:
                    if (v) {
                        writer << " $k='${v.encodeAsHTML()}'"
                    }
            }
        }
    }

    void outputWithLabel(attrs, payload) {
        def label = attrs.label ? attrs.remove("label").replaceAll('\\n', '<br>') : ''
        def delim = attrs.delim != null ? attrs.delim : ":"
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def error = attrs.remove("error")
        def name = attrs.name ? attrs.remove("name") : field
        def table = false
        if (attrs.table && !(attrs.remove('table') ==~ /(?i)false/)) {
            table = true
        }
        def div = table ? 'tr' : 'div'
        def span = table ? 'td' : 'span'
        def value = attrs.containsKey('value') ? attrs.remove("value").toString() : null
        def displayValue

        def err = label
        def errStyle = ""
        try {
            if (bean?.errors?.hasFieldErrors(field)) {
                errStyle = ' errors'
                err = g.message(error: bean.errors.getFieldError(field))
            } else {
                if (error) {
                    errStyle = ' errors'
                    err = error.encodeAsHTML()
                }
            }
        } catch (e) {
            log.debug "errors error: $e.message"
        }
        err = err.replaceAll(/'/, "&apos;")
        try {
            if (bean) {
                value = bean[field] ? bean[field] : ""
                if (bean instanceof java.util.Map) {
                    displayValue = value.encodeAsHTML().replaceAll(/'/, '&rsquo;')
                } else {
                    displayValue = fieldValue(bean: bean, field: field).replaceAll(/'/, '&rsquo;')
                }
            } else if (value) {
                displayValue = value.encodeAsHTML().replaceAll(/'/, '&rsquo;')
            }
        } catch (e) {
            log.debug "value error: $e.message"
        }

        if (!value) {
            value = ""
        }
        if (!displayValue) {
            displayValue = ""
        }

        if (label) {
            out << "<$div class='nerderg-formtags prop'>\n"
            out << "<$span class='name'><label"
            if (field) {out << " for='$field'"}
            out << ">$label$delim</label></$span>\n"
        }
        out << "<$span class='value$errStyle' title='$err'>"

        payload.call(name, displayValue, value)

        out << "</$span>\n"
        if (label) {
            out << "</$div>\n"
        }

    }

    /*
    * attributes:
    *  field, label, bean
    */
    def formfield = { attrs, body ->
        outputWithLabel(attrs) {name, displayValue, value ->
            if (!attrs.hideValue) {
                if (displayValue) {
                    out << "$displayValue "
                } else if (value) {
                    out << value.encodeAsHTML() + " "
                }
            }
            out << body()
        }
    }

    /*
     * attributes:
     *  field, label, bean
     */
    def inputfield = { attrs, body ->
        def type = attrs.remove("type")
        if (!type) {
            if (attrs.field ==~ /passw(or)?d/) {
                type = "password"
            } else {
                type = "text"
            }
        }
        def pre = attrs.remove("pre")
        def maxSize
        if (attrs.bean && attrs.field && attrs.bean.constraints) {
            maxSize = attrs.bean.constraints[attrs.field]?.getMaxSize()
        }
        outputWithLabel(attrs) {name, displayValue, value ->
            if (pre) {
                out << pre
            }
            out << "<input type='$type' name='$name' value='$displayValue'"
            if (!attrs.id) {
                out << " id='$name'"
            }
            if (maxSize && !attrs.maxlength) {
                out << " maxlength='$maxSize'"
            }
            outputAttributes(attrs)
            out << "/>"
            out << body()
        }
    }

    def datetimefield = { attrs, body ->

        outputWithLabel(attrs) {name, displayValue, value ->

            def date = ""
            def time = ""
            if (attrs.format) {
                displayValue = formatDate(attrs.remove('format'), value)
            }
            if (displayValue) {
                def valueSplit = displayValue.split(" ")
                if (valueSplit) {
                    date = valueSplit[0]
                    time = valueSplit.size() > 1 ? valueSplit[1] : ""
                    if (time && valueSplit.size() == 3) {
                        time += " ${valueSplit[2]}"
                    }
                }
            }
            out << "<input type='text' name='${name}.date' value='$date' class='date'"
            if (!attrs.size) {
                out << " size='10'"
            }
            if (!attrs.id) {
                out << " id='${name}.date'"
            }
            outputAttributes(attrs)
            out << "/>&nbsp;"
            out << "<input type='text' name='${name}.time' value='$time' class='time'"
            if (!attrs.size) {
                out << " size='7'"
            }
            if (!attrs.title) {
                out << " title='type or use mouse wheel to change'"
            }
            if (!attrs.id) {
                out << " id='${name}.time'"
            }
            outputAttributes(attrs)
            out << "/>"
            out << body()
        }
    }

    private String formatDate(format, value) {
        if (value) {
            try {
                if (value instanceof Date) {
                    SimpleDateFormat f = new SimpleDateFormat(format)
                    return f.format(value)
                }
                if (value instanceof String) {
                    try {
                        Date d = Date.parse(format, value)
                        return value
                    } catch (e) {
                        //next
                    }
                }
                def span = Chronic.parse(value.toString())
                return span.endCalendar.format(format)
            } catch (IllegalArgumentException e) {
                log.debug e
                return "$value"
            } catch (e) {
                log.debug e
                return ""
            }
        } else {
            return ""
        }
    }

    def datefield = { attrs, body ->
        outputWithLabel(attrs) {name, displayValue, value ->
            def date
            if (attrs.format) {
                String f = attrs.remove('format')
                date = formatDate(f, value)
            } else {
                date = displayValue ? displayValue.split(" ")[0] : ""
            }
            out << "<input type='text' name='$name' value='$date' class='date'"
            if (!attrs.size) {
                out << " size='10'"
            }
            if (!attrs.id) {
                out << " id='$name'"
            }
            outputAttributes(attrs)
            out << "/>"
            out << body()
        }
    }

    def timefield = { attrs, body ->
        outputWithLabel(attrs) {name, displayValue, value ->
            def time
            if (attrs.format) {
                time = formatDate(attrs.remove('format'), value)
            } else {
                if (displayValue) {
                    def splitValue = displayValue.split(" ")
                    time = splitValue.size() > 1 ? splitValue[1] : splitValue[0]
                } else {
                    time = ""
                }
            }
            out << "<input type='text' name='$name' value='$time' class='time'"
            if (!attrs.size) {
                out << " size='6'"
            }
            if (!attrs.id) {
                out << " id='$name'"
            }
            outputAttributes(attrs)
            out << "/>"
            out << body()
        }
    }

    def checkboxgroup = { attrs, body ->

        def allValues = []
        allValues.addAll(attrs.remove("from"))
        def subset = attrs.remove("subset")

        outputWithLabel(attrs) {name, unused0, value ->
            if (!subset) {
                subset = (value instanceof Collection) ? value : [value]
            }
            out << "\n<ul class='cblist'>\n"
            allValues.each {val ->
                def checked = subset?.contains(val)
                def cbvalue = val.hasProperty('id') ? val.id : val
                out << "<li><input type='checkbox' name='$name' value='$cbvalue'"
                if (checked) {
                    out << " checked='checked'"
                }
                outputAttributes(attrs)
                out << ">&nbsp;$val</input>"
                out << body()
                out << "</li>\n"
            }
            out << "</ul>\n"
        }
    }

    def asJSArray = {attrs ->
        out << "<script type='text/javascript'>\n"
        def items = attrs.items
        out << attrs.var.toString().encodeAsJavaScript() + " = "
        out << items.encodeAsJSON()
        out << ";\n</script>"
    }

}
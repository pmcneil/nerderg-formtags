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

class NerdergFormtagsTagLibTests extends GroovyTestCase {

    def taglib

    protected void setUp() {
        super.setUp()
        taglib = new NerdergFormtagsTagLib()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testFormField() {

        def expected = """<div class='prop'>
<span class='name'><label>Result:</label></span>
<span class='value' title='Result'>500</span>
</div>\n"""
        def result = taglib.formfield(label: 'Result') {"500"}
        assert result == expected

        //check for null on extra param value see JIRA NFT-1
        expected = """<div class='prop'>
<span class='name'><label for='result'>Result:</label></span>
<span class='value' title='Result'>500</span>
</div>\n"""
        result = taglib.formfield(label: 'Result', field: 'result', disabled: null, size: null) {"500"}
        assert result == expected

        expected = """<div class='prop'>
<span class='name'><label for='result'>Result:</label></span>
<span class='value' title='Result'>500</span>
</div>\n"""
        result = taglib.formfield(label: 'Result', field: 'result') {"500"}
        assert result == expected

        //test invalid bean value error class added
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value errors' title='Property [value] of class [class com.nerderg.taglib.FauxCommand] cannot be null'>500</span>
</div>\n"""
        def bean = new FauxCommand()
        assert !validate(bean)
        result = taglib.formfield(label: 'Result', field: 'value', bean: bean) {"500"}
        assert result == expected

        //test valid bean value
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'>&lt;hi there&gt; 500</span>
</div>\n"""
        bean = new FauxCommand(value: '<hi there>')
        assert validate(bean)
        result = taglib.formfield(label: 'Result', field: 'value', bean: bean) {"500"}
        assert result == expected

        //test delimiter
        expected = """<div class='prop'>
<span class='name'><label>Result</label></span>
<span class='value' title='Result'>500</span>
</div>\n"""
        result = taglib.formfield(label: 'Result', delim: '') {"500"}
        assert result == expected

        //test table format
        expected = """<tr class='prop'>
<td class='name'><label>Result:</label></td>
<td class='value' title='Result'>500</td>
</tr>\n"""
        result = taglib.formfield(label: 'Result', table: 'true') {"500"}
        assert result == expected

        //test explicit value not used if bean present
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'>&lt;hi there&gt; 500</span>
</div>\n"""
        result = taglib.formfield(label: 'Result', field: 'value', bean: bean, value: 'explicit value') {"500"}
        assert result == expected

        //test explicit value used if bean not present
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'>explicit value 500</span>
</div>\n"""
        result = taglib.formfield(label: 'Result', field: 'value', value: 'explicit value') {"500"}
        assert result == expected

        //test explicit value, bean not present, no body. note space after value is expected
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'>explicit value </span>
</div>\n"""
        result = taglib.formfield(label: 'Result', field: 'value', value: 'explicit value')
        assert result == expected

        //test explicit value HTML encoding
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'>&lt;explicit value&gt; </span>
</div>\n"""
        result = taglib.formfield(label: 'Result', field: 'value', value: '<explicit value>')
        assert result == expected

    }

    def testInputfield(){
        //bean field with error
        def expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value errors' title='Property [value] of class [class com.nerderg.taglib.FauxCommand] cannot be null'><input type='text' name='value' value='' id='value' maxlength='10'/></span>
</div>\n"""
        def bean = new FauxCommand()
        assert !validate(bean)
        def result = taglib.inputfield(label: 'Result', field: 'value', bean: bean)
        assert result == expected

        //bean field sans error
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' id='value' maxlength='10'/>ms</span>
</div>\n"""
        bean = new FauxCommand(value: '<blah>')
        assert validate(bean)
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean){"ms"}
        assert result == expected

        //bean field override maxSize
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' id='value' maxlength='6'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, maxlength: 6)
        assert result == expected

        //bean field override id
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' maxlength='10' id='1.value'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, id: "1.value")
        assert result == expected

        //bean field added attribute
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' id='value' maxlength='10' size='10'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, size: 10)
        assert result == expected

        //bean field added disabled truth
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' id='value' maxlength='10' disabled='disabled'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: true)
        assert result == expected
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: "blah")
        assert result == expected

        //bean field added disabled falth
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' id='value' maxlength='10'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: false)
        assert result == expected
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: "")
        assert result == expected
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: null)
        assert result == expected
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: "false")
        assert result == expected
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, disabled: "fAlSe")
        assert result == expected

        //bean field added readonly truth
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='&lt;blah&gt;' id='value' maxlength='10' readonly='readonly'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, readonly: true)
        assert result == expected
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, readonly: "blah")
        assert result == expected

        //bean field set type
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='password' name='value' value='&lt;blah&gt;' id='value' maxlength='10'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'value', bean: bean, type: 'password')
        assert result == expected

        //bean field set type by field name like /passw(or)?d/
        expected = """<div class='prop'>
<span class='name'><label for='passwd'>Result:</label></span>
<span class='value' title='Result'><input type='password' name='passwd' value='secret' id='passwd'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'passwd', value: 'secret')
        assert result == expected

        expected = """<div class='prop'>
<span class='name'><label for='password'>Result:</label></span>
<span class='value' title='Result'><input type='password' name='password' value='secret' id='password'/></span>
</div>\n"""
        result = taglib.inputfield(label: 'Result', field: 'password', value: 'secret')
        assert result == expected
    }

    def testDateField() {
        //Use a null Date with a format see JIRA NFT-2
        def expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date' value='' class='date' size='10' id='date'/>On the night of the prom</span>
</div>\n"""
        def result = taglib.datefield(label: 'Result', field: 'date', value: null, format: 'yyyy-MM-dd'){"On the night of the prom"}
        assert result == expected

        //Use a Date object with a format
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date' value='2010-10-28' class='date' size='10' id='date'/>On the night of the prom</span>
</div>\n"""
        result = taglib.datefield(label: 'Result', field: 'date', value: makeDate('28/10/2010 10:47'), format: 'yyyy-MM-dd'){"On the night of the prom"}
        assert result == expected

        //Use a string date representation with a format
        result = taglib.datefield(label: 'Result', field: 'date', value: '28/10/2010 10:47', format: 'yyyy-MM-dd'){"On the night of the prom"}
        assert result == expected

        //Use a string date representation without a format (pre formatted space separated time)
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date' value='28/10/2010' class='date' size='10' id='date'/></span>
</div>\n"""
        result = taglib.datefield(label: 'Result', field: 'date', value: '28/10/2010 10:47')
        assert result == expected

        //Use a string date representation with a format - native date string e.g. Thu Oct 28 10:47:00 EST 2010
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date' value='2010-10-28' class='date' size='10' id='date'/></span>
</div>\n"""
        result = taglib.datefield(label: 'Result', field: 'date', value: makeDate('28/10/2010 10:47').toString(), format: 'yyyy-MM-dd')
        assert result == expected

        def span = Chronic.parse("tomorrow 2pm")
        def date = span.endCalendar.format('dd/MM/yyyy')

        //Use a string relative date representation with a format e.g. "tomorrow 2pm"
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date' value='${date}' class='date' size='10' id='date'/></span>
</div>\n"""
        result = taglib.datefield(label: 'Result', field: 'date', value: "tomorrow 2pm", format: 'dd/MM/yyyy')
        assert result == expected

        //bean value formatted
        def command = new FauxCommand(value: 'hi', date: makeDate('29/10/2010 10:15'))
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date' value='2010-10-29' class='date' size='10' id='date'/></span>
</div>\n"""
        result = taglib.datefield(label: 'Result', field: 'date', bean: command, format: 'yyyy-MM-dd')
        assert result == expected

        //bean string pre formatted space separated
        command = new FauxCommand(value: '30/11/2010 12:15', date: makeDate('29/10/2010 10:15'))
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value' value='30/11/2010' class='date' size='10' id='value'/></span>
</div>\n"""
        result = taglib.datefield(label: 'Result', field: 'value', bean: command)
        assert result == expected

    }

    def testTimeField() {
        //Use a Date object with a format
        def expected = """<div class='prop'>
<span class='name'><label for='time'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='time' value='10:47 AM' class='time' size='6' id='time'/>On the night of the prom</span>
</div>\n"""
        def result = taglib.timefield(label: 'Result', field: 'time', value: makeDate('28/10/2010 10:47'), format: 'hh:mm a'){"On the night of the prom"}
        println result
        assert result == expected
        //Use a string date representation with a format
        result = taglib.timefield(label: 'Result', field: 'time', value: '28/10/2010 10:47', format: 'hh:mm a'){"On the night of the prom"}
        assert result == expected

    }

    def testDateTimeField() {
        //Use a Date object with a format
        def expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date.date' value='2010-10-28' class='date' size='10' id='date.date'/>&nbsp;<input type='text' name='date.time' value='10:47' class='time' size='7' title='type or use mouse wheel to change' id='date.time'/>Try to be accurate</span>
</div>\n"""
        def result = taglib.datetimefield(label: 'Result', field: 'date', value: makeDate('28/10/2010 10:47'), format: 'yyyy-MM-dd hh:mm'){"Try to be accurate"}
        assert result == expected

        //Use a string date representation with a format
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date.date' value='2010-10-28' class='date' size='10' id='date.date'/>&nbsp;<input type='text' name='date.time' value='10:47' class='time' size='7' title='type or use mouse wheel to change' id='date.time'/></span>
</div>\n"""
        result = taglib.datetimefield(label: 'Result', field: 'date', value: '28/10/2010 10:47', format: 'yyyy-MM-dd hh:mm')
        assert result == expected

        //Use a string date representation without a format (pre formatted space separated time)
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date.date' value='28/10/2010' class='date' size='10' id='date.date'/>&nbsp;<input type='text' name='date.time' value='10:47' class='time' size='7' title='type or use mouse wheel to change' id='date.time'/></span>
</div>\n"""
        result = taglib.datetimefield(label: 'Result', field: 'date', value: '28/10/2010 10:47')
        assert result == expected

        //Use a string date representation with a format - native date string e.g. Thu Oct 28 10:47:00 EST 2010
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date.date' value='2010-10-28' class='date' size='10' id='date.date'/>&nbsp;<input type='text' name='date.time' value='10:47' class='time' size='7' title='type or use mouse wheel to change' id='date.time'/></span>
</div>\n"""
        result = taglib.datetimefield(label: 'Result', field: 'date', value: makeDate('28/10/2010 10:47').toString(), format: 'yyyy-MM-dd hh:mm')
        assert result == expected

        def span = Chronic.parse("tomorrow 2pm")
        def date = span.endCalendar.format('dd/MM/yyyy')
        def time = span.endCalendar.format('hh:mm')

        //Use a string relative date representation with a format e.g. "tomorrow 2pm"
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date.date' value='${date}' class='date' size='10' id='date.date'/>&nbsp;<input type='text' name='date.time' value='${time}' class='time' size='7' title='type or use mouse wheel to change' id='date.time'/></span>
</div>\n"""
        result = taglib.datetimefield(label: 'Result', field: 'date', value: "tomorrow 2pm", format: 'dd/MM/yyyy hh:mm')
        assert result == expected

        //bean value formatted
        def command = new FauxCommand(value: 'hi', date: makeDate('29/10/2010 10:15'))
        expected = """<div class='prop'>
<span class='name'><label for='date'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='date.date' value='2010-10-29' class='date' size='10' id='date.date'/>&nbsp;<input type='text' name='date.time' value='10:15' class='time' size='7' title='type or use mouse wheel to change' id='date.time'/></span>
</div>\n"""
        result = taglib.datetimefield(label: 'Result', field: 'date', bean: command, format: 'yyyy-MM-dd hh:mm')
        assert result == expected

        //bean string pre formatted space separated
        command = new FauxCommand(value: '30/11/2010 12:15', date: makeDate('29/10/2010 10:15'))
        expected = """<div class='prop'>
<span class='name'><label for='value'>Result:</label></span>
<span class='value' title='Result'><input type='text' name='value.date' value='30/11/2010' class='date' size='10' id='value.date'/>&nbsp;<input type='text' name='value.time' value='12:15' class='time' size='7' title='type or use mouse wheel to change' id='value.time'/></span>
</div>\n"""
        result = taglib.datetimefield(label: 'Result', field: 'value', bean: command)
        assert result == expected

    }

    def testCheckboxgroup() {
        def expected = """<div class='prop'>
<span class='name'><label for='options'>Result:</label></span>
<span class='value' title='Result'>
<ul class='cblist'>
<li><input type='checkbox' name='options' value='opt1' checked='checked'>&nbsp;opt1</input>I'm a hippo</li>
<li><input type='checkbox' name='options' value='opt2'>&nbsp;opt2</input>I'm a hippo</li>
<li><input type='checkbox' name='options' value='opt3' checked='checked'>&nbsp;opt3</input>I'm a hippo</li>
</ul>
</span>
</div>\n"""
        def result = taglib.checkboxgroup(label: 'Result', field: 'options', from: ["opt1","opt2","opt3"], subset: ["opt1","opt3"]){"I'm a hippo"}
        assert result == expected

        expected = """<div class='prop'>
<span class='name'><label for='options'>Result:</label></span>
<span class='value' title='Result'>
<ul class='cblist'>
<li><input type='checkbox' name='options' value='opt1' checked='checked'>&nbsp;opt1</input></li>
<li><input type='checkbox' name='options' value='opt2' checked='checked'>&nbsp;opt2</input></li>
<li><input type='checkbox' name='options' value='opt3'>&nbsp;opt3</input></li>
</ul>
</span>
</div>\n"""
        def command = new FauxCommand(value: 'hi')
        result = taglib.checkboxgroup(label: 'Result', from: ["opt1","opt2","opt3"], field: 'options', bean: command)
        assert result == expected

        expected = """<div class='prop'>
<span class='name'><label for='options'>Result:</label></span>
<span class='value errors' title='Property [options] of class [class com.nerderg.taglib.FauxCommand] cannot be null'>
<ul class='cblist'>
<li><input type='checkbox' name='options' value='opt1'>&nbsp;opt1</input></li>
<li><input type='checkbox' name='options' value='opt2'>&nbsp;opt2</input></li>
<li><input type='checkbox' name='options' value='opt3'>&nbsp;opt3</input></li>
</ul>
</span>
</div>\n"""
        command = new FauxCommand(value: 'hi', options: null)
        validate(command)
        result = taglib.checkboxgroup(label: 'Result', from: ["opt1","opt2","opt3"], field: 'options', bean: command)
        assert result == expected
    }

    def testJSArray() {
        //Use a Date object with a format
        def expected = """<script type='text/javascript'>
options = ["opt1","opt2","opt3"];
</script>"""
        def result = taglib.asJSArray(var: 'options', items: ['opt1', 'opt2', 'opt3'])
        assert result == expected

        expected = """<script type='text/javascript'>
options = {"a":"opt1","b":"opt2","c":"opt3"};
</script>"""
        result = taglib.asJSArray(var: 'options', items: [a: 'opt1', b: 'opt2', c: 'opt3'])
        assert result == expected

        expected = """<script type='text/javascript'>
command = {"class":"com.nerderg.taglib.TestBean","date":"2012-02-22T13:00:00Z","options":["opt1","opt2"],"value":"hello world"};
</script>"""
        TestBean command = new TestBean(value: 'hello world', date: Date.parse('dd/MM/yyyy', '23/02/2012'))
        result = taglib.asJSArray(var: 'command', items: command)
        assert result == expected
    }

    def makeDate(str) {
        SimpleDateFormat f = new SimpleDateFormat('dd/MM/yyyy hh:mm')
        return f.parse(str)
    }

    def validate(bean){
        if(!bean.validate()){
            bean.errors.each {
                println it
            }
            return false
        }
        return true
    }
}

class TestBean {
    String value
    Date date
    List<String> options = ["opt1","opt2"]
}

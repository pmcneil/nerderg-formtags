nerderg-formtags
================

nerdErg Form Taglib, bringing Readability, Convention, Consistency and CSS to form design.

It gives you:

* Labels and structure without cluttering up your GSP form
* Standard error marking and handling without cluttering up your GSP form
* Compatibility with browsers
* Javascript assistance with pickers and choosers where appropriate using jQuery
* CSS themeable rendering of forms

    `<nerderg:formfield label='Select one' field='age' bean='${myCommand}'> <g:select name="age" from="${18..65}" value="${myCommand.age}"/>'
    `</nerderg:formfield>`
    `<nerderg:inputfield label='First name' field='firstname' bean='${myCommand}'/>`
    `<nerderg:datefield label='Date of birth' field='dob' bean='${myCommand}' format='dd/MM/yyyy'/>`
    `<nerderg:timefield label='Time of accident' field='toa' bean='${myCommand}' format='hh:mm a'/>`
    `<nerderg:datetimefield label='Start' field='eventStart' bean='${myCommand}' format='dd/MM/yyyy hh:mm'/>`
    `<nerderg:checkboxgroup label='Options' field='selOptions' from='${allOptions}' bean='${myCommand}'/>`
    `<nerderg:asJSArray var="options" items='["opt1", "opt2", "opt3"]'/>`

General attributes
------------------
label: the Label you wish to display for the field. if not set no label is displayed. Labels can have embedded \n chars that are translated to <br> to force a new line in a label
delim: the Delimiter char displayed after a label. By default a ":" is displayed after the label.
bean: the bean the field is contained in. If this bean has an errors property then the errors are highlighted.
field: the beans field to display
name: the name of the field. If no id attribute is set the id is also set to this value
table: if set true then a table is used to layout the label and value. If false CSS is used for layout. Default is false, use true for IE6 compatibility
value: if bean has not been set then this can be used to directly set the value for the field.

Notes:

You can add any attributes that make sense for an input of the type being used and they'll be automatically included. Specifying a name attribute for example will override any auto set name. This is useful when you have multiple sets of inputs with the same field name, so you might have name="${index}.firstName"
Specifying a size attribute on text inputs is also a good example of an additional attribute.
Boolean attributes like disabled, checked and readonly are correctly inserted if the value evaluates to Groovy true and left out completely if the value evaluates to false. So adding an attribute checked="${true}" will insert the attribute checked='checked'. Truth is extended to mean any non zero-length string that !(value ==~ /(?i)false/) so if you have checked='false' then checked will not be inserted.
As you'd hope/expect all the values are HTML encoded, and protected from null value errors.
The bean attribute can be just a map of values.


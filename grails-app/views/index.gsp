<%--
  User: pmcneil
  Date: 18/11/12
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nerderg Form Tags Test</title>
    <g:javascript library="jquery" plugin="jquery"/>
    <jqui:resources theme="ui-lightness"/>
    <g:javascript src="jquery.mousewheel.min.js" />
    <g:javascript src="jquery.timeentry.min.js" />
    <g:javascript src="nerdergFormtags.js" />
    <script language="JavaScript">
    nerdergFormTags.dateImg = "${resource(dir: 'images/nerderg-icons', file: 'date.png')}";
    nerdergFormTags.dateFormat = "dd/mm/yy";
    </script>
    <link rel="stylesheet" href="${resource(dir:'css',file: 'nerdergFormtags.css')}" />
<body>
<div id="pageBody">
    <h1>nerdErg Form Tags Test</h1>
    <p>An example of each tag is shown below.</p>

    <h3>CSS formatted</h3>
    <div>
        <nerderg:formfield label='Select one' field='age'>
            <g:select name="age" from="${18..65}" value="45"/>
        </nerderg:formfield>

        <nerderg:inputfield label='First name' field='firstname' value="nerdErg"/>
        <nerderg:datefield label='Date of birth' field='dob' bean='[dob: new Date()]' format='dd/MM/yyyy'/>
        <nerderg:timefield label='Time of birth' field='tob' bean='[tob: new Date()]' format='hh:mm a'/>
        <nerderg:datetimefield label='When' field='date' bean='[date: "Tomorrow 2pm"]' format='dd/MM/yyyy hh:mm a'/>
        <nerderg:checkboxgroup label='Options' field='selOptions' from='["opt1", "opt2", "opt3"]' subset='["opt2"]'/>
    </div>

    <h3>Inlined input fields</h3>
    <div class="inlineInputFields">
        <nerderg:inputfield label='Title' field='ititle' value="King"/>
        <nerderg:inputfield label='First name' field='ifirstname' value="Fred"/>
        <nerderg:inputfield label='Last name' field='ilastname' value="Bloggs"/>
    </div>

    <h3>Table formatted</h3>
    <table>
        <nerderg:formfield table="true" label='Select one' field='age'>
            <g:select name="age" from="${18..65}" value="45"/>
        </nerderg:formfield>

        <nerderg:inputfield table="true" label='First name' field='tfirstname' value="nerdErg"/>
        <nerderg:datefield table="true" label='Date of birth' field='tdob' bean='[tdob: new Date()]' format='dd/MM/yyyy'/>
        <nerderg:timefield table="true" label='Time of birth' field='ttob' bean='[ttob: new Date()]' format='hh:mm a'/>
        <nerderg:datetimefield table="true" label='When' field='tdate' bean='[tdate: new Date()]' format='dd/MM/yyyy hh:mm a'/>
        <nerderg:checkboxgroup table="true" label='Options' field='tselOptions' from='["opt1", "opt2", "opt3"]' subset='["opt2","opt1"]'/>
    </table>

    <nerderg:asJSArray var="options" items='["opt1", "opt2", "opt3"]'/>

</div>
</body>
</html>

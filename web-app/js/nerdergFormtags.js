/* 
 * Nerderg Form Tags support javascript file.
 */
(nerdergFormTags = {
    dateFormat: "dd/mm/yy", //date format that datepicker uses
    dateImg: "../images/icons/date.png"
});

$(function(){
    $('input.date').datepicker({
        dateFormat: nerdergFormTags.dateFormat,
        showOn: 'button',
        buttonImage: nerdergFormTags.dateImg,
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true
    })

    $('input.time').timeEntry({
        spinnerImage: '',
        ampmPrefix: ' '
    });
});

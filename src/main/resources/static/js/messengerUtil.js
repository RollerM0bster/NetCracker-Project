let userId = $("#zzz").attr("value");
let name = $("#mmm").attr("value");
let writeButtons = $(".write-button");

for (let button of writeButtons) {
    $(button).click(() => {
        write($(button).attr("data-recipientId"));
    });
}

function write(recipientId) {
    const regexp = /(.+?\/).*/;
    let token = $('#_csrf').attr('content');
    let header = $('#_csrf_header').attr('content');
    let url = document.URL.match(regexp)[1] + "messenger/";
    $.ajax({
        type: 'POST',
        url: url + userId + "/chat/" + recipientId,
        beforeSend: (xhr) => xhr.setRequestHeader(header, token),
        cache: false,
        async: false
    });
    window.location.href = url + recipientId;
}

function writeToEveryone() {
    let message = $("#message-text").val();
    if (message !== "") {
        for (let user of writeButtons) {
            let id = $(user).attr("data-recipientId");
            let username = $(user).attr("data-recipientName");
            sendMessage(message, userId, id, name, username);
        }
    }
    let modal = $("#modal-body-id");
    modal.empty();
    modal.text("Message sent successfully");
}
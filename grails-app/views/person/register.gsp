<%@ page contentType="text/html"%>
<html>
<head>
    <style type="text/css">
        body {

        }
        .container {

        }
        .align-right {
            text-align: right;
        }
        .align-left {
            text-align: left;
        }
        .align-center {
            text-align: center;
        }

        .align-justify {
            text-align: justify;
        }
        .navbar {
            position: relative;
            min-height: 50px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            padding-right: 15px;
            padding-left: 15px;
        }
        .navbar-default {
            background-color: #009688;
            color: rgba(255, 255, 255, .84);
        }
    </style>
</head>

<body style="font-family: RobotoDraft,Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-size: 12px;font-weight: 300;line-height: 1.42857143;color: #333;">
<div style="text-align: center;">
    <div style="padding-right: 15px;padding-left: 15px;margin-right: auto;margin-left: auto;min-width: 500px;max-width: 750px;">
        <div style="position: relative;min-height: 50px;margin-bottom: 20px;border: 1px solid transparent;padding-right: 15px;padding-left: 15px;background-color: #009688;color: rgba(255, 255, 255, .84);text-align: left;">
            <a href="${urlServidor}"><img src="cid:logo" /></a>
        </div>
        <div style="padding-right: 15px;padding-left: 15px;margin-right: auto;margin-left: auto;min-width: 500px;max-width: 750px;">
            <div style="text-align: justify;">
                <p style="margin: 0 0 10px;"><g:message code="user.newUser.email.text" args="[nome]" /> </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>

# Zuora Callout Viewer
Zuora callout is good mechanism for integration, when the business events happen, your application will be notified with rich event context, so that you can continue the workflow.

This is a sample application which is able to receive callout request from Zuora, and to diagnose the callout request by viewing request informations, parameters, headers, raw XML content, parsed event context.

## How to Run
1. To config the notification settings in Zuora side
  + set the `base url` to : **https://zuora-callout-viewer.herokuapp.com/callout**
  + add necessary parameters
  + make sure you are using `POST`
2. Trigger an event which will lead to a callout notification
3. Open the viewer page and check it out

I have deployed this app to [heroku](https://zuora-callout-viewer.herokuapp.com/view).
Of course you can deploy this app to your own site.

## How to Deploy
This application is built on [Playframework](https://www.playframework.com), to run it in your local:

+ install `play` or `activator` from typesafe.
+ `activator dist`, which will package the app and generate a 'zip' file under `/target`
+ unzip the zip file, and run `bin/callout`



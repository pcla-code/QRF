Note: this Readme contains markdown and should be viewed with a markdown viewer. A good online renderer
can be found at: https://dillinger.io/ You can simply paste the contents of this file there to see
the proper rendering.

[![N|Solid](http://www.knossys.com/deploy/QRF/icon-large.png)](http://www.knossys.com/deploy/QRF/)

# Overview

This is the Readme that goes with the QRF Android app and corresponding QRF Broker. You will find 
two applications in this directory:

* QRF, the Android application as an Eclipse project. Please follow the Android Eclipse plugin directions on how to use this code. Note that for compatibility reasons we're using version 8 of the SDK and version java version 6.

* QRFBroker, the message broker implemented as a RabbitMQ power-client. This is what you will integrate against if you want to communicate with all the connected QRF Android apps.

# Prerequisites:

Please install the RabbitMQ broker first. this server/service is the machinery that will route 
all the messages from and to the QRF Android apps.

* Main page: https://www.rabbitmq.com/
* Latest version at: https://www.rabbitmq.com/#getstarted

After you've install RabbitMQ, please verify by logging into the web based management portal 
that comes with the installation. Open a browser to:

http://localhost:15672/#/queues

Login with username: **guest** and password: **guest**. You should be able to keep this page 
open and see all the network activity as the system is running. This should give you a good 
idea of all the queues, clients and their behavior.

- Preparing a New Device 

The new device MUST have an SDCard. Plug the phone into your computer via the USB cable. 
From Eclipse, run QRF and choose the phone as the target. Before starting the application 
on the phone, add the "QRFData" director to the SDCard. Do this by mounting the device 
and editing the directory structure in the folder "sdcard." If you don't do this that's
ok too the app will create one for you but then you don't have the opportunity to place
configuration files there beforehand. We recommend to see what happens when you first
use the device and inspect what ends up in the QRFData folder. Once you see that you can
add any files you see fit.

# Testing

**NOTE!!! Please open the provided .sh files and change the hostname, username and password
to match your RabbitMQ installation. Please also see the troubleshooting section at the
bottom of this Readme in case you get stuck.**

We've provided a test rig with the code, which will familiarize you with the setup and how
to use the code. These two Java classes mimic the broker and the client implementation. Both
are console input applications that will take typed statements simulating step-by-step what
you would do in the application. When you take a step in the client using the test rig you
can create a response in the broker test rig (for those steps that aren't automatically
resolved)

To use the two test/simulation console applications take the following steps:

1. Make sure your RabbitMQ server is running

2. Start the broker test rig by using the provided shell script:

```
<svn>/Android/QRFBroker/runbrokertester.sh
```

It's best to run these directly from the broker folder, so that you can do:

```
./runbrokertester.sh
```

3. Now start one or more client test rigs. These simulate an Android App and give you a console interface that simulates how a user would interact with the app

``` 
<svn>/Android/QRFBroker/runclienttester.sh
```

Again it's easier if you do all your testing from the broker project directory since
you can easier start multiple instances of the client. You can then do:

```
./runclienttester.sh
```

Each client will get a unique id so there is no need to configure them.

# Usage

Let's go through the steps as if you were in a classroom and wanted to start the system with 1 coder, meaning one Android application running. For now we will use the test client java program to demonstrate how the code works. In a later section we will explain how you would use the device instead, although almost all the steps are the same except you're only running the production broker and not any of the test client java applications but the Android apps instead. 

We will go through this as a recipe so that you can easily reproduce the steps whilst looking at the code. 

1. First make sure that RabbitMQ is up and running. You can verify this by seeing of your browser can connect to the management website on the machine you started RabbitMQ. For this example we will assume that you're running everything on a desktop or laptop for development purposes. So see if you can open a browser to:

```
http://localhost:15672/#/queues
```

2. Now start the test java broker by running our convenient script like so:

```
<svn>/Android/QRFBroker/runbrokertester.sh
```

You should not get any errors and you should see that the broker successfully connected to RabbitMQ. Note that you can start multiple clients, but for now testing with one is enough to get a flavour of the asynchronous nature of the system.
There will probably be a warning that the specific logging library hasn't been found but that can be safely ignored.

3. Run one test client by using our start script:

```
./runclienttester.sh
```

The client connected to the Broker and setup a specific channel between the two. That ensures that the broker can address the client directly without having to broadcast.

## Workflow 

If you've completed the steps above you should have one broker tester up and running and one or more client testers. You can use these to test the back and forth of
commands between server and app. Below we included a rough flow of what users will typically do in the app through command line input. You should be able to follow
along and get the exact same results as you see below. If you want a visual guide to the workflow then pull up slide 2 in the ./Documentation/QRF-Design-and-Workflow.pptx
powerpoint

1. In the client type: 

```
register
```

or you can provide a class name, as in:

```
register testclass
```
This will send a message to the server to register the app/client to work with a specific class name. The app will receive a yes or no message depending on what the server determines and either progress to the next screen or stay within the same class registration screen and provide a warning popup. Of course in the command line version you won't get that and you will only see the yes/no xml come in.

2. Next you can indicate that you're ready to process student information. Essentially you're simulating that the user clicks the 'OK' button in the interface to indcate that her or she has seen the student list. To trigger this in the command line client type:

```
ready
```

In the client you should see two messages come in and two actions being executed. First of all you will see that the server responds with an xml 'yes' message. This indicates that the class exists and that the client is now associated with that class. In the console you should see that the client (app) navigated to the student list screen. You should also immediately see the message come in that lists all the students and the client should show you that it received a proper list by printing out a parsed list of the data. You're now in a state where you can start to simulate entering data. There are three possible commands that could follow at this point:

3.

```
accept
```

```
reject
```

You can (and should try to) enter multiple commands one after another simulating the user entering various evaluations. Make sure that every time you do that you see a new student information package xml come in.

```
accept
accept
reject
accept
reject
reject
```

4. When you're done testing you need to simulate navigating to the Finish screen where you can have the App send all the log data to the server. To simulate that you can use the 'end' command, like so:

```
end
```

# Logging

The QRF Android application will try to store as much data as possible for analytics purposes. You
will see three types of files end up in the QRFData directory on your SD card:

1. <sessionid>.csv.log : Ready to analyze files in CSV format that record transactions in SAI form between the server and the app

2. <sessionid>.raw.log : Very low level data that can be used for debugging purposes. You will see developer-readable statements in there only

3. <sessionid>.audio.<index>.3gp : Audio files as recorded by the coder. Note that we allow coders to stop and start audio recording so you might get more than one file per session

As you can see we strongly tie the log files to sessions. A session is a unique id (UUID) that is dynamically created each time you register a class. So for example when you go to the Finish screen and decide to start a new class you will also get a new session and therefore a new set of log files

Each line in the logfile is a ',' separated series of values, making sure it can be read by Excel, etc.
Currently we support the following colums, but that can be changed at any time:

1. TimestampRaw
2. Timestamp
3. Selection
4. Action
5. Input
6. Supplement

- Retrieving the Files

The data files are stored on the SDCard of the device. Mount the device on your machine and navigate 
to /sdcard/QRFData/. Your files will be in that directory.

- Notes on retrieving files

When the Android device is connected to a desktop you can not run the QRF application on the
device since it can either make the contents of the SDCARD available to the Android device or
the desktop it's connected to. 

# Using the Ruby/Rails broker

Included in the package is a Ruby version of the broker written as a gem and an example Rails application that loads and starts the broker in the background. You should be able to use these examples as a starting point to integrating the broker in any Rails application. To test out how the code works we've provided an easy to use set of shell scripts. We assume here you're running a local instance of RabbitMQ. If you're running against the one on knossys.com or your own remote production version you will have to configure the code in **QRFRubyBrokerGem/lib/qrfrubybroker.rb**

Try out the system by executing the following steps:

```
cd QRFRubyBrokerGem/
./gem-build.sh
./gem-install.sh
cd ../
cd QRFRailsBroker
rails server
```

If all went well you should now have a rails server running with a booted Ruby based message listener that behaves like the Java broker example. That means you can run the example Java client in a different shell and run the same command line tests as described above. In other words you should be able to run:

```
cd QRFBroker/
./runclienttester.sh
```
You can now enter the same set of commands as in the sequence described above.

# RabbitMQ Troubleshooting

The RabbitMQ server is fairly closed with respect to networking right out of the box.
For example user guest with password guest can only connect when working on localhost.
In other words if you want to create a client that can connect remotely you will have
to configure the server with a new username, password and permissions. The easiest
way to do this is directly on the machine in the rabbitmq sbin directory.

```
$ ./rabbitmqctl add_user YOUR_USERNAME YOUR_PASSWORD
$ ./rabbitmqctl set_user_tags YOUR_USERNAME administrator
$ ./rabbitmqctl set_permissions -p / YOUR_USERNAME ".*" ".*" ".*"
```

Make sure you note down these credentials because yo will need to configure the java
code with them.

Once you have configured the new user with the command above you should test it by
navigating to your RabbitMQ management page and logging in with the new credentials:

http://<hostname>:15672/#/

Note: by default the management plugin is not activated, so you will have the use
the same command line control application to enable it:

```
$ ./rabbitmq-plugins enable rabbitmq_management
```

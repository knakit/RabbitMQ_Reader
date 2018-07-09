/**
 * Copyright (c) 2018-present, Damith Jinasena for Knak.It
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.knakit.connector.reader;

import ifs.fnd.connectserver.*;
import com.rabbitmq.client.*;
import ifs.fnd.connectserver.mess.MessageSender;
import ifs.fnd.connectserver.mess.MessageSender.Response;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RabbitMQReader extends ConnectorAgent {
    private final static int INIT = 0;
    private final static int READ = 1;
    private final static int EXECUTE = 2;
    private final static int RESPONSE = 3;

    private MessageSender sender;

    /**
     * Constructs a new FileReaderAgent thread
     *
     * @param    fw The ConnectorFramework
     */
    public RabbitMQReader(ConnectorFramework fw)  {
        super(fw);
        this.start();
    }

    @Override
    public void doRun() {

        this.sender = new MessageSender();
        Response response = null;
        try {
            int state = INIT;
            int iPollTime = fw.getNumParameter("POLL_TIME");
            String host = fw.getParameter("HOST");
            int port = fw.getNumParameter("PORT");
            String username = fw.getParameter("USER_NAME");
            String password = fw.getParameter("PASSWORD");
            String queueName = fw.getParameter("QUEUE_NAME");

            Connection connection = null;
            Channel channel = null;
            byte[] messageBody = null;

            int messageCount = 0;

            while (!this.isStopped()) {
                switch (state) {
                    case INIT: {
                        fw.debug("State = INIT", 3);
                        try {
                            //--------------------INIT----------------------------\\
                            //----------Connect to RabbitMQ queue and ------------\\
                            //---------------check for messages-------------------\\
                            //----------------------------------------------------\\

                            ConnectionFactory factory = new ConnectionFactory();
                            factory.setHost(host);
                            factory.setPort(port);
                            factory.setUsername(username);
                            factory.setPassword(password);
                            connection = factory.newConnection();
                            channel = connection.createChannel();

                            messageCount = channel.queueDeclare(queueName, false, false, false, null).getMessageCount();
                            fw.debug("State = INIT - Connected to " + host + ":" + port + "/" + queueName, 3);
                            fw.debug("State = INIT - Messages in the queue /" + queueName + ": " + messageCount, 3);

                            if (messageCount > 0 ){
                                fw.debug("State = INIT - Start reading messages", 3);
                                state = READ;
                            }
                            else{
                                fw.debug("State = INIT - No messages found in the queue /" + queueName, 3);
                                fw.debug("State = INIT - Closing queue " + queueName, 3);
                                channel.close();
                                connection.close();
                                sleep(iPollTime);
                                state = INIT;
                            }

                        } catch (Exception e) {
                            fw.debug("State = INIT - Error during connection. Trying to reconnect...", 3);
                            fw.debug(getStackTrace(e), 3);
                            sleep(iPollTime);
                            state = INIT;
                        }
                        break;
                    }

                    case READ: {
                        fw.debug("State = READ", 3);
                        messageBody = null;
                        try {
                            //----------------------------------------------------\\
                            //--------Read the next message from the queue--------\\
                            // ----------------------------------------------------\\
                            fw.debug("-State = READ - Reading next message from the queue", 3);
                            GetResponse chResponse = channel.basicGet(queueName, true);
                            messageCount = chResponse.getMessageCount();
                            fw.debug("State = READ - Remaining messages count: " + messageCount, 3);
                            messageBody = chResponse.getBody();
                            //messageBody = new String(body);
                            state = EXECUTE;
                        } catch (Exception e) {
                            //@TODO
                            // need a better implementation
                            fw.debug("State = READ - Error during getting the message.", 3);
                            fw.debug(getStackTrace(e), 3);
                            sleep(iPollTime);
                            state = READ;
                        }
                        break;
                    }

                    case EXECUTE: {
                        try {
                            fw.debug("State = EXECUTE", 3);
                            //----------------------------------------------------\\
                            //---------------send message to IFS------------------\\
                            //----------------------------------------------------\\
                            fw.debug("State = EXECUTE - message size: " + messageBody.length, 3);
                            fw.debug("State = EXECUTE - Sending message to IFS", 3);
                            response = this.sender.sendMessage(messageBody, "RabbitMQ_Queue_"+ queueName, "RabbitMQ_Message", this.fw);

                            if (    //(response.status == 1) || //FAILED
                                    //(response.status == 2) || //EXECUTION_WITH_ERROR
                                    (response.status == 4)    // EXCEPTION
                                )
                            {
                                fw.debug("State = EXECUTE - Error during sending message to IFS. Trying to reconnect", 3);
                                sleep(iPollTime);
                                state = EXECUTE;
                            }
                            else {
                                state = RESPONSE;
                            }
                        } catch (Exception e) {
                            fw.debug("State = EXECUTE - Error during sending message to IFS. Trying to reconnect", 3);
                            fw.debug(getStackTrace(e), 3);
                            sleep(iPollTime);
                            state = EXECUTE;
                        }
                        break;

                    }
                    case RESPONSE: {
                        try{
                            fw.debug("State = RESPONSE", 3);
                            //----------------------------------------------------\\
                            //---------handle response from IFS-------------------\\
                            //----------------------------------------------------\\
                            String ifsResponse = new String(response.data);
                            fw.debug("State = RESPONSE - response message" + ifsResponse, 3);

                            if (messageCount > 0 ){
                                fw.debug("State = RESPONSE - Start reading next message", 3);
                                state = READ;
                            }
                            else{
                                fw.debug("State = RESPONSE - All messages caught up in the queue /" + queueName, 3);
                                fw.debug("State = RESPONSE - Closing queue " + queueName, 3);
                                channel.close();
                                connection.close();
                                sleep(iPollTime);
                                state = INIT;
                            }

                        } catch (Exception e) {
                            fw.debug("State = RESPONSE - Error during processing the response. Retrying...", 3);
                            fw.debug(getStackTrace(e), 3);
                            sleep(iPollTime);
                            state = RESPONSE;
                        }
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }
            fw.debug("Main loop error: " + getStackTrace(t));
            fw.writeErrorToLog(fw.getInstance() + " is restarted");
        }
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
    private static String getStackTrace(final Exception exception) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}



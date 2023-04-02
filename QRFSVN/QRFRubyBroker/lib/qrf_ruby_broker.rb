require "bunny"
require "rexml/document"
require "securerandom"
require "set"
require_relative "qrf_message_generator"
require_relative "qrf_client_proxy"
require_relative "qrf_logger"
require_relative "qrf_pattern_msg"
require_relative "qrf_priority_queue"
include REXML

#
# https://www.rabbitmq.com/tutorials/tutorial-one-ruby.html
# https://github.com/ruby-amqp/bunny
#
class QRFRubyBroker
    ##
    # Constructor
    #
    def initialize(ttl, interview_gap, max_interviews, max_queue_size, pattern_threshhold)
        @broker_session = "S" + SecureRandom.uuid.gsub!(/[^\d]/, "")
        @registered_sessions = Hash.new
        @student_interview_timestamp = Hash.new
        @student_num_interviews = Hash.new(0)
        @comm_queue = nil
        @comm_exchange = nil
        @priority_queue = QRFPriorityQueue.new(ttl, max_queue_size)
        @interview_gap = interview_gap
        @max_interviews = max_interviews
        @pending_ack_map = Hash.new
        @clients_awaiting_msg = Set.new
        QRFPatternMsg.set_threshold_value(pattern_threshhold)
    end

    def register_new_session(session)
        QRFLogger.debug " [*] Attempt Registering: " + session
        unless @registered_sessions.key? session
            QRFLogger.debug " [*] Adding New Session: " + session
            @registered_sessions[session] = QRFClientProxy.new(session)
            add_subject(session)
        end
        @registered_sessions[session]
    end

    def add_subject(subject)
        @comm_queue.bind(@comm_exchange, routing_key: subject + ".*")
    end

    def publish_msg(receiver, xml)
        xml_string = QRFMessageGenerator.xml_to_string(xml)
        QRFLogger.debug " [*] Sending: " + receiver + ".*"
        QRFLogger.debug " [*] Msg:" + xml_pretty_print(xml)
        @comm_exchange.publish(xml_string, routing_key: receiver + ".*")
    end

    def element_exists(parent, child)
        !parent.elements[child].nil?
    end

    def process_client_response(element)
        session = element.elements["session"].text unless element.elements["session"].nil?
        device_id = element.elements["device_identifier"].text unless element.elements["device_identifier"].nil?
        client_proxy = register_new_session(session)

        if element_exists(element, "acknowledgement_to_server")
            ack_to_server = element.elements["acknowledgement_to_server"].text unless element.elements["acknowledgement_to_server"].nil?
            id = element.elements["id"].text unless element.elements["id"].nil?
            process_acknowledgement(client_proxy, ack_to_server, id)
        elsif element_exists(element, "class_name")
            class_name = element.elements["class_name"].text unless element.elements["class_name"].nil?
            process_class(client_proxy, class_name)
        elsif element_exists(element, "action")
            action = element.elements["action"].text unless element.elements["action"].nil?
            process_action(client_proxy, action)
        end
    end

    def process_class(client_proxy, class_name)
        QRFLogger.debug " [*] Processing class: " + class_name
        if true
            msg = QRFMessageGenerator.create_yes_no_msg(@broker_session, "yes")
            publish_msg(client_proxy.get_session_id, msg)
            if !client_proxy.set_state(QRFClientProxy::ClientState::REGISTERED)
                send_state_transition_error(client_proxy)
            end
        else
            msg = QRFMessageGenerator.create_yes_no_msg(@broker_session, "no")
            publish_msg(client_proxy.get_session_id, msg)
        end
    end

    def process_acknowledgement(client_proxy, ack_to_server, id)
        QRFLogger.debug " [*] Processing acknowledgement to server: " + ack_to_server + " - ID: " + id
        if ack_to_server.casecmp("message accepted") == 0
            if !client_proxy.set_state(QRFClientProxy::ClientState::INPUT)
                send_state_transition_error(client_proxy)
            else
                qrf_msg_accept_handler(id)
            end
        elsif ack_to_server.casecmp("message dropped") == 0
            if !client_proxy.set_state(QRFClientProxy::ClientState::INPUT)
                send_state_transition_error(client_proxy)
            else
                qrf_msg_reject_handler(id)
            end
        end
        qrf_msg_request_handler(client_proxy)
    end

    def process_action(client_proxy, action)
        QRFLogger.debug " [*] Processing action: " + action
        if action.casecmp("ping") == 0
          msg = QRFMessageGenerator.create_action_msg(@broker_session, "pong")
          publish_msg(client_proxy.get_session_id, msg)
        end

        if action.casecmp("init") == 0
          if !client_proxy.set_state(QRFClientProxy::ClientState::INITIALIZED)
            send_state_transition_error(client_proxy)
          else
            msg = QRFMessageGenerator.create_configuration(@broker_session)
            publish_msg(client_proxy.get_session_id, msg)
          end
        end

        if action.casecmp("ready") == 0
          if !client_proxy.set_state(QRFClientProxy::ClientState::READY)
            send_state_transition_error(client_proxy)
          else
            qrf_msg_request_handler(client_proxy)
            client_proxy.set_state(QRFClientProxy::ClientState::INPUT)
          end
        end
    end

    def send_state_transition_error(client_proxy)
        QRFLogger.debug " [*] " + client_proxy.get_state_advice
        msg = QRFMessageGenerator.create_advice_msg(@broker_session, client_proxy.get_state_advice)
        publish_msg(client_proxy.get_session_id, msg)
    end

    def qrf_msg_request_handler(client_proxy)
        QRFLogger.debug " [*] qrfMsgRequestHandler()"
        recursive_request_handler(client_proxy)
    end

    def recursive_request_handler(client_proxy)
        priority_msg = @priority_queue.dequeue
        if priority_msg.nil? and !@clients_awaiting_msg.include? client_proxy
            msg = QRFMessageGenerator.create_empty_queue_msg(@broker_session)
            print "*************** EmptyQueue:\n"
            print msg, "\n"
            print "***************\n"
            @clients_awaiting_msg.add(client_proxy)
            publish_msg(client_proxy.get_session_id, msg)
        elsif !priority_msg.nil?
            student_id = priority_msg.get_id
            if @pending_ack_map.include? student_id
                recursive_request_handler(client_proxy)
            elsif !check_interview_timestamp(student_id)
                recursive_request_handler(client_proxy)
            elsif !check_num_interviews(student_id)
                recursive_request_handler(client_proxy)
            else
                msg = QRFMessageGenerator.create_student_msg(priority_msg)
                print "*************** ActualMsg:\n"
                print msg, "\n"
                print "***************\n"
                @pending_ack_map[student_id] = priority_msg
                @clients_awaiting_msg.delete(client_proxy)
                publish_msg(client_proxy.get_session_id, msg)
            end
        end
    end

    def check_interview_timestamp(student_id)
        if @student_interview_timestamp.include? student_id
            time_diff = Time.now.getutc - @student_interview_timestamp[student_id]
            if time_diff < @interview_gap * 60
                return false
            end
        end
        true
    end

    def check_num_interviews(student_id)
        if @student_num_interviews.include? student_id
            if @student_num_interviews[student_id] >= @max_interviews
                return false
            end
        end
        true
    end

    def qrf_msg_accept_handler(student_id)
        msg = @pending_ack_map.delete(student_id)
        msg.increment_patterns
        @priority_queue.refresh
        @student_interview_timestamp[student_id] = Time.now.getutc
        @student_num_interviews[student_id] = @student_num_interviews[student_id] + 1
    end

    def qrf_msg_reject_handler(student_id)
        @pending_ack_map.delete(student_id)
    end

    def append_msg(msg)
        print "************** HERE: ", msg, " *************\n"
        @priority_queue.enqueue(msg)
        unless @clients_awaiting_msg.empty?
            recursive_request_handler(@clients_awaiting_msg.first)
        end

    end

    def xml_pretty_print(xml)
        xml_string = QRFMessageGenerator.xml_to_string(xml)
        spacing = "\n\s\s\s\s\s"
        spacing + xml_string.gsub(/\n/, spacing) + "\n"
    end

    def get_session_id
        @broker_session
    end

    ##
    # Kickoff the broker, connect to RabbitMQ and process incoming messages
    #
    def start
        QRFLogger.debug " [*] Starting the Ruby QRF broker with session id: " + @broker_session
        connection = Bunny.new(hostname: "localhost", username: "betty", password: "betty")
        connection.start
        QRFLogger.debug " [*] Waiting for messages. To exit press CTRL+C"
        channel = connection.create_channel
        @comm_exchange = channel.topic("QRF")
        QRFLogger.debug " [*] Creating queue ..."
        @comm_queue = channel.queue("", :exclusive => true)
        QRFLogger.debug " [*] Subscribing ..."
        add_subject("broadcast")
        begin
            @comm_queue.subscribe(block: true) do |delivery_info, metadata, payload|
                QRFLogger.debug " [*] Incoming data with routing key: #{delivery_info.routing_key}"
                doc = Document.new(payload)
                doc.elements.each do |element|
                    session =  element.elements["session"].nil? ? "" : element.elements["session"].text
                    if session == @broker_session
                        QRFLogger.debug " [*] Detected message from ourself or our own channel, bump"
                        # QRFLogger.debug " [*] Msg: [" + element.name.to_s + "]" + xmlPrettyPrint(QRFMessageGenerator.XMLToString(element))
                    else
                        QRFLogger.debug " [*] Inspecting object: [" + element.name.to_s + "]" + xml_pretty_print(element)
                        if element.name.to_s=="msg"
                            process_client_response(element)
                        end
                    end
                end
            end
        rescue Interrupt => _
            QRFLogger.debug " [*] Shutting down broker ..."
            channel.close
            connection.close
            exit(0)
        end 
    end
end

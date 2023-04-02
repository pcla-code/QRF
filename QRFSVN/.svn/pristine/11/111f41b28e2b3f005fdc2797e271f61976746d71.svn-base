require_relative 'qrf_ruby_broker'
require_relative 'qrf_pattern_msg'

class QRFRubyBrokerRunner

  @@initialized = false

  def self.initialize
    unless @@initialized
      QRFLogger.use_debugging = true
      @@broker = QRFRubyBroker.new(3, 10, 4, 20, 5)
      broker_thread = Thread.new{
        @@broker.start
      }
      # broker_thread.join
      @@initialized = true
    end
  end

  def self.publish(msg)
    unless @@initialized
      initialize
    end
    converted_msg = convert_msg(msg)
    @@broker.append_msg(converted_msg)
  end

  def self.publish_qrf_pattern_msg(msg)
    unless @@initialized
      initialize
    end
    msg.set_session(@@broker.get_session_id)
    @@broker.append_msg(msg)
  end
  
  def self.convert_msg(message)
    #Parse the message to extract necessary information
    doc = Document.new(message)
    root = doc.root
    pat_session = root.elements['header'].elements['session-id'].text.to_i
    emo = root.elements['emotion-pattern'].elements['emotion-type'].text
    beh = root.elements['behavior-pattern'].elements['pattern-type'].text
    timestamp = root.elements['header'].elements['timestamp'].text.to_i
    emo_pri = root.elements['emotion-pattern'].elements['priority'].text.to_i
    beh_pri = root.elements['behavior-pattern'].elements['priority'].text.to_i
    pat_pri = [emo_pri, beh_pri].min

    #get student name/id from the database.
    session = ApiSession.find(pat_session)
    # puts session
    session_name = session.user.login

    pattern_msg = QRFPatternMsg.new
    pattern_msg.set_session(@@broker.get_session_id)
    pattern_msg.set_name(pat_session)
    pattern_msg.set_id(session_name)
    pattern_msg.set_timestamp(timestamp)
    pattern_msg.set_location("?")
    pattern_msg.set_emotion(emo)
    pattern_msg.set_behavior(beh)
    pattern_msg.set_occurrence("Change in affect")
    pattern_msg.set_priority(pat_pri)
    return pattern_msg
  end

end

def create_test_qrf_msg
  priority = 1 + rand(6)
	pattern_msg = QRFPatternMsg.new
  pattern_msg.set_name("Priority=" + priority.to_s)
  pattern_msg.set_id("dummy_id")
  pattern_msg.set_location("?")
  pattern_msg.set_emotion("EMOTION")
  pattern_msg.set_behavior("BEHAVIOR")
  pattern_msg.set_occurrence("Change in affect")
  pattern_msg.set_priority(priority)
  pattern_msg
end

if __FILE__ == $0
  priority = []
  emotion = []
  behavior = []

	send_msg = 'y'
	while send_msg.eql? 'y'
    print "Send msg? (y or n) \n"
		send_msg = gets.chomp
		if send_msg.eql? "y"
      msg = create_test_qrf_msg
      puts msg
			QRFRubyBrokerRunner.publish_qrf_pattern_msg msg
      print "Message sent. Priority: ", msg.get_priority,"\n"
    end
  end
end
	

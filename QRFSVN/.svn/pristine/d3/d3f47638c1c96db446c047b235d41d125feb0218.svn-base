require 'bunny'
require 'rexml/document'

include REXML

#
# https://www.rabbitmq.com/tutorials/tutorial-one-ruby.html
# https://github.com/ruby-amqp/bunny
#
class QRFRubyBroker
  ##
  # Constructor
  #
  def initialize(aUseDebugging)  
    @useDebugging = aUseDebugging  
  end

  ##
  # A controlled way of displaying console debug output. For a production
  # system you would turn useDebugging off.
  #
  def debug (*aMessage)
    if (@useDebugging==true)
      puts aMessage
    end
  end

  ##
  # Kickoff the broker, connect to RabbitMQ and process incoming messages
  #
  def start
    debug "Starting the Ruby QRF broker ..."

    #connection = Bunny.new(hostname: 'rabbit.local')
    connection = Bunny.new(hostname: 'localhost', username: 'guest', password: 'guest')
    connection.start

    debug ' [*] Waiting for messages. To exit press CTRL+C'

    channel = connection.create_channel

    #exchange = channel.topic("QRF.*", :auto_delete => true)
    exchange = channel.topic("QRF")

    debug ' Creating queue ...'

    queue = channel.queue('QRF')

    debug ' Subscribing ...'

    begin
      channel.queue("", :exclusive => true).bind(exchange, :routing_key => "broadcast.*").subscribe do |delivery_info, metadata, payload|
      #puts "Incoming data: #{payload}"
      debug "Incoming data with routing key: #{delivery_info.routing_key}"

      doc = Document.new(payload)
      #debug doc

      root = doc.root

      doc.elements.each do |element|
        print "Inspecting object: [", element.name.to_s, "]\n"
        if (element.name.to_s=='msg')
          element.elements.each do |content|
            #print "Inspecting msg element: [", content.name.to_s, "]\n"

            if (content.name.to_s=="session")
              print "session: ", content.text, "\n"
            end

            if (content.name.to_s=="device_identifier")
              print "device_identifier: ", content.text, "\n"
            end

            if (content.name.to_s=="action")
              print "action: ", content.text, "\n"
            end

          end   
        end
      end
    end
    rescue Interrupt => _
      debug "Shutting down broker ..."

      connection.close

      exit(0)
    end 
  end
end


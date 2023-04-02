require 'rexml/document'

include REXML

class QRFMessageGenerator

	def self.create_element(name, value)
		element = Element.new name
		element.text = value
		return element
	end

	def self.add_to_new_document(root)
		doc = Document.new "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"
		doc.add_element root
		return doc
	end

	def self.xml_to_string(root)
		output = ""
		formatter = REXML::Formatters::Pretty.new(2)
		formatter.compact = true
		formatter.write(root, output)
		return output
	end

	def self.create_configuration(session)
		root = Element.new "configuration"
		list = Element.new "list"
		list.add_element create_element("item", "Model wrong")
		list.add_element create_element("item", "Can't observe")
		list.add_element create_element("item", "Same student too recent")
		list.add_element create_element("item", "Old information")
		list.add_element create_element("item", "Other")
		list.add_element create_element("item", "Dummy Configuration Item")
		root.add_element create_element("session", session)
		root.add_element list
		return add_to_new_document(root)
	end

	def self.create_action_msg(session, action)
		root = Element.new "msg"
		root.add_element create_element("session", session)
		root.add_element create_element("device_identifier", "BROKER")
		root.add_element create_element("action", action)
		return add_to_new_document(root)
	end


	def self.create_advice_msg(session, msg)
		root = Element.new "msg"
		root.add_element create_element("session", session)
		root.add_element create_element("device_identifier", "BROKER")
		root.add_element create_element("advice", "<![CDATA[" + msg + "]]>")
		return add_to_new_document(root)
	end

	def self.create_yes_no_msg(session, yes_no)
		root = Element.new "msg"
		root.add_element create_element("string", yes_no.downcase)
		root.add_element create_element("session", session)
		return add_to_new_document(root)
	end

	def self.create_student_msg(msg)
		root = Element.new "msg"
		root.add_element create_element("session", msg.get_session)
		root.add_element create_element("name", msg.get_name)
		root.add_element create_element("ID", msg.get_id)
		root.add_element create_element("Location", msg.get_location)
		root.add_element create_element("time_when_occurred", msg.get_timestamp)
		root.add_element create_element("emotion-pattern", msg.get_emotion)
		root.add_element create_element("behavior-pattern", msg.get_behavior)
		root.add_element create_element("occurrence_type", msg.get_occurrence)
		return add_to_new_document(root)
	end

	def self.create_empty_queue_msg(session)
		root = Element.new "msg"
		root.add_element create_element("session", session)
		root.add_element create_element("name", "EMPTY_QUEUE")
		root.add_element create_element("ID", "EMPTY_QUEUE")
		root.add_element create_element("Location", "EMPTY_QUEUE")
		root.add_element create_element("time_when_occurred", Time.now.getutc)
		root.add_element create_element("emotion-pattern", "")
		root.add_element create_element("behavior-pattern", "")
		root.add_element create_element("occurrence_type", "EMPTY_QUEUE")
		return add_to_new_document(root)
	end
end

if __FILE__ == $0
	element = Document.new "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"
	print element
end
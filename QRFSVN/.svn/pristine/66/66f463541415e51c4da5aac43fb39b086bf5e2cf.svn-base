require_relative 'qrf_pattern_msg'

class QRFPriorityQueue

	def initialize(minute_timeout, preferable_size)
		@timeout = minute_timeout
		@preferable_size = preferable_size
		@queue = Array.new
	end

	def enqueue(msg)
		binary_insert(msg)
		if has_reached_preferable_size
			eliminate_timed_out_items
		end
	end

	def binary_insert(msg)
		n = insert_msg(msg)
		@queue.insert(n, msg)
		msg.set_timestamp(Time.now.getutc)
	end

	def dequeue
		if is_empty
			return nil
		else
			head = @queue.shift
			age = Time.now.getutc - head.get_timestamp
			if age > @timeout * 60
				print "Discard ", head, "\n"
				return dequeue
			else
				print "Return ", head, "\n"
				return head
			end
		end
	end

	def is_empty
		@queue.empty?
	end

	def has_reached_preferable_size
		@queue.length >= @preferable_size
	end

	def eliminate_timed_out_items
		changed = false
		@queue.each do | item |
			age = Time.now.getutc - item.get_timestamp
			# puts age
			if age > @timeout * 60
				print "*** Removing ", item, " \n"
				@queue.delete(item)
				changed = true
			end
		end
		changed
	end

	def self.binary_search(n, arr)
		if arr.empty?
			return 0
		end
	  	middle = arr.length / 2
	  	i = 0
	  	j = arr.length - 1
	  	while i <= j
	    	if arr[middle].get_priority == n.get_priority
	      		return middle
	    	elsif arr[middle].get_priority < n.get_priority
	      		i = middle + 1
	      		middle = (i + j) / 2
	    	else
	      		j = middle - 1
	      		middle = (i + j) / 2
	    	end
	  	end
	  	middle+1
	end

	def insert_msg(msg)
		index = 0
		@queue.each do |item|
			if item.get_priority < msg.get_priority
				index += 1
			elsif item.get_priority == msg.get_priority and item.get_threshold > msg.get_threshold
				index += 1
			else
				break
			end
		end
		index
	end

	def refresh
		@queue = @queue.sort
	end

	# def to_s
	# 	"Size=" + @queue.size.to_s + " [ " + @queue.map{ |msg| ("("+msg.to_s+")") }.join(', ') + " ]"
	# end

	def to_s
		"Size=" + @queue.size.to_s + " [\n" + @queue.map{ |msg| ("("+msg.to_s+")\n") }.join('') + "]"
	end
end

if __FILE__ == $0

	threshold_table = {
		'p1' => 4,
		'p2' => 0, 
		'p3' => 10, 
		'p4' => 2,
		'p5' => 7,
		'p6' => 0,
		'p7' => 3,
		'p8' => 4,
		'p9' => 0, 
		'p10' => 10,
		'p11' => 2,
		'p12' => 7,
		'p13' => 0,
		'p14' => 3
	}  


	# arr = [1, 3, 5, 7, 9, 11]
 	append = [4, 7, 4, 1, 0, 5, 5, 3, 1, 0]


	QRFPatternMsg.set_threshold_table(threshold_table)
	priority_queue = QRFPriorityQueue.new(1, 8)
	# puts priority_queue

	# arr.each do |x|
	# 	msg = QRFPatternMsg.new 
	# 	msg.set_name("Priority=" + x.to_s)
	# 	msg.set_priority(x)
	# 	priority_queue.enqueue(msg)
	# 	print "Add: ", msg, " => ", priority_queue, "\n"
	# 	sleep(5)
	# end


	threshold_table.each_key do |key|
		if key.eql? "NONE"
			next
		end
		append.each do |x| 
			msg = QRFPatternMsg.new
			msg.set_name("Priority=" + x.to_s)
			msg.set_priority(x)
			msg.set_behavior(key)
			priority_queue.enqueue(msg)
			# print "Add: ", msg, "\n"
			# , " => ", priority_queue, "\n"
			# sleep(3)
		end	
	end

	puts priority_queue

	threshold_table = {
		'p1' => 7,
		'p2' => 10, 
		'p3' => 1, 
		'p4' => 5,
		'p5' => 7,
		'p6' => 0,
		'p7' => 2,
		'p8' => 0,
		'p9' => 8, 
		'p10' => 3,
		'p11' => 5,
		'p12' => 7,
		'p13' => 6,
		'p14' => 7
	}

	QRFPatternMsg.set_threshold_table(threshold_table)
	priority_queue.refresh
	puts priority_queue


	# print "Continue... ? (y or n) \n"
	# send_msg = gets.chomp
	# while send_msg.eql? 'y'
	# 	priority = 1 + rand(6)
	# 	msg = QRFPatternMsg.new
	# 	msg.set_name("Priority=" + priority.to_s)
	# 	msg.set_priority(priority)
	# 	priority_queue.enqueue(msg)
	# 	print "Inserting: ", msg, " => ", priority_queue, "\n"
	# 	# sleep(5)
	# 	print "Continue... ? (y or n) \n"
	# 	send_msg = gets.chomp
	# end

	# print "Removed: ", priority_queue.dequeue, "\n"
	# print priority_queue, "\n"

	# sleep(5)

	# print "Removed: ", priority_queue.dequeue, "\n"
	# print priority_queue, "\n"

	# sleep(40)

	# print priority_queue, "\n"
	# removed = priority_queue.dequeue
	# print "Removed: ", removed, "\n"
	# print priority_queue, "\n"
	# print removed.nil?, "\n"
	# print (removed.eql? nil), "\n"

	# sleep(15)

	# print priority_queue, "\n"
	# removed = priority_queue.dequeue
	# print "Removed: ", removed, "\n"
	# print priority_queue, "\n"
	# print "Empty? ", removed.nil?, "\n"
	#print (removed.eql? nil), "\n"

end

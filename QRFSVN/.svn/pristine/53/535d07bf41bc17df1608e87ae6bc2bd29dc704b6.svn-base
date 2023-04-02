class QRFPatternMsg

  include Comparable

  NONE_PATTERN = "NONE"
  @@threshold_table = Hash.new
  @@threshold_table[NONE_PATTERN] = -1
  @@threshold_value = -1

  def initialize
    @emotion = NONE_PATTERN
    @behavior = NONE_PATTERN
    @timestamp = Time.now.getutc
  end

  def <=>(other)
    if @priority < other.get_priority
      return -1
    elsif @priority > other.get_priority
      return 1
    else
      if get_threshold > other.get_threshold
        return -1
      elsif get_threshold < other.get_threshold
        return 1
      else 
        return 0
      end
    end
  end 

  def self.set_threshold_value(val)
    @@threshold_value = val
  end

  def self.set_threshold_table(table)
    @@threshold_table = table
    @@threshold_table[NONE_PATTERN] = -1
  end

  def self.get_the_value
    return @@threshold_table[NONE_PATTERN]
  end

  def self.increment_pattern(pattern)
    if NONE_PATTERN.eql? pattern
      return
    elsif @@threshold_table.key?(pattern)
      @@threshold_table[pattern] = @@threshold_table[pattern] + 1
    end 
  end

  def get_threshold
    emo_threshold = @@threshold_table[@emotion] >= @@threshold_value ? -1 : @@threshold_table[@emotion]
    beh_threshold = @@threshold_table[@behavior] >= @@threshold_value ? -1 : @@threshold_table[@behavior]
    [emo_threshold, beh_threshold].max
  end

  def increment_patterns
    QRFPatternMsg.increment_pattern(@emotion)
    QRFPatternMsg.increment_pattern(@behavior)
  end

  def set_priority(priority)
    @priority = priority
  end

  def set_session(session)
    @session = session
  end

  def set_name(name)
    @name = name
  end

  def set_id(id)
    @id = id
  end

  def set_timestamp(timestamp)
    @timestamp = timestamp
  end

  def set_location(location)
    @location = location
  end

  def set_emotion(emotion)
    @emotion = emotion == "null" ? NONE_PATTERN: emotion
    unless @@threshold_table.key?(@emotion)
      @@threshold_table[@emotion] = 0
    end
  end

  def set_behavior(behavior)
    @behavior = behavior == "null" ? NONE_PATTERN: behavior
    unless @@threshold_table.key?(@behavior)
      @@threshold_table[@behavior] = 0
    end
  end

  def set_occurrence(occurrence)
    @occurrence = occurrence
  end

  def get_priority
    @priority
  end

  def get_session
    @session
  end

  def get_name
    @name
  end

  def get_id
    @id
  end

  def get_timestamp
    @timestamp
  end

  def get_location
    @location
  end

  def get_emotion
    @emotion
  end

  def get_behavior
    @behavior
  end

  def get_occurrence
    @occurrence
  end

  def get_age
    Time.now.getutc - @timestamp
  end

  def to_s
    "P=" + @priority.to_s + ", T=" + get_threshold.to_s + ", A=" + get_age.to_s
  end

end


if __FILE__ == $0

  puts QRFPatternMsg.get_the_value

  cur_val = -1
  puts cur_val == -1
  puts cur_val.eql? -1

  # threshold_table = {
  #   'p1' => 7,
  #   'p2' => 10, 
  #   'p3' => 1, 
  #   'p4' => 5,
  #   'p5' => 7,
  #   'p6' => 0,
  #   'p7' => 2,
  #   'p8' => 0,
  #   'p9' => 8, 
  #   'p10' => 3,
  #   'p11' => 5,
  #   'p12' => 7,
  #   'p13' => 6,
  #   'p14' => 7
  # }

  # QRFPatternMsg.set_threshold_table(threshold_table)

  # msg1 = QRFPatternMsg.new
  # msg1.set_name("Priority=1")
  # msg1.set_priority(1)
  # msg1.set_behavior('p2')

  # msg2 = QRFPatternMsg.new
  # msg2.set_name("Priority=1")
  # msg2.set_priority(1)
  # msg2.set_behavior('p5')

  # puts msg1 < msg2

  # msg3 = QRFPatternMsg.new
  # msg3.set_name("Priority=1")
  # msg3.set_priority(1)
  # msg3.set_behavior('p2')

  # puts msg1 < msg3
  # puts msg1 == msg3

  # msg4 = QRFPatternMsg.new
  # msg4.set_name("Priority=1")
  # msg4.set_priority(1)
  # msg4.set_behavior('p6')

  # puts msg4 > msg3

  # arr = [msg1, msg2, msg3, msg4].sort
  # print arr

end
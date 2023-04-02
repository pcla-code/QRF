class QRFLogger

	@@use_debugging = false

	##
	# A controlled way of displaying console debug output. For a production
	# system you would turn useDebugging off.
	#
	def self.debug (*msg)
		if @@use_debugging
		  puts msg
		end
	end

    # If you want to provide getters and setters you cant use the attr_accessor
    # keyword, you have to create them manually. By adding the class name to
    # the method definition, we are indicating that this is a method on the
    # class, not in instances of this class
	def QRFLogger.use_debugging= (val)
        @@use_debugging = val
    end

    # the getter is defined to return the class variable when called
    def QRFLogger.use_debugging
        return @@use_debugging
    end
end
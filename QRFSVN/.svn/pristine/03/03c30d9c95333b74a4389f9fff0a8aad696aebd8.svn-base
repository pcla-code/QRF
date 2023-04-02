class QRFClientProxy

	module ClientState
		IDLE = :IDLE
		INITIALIZED = :INITIALIZED
		REGISTERED = :REGISTERED
		READY = :READY
		INPUT = :INPUT
		FINISHING = :FINISHING

		STATE_STRINGS = {
			IDLE: "CLIENT_STATE_IDLE",
			INITIALIZED: "CLIENT_STATE_INITIALIZED",
			REGISTERED: "CLIENT_STATE_REGISTERED",
			READY: "CLIENT_STATE_READY",
			INPUT: "CLIENT_STATE_INPUT",
			FINISHING: "CLIENT_STATE_FINISHING"
		}
	end

	def initialize(session_id)
	    @state = ClientState::IDLE
	    @state_advice = "No advice"
	    @session_id = session_id
	end

	def get_state
		@state
	end

	def set_state(state)
		if !ClientState::STATE_STRINGS.key?(state)
			QRFLogger.debug " [*] Error: Cannot set state = " + state.to_s + ", reason: Unknown State Value\n"
			return false
		elsif !verify_state_transition(state)
			QRFLogger.debug " [*] Error: Cannot set state = " + state.to_s + ", reason: Invalid State Transition\n"
			return false
		end
		@state = state
		QRFLogger.debug " [*] Setting state: " + @state.to_s
		true
	end

	def verify_state_transition(state)
		if @state == ClientState::INPUT
			return true
		elsif state == ClientState::INITIALIZED and @state != ClientState::IDLE
			set_state_advice("Error: You're trying to initialize (CLIENT_STATE_INITIALIZED) the client while it is in an active state, not idling (CLIENT_STATE_IDLE)")
			return false
		elsif state == ClientState::REGISTERED and @state != ClientState::INITIALIZED
			set_state_advice("Error: You're trying to register a class (CLIENT_STATE_REGISTERED) while the client hasn't been initialized and configured yet (CLIENT_STATE_INITIALIZED)")
			return false
		elsif state == ClientState::READY and @state != ClientState::REGISTERED
			set_state_advice("Error: You're trying to navigate to the student input screen (CLIENT_STATE_READY) while the class name hasn't been registered yet (CLIENT_STATE_REGISTERED)")
			return false
		elsif state == ClientState::INPUT and @state != ClientState::READY
			set_state_advice("Error: You're trying to process student observation information (CLIENT_STATE_INPUT) while you haven't navigated away from the student list screen yet (CLIENT_STATE_READY)")
			return false
		elsif state == ClientState::FINISHING and @state != ClientState::INPUT
			set_state_advice("Error: You're trying to wrap up a session (CLIENT_STATE_FINISHING) while you're not coming from the student observation input screen (CLIENT_STATE_INPUT)")
			return false
		end
		true
	end

	def get_state_string(state)
		if ClientState::STATE_STRINGS.key?(state)
			ClientState::STATE_STRINGS[state]
		else
			"Unknown State"
		end
	end

	def get_state_advice
		@state_advice
	end

	def set_state_advice(advice)
		@state_advice = advice
	end

	def get_session_id
		 @session_id
	end

	def to_s
		"id=" + @session_id + ", state=" + @state
	end
end

# proxy = QRFClientProxy.new("DUMMY-SESSION-ID")
# print proxy.getState(), "\n"
# print proxy.getStateString(proxy.getState()), "\n"
# print proxy.getStateString(:Hello), "\n"
# proxy.setState(QRFClientProxy::ClientState::READY)
# proxy.setState(:Hello)
# proxy.setState(QRFClientProxy::ClientState::INITIALIZED)


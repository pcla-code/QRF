require_relative 'boot'
require 'qrfrubybroker'

require 'rails/all'

# Require the gems listed in Gemfile, including any gems
# you've limited to :test, :development, or :production.
Bundler.require(*Rails.groups)

module QRFRailsBroker
  class Application < Rails::Application
    # Settings in config/environments/* take precedence over those specified here.
    # Application configuration should go into files in config/initializers
    # -- all .rb files in that directory are automatically loaded.

    # Start the QRF broker and let it take control over message processing
    # and communcations. You will have to manage any integration with a
    # larger system either here by adding event handlers or by modifying 
    # the QRFRubyBroker code.
    broker=QRFRubyBroker.new (true)
    broker.start
  end
end

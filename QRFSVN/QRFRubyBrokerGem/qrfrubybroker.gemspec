Gem::Specification.new do |s|
  s.name        = 'qrfrubybroker'
  s.version     = '0.0.0'
  s.date        = '2018-03-19'
  s.summary     = "Ruby implentation of the QRF broker"
  s.description = "A ruby version of the Java QRF broker code"
  s.authors     = ["Martin van Velsen"]
  s.email       = 'vvelsen@knossys.com'
  s.files       = ["lib/qrfrubybroker.rb"]
  s.homepage    = 'http://knossys.com/qrf'
  s.license       = 'MIT'
  s.add_runtime_dependency 'bunny', '~> 2.9', '>= 2.9.2'
end

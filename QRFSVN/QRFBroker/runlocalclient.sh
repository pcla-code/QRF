################################################################################################
#
# This script starts one instance of the Android App test client. It uses the exact same code
# as any derived class you wrote in your own client but it comes with an interactive console
# so that you can test the various messages as they should be send to the broker.
#
################################################################################################
clear
cat ../banner.txt
java -cp ./target/QRF-jar-with-dependencies.jar edu.upenn.qrf.comm.QRFAppTester -username guest -password guest -host localhost

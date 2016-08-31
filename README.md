## About the tool
EasyVPN is a tool to enable you to access your remote intranet contents from anywhere, you can use whatever clients like putty, WinSCP or mRemoteN to access your remote machine, file system or remote desktop.

## How to run the tool 
EasyVPN includes EasyVPNSerer and EasyVPNClient

EasyVPNServer needs to be deployed to a machine with public ip address, e.g, an Amazon EC2 VM.

Usage: 
java EasyVPNServer `remote port number` `local port number`
  - remote port number: on which port you want your EasyVPNClient to connect with the EasyVPNServer
  - local port number: on which port you want your network clients like putty, winscp or mRemoteN to connect  

EasyVPNClient should be launched on a machine within the intranet, and then you'll be able to access your intranet resource from wherever you want.

Usage: java EasyVPNClient `EasyVPNServer host name` `port number` `Service Host Name` `Service Port Number`
  - EasVPNServer host name: the host name or ip address of your EasyVPNServer
  - port number: the remote port number when you start your EasyVPNServer
  - Service Host Name: the host name or ip address of the intranet machine you want to connect to
  - Service port Number: the port number that your network client used to connect to the intranet resource, e.g, 22 for SSH, 3389 for RDP etc.

e.g, you can launch the EasyVPNServer on your VM or machine with public IP address(e.g, easyvpn.tomwang.com) like following:

      java EasyVPNServer 8888 8889
 
After EasyVPNServer is launched, you can then launch EasyVPNClient from a intranet machine to enable you to SSH to a intranet machine 192.168.1.88 like below:

      java EasyVPNClient easyvpn.tomwang.com 8888 192.168.1.88 22

And then you can use your ssh client from anywhere to ssh to 192.168.1.88 by connecting to port 8889 or your EasyVPNServer. 

## About Security
EasyVPNServer acts simply as a switch or connector between 2 networks which can not reach each others directly, it doesn't really take part in the authentication and authorization between your intranet services and network clients. so you can improve security at different levels:
 
(1) Keep secret the IP address of your EasyVPNServer.  
(2) Make your intranet password(s) strong enough, if possible, use private key for authentication.
(3) Configure firewall for EasyVPNServer to only allow requests from your machine(s) or network segment(s).
 

### SERVER ###

use strict;
use warnings;
use IO::Socket;
use Encode qw(decode encode);

use constant PORT           =>  8080;
use constant HOST           =>  '127.0.0.1';
use constant BUFFER_SIZE    =>  1024;

my $messages_count = 0;

my $server = new IO::Socket::INET
(
    LocalHost   =>  HOST,
    LocalPort   =>  PORT,
    Proto       =>  'tcp',
    Listen      =>  1,
    Reuse       =>  1
) or die "Unable to create socket: $!\n";

my $socket = $server->accept();
$socket->autoflush(1);
print sprintf("Connected to: %s:%d\n", $socket->peerhost, $socket->peerport);

while (1) {
    my $data = '';
    $socket->recv($data, BUFFER_SIZE);
    $data = decode('ascii', $data);
    last if ($data eq 'stop');
    print sprintf("[%d] Received: %s\n", ++$messages_count, $data);
    print 'Type something: ';
    $data = <STDIN>;
    chomp $data;
    $socket->send(encode('ascii', $data));
    print sprintf("[%d] Sent: %s\n", ++$messages_count, $data);
    last if ($data eq 'stop');
};

close($server);
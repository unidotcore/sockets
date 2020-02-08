### CLIENT ###

use strict;
use warnings;
use IO::Socket;
use Encode qw(decode encode);

use constant PORT           =>  8080;
use constant HOST           =>  '127.0.0.1';
use constant BUFFER_SIZE    =>  1024;

my $messages_count = 0;

my $socket = new IO::Socket::INET
(
    PeerAddr    =>  HOST,
    PeerPort    =>  PORT,
    Proto       =>  'tcp'
) or die "Unable to create socket: $!\n";

$socket->autoflush(1);

while (1) {
    print 'Type something: ';
    my $data = <STDIN>;
    chomp $data;
    $socket->send(encode('ascii', $data));
    print sprintf("[%d] Sent: %s\n", ++$messages_count, $data);
    last if ($data eq 'stop');
    $socket->recv($data, BUFFER_SIZE);
    $data = decode('ascii', $data);
    last if ($data eq 'stop');
    print sprintf("[%d] Received: %s\n", ++$messages_count, $data);
};

close($socket);
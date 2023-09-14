import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.regex.Pattern;

public class time {

    private static int PORT = 1978;
    private static int port = PORT;

    private static Charset charset = Charset.forName("US-ASCII");
    private static CharsetEncoder encoder = charset.newEncoder();

    private static ByteBuffer dbuf = ByteBuffer.allocateDirect(1024);

    // ukendt bug, usikker på kommende fremgang, kode virker ikke 100% som den skal
    // åbner socket
    private static ServerSocketChannel setup() throws IOException {
  ServerSocketChannel ssc = ServerSocketChannel.open();
  InetSocketAddress isa
      = new InetSocketAddress(InetAddress.getLocalHost(), port);
  ssc.socket().bind(isa);
  return ssc;
    }

    
    private static void serve(ServerSocketChannel ssc) throws IOException {
  SocketChannel sc = ssc.accept();
  try {
      String now = new Date().toString();
      sc.write(encoder.encode(CharBuffer.wrap(now + "\r\n")));
      System.out.println(sc.socket().getInetAddress() + " : " + now);
      sc.close();
  } finally {
      sc.close();
      // lukker socket
  }
    }

    public static void main(String[] args) throws IOException {
  if (args.length > 1) {
      System.err.println("Timeserver [port] er ikke korrekt");
      return;
  }


  if ((args.length == 1) && Pattern.matches("[0-9]+", args[0]))
      port = Integer.parseInt(args[0]);

  ServerSocketChannel ssc = setup();
  for (;;)
      serve(ssc);

    }

}

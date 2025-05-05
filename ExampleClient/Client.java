import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		if (args.length != 2)
			System.exit(-1);
		try {
			new Client(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Client(final String add, final int port) {
		try (
				var socket = new Socket(add, port);
				var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				var writer = new PrintWriter(socket.getOutputStream(), true);
				var inputReader = new BufferedReader(new InputStreamReader(System.in));) {
			Thread.ofVirtual().start(() -> {
				String temp;
				try {
					while ((temp = reader.readLine()) != null) {
						System.out.println(temp);
					}
				} catch (IOException e) {
					System.err.println("Connection closed");
					System.exit(-1);
				}
			});

			String input;
			while (true) {
				String temp;
				if ((temp = inputReader.readLine()) == null) {
					continue;
				}
				input = temp.trim();
				if (input.equals("exit")) {
					writer.println("quit");
					System.exit(0);
				}
				writer.println(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

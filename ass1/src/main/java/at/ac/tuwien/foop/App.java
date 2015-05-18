package at.ac.tuwien.foop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.foop.client.userInterface.Controller.StartController;
import at.ac.tuwien.foop.server.domain.Game;
import at.ac.tuwien.foop.server.network.NettyServer;
import at.ac.tuwien.foop.server.service.GameLogicService;

public class App {
	private static Logger log = LoggerFactory.getLogger(App.class);

	private Thread server;

	public App() {
		log.info("start system");
		server = new Thread(new NettyServer(new Game(
				new GameLogicService().loadBoard(GameLogicService.BOARD_PATH))));
		StartController sc = new StartController();
		sc.showStart();
	}

	public void run() {
		try {
			server.start();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		App app = new App();
		app.run();
	}
}

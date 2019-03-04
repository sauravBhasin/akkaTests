package test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Application {
	public static void main(String[] args) throws Exception {
		final ActorSystem system = ActorSystem.create();

//		final ActorRef akkaBot = system.actorOf(Props.create(AkkaBot.class), "akkaBot");
//
//		akkaBot.tell(new AkkaBot.Move(AkkaBot.Direction.FORWARD), ActorRef.noSender());
//		akkaBot.tell(new AkkaBot.Move(AkkaBot.Direction.BACKWARDS), ActorRef.noSender());
//		akkaBot.tell(new AkkaBot.Stop(), ActorRef.noSender());

		final ActorRef botMaster = system.actorOf(Props.create(BotMaster.class), "botMaster");
		botMaster.tell(new BotMaster.StartChildBots(), ActorRef.noSender());

		System.out.println("Press any key to terminate");
		System.in.read();
		System.out.println("Shutting down actor system...");
		system.terminate();
	}
}

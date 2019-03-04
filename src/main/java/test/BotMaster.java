package test;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import test.AkkaBot.Direction;

public class BotMaster extends AbstractActor {

	public BotMaster() {
		for (int index = 0; index < 10; index++) {
			final ActorRef child = getContext().actorOf(Props.create(AkkaBot.class));
			getContext().watch(child);
		}
	}

	public static class StartChildBots {
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(StartChildBots.class, this::onStartChildBots)
				.match(Terminated.class, this::onChildTerminated).build();
	}

	private void onChildTerminated(Terminated terminated) {
		System.out.println("Child has stopped, starting a new one");
		final ActorRef child = getContext().actorOf(Props.create(AkkaBot.class));
		getContext().watch(child);

		final AkkaBot.RobotState robotState = new AkkaBot.RobotState(Direction.FORWARD, true);
		child.tell(robotState, getSelf());

	}

	private void onStartChildBots(StartChildBots startChildBots) {
		final AkkaBot.Move move = new AkkaBot.Move(AkkaBot.Direction.FORWARD);
		for (ActorRef child : getContext().getChildren()) {
			System.out.println("Master started moving " + child);
			child.tell(move, getSelf());
		}
	}

}
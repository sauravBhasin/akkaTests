package test;

import java.util.Optional;

import akka.actor.AbstractActor;

public class AkkaBot extends AbstractActor {

	public enum Direction {
		FORWARD, BACKWARDS, RIGHT, LEFT
	}

	public static class Move {
		public final Direction direction;

		public Move(Direction direction) {
			this.direction = direction;
		}
	}

	public static class Stop {

	}

	public static class RobotState {
		public final Direction direction;
		public final boolean moving;

		public RobotState(Direction direction, boolean moving) {
			this.direction = direction;
			this.moving = moving;
		}
	}

	private Optional<Direction> direction = Optional.empty();
	private boolean moving = false;

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Move.class, this::onMove).match(Stop.class, this::onStop)
				.match(RobotState.class, this::getState).build();

	}

	private void onMove(Move move) {
		this.moving = true;
		direction = Optional.of(move.direction);
		System.out.println(getSelf().path() + ": I am now moving " + direction.get());

		final int nextInt = new java.util.Random().nextInt(10);
//		if ((nextInt % 2) == 0) {
		getContext().stop(getSelf());
//		}
	}

	private void getState(RobotState robotState) {
		System.out.println("Currently moving " + this.moving);
	}

	private void onStop(Stop stop) {
		this.moving = false;
		System.out.println("I stopped moving");
	}

}

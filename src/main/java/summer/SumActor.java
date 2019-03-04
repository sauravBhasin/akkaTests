package summer;

import akka.actor.AbstractActor;

public class SumActor extends AbstractActor {

	public static class SumOp {

		int start;
		int end;

		public SumOp(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SumOp.class, this::sum).build();
	}

	private void sum(SumOp sumOp) {
		int sum = 0;
		for (int i = sumOp.start; i <= sumOp.end; ++i) {
			sum += i;
		}
		final int nextInt = new java.util.Random().nextInt(10);
		if ((nextInt % 2) == 0) {
			throw new NullPointerException();
		}
		getContext().sender().tell(sum, getSelf());
	}

}

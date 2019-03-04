package summer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

public class SumMaster extends AbstractActor {

	public static class SumRanges {
		List<Integer> range;

		public SumRanges(List<Integer> range) {
			this.range = range;
		}
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(
			DeciderBuilder.match(ArithmeticException.class, e -> SupervisorStrategy.restart())
					.match(NullPointerException.class, e -> SupervisorStrategy.restart())
					.match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())
					.matchAny(o -> SupervisorStrategy.escalate()).build());

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SumRanges.class, this::onRecieveSumRange).build();
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		System.out.println("actor failed");
		return strategy;
	}

	private void onRecieveSumRange(SumRanges sumRanges) throws Exception {

		Timeout t = new Timeout(6, TimeUnit.SECONDS);
		List<Future<Object>> futures = new ArrayList<>();

		for (int i = 0; i < sumRanges.range.size() - 1; ++i) {
			ActorRef child = getContext().actorOf(Props.create(SumActor.class));
			getContext().watch(child);
			System.out.println("SUM FOR " + (sumRanges.range.get(i) + 1) + " to " + sumRanges.range.get(i + 1));
			futures.add(
					Patterns.ask(child, new SumActor.SumOp(sumRanges.range.get(i) + 1, sumRanges.range.get(i + 1)), t));
		}

		int totalSum = 0;
		for (Future<Object> future : futures) {
			totalSum += (int) Await.result(future, t.duration());
		}
		getContext().sender().tell(totalSum, getSelf());
	}
}

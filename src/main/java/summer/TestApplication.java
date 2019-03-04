package summer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import summer.SumMaster.SumRanges;

public class TestApplication {

	public static void main(String args[]) throws Exception {

		final ActorSystem system = ActorSystem.create();

		final ActorRef sumMaster = system.actorOf(Props.create(SumMaster.class));
		List<Integer> range = new ArrayList<>();

		range.add(0);
		range.add(10);
		range.add(20);
		range.add(30);
		range.add(40);
		range.add(50);
		range.add(60);
		range.add(70);
		range.add(80);

		Timeout t = new Timeout(6, TimeUnit.SECONDS);
		SumMaster.SumRanges sumRanges = new SumRanges(range);

		Future<Object> future = Patterns.ask(sumMaster, sumRanges, t);

		int result = (int) Await.result(future, t.duration());
		System.out.println(result);

		system.terminate();
	}
}
